The implementation of the web server that crawls data for a particular reference.
Listens for incoming requests and send short acks to a sender. 
Consists of a few pools of workers that validate / parse / save / process successive links concurrently.

