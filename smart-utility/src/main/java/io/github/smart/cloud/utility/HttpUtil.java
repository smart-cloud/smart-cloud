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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * http工具类
 *
 * @author collin
 * @date 2019-05-03
 */
@Slf4j
public class HttpUtil {

    /**
     * 默认编码
     */
    private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();
    /**
     * 默认从服务器获取响应数据的超时时间（单位毫秒，默认10秒）
     */
    private static final int DEFAULT_SOCKET_TIMEOUT = 10000;
    /**
     * 默认socket的连接超时时间（单位毫秒，默认10秒）
     */
    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    /**
     * 从连接池中获取连接的超时时间（单位毫秒，默认2秒）
     */
    private static final int CONNECTION_REQUEST_TIMEOUT = 2000;

    private HttpUtil() {
    }
    /**
     * post（raw）方式请求（编码为UTF-8，超时时间10秒）
     *
     * @param url
     * @param req           请求参数对象
     * @param typeReference 返回对象
     * @return
     * @throws IOException
     */
    public static <T> T postWithRaw(String url, Object req, TypeReference<T> typeReference) throws IOException {
        return postWithRaw(url, req, typeReference, null);
    }

    /**
     * post（raw）方式请求（编码为UTF-8，超时时间10秒）
     *
     * @param url
     * @param req           请求参数对象
     * @param typeReference 返回对象
     * @return
     * @throws IOException
     */
    public static <T> T postWithRaw(String url, Object req, TypeReference<T> typeReference, HttpHost proxy) throws IOException {
        return postWithRaw(url, null, req, typeReference, proxy);
    }

    /**
     * post（raw）方式请求（编码为UTF-8，超时时间10秒）
     *
     * @param url
     * @param headers       请求头参数对象
     * @param req           请求体参数对象
     * @param typeReference 返回对象
     * @return
     * @throws IOException
     */
    public static <T> T postWithRaw(String url, Header[] headers, Object req, TypeReference<T> typeReference) throws IOException {
        return postWithRaw(url, headers, req, typeReference, null);
    }

    /**
     * post（raw）方式请求（编码为UTF-8，超时时间10秒）
     *
     * @param url
     * @param headers       请求头参数对象
     * @param req           请求体参数对象
     * @param typeReference 返回对象
     * @return
     * @throws IOException
     */
    public static <T> T postWithRaw(String url, Header[] headers, Object req, TypeReference<T> typeReference, HttpHost proxy) throws IOException {
        String stringEntity = null;
        if (req instanceof String) {
            stringEntity = String.valueOf(req);
        } else {
            stringEntity = JacksonUtil.toJson(req);
        }
        String content = postWithRaw(url, stringEntity, headers, DEFAULT_CHARSET, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, proxy);
        return JacksonUtil.parseObject(content, typeReference);
    }

    /**
     * post（raw）方式请求（编码为UTF-8，超时时间10秒）
     *
     * @param url
     * @param stringEntity 请求体中的数据
     * @return
     * @throws IOException
     */
    public static String postWithRaw(String url, String stringEntity) throws IOException {
        return postWithRaw(url, stringEntity, DEFAULT_CHARSET, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, null);
    }

    /**
     * post（raw）方式请求（编码为UTF-8，超时时间10秒）
     *
     * @param url
     * @param stringEntity 请求体中的数据
     * @return
     * @throws IOException
     */
    public static String postWithRaw(String url, String stringEntity, HttpHost proxy) throws IOException {
        return postWithRaw(url, stringEntity, DEFAULT_CHARSET, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, proxy);
    }

    /**
     * 以post（raw）方式请求（编码为UTF-8）
     *
     * @param url
     * @param stringEntity   请求体中的数据
     * @param socketTimeout  从服务器获取响应数据的超时时间
     * @param connectTimeout socket的连接超时时间，单位毫秒
     * @return
     * @throws IOException
     */
    public static String postWithRaw(String url, String stringEntity, int socketTimeout, int connectTimeout, HttpHost proxy)
            throws IOException {
        return postWithRaw(url, stringEntity, DEFAULT_CHARSET, socketTimeout, connectTimeout, proxy);
    }

