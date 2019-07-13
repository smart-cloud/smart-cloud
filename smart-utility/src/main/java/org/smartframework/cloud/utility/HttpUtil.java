package org.smartframework.cloud.utility;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.net.MediaType;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * http工具类
 *
 * @author liyulin
 * @date 2019-05-03
 */
@UtilityClass
@Slf4j
public class HttpUtil {
	
	/** 默认编码 */
	private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();
	/** 默认从服务器获取响应数据的超时时间（单位毫秒，默认10秒） */
	private static final int DEFAULT_SOCKET_TIMEOUT = 10000;
	/** 默认socket的连接超时时间（单位毫秒，默认10秒） */
	private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
	/** 从连接池中获取连接的超时时间（单位毫秒，默认2秒） */
	private static final int CONNECTION_REQUEST_TIMEOUT = 2000;
	
	/**
	 * post方式请求（编码为UTF-8，超时时间10秒）
	 * 
	 * @param url 
	 * @param req 请求参数对象
	 * @param typeReference 返回对象
	 * @return
	 * @throws IOException
	 */
	public static <T> T postWithRaw(String url, Object req, TypeReference<T> typeReference) throws IOException {
		String stringEntity = null;
		if(req instanceof String) {
			stringEntity = String.valueOf(req);
		} else {
			stringEntity = JSON.toJSONString(req);
		}
		String content = postWithRaw(url, stringEntity);
		return JSON.parseObject(content, typeReference);
	}
	
	/**
	 * post方式请求（编码为UTF-8，超时时间10秒）
	 * 
	 * @param url
	 * @param stringEntity 请求体中的数据
	 * @return
	 * @throws IOException
	 */
	public static String postWithRaw(String url, String stringEntity) throws IOException {
		return postWithRaw(url, stringEntity, DEFAULT_CHARSET, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT);
	}

	/**
	 * 以post方式请求（编码为UTF-8）
	 * 
	 * @param url
	 * @param stringEntity   请求体中的数据
	 * @param socketTimeout  从服务器获取响应数据的超时时间
	 * @param connectTimeout socket的连接超时时间，单位毫秒
	 * @return
	 * @throws IOException
	 */
	public static String postWithRaw(String url, String stringEntity, int socketTimeout, int connectTimeout)
			throws IOException {
		return postWithRaw(url, stringEntity, DEFAULT_CHARSET, socketTimeout, connectTimeout);
	}

	/**
	 * 以post方式请求
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
			int connectTimeout) throws IOException {
		log.info("stringEntity=>{}", stringEntity);
		
		RequestConfig requestConfig = createRequestConfig(socketTimeout, connectTimeout);

		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		httpPost.setHeader("Content-Type", MediaType.JSON_UTF_8.toString());
		httpPost.setEntity(new StringEntity(stringEntity, charset));
		String result = null;
		try (CloseableHttpClient client = HttpClientBuilder.create().build();) {
			HttpResponse response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, charset);
			}
		}

		log.info("result=>{}", result);
		return result;
	}
	
	/**
	 * 以get方式请求
	 * 
	 * @param <T>
	 * @param url
	 * @param typeReference
	 * @return
	 * @throws IOException
	 */
	public static <T> T get(String url, TypeReference<T> typeReference) throws IOException {
		String content = get(url);
		return JSON.parseObject(content, typeReference);
	}
	
	/**
	 * 以get方式请求
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String get(String url) throws IOException {
		return get(url, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT);
	}
	
	/**
	 * 以get方式请求
	 * 
	 * @param url
	 * @param socketTimeout
	 * @param connectTimeout
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, int socketTimeout, int connectTimeout) throws IOException {
		return get(url, DEFAULT_CHARSET, socketTimeout, connectTimeout);
	}

	/**
	 * 以get方式请求
	 * 
	 * @param url
	 * @param charset
	 * @param socketTimeout
	 * @param connectTimeout
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, String charset, int socketTimeout, int connectTimeout) throws IOException {
		log.info("url=>{}", url);

		RequestConfig requestConfig = createRequestConfig(socketTimeout, connectTimeout);

		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		httpGet.setHeader("Content-Type", MediaType.JSON_UTF_8.toString());
		String result = null;
		try (CloseableHttpClient client = HttpClientBuilder.create().build();) {
			HttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, charset);
			}
		}

		log.info("result=>{}", result);
		return result;
	}
	
	/**
	 * 构建RequestConfig对象
	 * 
	 * @param socketTimeout  从服务器获取响应数据的超时时间，单位毫秒
	 * @param connectTimeout socket的连接超时时间，单位毫秒
	 * @return
	 */
	private static RequestConfig createRequestConfig(int socketTimeout, int connectTimeout) {
		return RequestConfig.custom()
				.setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
				.build();
	}

}