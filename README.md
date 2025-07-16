The implementation of the web server that crawls data for a particular reference.
Listens for incoming requests and sends short acks to the sender. 
Consists of a few pools of workers that validate / parse / save/process successive links concurrently.
Prevents duplication 

1. MongoDB should be up and running
2. Add the next file: /src/main/resources/config.properties
3. Add the next parameters below to the file config.properties:
  DB_HOST=
  DB_PORT=
  DB_NAME=
  COLLECTION=

Final thoughts : 
  Add more flexibility to a client, such as passing a number of retries and a rate limit
