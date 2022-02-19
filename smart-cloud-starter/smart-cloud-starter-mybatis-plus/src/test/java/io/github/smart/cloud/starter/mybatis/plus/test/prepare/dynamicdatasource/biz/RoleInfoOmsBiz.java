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
package io.github.smart.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.biz;

import com.baomidou.dynamic.datasource.annotation.DS;
import io.github.smart.cloud.starter.mybatis.plus.common.biz.BaseBiz;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.constants.DatasourceNames;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.entity.RoleInfoEntity;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.mapper.RoleInfoBaseMapper;
import org.springframework.stereotype.Repository;

@Repository
@DS(DatasourceNames.AUTH)
public class RoleInfoOmsBiz extends BaseBiz<RoleInfoBaseMapper, RoleInfoEntity> {


}