package com.myretail.settings

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
public class ApplicationSettings {
    public static String ELASTICSEARCH_URL

    @Value('${elasticsearch.host}')
    void setElasticsearchUrl(String uri) {
        ELASTICSEARCH_URL = uri
    }
}
