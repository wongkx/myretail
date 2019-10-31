package com.myretail


import org.apache.http.HttpHost
import org.apache.http.client.config.RequestConfig
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestClientBuilder
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ElasticConfig {
    @Value('${elasticsearch.host}')
    private String EsHost;

    @Value('${elasticsearch.clustername}')
    private String EsClusterName;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(EsHost, 443, "https"))
                .setRequestConfigCallback(
                        new RestClientBuilder.RequestConfigCallback() {
                            @Override
                            RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                                return requestConfigBuilder
                                        .setConnectTimeout(5000)
                                        .setSocketTimeout(60000)
                            }
                        }
                )
        )

        return client
    }
}
