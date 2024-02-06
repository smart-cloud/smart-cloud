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
package io.github.smart.cloud.utility;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * telnet工具类
 *
 * @author collin
 * @date 2023-01-03
 */
public class TelnetUtil {

    private TelnetUtil() {
    }

    /**
     * 测试telnet 机器端口的连通性
     *
     * @param hostname
     * @param port
     * @param timeout      单位毫秒
     * @param failRetryCount 失败重试次数
     * @return
     */
    public static boolean isOk(String hostname, int port, int timeout, int failRetryCount) {
        for (int i = 0; i < failRetryCount; i++) {
            if (TelnetUtil.telnet(hostname, port, timeout)) {
                return true;
            } else {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        }
        return false;
    }

    /**
     * 测试telnet 机器端口的连通性
     *
     * @param hostname
     * @param port
     * @param timeout  单位毫秒
     * @return
     */
    public static boolean telnet(String hostname, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(hostname, port), timeout);
            return socket.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

}