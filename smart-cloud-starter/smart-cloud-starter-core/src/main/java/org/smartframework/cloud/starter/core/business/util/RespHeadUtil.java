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
package org.smartframework.cloud.starter.core.business.util;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.pojo.ResponseHead;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCodes;
import org.smartframework.cloud.utility.NonceUtil;
import org.smartframework.cloud.utility.spring.I18nUtil;

/**
 * {@link ResponseHead}工具类
 *
 * @author collin
 * @date 2019-04-14
 */
public class RespHeadUtil {

    private RespHeadUtil() {
    }

    /**
     * 构造RespHead对象
     *
     * @return 默认返回状态码{@code ReturnCodeEnum.SUCCESS}
     */
    public static ResponseHead of() {
        return of(CommonReturnCodes.SUCCESS, null);
    }

    /**
     * 构造RespHead对象
     *
     * @param code
     * @param message
     * @return
     */
    public static ResponseHead of(String code, String message) {
        ResponseHead respHeadVO = new ResponseHead(code, message);
        respHeadVO.setTimestamp(System.currentTimeMillis());
        respHeadVO.setNonce(NonceUtil.getInstance().nextId());

        return respHeadVO;
    }

    /**
     * 构造RespHead对象
     *
     * @param returnCodes
     * @param message
     * @return
     */
    public static ResponseHead of(IBaseReturnCodes returnCodes, String message) {
        ResponseHead respHeadVO = new ResponseHead(returnCodes);
        respHeadVO.setTimestamp(System.currentTimeMillis());
        respHeadVO.setNonce(NonceUtil.getInstance().nextId());
        if (StringUtils.isNotBlank(message)) {
            respHeadVO.setMessage(message);
        }

        return respHeadVO;
    }

    /**
     * 构造RespHead对象（i18n message）
     *
     * @param returnCodes
     * @return
     */
    public static ResponseHead ofI18n(IBaseReturnCodes returnCodes) {
        ResponseHead respHeadVO = new ResponseHead(returnCodes);
        respHeadVO.setTimestamp(System.currentTimeMillis());
        respHeadVO.setNonce(NonceUtil.getInstance().nextId());
        respHeadVO.setMessage(I18nUtil.getMessage(returnCodes.getCode()));

        return respHeadVO;
    }

}