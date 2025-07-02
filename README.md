The implementation of the web server that crawls data for a particular reference.
Listens for incoming requests.
A request consists of a reference and a number of levels.
Information is saved into a mongo Database.
Each Json from a collection has a shortened link reference and a html response body as a string. 