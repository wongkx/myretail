package com.myretail


import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@ComponentScan("com.myretail")
class ApplicationConfig {

    @Value('${elasticsearch.host}')
    public String host

    public static final hostUrl = "redsky.target.com"

    @Bean
    RestTemplate restTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
        RestTemplate restTemplate = restTemplateBuilder.rootUri(hostUrl).build()
        return restTemplate
    }
}
