package com.myretail.view

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class ProductView {
    String id
    String name
    Double currentPrice
    String currencyCode
}
