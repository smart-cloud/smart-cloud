//package org.smartframework.cloud.starter.mybatis.autoconfigure;
//
//import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.MapUtils;
//import org.flywaydb.core.Flyway;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.BeanFactory;
//import org.springframework.beans.factory.BeanFactoryAware;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//import java.util.Map;
//
//@ConditionalOnClass(Flyway.class)
//@ConditionalOnProperty(prefix = "spring.flyway", name = "enabled", matchIfMissing = false)
//@AutoConfigureAfter({DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class})
//@Configuration
//@Slf4j
//public class SmartFlywayAutoConfiguration implements InitializingBean, BeanFactoryAware {
//
//
//    private final String FLYWAY_BASE_DIR = "classpath*:db/migration";
//    private DynamicRoutingDataSource dynamicRoutingDataSource;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        if (dynamicRoutingDataSource == null) {
//            return;
//        }
//
//        Map<String, DataSource> dataSourceMap = dynamicRoutingDataSource.getCurrentDataSources();
//        if (MapUtils.isEmpty(dataSourceMap)) {
//            log.warn("CurrentDataSources is null");
//            return;
//        }
//
//        dataSourceMap.forEach((name, dataSource) -> {
//            Flyway dmFlyway = Flyway.configure()
//                    .dataSource(dataSource)
//                    .baselineOnMigrate(true)
//                    .cleanOnValidationError(false)
//                    .locations(new StringBuilder()
//                            .append(FLYWAY_BASE_DIR)
//                            .append("/")
//                            .append(name).toString())
//                    .load();
//            dmFlyway.migrate();
//        });
//    }
//
//    @Override
//    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        try {
//            dynamicRoutingDataSource = beanFactory.getBean(DynamicRoutingDataSource.class);
//        } catch (BeansException e) {
//            log.warn("DynamicRoutingDataSource is not found!flyway is skipped!");
//        }
//    }
//
//}