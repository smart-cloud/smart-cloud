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
package org.smartframework.cloud.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 分页请求响应信息
 *
 * @author liyulin
 * @date 2020-05-07
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasePageResponse<T> extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 响应信息
     */
    private List<T> datas;

    /**
     * 页数
     */
    protected int pageIndex;

    /**
     * 页码大小
     */
    protected int pageSize;

    /**
     * 总数据条数
     */
    private long pageTotal;

    public List<T> getDatas() {
        if (Objects.isNull(datas)) {
            datas = new ArrayList<>(0);
        }

        return datas;
    }

}