package com.myretail

import io.searchbox.client.JestClient
import io.searchbox.client.JestClientFactory
import io.searchbox.client.config.HttpClientConfig
import org.apache.http.HttpHost
import org.apache.http.client.config.RequestConfig
import org.elasticsearch.client.Client
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestClientBuilder
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.myretail.repository")
class ElasticConfig {
        @Value('${elasticsearch.host}')
        private String EsHost;

//        @Value('${elasticsearch.port}')
//        private int EsPort;

        @Value('${elasticsearch.clustername}')
        private String EsClusterName;

//        @Bean
//        public Client client() throws Exception {
//
//            Settings esSettings = Settings.builder().put("cluster.name", EsClusterName).build()
//
//            //https://www.elastic.co/guide/en/elasticsearch/guide/current/_transport_client_versus_node_client.html
//            return TransportClient(esSettings)
//                    .addTransportAddress(
//                            new InetSocketTransportAddress(InetAddress.getByName(EsHost), EsPort));
//        }
//
//        @Bean
//        public ElasticsearchOperations elasticsearchTemplate() throws Exception {
//            return new ElasticsearchTemplate(client());
//        }
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

    @Bean
    public JestClient jestClient() {
        JestClientFactory factory = new JestClientFactory();
        String serverUrl = "https://"+EsHost
        factory.setHttpClientConfig(
                new HttpClientConfig.Builder(serverUrl)
                        .multiThreaded(true)
                        .defaultMaxTotalConnectionPerRoute(2)
                        .maxTotalConnection(10)
                        .build());
        return factory.getObject();
    }
}
