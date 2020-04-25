package com.centime.greeting.configuration;

import com.centime.util.aspect.LoggerAspect;
import com.centime.util.interceptor.AuthenticationRequestInterceptor;
import com.google.common.base.Predicate;
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
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static com.centime.util.constants.StringConstants.ACCESS_TOKEN;
import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
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
        registry.addInterceptor(authenticationRequestInterceptor()).addPathPatterns("/greeting");
    }

    @Scope("prototype")
    @Bean
    public RestTemplate restTemplate() {
        return restTemplate(Integer.parseInt(connectionTimeOut), Integer.parseInt(readTimeOut),
                Integer.parseInt(poolMaxTotal));
    }

    @Bean
    public Docket api() {
        ParameterBuilder authParameterBuilder = new ParameterBuilder();
        authParameterBuilder.name(ACCESS_TOKEN)                 // name of header
                .modelRef(new ModelRef("string"))
                .parameterType("header")               // type - header
                .defaultValue("static token")        // based64 of - zone:mypassword
                .required(true)                // for compulsory
                .build();
        java.util.List<Parameter> aParameters = new ArrayList<>();
        aParameters.add(authParameterBuilder.build());             // add parameter

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.centime.greeting.controller"))
//                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(aParameters);
    }
    //http://localhost:10001/v2/api-docs
    //http://localhost:10001/swagger-ui.html

    private Predicate<String> postPaths() {
        return or(regex("/greeting"),
                regex("/health")
        );
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
