package com.myretail.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.domain.Product
import com.myretail.repository.ProductRepository
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.action.update.UpdateResponse
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ProductServiceImpl implements ProductService {

    private RestHighLevelClient client

    private ObjectMapper objectMapper

    @Autowired
    RestTemplate restTemplate

    @Autowired
    ProductRepository productRepository

    @Autowired
    public ProductServiceImpl(RestHighLevelClient client, ObjectMapper objectMapper) {
        this.client = client
        this.objectMapper = objectMapper
    }

    public Product getProduct(String productId) {
        Product product = productRepository.getProduct(productId)

        SearchRequest searchRequest = new SearchRequest()
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
        searchSourceBuilder.query(QueryBuilders.queryStringQuery(productId))
        searchRequest.source(searchSourceBuilder)

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)
        Product productElasticEntity = getSearchResult(searchResponse)
        if (productElasticEntity) {
            product.currentPrice = productElasticEntity.currentPrice
            product.currencyCode = productElasticEntity.currencyCode
        }

        return product
    }

    private Product getSearchResult(SearchResponse searchResponse) {
        SearchHit[] searchHit = searchResponse.getHits().getHits();

        List<Product> products = new ArrayList<>();

        searchHit.each { it ->
            products.add(objectMapper.convertValue(it.getSourceAsMap(), Product.class))}

        //there should only be one product matching on one ID, which is assumed to be unique
        return products ? products[0] : null
    }

    public Product updateProduct(Product product) {
        UpdateRequest updateRequest = new UpdateRequest("myretail", "_doc", product.id).fetchSource(true)
        String documentMapper = objectMapper.writeValueAsString(product);
        updateRequest.doc(documentMapper, XContentType.JSON)
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT)
        Map<String, Object> sourceAsMap = updateResponse.getGetResult().sourceAsMap()

        return convertFromMap(sourceAsMap)
    }

    private Product convertFromMap(Map<String, Object> sourceAsMap) {
        return new Product(
                id: sourceAsMap.id,
                name: sourceAsMap.name,
                currentPrice: sourceAsMap.currentPrice,
                currencyCode: sourceAsMap.currencyCode
        )
    }
}
