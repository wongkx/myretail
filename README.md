# myRetail

1. Install Gradle (https://gradle.org/install/)

2. Clone the repository using a Git client.

3. Build the application
    * Command line: "gradle build" or "./gradlew build"
    
4. Start the application
    * Command line: "gradle run" or "./gradlew run"
    
5. GET endpoint (you can test in Postman or a browser)
    * http://localhost:3000/api/v1/products/13860428
    
6. PUT endpoint (you can test in Postman)
    * http://localhost:3000/api/v1/products/13860428
    ```
    {
        "id": "13860428",
        "name": "Big Lebowski",
        "currentPrice": "99.99",
        "currencyCode": "USD"
   }
   ```

7. To validate the result, AWS provides a link where you can directly issue GET and PUT commands.
    * https://search-myretail-plefatlmlke5x5vfmkndd7qnf4.us-east-1.es.amazonaws.com/_plugin/kibana/app/kibana#/dev_tools/console?_g=()
    
    * Some useful commands:
    ```
   GET _search
   {
     "query": {
       "match_all": {}
     }
   }
   
   GET /_cat/indices
   
   GET _search
   {
     "query": {
       "match": {
         "id": "13860428"
       }
     }
   }
   
   POST myretail/_doc/13860429
   {
     "type": "product",
     "id": "13860429",
     "name": "Big Lebowski 2",
     "currentPrice": "19.99",
     "currencyCode": "USD"
   }
    ```
8. Unit tests:
    * ProductControllerSpec
    * ProductRepositorySpec