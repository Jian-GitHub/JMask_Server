package com.jian;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class Application {
    @Value("${server.port}")
    private int sslPort;//https的端口

    @Value("${server.http-port}")
    private int httpPort;//http的端口

    @Bean
    public TomcatServletWebServerFactory servletContainerFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                //设置安全性约束
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                //设置约束条件
                SecurityCollection collection = new SecurityCollection();
                //拦截所有请求
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        //设置将分配给通过此连接器接收到的请求的方案
        connector.setScheme("http");

        //true： http使用http, https使用https;
        //false： http重定向到https;
        connector.setSecure(false);

        //设置监听请求的端口号，这个端口不能其他已经在使用的端口重复，否则会报错
        connector.setPort(httpPort);

        //重定向端口号(非SSL到SSL)
        connector.setRedirectPort(sslPort);

        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            //            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        //设置允许跨域请求的域名
                        .allowedOriginPatterns("*")
                        //是否允许证书 不再默认开启
                        .allowCredentials(true)
                        //设置允许的方法
                        .allowedMethods("*")
                        //跨域允许时间
                        .maxAge(3600);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}