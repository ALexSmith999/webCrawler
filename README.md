WebCrawler. 
Crawls, parses, validate, and loads data.

Current setup configured to work with the next domains :  
"https://books.toscrape.com"  
"https://www.olympedia.org/""https://quotes.toscrape.com/"  
"https://sandbox.oxylabs.io/products"  
"https://www.scrapethissite.com/pages/"

Technical requirements:   
  Java 21 or higher  
  Apache Maven 3.9.x or later  
  MongoDB 4.4+  
  
Configure your /src/main/resources/config.properties and provide Mongo DB information:  
  DB_HOST=  
  DB_PORT=  
  DB_NAME=  
  COLLECTION=  

Mongo DB Documents :  
  db.COLLECTION.insertOne({  
      "link" : "https://books.toscrape.com"  
      "content": "<html><div</div></html>"  
      "uid": "s3d4f5",  
      "content_hash" : "s3d4f5"  
      "version": 1,  
  });  

Configurable parameters :   
  Required :  
    PORT_NUMBER - Should be provided always  
  Addtional :   
    NUM_OF_THREADS // number of threads(workers) per thread pool, default — number of cores  
    DEPTH // tree depth (main reference -> child references)  
    HTTP_RESPONSE_TIME_OUT // for how long to wait for a response  
    MAX_QUEUE_SIZE // limits a value to avoid memory leaks  
  
To Run a Project :  
  mvn clean install  
  java -jar webCrawler-1.0-VERSION.jar 1300 1 5 5 100000    

To test :  
  /src/main/java/Request   
      - Provide the host and a port number where an application is running     

