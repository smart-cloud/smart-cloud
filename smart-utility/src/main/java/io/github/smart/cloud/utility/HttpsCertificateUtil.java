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

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * https证书有效期工具类
 *
 * @author collin
 * @date 2024-02-02
 */
public class HttpsCertificateUtil {

    /**
     * https前缀
     */
    private static final String HTTPS_PREFIX = "https://";

    /**
     * 获取证书有效期（有效期从小到大排序）
     *
     * @param url
     * @param proxy
     * @return
     * @throws IOException
     */
    public static List<Date> getValidTimes(String url, Proxy proxy) throws IOException {
        if (!url.startsWith(HTTPS_PREFIX)) {
            url = HTTPS_PREFIX + url;
        }
        List<Date> validTimes = new ArrayList<>();
        HttpsURLConnection connection = null;
        try {
            if (proxy != null) {
                connection = (HttpsURLConnection) new URL(url).openConnection(proxy);
            } else {
                connection = (HttpsURLConnection) new URL(url).openConnection();
            }
            connection.connect();

            Certificate[] certificates = connection.getServerCertificates();
            for (Certificate certificate : certificates) {
                X509Certificate x509Certificate = (X509Certificate) certificate;
                // 截止日期
                validTimes.add(x509Certificate.getNotAfter());
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        Collections.sort(validTimes, Comparator.comparing(Date::getTime));
        return validTimes;
    }

}