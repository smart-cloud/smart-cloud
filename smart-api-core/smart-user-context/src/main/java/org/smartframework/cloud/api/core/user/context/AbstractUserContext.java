package org.smartframework.cloud.api.core.user.context;

/**
 * 用户上下文
 *
 * @author collin
 * @date 2021-03-03
 */
public abstract class AbstractUserContext {

    protected static final ThreadLocal<ParentUserBO> USER_THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void setContext(ParentUserBO userBO) {
        USER_THREAD_LOCAL.set(userBO);
    }

    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }

}