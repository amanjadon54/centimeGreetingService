package com.centime.greeting.configuration;

import com.centime.util.aspect.LoggerAspect;
import com.centime.util.interceptor.AuthenticationRequestInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class GreetingConfiguration extends WebMvcConfigurerAdapter {

    @Value("${httpClientFactory.connection.timeout:5000}")
    private String connectionTimeOut;

    @Value("${httpClientFactory.read.timeout:10000}")
    private String readTimeOut;

    @Value("${httpClient.connection.pool.size:200}")
    private String poolMaxTotal;

    @Bean
    public com.centime.util.interceptor.AuthenticationRequestInterceptor authenticationRequestInterceptor() {
        return new AuthenticationRequestInterceptor();
    }

    @Bean
    public com.centime.util.aspect.LoggerAspect loggerAspect() {
        return new LoggerAspect();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationRequestInterceptor()).addPathPatterns("/hello");
    }

    @Scope("prototype")
    @Bean
    public RestTemplate restTemplate() {
        return restTemplate(Integer.parseInt(connectionTimeOut), Integer.parseInt(readTimeOut),
                Integer.parseInt(poolMaxTotal));
    }

    private RestTemplate restTemplate(int connectionTimeout, int readTimeout, int maxConnections) {
        RestTemplate template =
                new RestTemplate(httpRequestFactory(connectionTimeout, readTimeout, maxConnections));
        List<HttpMessageConverter<?>> messageConverters = template.getMessageConverters();
        messageConverters.add(new FormHttpMessageConverter());
        template.setMessageConverters(messageConverters);
        return template;
    }

    private ClientHttpRequestFactory httpRequestFactory(int connectionTimeout, int readTimeout,
                                                       int maxConnections) {
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient(maxConnections));
        factory.setConnectTimeout(connectionTimeout);
        factory.setReadTimeout(readTimeout);
        return factory;
    }

    private HttpClient httpClient(int noOfConnections) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(noOfConnections);
        return HttpClientBuilder.create().setConnectionManager(connectionManager).build();
    }
}
