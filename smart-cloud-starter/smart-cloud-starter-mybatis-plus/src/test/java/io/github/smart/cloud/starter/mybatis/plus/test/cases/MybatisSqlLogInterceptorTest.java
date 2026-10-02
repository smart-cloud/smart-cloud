/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.starter.mybatis.plus.test.cases;

import io.github.smart.cloud.starter.configure.properties.MybatisProperties;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.starter.mybatis.plus.enums.DeleteState;
import io.github.smart.cloud.starter.mybatis.plus.plugin.MybatisSqlLogInterceptor;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.MybatisplusApp;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.entity.ProductInfoEntity;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.mapper.ProductInfoBaseMapper;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.repository.ProductInfoRepository;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.vo.PageProductReqVO;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.vo.ProductInfoRespVO;
import io.github.smart.cloud.utility.NonceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.cursor.Cursor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.StringReader;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * {@link MybatisSqlLogInterceptor}集成测试
 *
 * <p>
 * 通过挂载在拦截器logger上的内存appender捕获日志，按级别和内容断言
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisplusApp.class, args = "--spring.profiles.active=mybatisplus")
class MybatisSqlLogInterceptorTest {

    private static final String SEPARATOR = "==>";
    private static final String TRUNCATED_SUFFIX = " (truncated)...";
    private static final String DATETIME_REGEX = "'\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}'";

    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private SmartProperties smartProperties;
    @Autowired
    private PlatformTransactionManager transactionManager;

    private final List<LogRecord> records = new CopyOnWriteArrayList<>();
    private Logger interceptorLogger;
    private Level originalLevel;
    private CollectAppender appender;

    @BeforeEach
    void setUp() {
        productInfoRepository.truncate();

        interceptorLogger = (Logger) LogManager.getLogger(MybatisSqlLogInterceptor.class);
        originalLevel = interceptorLogger.getLevel();
        appender = new CollectAppender(records);
        appender.start();
        // addAppender会刷新logger配置，必须在setLevel之前调用
        interceptorLogger.addAppender(appender);
        interceptorLogger.setLevel(Level.INFO);
        records.clear();
    }

    @AfterEach
    void tearDown() {
        // 先还原日志与配置，避免断言失败时影响共享同一Spring上下文的其他测试
        interceptorLogger.removeAppender(appender);
        appender.stop();
        interceptorLogger.setLevel(originalLevel);

        MybatisProperties mybatisProperties = smartProperties.getMybatis();
        mybatisProperties.setLogMaxLength(null);
        mybatisProperties.setSlowSqlMinCost(1000);
        mybatisProperties.setOnlySlowSql(false);
        mybatisProperties.setLogResult(true);

        // 任何场景下日志格式化都不应该失败
        Assertions.assertThat(records).noneMatch(r -> r.message.contains("mybatis sql log failed"));
        // 日志不能换行，否则ELK中日志顺序会错乱
        Assertions.assertThat(records).noneMatch(r -> r.message.contains("\n") || r.message.contains("\r"));
    }

    // ------------------------------------------------ 各类sql执行入口（StatementHandler.update/query/queryCursor/batch）

    /**
     * insert：实体参数走json分支
     */
    @Test
    void testInsertWithEntityParameter() {
        ProductInfoEntity entity = create("mobile");
        Assertions.assertThat(productInfoRepository.save(entity)).isTrue();

        LogRecord record = single("ProductInfoBaseMapper.insert:");
        Assertions.assertThat(record.level).isEqualTo(Level.INFO);
        Assertions.assertThat(record.message)
                .startsWith("ProductInfoBaseMapper.insert:INSERT INTO t_product_info")
                .contains("VALUES ( ?, ?, ?, ?, ?, ?, ? )" + SEPARATOR + "{")
                .contains("\"id\":" + entity.getId(), "\"name\":\"mobile\"")
                .containsSubsequence(SEPARATOR + "spend:", "ms" + SEPARATOR + "result" + SEPARATOR)
                .endsWith(SEPARATOR + "result" + SEPARATOR + "1");
    }

    /**
     * 根据主键查询：简单类型参数（Long）走json分支，结果为实体列表json
     */
    @Test
    void testSelectByIdWithSimpleParameter() {
        ProductInfoEntity entity = create("phone");
        productInfoRepository.save(entity);
        records.clear();

        Assertions.assertThat(productInfoRepository.getById(entity.getId())).isNotNull();

        LogRecord record = single("ProductInfoBaseMapper.selectById:");
        Assertions.assertThat(record.message)
                .contains("WHERE f_id=?", SEPARATOR + entity.getId() + SEPARATOR + "spend:")
                .contains(SEPARATOR + "result" + SEPARATOR + "[{", "\"name\":\"phone\"");
    }

