package com.myretail.service

import static io.searchbox.core.Get.*
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder

import com.fasterxml.jackson.databind.ObjectMapper
import com.myretail.domain.Product
import com.myretail.repository.ProductRepository
import com.myretail.view.ProductView
import io.searchbox.client.JestClient
import io.searchbox.client.JestResult
import io.searchbox.core.DocumentResult
import io.searchbox.core.Get
import io.searchbox.core.Search
import io.searchbox.core.SearchResult
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.elasticsearch.action.admin.indices.get.GetIndexResponse
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.action.update.UpdateResponse
import org.elasticsearch.client.Client
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.Response
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indices.GetIndexRequest
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import com.myretail.settings.ApplicationSettings

@Service
class ProductServiceImpl implements ProductService {

    private RestHighLevelClient client

    private ObjectMapper objectMapper

//    @Autowired
//    HttpClient httpClient

    @Autowired
    RestTemplate restTemplate

    @Autowired
    JestClient jestClient

    @Autowired
    Client client

    @Autowired
    ProductRepository productRepository

    @Autowired
    public ProductServiceImpl(RestHighLevelClient client, ObjectMapper objectMapper) {
        this.client = client
        this.objectMapper = objectMapper
    }

//    public String createProduct(Product product) {
//        Map<String, Object> documentMapper = objectMapper.convertValue(product, Map.class)
//
//        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, product.getId())
//                .source(documentMapper);
//    }

    //WORKING!!! (but messy)
//    public Product getProduct(String productId) {
//        RestClient restClient = client.getLowLevelClient()
//        Response response = restClient.performRequest("GET", "/_search?q=${productId}")
//        String rawBody
//        if (response) {
//            rawBody = EntityUtils.toString(response.getEntity())
//        }
//        rawBody
//    }

//    public Product getProduct(String productId) {
//        DocumentResult builder = jestClient.execute(new Builder("myretail", productId).build())
//        Product result = builder.getSourceAsObject(Product.class)
//        return result
//    }

    public String getProductName(String productId) {
        String productName = productRepository.getProductName(productId)
        return productName
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

    private String buildRequest(String productId) {
        XContentBuilder requestBuilder = jsonBuilder()
                .startObject()
                .field("id", productId)
                .field("size", 0)
                .startObject("query")
        QueryBuilders.termQuery("id", productId).doXContent(requestBuilder, null)
//        requestBuilder.endObject()
    }

//    public Product getProduct(String productId) {
//        Product result = restTemplate.getForObject(createUrl(ApplicationSettings.ELASTICSEARCH_URL, productId), Product.class)
//        return result
//    }

    private String createUrl(String url, String id) {
        StringBuilder sb = new StringBuilder("https://"+url)
        sb.append("/_search?q=/${id}")
        return sb.toString()
    }

    public Product updateProduct(ProductView productView) {
        Product product = convertFromView(productView)
        UpdateRequest updateRequest = new UpdateRequest("myretail", "_doc", product.id)
        Map<String, Object> documentMapper =
                objectMapper.convertValue(product, Map.class);

        updateRequest.doc(documentMapper);

        UpdateResponse updateResponse =
                client.update(updateRequest, RequestOptions.DEFAULT);

        println updateResponse
                .getResult()
                .name()
    }

    private Product convertFromView(ProductView productView) {
        return new Product(
                id: productView.id,
                name: productView.name,
                currentPrice: productView.currentPrice,
                currencyCode: productView.currencyCode
        )
    }
}
