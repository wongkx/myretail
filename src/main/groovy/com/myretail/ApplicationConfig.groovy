package com.myretail

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@ComponentScan("com.myretail")
class ApplicationConfig {

    @Value('${elasticsearch.host}')
    public String host

//    @Value('${elasticsearch.port:443}')
//    public int port

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate()
        return restTemplate
    }

//    @Bean
//    ElasticsearchTemplate elasticsearchTemplate() {
//        ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate()
//        return elasticsearchTemplate
//    }

//    @Bean
//    public Client client() {
//        TransportClient client = null
//        try {
//            client = new PreBuiltTransportClient(Settings.EMPTY)
//            .addTransportAddress(new TransportAddress(InetAddress.getByName(host), 9200))
//        } catch (Exception e) {
//            e.printStackTrace()
//        }
//        return client
//    }
}