    /**
     * wrapper条件查询：参数为Map，参数值内联到sql中；参数值中的?、$、\不影响后续参数替换
     */
    @Test
    void testQueryWithWrapperInlineParameters() {
        String name = "a?b$1\\c";
        productInfoRepository.save(create(name));
        records.clear();

        List<ProductInfoEntity> list = productInfoRepository.lambdaQuery()
                .eq(ProductInfoEntity::getName, name)
                .eq(ProductInfoEntity::getStock, 10L)
                .list();
        Assertions.assertThat(list).hasSize(1);

        LogRecord record = single("ProductInfoBaseMapper.selectList:");
        Assertions.assertThat(record.message)
                .contains("(f_name = 'a?b$1\\c' AND f_stock = 10)")
                .contains(SEPARATOR + "result" + SEPARATOR + "[{");
    }

    /**
     * wrapper更新：Date参数格式化为'yyyy-MM-dd HH:mm:ss'，Long参数原样输出
     */
    @Test
    void testUpdateWithDateParameter() {
        ProductInfoEntity entity = create("update");
        productInfoRepository.save(entity);
        records.clear();

        Assertions.assertThat(productInfoRepository.logicDelete(entity.getId(), 10L)).isTrue();

        LogRecord record = single("ProductInfoBaseMapper.update:");
        Assertions.assertThat(record.message)
                .startsWith("ProductInfoBaseMapper.update:UPDATE t_product_info SET f_sys_del_user=10,f_sys_del_time=")
                .containsPattern("f_sys_del_time=" + DATETIME_REGEX)
                .contains("(f_id = " + entity.getId() + ")")
                .endsWith(SEPARATOR + "result" + SEPARATOR + "1");
    }

    /**
     * 多@Param参数（ParamMap）：java.sql.Date也能正常格式化
     */
    @Test
    void testMultiParamWithSqlDate() {
        productInfoRepository.save(create("sqlDate"));
        records.clear();

        // java.sql.Date绑定时只保留日期部分，取两天后保证条件成立
        Long count = mapper().countByNameAndTime("sqlDate", new java.sql.Date(System.currentTimeMillis() + 2 * 24 * 3600 * 1000L));
        Assertions.assertThat(count).isEqualTo(1L);

        LogRecord record = single("ProductInfoBaseMapper.countByNameAndTime:");
        Assertions.assertThat(record.message)
                // 垂直制表符、换页符被压缩为一个空格
                .startsWith("ProductInfoBaseMapper.countByNameAndTime:SELECT COUNT(*) FROM t_product_info WHERE")
                .containsPattern("WHERE f_name = 'sqlDate' AND f_sys_insert_time <= " + DATETIME_REGEX)
                .endsWith(SEPARATOR + "result" + SEPARATOR + "[1]");
    }

    /**
     * foreach批量插入：附加参数（__frch_）内联，所有占位符都被替换
     */
    @Test
    void testInsertBatchSomeColumnWithAdditionalParameters() {
        List<ProductInfoEntity> entities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            entities.add(create("batch" + i));
        }
        Assertions.assertThat(productInfoRepository.insertBatchSomeColumn(entities)).isEqualTo(5);

