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
package io.github.smart.cloud.starter.mybatis.plus.util;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 编程式事务管理工具类
 * <br>允许在不使用@Transactional注解的情况下控制事务边界
 *
 * @author collin
 * @date 2025-10-07
 */
@RequiredArgsConstructor
public class TransactionUtil {

    private final PlatformTransactionManager transactionManager;

    /**
     * 在事务中执行无返回值的操作
     *
     * @param action 要执行的操作
     */
    public void executeInTransaction(Consumer<TransactionStatus> action) {
        executeInTransaction(action, TransactionDefinition.PROPAGATION_REQUIRED);
    }

    /**
     * 在指定传播行为的事务中执行无返回值的操作
     *
     * @param action              要执行的操作
     * @param propagationBehavior 事务传播行为
     */
    public void executeInTransaction(Consumer<TransactionStatus> action, int propagationBehavior) {
        TransactionStatus status = startTransaction(propagationBehavior);
        try {
            action.accept(status);
            commitTransaction(status);
        } catch (RuntimeException | Error e) {
            rollbackTransaction(status);
            throw e;
        }
    }

    /**
     * 在事务中执行有返回值的操作
     *
     * @param action 要执行的操作
     * @param <T>    返回值类型
     * @return 操作结果
     */
    public <T> T executeInTransaction(Function<TransactionStatus, T> action) {
        return executeInTransaction(action, TransactionDefinition.PROPAGATION_REQUIRED);
    }

    /**
     * 在指定传播行为的事务中执行有返回值的操作
     *
     * @param action              要执行的操作
     * @param propagationBehavior 事务传播行为
     * @param <T>                 返回值类型
     * @return 操作结果
     */
    public <T> T executeInTransaction(Function<TransactionStatus, T> action, int propagationBehavior) {
        TransactionStatus status = startTransaction(propagationBehavior);
        try {
            T result = action.apply(status);
            commitTransaction(status);
            return result;
        } catch (RuntimeException | Error e) {
            rollbackTransaction(status);
            throw e;
        }
    }

    /**
     * 开始一个新事务
     *
     * @return 事务状态
     */
    private TransactionStatus startTransaction() {
        return startTransaction(TransactionDefinition.PROPAGATION_REQUIRED);
    }

    /**
     * 使用指定传播行为开始一个新事务
     *
     * @param propagationBehavior 传播行为
     * @return 事务状态
     */
    private TransactionStatus startTransaction(int propagationBehavior) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(propagationBehavior);
        return transactionManager.getTransaction(definition);
    }

    /**
     * 提交事务
     *
     * @param status 事务状态
     */
    private void commitTransaction(TransactionStatus status) {
        if (status != null && !status.isCompleted()) {
            transactionManager.commit(status);
        }
    }

    /**
     * 回滚事务
     *
     * @param status 事务状态
     */
    private void rollbackTransaction(TransactionStatus status) {
        if (status != null && !status.isCompleted()) {
            transactionManager.rollback(status);
        }
    }

    /**
     * 嵌套事务执行 - 支持在已有事务中创建嵌套事务
     *
     * @param action 要执行的操作
     */
    public void executeInNestedTransaction(Consumer<TransactionStatus> action) {
        executeInTransaction(action, TransactionDefinition.PROPAGATION_NESTED);
    }

    /**
     * 嵌套事务执行 - 支持在已有事务中创建嵌套事务（有返回值）
     *
     * @param action 要执行的操作
     * @param <T>    返回值类型
     * @return 操作结果
     */
    public <T> T executeInNestedTransaction(Function<TransactionStatus, T> action) {
        return executeInTransaction(action, TransactionDefinition.PROPAGATION_NESTED);
    }

    /**
     * 在新事务中执行操作（无论当前是否存在事务）
     *
     * @param action 要执行的操作
     */
    public void executeInNewTransaction(Consumer<TransactionStatus> action) {
        executeInTransaction(action, TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    /**
     * 在新事务中执行操作（无论当前是否存在事务）（有返回值）
     *
     * @param action 要执行的操作
     * @param <T>    返回值类型
     * @return 操作结果
     */
    public <T> T executeInNewTransaction(Function<TransactionStatus, T> action) {
        return executeInTransaction(action, TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }
}