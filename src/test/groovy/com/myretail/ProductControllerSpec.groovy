package com.myretail

import com.myretail.controller.ProductController
import com.myretail.domain.Product
import com.myretail.exception.InvalidArgumentException
import spock.lang.Specification

class ProductControllerSpec extends Specification{

    ProductController sut = new ProductController()

    def "mismatch IDs throws InvalidArgumentException"() {
        given:
        Product product = new Product(
                id: "product1",
                name: "product name",
                currentPrice: 123.00,
                currencyCode: "USD"
        )

        when:
        sut.updateProduct("product2", product)

        then:
        InvalidArgumentException exception = thrown()
        exception.message == "Mismatching productIds"
    }
}
