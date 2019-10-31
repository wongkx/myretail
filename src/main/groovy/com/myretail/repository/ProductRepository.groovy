package com.myretail.repository


import com.myretail.domain.Product
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder

@Component
class ProductRepository {
    public static final hostUrl = "redsky.target.com"
    public static final urlParams = "?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics"

    public String result

    @Autowired
    RestTemplate restTemplate

//    public ProductRepository(RestTemplateBuilder restTemplateBuilder) {
//        restTemplate = restTemplateBuilder.rootUri(hostUrl).build()
//    }

    public Product getProduct(String productId) {
        Product product = new Product(id: productId)
        product.name = getProductName(productId)
        return product
    }

    public String getProductName(String productId) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("https").host(hostUrl).path("/v2/pdp/tcin/${productId}/${urlParams}").build()
        ResponseEntity<String> response = restTemplate.exchange(uriComponents.toUriString(), HttpMethod.GET, null, String.class)
        JSONObject json = new JSONObject(response.getBody())
        return parseJson(json)
    }

    // handle unknown json structure, my only assumption is that the product name is "title" inside "product_description"
    private String parseJson(JSONObject jsonObject) {
        if (result) {
            return result
        }
        Set<String> currentKeys = jsonObject.keySet()
        currentKeys.each {
            if (it.equals("product_description")) {
                parseJson(jsonObject.get(it) as JSONObject)
            } else if (it.equals("title")) {
                result = jsonObject.get(it)
                return result
            } else if (jsonObject.get(it) instanceof JSONObject) {
                parseJson(jsonObject.get(it) as JSONObject)
            } else if (jsonObject.get(it) instanceof JSONArray) {
                jsonObject.get(it).each {
                    if (it instanceof JSONObject) {
                        parseJson(it)
                    }
                }
            }
        }
        return result
    }
}
