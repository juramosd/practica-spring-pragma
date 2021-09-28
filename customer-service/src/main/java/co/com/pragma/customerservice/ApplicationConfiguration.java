package co.com.pragma.customerservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfiguration {
    @Bean("restTemplatePhoto")
    public RestTemplate registerRestTemplate(){
        return new RestTemplate();
    }
}