    /**
     * 以post（raw）方式请求
     *
     * @param url
     * @param stringEntity   请求体中的数据
     * @param charset        编码方式
     * @param socketTimeout  从服务器获取响应数据的超时时间，单位毫秒
     * @param connectTimeout socket的连接超时时间，单位毫秒
     * @return
     * @throws IOException
     */
    public static String postWithRaw(String url, String stringEntity, String charset, int socketTimeout,
                                     int connectTimeout, HttpHost proxy) throws IOException {
        return postWithRaw(url, stringEntity, null, charset, socketTimeout, connectTimeout, proxy);
    }

    /**
     * 以post（raw）方式请求
     *
     * @param url
     * @param stringEntity   请求体中的数据
     * @param headers        请求头中的数据
     * @param charset        编码方式
     * @param socketTimeout  从服务器获取响应数据的超时时间，单位毫秒
     * @param connectTimeout socket的连接超时时间，单位毫秒
     * @return
     * @throws IOException
     */
    public static String postWithRaw(String url, String stringEntity, Header[] headers, String charset, int socketTimeout,
                                     int connectTimeout, HttpHost proxy) throws IOException {
        RequestConfig requestConfig = createRequestConfig(socketTimeout, connectTimeout, proxy);

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        httpPost.setEntity(new StringEntity(stringEntity, ContentType.APPLICATION_JSON));
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        if (headers != null) {
            httpPost.setHeaders(headers);
        }
        String result = null;
        try (CloseableHttpClient client = HttpClientBuilder.create().build();) {
            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, charset);
            }
        } finally {
            if (log.isInfoEnabled()) {
                log.info("{} | headers=>{}, stringEntity=>{}, result=>{}", url, JacksonUtil.toJson(headers), stringEntity, result);
            }
        }

        return result;
    }

    /**
     * post（x-www-form-urlencoded）方式请求（编码为UTF-8，超时时间10秒）
     *
     * @param url
     * @param parameters    请求参数对象
     * @param typeReference 返回对象
     * @return
     * @throws IOException
     */
    public static <T> T postWithUrlEncoded(String url, List<? extends NameValuePair> parameters, TypeReference<T> typeReference, HttpHost proxy) throws IOException {
        return JacksonUtil.parseObject(postWithUrlEncoded(url, parameters, proxy), typeReference);
    }

    /**
     * post（x-www-form-urlencoded）方式请求（编码为UTF-8，超时时间10秒）
     *
     * @param url
     * @param parameters 请求体中的数据
     * @return
     * @throws IOException
     */
    public static String postWithUrlEncoded(String url, List<? extends NameValuePair> parameters, HttpHost proxy) throws IOException {
        return postWithUrlEncoded(url, parameters, DEFAULT_CHARSET, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, proxy);
    }

    /**
     * 以post（x-www-form-urlencoded）方式请求
     *
     * @param url
     * @param parameters     请求参数
     * @param charset        编码方式
     * @param socketTimeout  从服务器获取响应数据的超时时间，单位毫秒
     * @param connectTimeout socket的连接超时时间，单位毫秒
     * @return
     * @throws IOException
     */
    public static String postWithUrlEncoded(String url, List<? extends NameValuePair> parameters, String charset,
                                            int socketTimeout, int connectTimeout, HttpHost proxy) throws IOException {
        RequestConfig requestConfig = createRequestConfig(socketTimeout, connectTimeout, proxy);

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        httpPost.setEntity(new UrlEncodedFormEntity(parameters, charset));
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.toString());

        String result = null;
        try (CloseableHttpClient client = HttpClientBuilder.create().build();) {
            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, charset);
            }
        } finally {
            if (log.isInfoEnabled()) {
                log.info("{} | parameters=>{}, result=>{}", url, JacksonUtil.toJson(parameters), result);
            }
        }

        return result;
    }

    /**
     * 以get方式请求
     *
     * @param <T>
     * @param url
     * @param req
     * @param typeReference
     * @return
     * @throws IOException
     */
    public static <T> T get(String url, Object req, TypeReference<T> typeReference, HttpHost proxy) throws IOException {
        String content = get(url, req, proxy);
        return JacksonUtil.parseObject(content, typeReference);
    }

    /**
     * 以get方式请求
     *
     * @param <T>
     * @param url
     * @param headers
     * @param req
     * @param typeReference
     * @return
     * @throws IOException
     */
    public static <T> T get(String url, Header[] headers, Object req, TypeReference<T> typeReference, HttpHost proxy) throws IOException {
        String content = get(url, headers, req, DEFAULT_CHARSET, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, proxy);
        return JacksonUtil.parseObject(content, typeReference);
    }

    /**
     * 以get方式请求
     *
     * @param url
     * @param req
     * @return
     * @throws IOException
     */
    public static String get(String url, Object req, HttpHost proxy) throws IOException {
        return get(url, req, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, proxy);
    }

    /**
     * 以get方式请求
     *
     * @param url
     * @param req
     * @param socketTimeout
     * @param connectTimeout
     * @return
     * @throws IOException
     */
    public static String get(String url, Object req, int socketTimeout, int connectTimeout, HttpHost proxy) throws IOException {
        return get(url, req, DEFAULT_CHARSET, socketTimeout, connectTimeout, proxy);
    }

    /**
     * 以get方式请求
     *
     * @param url
     * @param req
     * @param charset
     * @param socketTimeout
     * @param connectTimeout
     * @param proxy
     * @return
     * @throws IOException
     */
    public static String get(String url, Object req, String charset, int socketTimeout, int connectTimeout, HttpHost proxy) throws IOException {
        return get(url, null, req, charset, socketTimeout, connectTimeout, proxy);
    }

    /**
     * 以get方式请求
     *
     * @param url
     * @param req
     * @param headers
     * @param charset
     * @param socketTimeout
     * @param connectTimeout
     * @param proxy
     * @return
     * @throws IOException
     */
    public static String get(String url, Header[] headers, Object req, String charset, int socketTimeout, int connectTimeout, HttpHost proxy) throws IOException {
        String requestJsonStr = (req == null) ? null : JacksonUtil.toJson(req);
        URIBuilder builder = new URIBuilder(URI.create(url));
        builder.setCharset(StandardCharsets.UTF_8);
        if (StringUtils.isNotBlank(requestJsonStr)) {
            JsonNode jsonNodeElements = JacksonUtil.parse(requestJsonStr);
            Iterator<Map.Entry<String, JsonNode>> jsonNodeIterator = jsonNodeElements.fields();
            while (jsonNodeIterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = jsonNodeIterator.next();
                JsonNode jsonNode = entry.getValue();
                if (jsonNode.isArray()) {
                    String jsonArrayStr = jsonNode.toString();
                    // 移除首尾的“[]”
                    builder.setParameter(entry.getKey(), jsonArrayStr.substring(1, jsonArrayStr.length() - 1));
                } else if (!jsonNode.isNull()) {
                    builder.setParameter(entry.getKey(), jsonNode.asText());
                }
            }
        }

        HttpGet httpGet = new HttpGet(builder.toString());
        httpGet.setConfig(createRequestConfig(socketTimeout, connectTimeout, proxy));
        httpGet.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        if (headers != null) {
            httpGet.setHeaders(headers);
        }

        String result = null;
        try (CloseableHttpClient client = HttpClientBuilder.create().build();) {
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, charset);
            }
        } finally {
            if (log.isInfoEnabled()) {
                log.info("{} | requestJsonStr={}, result=>{}", url, requestJsonStr, result);
            }
        }

        return result;
    }

    /**
     * 构建RequestConfig对象
     *
     * @param socketTimeout  从服务器获取响应数据的超时时间，单位毫秒
     * @param connectTimeout socket的连接超时时间，单位毫秒
     * @param proxy 代理
     * @return
     */
    private static RequestConfig createRequestConfig(int socketTimeout, int connectTimeout, HttpHost proxy) {
        return RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setProxy(proxy)
                .build();
    }

}