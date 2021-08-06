package org.smartframework.cloud.starter.configure.properties;

import lombok.Getter;
import lombok.Setter;
import org.smartframework.cloud.common.pojo.Base;

import java.util.Set;

/**
 * mock配置
 *
 * @author liyulin
 * @date 2020-12-21
 */
@Getter
@Setter
public class MockProperties extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * api mock开关 （默认false）
     */
    private boolean api = false;

    /**
     * method mock开关（默认false）
     */
    private boolean method = false;

    /**
     * 白名单（不走mock）
     */
    private Set<String> whilelist;

}