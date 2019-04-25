
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * @author Leone
 * @since 1.8
 **/
@Configuration
public class CmsSwaggerConfig {

    @Value("${swagger.show}")
    private Boolean enable;

    @Bean
    public Docket swaggerApi() {
        Parameter parameter = new ParameterBuilder()
                .name("Authorization")
                .description("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .defaultValue("token ")
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enable)
                .apiInfo(apiInfo())
                .groupName("cms-API")
                .globalOperationParameters(Collections.singletonList(parameter))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xxx"))
                .paths(Predicates.alwaysTrue())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CMS端接口文档")
                .description("CMS端接口文档,注意传入的参数!")
                .version("v1.0.0")
                .build();
    }
}
