package com.myretail.controller

import com.myretail.domain.Product
import com.myretail.exception.InvalidArgumentException
import com.myretail.service.ProductService
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    ProductService productService

    @ApiImplicitParams([@ApiImplicitParam(name = "productId", dataType = "string", paramType = "path", required = true)])
    @RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
    public ResponseEntity<Product> getProduct(
            @PathVariable("productId") String productId
    ) {
        Product product = productService.getProduct(productId)
        return new ResponseEntity<Product>(product, HttpStatus.OK)
    }

    @RequestMapping(value = "/products/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<Product> updateProduct(
            @PathVariable("productId") String productId,
            @ApiParam(name = "product", value = "product update", required = true) @RequestBody Product product) {

        if (product.id != productId) {
            throw new InvalidArgumentException("Mismatching productIds")
        }
        Product updatedProduct = productService.updateProduct(product)
        return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK)
    }
}