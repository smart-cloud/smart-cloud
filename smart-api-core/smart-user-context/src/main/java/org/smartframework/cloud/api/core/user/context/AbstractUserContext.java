package org.smartframework.cloud.api.core.user.context;

/**
 * 用户上下文
 *
 * @author collin
 * @date 2021-03-03
 */
public abstract class AbstractUserContext {

    protected static final ThreadLocal<SmartUser> USER_THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void setContext(SmartUser smartUser) {
        USER_THREAD_LOCAL.set(smartUser);
    }

    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }

}