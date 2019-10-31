package com.myretail.service

import com.myretail.domain.Product

interface ProductService {
    public Product getProduct(String productId)

    public Product updateProduct(Product product)
}