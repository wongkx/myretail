package com.myretail.domain

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "myretail", type = "_doc")
class Product {
    @Id
    String id

    String name

    @Field(type = FieldType.Double)
    Double currentPrice

    @Field(type = FieldType.Keyword)
    String currencyCode
}
