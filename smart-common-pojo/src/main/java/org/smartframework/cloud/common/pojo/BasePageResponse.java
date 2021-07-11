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