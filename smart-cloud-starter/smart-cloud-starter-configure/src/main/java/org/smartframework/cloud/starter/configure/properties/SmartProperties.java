package org.smartframework.cloud.starter.configure.properties;

import lombok.Getter;
import lombok.Setter;
import org.smartframework.cloud.common.pojo.Base;
import org.smartframework.cloud.starter.configure.constants.SmartConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * yml文件公共属性配置定义
 *
 * @author liyulin
 * @date 2019-04-14
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SmartConstant.SMART_PROPERTIES_PREFIX)
public class SmartProperties extends Base {

    private static final long serialVersionUID = 1L;
    /**
     * id生成器数据机器标识配置
     */
    private Long dataMachineId;
    /**
     * @Async配置开关
     */
    private boolean async = true;
    /**
     * rpc配置
     */
    private RpcProperties rpc = new RpcProperties();
    /**
     * 切面配置
     */
    private LogProperties log = new LogProperties();
    /**
     * xxl-job配置
     */
    private XxlJobProperties xxlJob = new XxlJobProperties();
    /**
     * 多语言配置
     */
    private LocaleProperties locale = new LocaleProperties();
    /**
     * mock配置
     */
    private MockProperties mock = new MockProperties();

    public static final class PropertiesName {
        public static final String DATA_MACHINE_ID = "dataMachineId";
    }

}