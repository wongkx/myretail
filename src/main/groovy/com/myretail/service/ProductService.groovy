package com.myretail.service

import com.myretail.domain.Product
import com.myretail.view.ProductView

interface ProductService {
    public Product getProduct(String productId)

    public Product updateProduct(Product product)
}