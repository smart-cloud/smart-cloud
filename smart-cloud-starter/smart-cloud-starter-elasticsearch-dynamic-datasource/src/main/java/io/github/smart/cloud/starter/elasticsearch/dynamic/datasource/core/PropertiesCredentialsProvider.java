package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.core;

import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.properties.ElasticsearchProperties;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.springframework.util.StringUtils;

import java.net.URI;

public class PropertiesCredentialsProvider extends BasicCredentialsProvider {

    public PropertiesCredentialsProvider(ElasticsearchProperties properties) {
        if (StringUtils.hasText(properties.getUsername())) {
            Credentials credentials = new UsernamePasswordCredentials(properties.getUsername(),
                    properties.getPassword());
            setCredentials(AuthScope.ANY, credentials);
        }
        properties.getUris().stream().map(this::toUri).filter(this::hasUserInfo)
                .forEach(this::addUserInfoCredentials);
    }

    private URI toUri(String uri) {
        try {
            return URI.create(uri);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private boolean hasUserInfo(URI uri) {
        return uri != null && StringUtils.hasLength(uri.getUserInfo());
    }

    private void addUserInfoCredentials(URI uri) {
        AuthScope authScope = new AuthScope(uri.getHost(), uri.getPort());
        Credentials credentials = createUserInfoCredentials(uri.getUserInfo());
        setCredentials(authScope, credentials);
    }

    private Credentials createUserInfoCredentials(String userInfo) {
        int delimiter = userInfo.indexOf(":");
        if (delimiter == -1) {
            return new UsernamePasswordCredentials(userInfo, null);
        }
        String username = userInfo.substring(0, delimiter);
        String password = userInfo.substring(delimiter + 1);
        return new UsernamePasswordCredentials(username, password);
    }

}