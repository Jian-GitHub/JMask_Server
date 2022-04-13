package com.jian.configuration;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;


/**
 * @author Jian Qi
 * @Date 2022/4/11 11:45 上午
 * @Description Swagger配置类
 * @Version 1
 */
@Configuration
@OpenAPIDefinition(
//        tags = {
//                @Tag(name = "用户管理", description = "用户模块操作"),
//                @Tag(name = "角色管理", description = "角色模块操作")
//        },
        info = @Info(
                title = "JMask 人脸口罩识别应用系统接口 API 文档",
                description = "系统API说明文档",
                version = "0.0.5",
                contact = @Contact(name = "Jian", email = "Jian@mail.Jian-Internet.com", url = "https://space.bilibili.com/21542929"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        servers = {
                @Server(description = "JMask服务器", url = "https://JMask.Jian-Internet.com:8081"),
//                @Server(description = "测试环境服务器", url = "https://localhost:8081")
        },
        security = @SecurityRequirement(name = "Oauth2"),
        externalDocs = @ExternalDocumentation(
                description = "项目编译部署说明",
                url = "http://localhost/deploy/README.md"
        )
)
public class SwaggerConfig {

//    @Bean //配置docket以配置Swagger具体参数
//    public Docket docket() {
//        return new Docket(DocumentationType.OAS_30)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.jian.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    //配置文档信息
//    private ApiInfo apiInfo() {
////        //作者信息
////        Contact contact = new Contact("Jian", "https://space.bilibili.com/21542929", "Jian@mail.Jian-Internet.com");
////
////        return new ApiInfo(
////                "JMask人脸口罩识别应用系统",
////                "JMask Api 文档",
////                "v0.5",
////                "https://JMask.Jian-Internet.com",
////                contact,
////                "Apache 2.0",
////                "http://www.apache.org/licenses/LICENSE-2.0",
////                new ArrayList()
////        );
//        return new ApiInfoBuilder()
//                //页面标题
//                .title("JMask人脸口罩识别应用系统")
//                //创建人
//                .contact(new Contact("Jian", "https://localhost:8081/Api.html", "Jian@mail.Jian-Internet.com"))
//                //版本号
//                .version("0.5")
//                //描述
//                .description("JMask Api描述")
//                .build();
//    }

}
