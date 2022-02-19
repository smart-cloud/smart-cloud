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
package io.github.smart.cloud.code.generate.test.cases;

import io.github.smart.cloud.code.generate.core.CodeGenerateUtil;
import io.github.smart.cloud.code.generate.test.util.CompilerUtil;
import io.github.smart.cloud.code.generate.util.PathUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class CodeFileGenerateUtilTest {

    @Test
    void testInit() throws Exception {
        CodeGenerateUtil.init();
        Iterable<String> classNames = Arrays.asList(
                // response
                PathUtil.getDefaultRpcDir() + "src/main/java/org/smartframework/cloud/examples/basic/rpc/auth/response/base/PermissionInfoBaseRespVO.java",
                PathUtil.getDefaultRpcDir() + "src/main/java/org/smartframework/cloud/examples/basic/rpc/auth/response/base/RoleInfoBaseRespVO.java",
                PathUtil.getDefaultRpcDir() + "src/main/java/org/smartframework/cloud/examples/basic/rpc/auth/response/base/RolePermissionRelaBaseRespVO.java",
                PathUtil.getDefaultRpcDir() + "src/main/java/org/smartframework/cloud/examples/basic/rpc/auth/response/base/UserRoleRelaBaseRespVO.java",
                // entity
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/basic/auth/entity/PermissionInfoEntity.java",
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/basic/auth/entity/RoleInfoEntity.java",
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/basic/auth/entity/RolePermissionRelaEntity.java",
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/basic/auth/entity/UserRoleRelaEntity.java",
                // mapper
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/basic/auth/mapper/base/PermissionInfoBaseMapper.java",
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/basic/auth/mapper/base/RoleInfoBaseMapper.java",
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/basic/auth/mapper/base/RolePermissionRelaBaseMapper.java",
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/basic/auth/mapper/base/UserRoleRelaBaseMapper.java"
        );
        Assertions.assertThat(CompilerUtil.compile(classNames)).isEmpty();
    }

    @Test
    void testUser() throws Exception {
        CodeGenerateUtil.init("config/basic_user.yaml");
        Iterable<String> classNames = Arrays.asList(
                // response
                PathUtil.getDefaultRpcDir() + "src/main/java/org/smartframework/cloud/examples/basic/rpc/user/response/base/LoginInfoBaseRespVO.java",
                PathUtil.getDefaultRpcDir() + "src/main/java/org/smartframework/cloud/examples/basic/rpc/user/response/base/UserInfoBaseRespVO.java",
                // entity
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/basic/user/entity/LoginInfoEntity.java",
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/basic/user/entity/UserInfoEntity.java",
                // mapper
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/basic/user/mapper/base/LoginInfoBaseMapper.java",
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/basic/user/mapper/base/UserInfoBaseMapper.java"
        );
        Assertions.assertThat(CompilerUtil.compile(classNames)).isEmpty();
    }

    @Test
    void testOrder() throws Exception {
        CodeGenerateUtil.init("config/mall_order.yaml");
        Iterable<String> classNames = Arrays.asList(
                // response
                PathUtil.getDefaultRpcDir() + "src/main/java/org/smartframework/cloud/examples/mall/rpc/order/response/base/OrderBillBaseRespVO.java",
                PathUtil.getDefaultRpcDir() + "src/main/java/org/smartframework/cloud/examples/mall/rpc/order/response/base/OrderDeliveryInfoBaseRespVO.java",
                // entity
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/mall/order/entity/OrderBillEntity.java",
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/mall/order/entity/OrderDeliveryInfoEntity.java",
                // mapper
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/mall/order/mapper/base/OrderBillBaseMapper.java",
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/mall/order/mapper/base/OrderDeliveryInfoBaseMapper.java"
        );
        Assertions.assertThat(CompilerUtil.compile(classNames)).isEmpty();
    }

    @Test
    void testProduct() throws Exception {
        CodeGenerateUtil.init("config/mall_product.yaml");
        Iterable<String> classNames = Arrays.asList(
                // response
                PathUtil.getDefaultRpcDir() + "src/main/java/org/smartframework/cloud/examples/mall/rpc/product/response/base/ProductInfoBaseRespVO.java",
                // entity
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/mall/product/entity/ProductInfoEntity.java",
                // mapper
                PathUtil.getDefaultServiceDir() + "src/main/java/org/smartframework/cloud/examples/mall/product/mapper/base/ProductInfoBaseMapper.java"
        );
        Assertions.assertThat(CompilerUtil.compile(classNames)).isEmpty();
    }

}