        LogRecord record = single("ProductInfoBaseMapper.insertBatchSomeColumn:");
        String sql = record.message.substring(0, record.message.indexOf(SEPARATOR));
        Assertions.assertThat(sql)
                .doesNotContain("?")
                .contains("'batch0'", "'batch4'", String.valueOf(entities.get(0).getId()))
                .containsPattern(DATETIME_REGEX);
        Assertions.assertThat(record.message).endsWith(SEPARATOR + "result" + SEPARATOR + "5");
    }

    /**
     * saveBatch：BatchExecutor走StatementHandler.batch，每条记录打印一次，结果为null
     */
    @Test
    void testSaveBatchWithBatchExecutor() {
        List<ProductInfoEntity> entities = Arrays.asList(create("sb0"), create("sb1"), create("sb2"));
        Assertions.assertThat(productInfoRepository.saveBatch(entities)).isTrue();

        List<LogRecord> inserts = filter("ProductInfoBaseMapper.insert:");
        Assertions.assertThat(inserts).hasSize(3);
        Assertions.assertThat(inserts).allMatch(r -> r.message.endsWith(SEPARATOR + "result" + SEPARATOR + "null"));
        Assertions.assertThat(inserts.stream().map(r -> r.message).collect(Collectors.joining()))
                .contains("\"name\":\"sb0\"", "\"name\":\"sb1\"", "\"name\":\"sb2\"");
    }

    /**
     * 分页：打印count sql和分页插件改写后的最终sql（含LIMIT）
     */
    @Test
    void testPageLogsFinalSql() {
        productInfoRepository.save(create("page"));
        records.clear();

        PageProductReqVO reqVO = new PageProductReqVO();
        reqVO.setPageNum(1);
        reqVO.setPageSize(10);
        LambdaQueryWrapper<ProductInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(ProductInfoEntity::getName, "page");
        wrapper.orderByDesc(ProductInfoEntity::getInsertTime);
        Assertions.assertThat(productInfoRepository.page(reqVO, wrapper, ProductInfoRespVO.class).getDatas()).hasSize(1);

        Assertions.assertThat(single("ProductInfoBaseMapper.selectPage_mpCount:").message)
                .contains("SELECT COUNT(*)", "f_name LIKE '%page%'")
                .endsWith(SEPARATOR + "result" + SEPARATOR + "[1]");
        Assertions.assertThat(single("ProductInfoBaseMapper.selectPage:").message)
                .contains("f_name LIKE '%page%'", "ORDER BY f_sys_insert_time DESC LIMIT 10");
    }

    /**
     * 自定义注入方法：isExist（带LIMIT 1）、truncate（无参数，原样输出sql）
     */
    @Test
    void testInjectedMethods() {
        productInfoRepository.save(create("exist"));
        LambdaQueryWrapper<ProductInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductInfoEntity::getName, "exist");
        Assertions.assertThat(productInfoRepository.isExist(wrapper)).isTrue();
        productInfoRepository.truncate();

        Assertions.assertThat(single("ProductInfoBaseMapper.isExist:").message)
                .contains("(f_name = 'exist') LIMIT 1")
                .endsWith(SEPARATOR + "result" + SEPARATOR + "[1]");
        Assertions.assertThat(single("ProductInfoBaseMapper.truncate:").message)
                .matches("ProductInfoBaseMapper\\.truncate:TRUNCATE TABLE t_product_info==>spend:\\d+ms==>result==>0");
    }

    /**
     * 游标查询：走StatementHandler.queryCursor；sql空白被压缩；打印日志不会消费游标
     */
    @Test
    void testQueryCursor() {
        productInfoRepository.save(create("cursor"));
        productInfoRepository.save(create("cursor"));
        records.clear();

        List<String> names = new TransactionTemplate(transactionManager).execute(status -> {
            List<String> result = new ArrayList<>();
            try (Cursor<ProductInfoEntity> cursor = mapper().cursorByName("cursor")) {
                cursor.forEach(e -> result.add(e.getName()));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            return result;
        });
        Assertions.assertThat(names).containsExactly("cursor", "cursor");

        LogRecord record = single("ProductInfoBaseMapper.cursorByName:");
        Assertions.assertThat(record.message)
                .contains("SELECT f_id AS id, f_name AS name FROM t_product_info WHERE f_name = ?" + SEPARATOR + "\"cursor\"")
                .doesNotContain("\t", "  ");
    }

    /**
     * 一级缓存命中时不执行sql，因此不打印日志
     */
    @Test
    void testLocalCacheHitNotLogged() {
        ProductInfoEntity entity = create("cache");
        productInfoRepository.save(entity);
        records.clear();

        new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            Assertions.assertThat(productInfoRepository.getById(entity.getId())).isNotNull();
            Assertions.assertThat(productInfoRepository.getById(entity.getId())).isNotNull();
        });

        Assertions.assertThat(filter("ProductInfoBaseMapper.selectById:")).hasSize(1);
    }

    /**
     * sql执行异常：原始异常正常抛出，日志仍然打印且结果为null
     */
    @Test
    void testSqlExceptionStillLogged() {
        ProductInfoEntity entity = create("dup");
        productInfoRepository.save(entity);
        records.clear();

        Assertions.assertThatThrownBy(() -> productInfoRepository.save(entity)).isInstanceOf(DuplicateKeyException.class);

        Assertions.assertThat(single("ProductInfoBaseMapper.insert:").message)
                .endsWith(SEPARATOR + "result" + SEPARATOR + "null");
    }

    // ------------------------------------------------ 日志级别与配置项

    /**
     * 慢sql以warn级别打印
     */
    @Test
    void testSlowSqlLoggedAsWarn() {
        smartProperties.getMybatis().setSlowSqlMinCost(0);

        productInfoRepository.save(create("slow"));

        Assertions.assertThat(single("ProductInfoBaseMapper.insert:").level).isEqualTo(Level.WARN);
    }

    /**
     * 只打印慢sql：非慢sql不打印，慢sql以warn打印
     */
    @Test
    void testOnlySlowSql() {
        MybatisProperties mybatisProperties = smartProperties.getMybatis();
        mybatisProperties.setOnlySlowSql(true);
        mybatisProperties.setSlowSqlMinCost(Integer.MAX_VALUE);
        productInfoRepository.save(create("onlySlow"));
        Assertions.assertThat(records).isEmpty();

        mybatisProperties.setSlowSqlMinCost(0);
        productInfoRepository.save(create("onlySlow"));
        Assertions.assertThat(single("ProductInfoBaseMapper.insert:").level).isEqualTo(Level.WARN);
    }

    /**
     * 关闭结果打印
     */
    @Test
    void testLogResultDisabled() {
        smartProperties.getMybatis().setLogResult(false);

        productInfoRepository.save(create("noResult"));

        Assertions.assertThat(single("ProductInfoBaseMapper.insert:").message)
                .endsWith("ms")
                .doesNotContain(SEPARATOR + "result" + SEPARATOR);
    }

    /**
     * info关闭、warn开启：普通sql不打印，慢sql仍以warn打印
     */
    @Test
    void testInfoDisabledWarnEnabled() {
        interceptorLogger.setLevel(Level.WARN);
        productInfoRepository.save(create("warnOnly"));
        Assertions.assertThat(records).isEmpty();

        smartProperties.getMybatis().setSlowSqlMinCost(0);
        productInfoRepository.save(create("warnOnly"));
        Assertions.assertThat(single("ProductInfoBaseMapper.insert:").level).isEqualTo(Level.WARN);
    }

    /**
     * 日志级别高于warn：不打印任何sql日志，业务不受影响
     */
    @Test
    void testLogDisabled() {
        interceptorLogger.setLevel(Level.ERROR);
        smartProperties.getMybatis().setSlowSqlMinCost(0);

        ProductInfoEntity entity = create("off");
        Assertions.assertThat(productInfoRepository.save(entity)).isTrue();
        Assertions.assertThat(productInfoRepository.getById(entity.getId())).isNotNull();

        Assertions.assertThat(records).isEmpty();
    }

    /**
     * 大结果集：日志按最大长度截断（大结果集序列化提前中止），业务结果完整
     */
    @Test
    void testLargeResultTruncated() {
        List<ProductInfoEntity> entities = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            entities.add(create("large" + i));
        }
        productInfoRepository.insertBatchSomeColumn(entities);
        int maxLength = 300;
        smartProperties.getMybatis().setLogMaxLength(maxLength);
        records.clear();

        Assertions.assertThat(productInfoRepository.list()).hasSize(200);

        Assertions.assertThat(single("ProductInfoBaseMapper.selectList:").message)
                .hasSize(maxLength + TRUNCATED_SUFFIX.length())
                .endsWith(TRUNCATED_SUFFIX);
    }

    /**
     * 未配置或配置非法的最大长度时使用默认值2048
     */
    @Test
    void testDefaultLogMaxLength() {
        List<ProductInfoEntity> entities = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            entities.add(create("default" + i));
        }
        productInfoRepository.insertBatchSomeColumn(entities);
        smartProperties.getMybatis().setLogMaxLength(0);
        records.clear();

        productInfoRepository.list();

        Assertions.assertThat(single("ProductInfoBaseMapper.selectList:").message)
                .hasSize(2048 + TRUNCATED_SUFFIX.length())
                .endsWith(TRUNCATED_SUFFIX);
    }

    // ------------------------------------------------ 参数拼接与截断细节

    /**
     * 不可序列化、但有TypeHandler的单个参数（Reader）：走参数内联分支，替换占位符
     */
    @Test
    void testSimpleNonSerializableParameter() {
        productInfoRepository.save(create("reader"));
        records.clear();

        StringReader reader = new StringReader("reader");
        Assertions.assertThat(mapper().countByReader(reader)).isEqualTo(1L);

        Assertions.assertThat(single("ProductInfoBaseMapper.countByReader:").message)
                .startsWith("ProductInfoBaseMapper.countByReader:SELECT COUNT(*) FROM t_product_info WHERE f_name = " + reader + SEPARATOR + "spend:");
    }

    /**
     * 参数对象为null：输出原始sql（保留占位符），不输出参数段
     */
    @Test
    void testNullParameterObject() {
        Assertions.assertThat(productInfoRepository.getById(null)).isNull();

        String message = single("ProductInfoBaseMapper.selectById:").message;
        Assertions.assertThat(message).contains("WHERE f_id=?");
        // sql之后紧跟耗时段
        Assertions.assertThat(message.indexOf(SEPARATOR)).isEqualTo(message.indexOf(SEPARATOR + "spend:"));
    }

    /**
     * 截断边界：恰好等于最大长度不截断，超过1个字符即截断
     *
     * <p>
     * 日志中的耗时位数可能变化（如0ms→10ms），位数不一致时重试
     */
    @Test
    void testTruncateBoundary() {
        for (int attempt = 0; attempt < 10; attempt++) {
            String full = truncateAndGetLog(null);
            String equal = truncateAndGetLog(full.length());
            String exceeded = truncateAndGetLog(full.length() - 1);
            if (!sameSpendLength(full, equal) || !sameSpendLength(full, exceeded)) {
                continue;
            }

            Assertions.assertThat(equal).hasSize(full.length()).doesNotEndWith(TRUNCATED_SUFFIX);
            Assertions.assertThat(exceeded)
                    .hasSize(full.length() - 1 + TRUNCATED_SUFFIX.length())
                    .endsWith(SEPARATOR + "result" + SEPARATOR + TRUNCATED_SUFFIX);
            return;
        }
        Assertions.fail("sql spend time is unstable, boundary not verified");
    }

    /**
     * 超长字符串参数：只追加剩余长度部分，后续内容不再拼接
     */
    @Test
    void testLongStringParameterTruncated() {
        smartProperties.getMybatis().setLogMaxLength(200);

        mapper().countByNameAndTime(repeat('x', 10_000), new Date());

        Assertions.assertThat(single("ProductInfoBaseMapper.countByNameAndTime:").message)
                .startsWith("ProductInfoBaseMapper.countByNameAndTime:SELECT COUNT(*) FROM t_product_info WHERE f_name = 'xxx")
                .hasSize(200 + TRUNCATED_SUFFIX.length())
                .doesNotContain("f_sys_insert_time", "spend:")
                .endsWith("x" + TRUNCATED_SUFFIX);
    }

    // ------------------------------------------------ helpers

    private ProductInfoBaseMapper mapper() {
        return productInfoRepository.getBaseMapper();
    }

    private String truncateAndGetLog(Integer logMaxLength) {
        records.clear();
        smartProperties.getMybatis().setLogMaxLength(logMaxLength);
        productInfoRepository.truncate();
        return single("ProductInfoBaseMapper.truncate:").message;
    }

    private static boolean sameSpendLength(String a, String b) {
        return spend(a).length() == spend(b).length();
    }

    private static String spend(String message) {
        String flag = SEPARATOR + "spend:";
        int start = message.indexOf(flag) + flag.length();
        return message.substring(start, message.indexOf("ms", start));
    }

    private List<LogRecord> filter(String prefix) {
        return records.stream().filter(r -> r.message.startsWith(prefix)).collect(Collectors.toList());
    }

    private LogRecord single(String prefix) {
        List<LogRecord> matched = filter(prefix);
        Assertions.assertThat(matched).as("log records start with [%s]: %s", prefix, records).hasSize(1);
        return matched.get(0);
    }

    private static String repeat(char c, int count) {
        char[] chars = new char[count];
        Arrays.fill(chars, c);
        return new String(chars);
    }

    private ProductInfoEntity create(String name) {
        ProductInfoEntity entity = new ProductInfoEntity();
        entity.setId(NonceUtil.nextId());
        entity.setInsertTime(new Date());
        entity.setDelState(DeleteState.NORMAL);
        entity.setName(name);
        entity.setSellPrice(100L);
        entity.setStock(10L);
        entity.setInsertUser(10L);
        return entity;
    }

    private static final class LogRecord {
        private final Level level;
        private final String message;

        private LogRecord(Level level, String message) {
            this.level = level;
            this.message = message;
        }

        @Override
        public String toString() {
            return level + " " + message;
        }
    }

    private static final class CollectAppender extends AbstractAppender {
        private final List<LogRecord> records;

        private CollectAppender(List<LogRecord> records) {
            super("mybatis-sql-log-collector", null, null, true, Property.EMPTY_ARRAY);
            this.records = records;
        }

        @Override
        public void append(LogEvent event) {
            records.add(new LogRecord(event.getLevel(), event.getMessage().getFormattedMessage()));
        }
    }

}