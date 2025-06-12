package eduardoamaral.devicechallenge.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Device Challenge API documentation")
                        .version("1.0")
                        .description("""
                                Welcome to the device challenge api docs, here you can check the features implemented so far:
                                
                                Required:
                                
                                • Create a new device ✅
                                
                                • Fully and/or partially update an existing device ✅
                                
                                • Fetch a single device ✅
                                
                                • Fetch all devices ✅
                                
                                • Fetch devices by brand ✅
                                
                                • Fetch devices by state ✅
                                
                                • Delete a single device ✅
                                """));
    }
}