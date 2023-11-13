# Knowledge Sharing Platform Project
The purpose of this project is to provide a knowledge sharing platform.

In the current version TED talks listed in a csv file are shared.

# How To Run
Run the below command to start the application.

```sh
java -jar knowledge-<version>.jar [csv_data_file]
```
The optional 'csv_data_file' parameter can be given as an argument to define a knowledge data file to load to the database. The file name extension shall be .csv and the format of the file lines shall be as below. If no 'csv_data_file' is given, the application runs without any initial data loading.
```sh
<title>,<author>,<date>,<views>,<likes>,<link>
```

## Unit and Integration Tests
The integration and unit tests can be run sperately. Use below commands from the project root directory to run the tests.

To run all tests:
```sh
mvn test 
```
To run only the unit tests:
```sh
mvn test -Dtest='**/*Test'
```
To run only the integration tests:
```sh
mvn test -Dtest='**/*IT'
```

## Sample API Calls Using curl

Initially, a POST call to retrieve a token should be made:

```sh
curl -v -X POST  http://localhost:8080/api/token -u "admin:password"
```

The retrieved token can be used in all further Knowledge API calls. 

To search on knowledge data:
```sh
curl -v 'http://localhost:8080/api/knowledge?author=Jonathan%20Haidt&viewCount=100000&minimumLikes=50000' -H 'Authorization: Bearer {JWT_TOKEN}'
```
You can use the below query parameters to search on the knowledge data. The API requires the user to have 'READ' authority.

* author: Input for search criteria on author field. Tries to make case-insensitive exact length match.
* title: Input for search criteria on title field. Tries to make case-insensitive exact length match.
* minimumViews: Input for search criteria on viewCount. The criteria match rows greater than or equal
                to this value.
* minimumLikes: Input for search criteria on likeCount. The criteria match rows greater than or equal
                to this value.

The API returns the results as paged lists. 

To create new knowledge entry you can use the below API. The API requires the user to have 'WRITE' authority.

```sh
curl -v 'http://localhost:8080/api/knowledge' -H 'Authorization: Bearer {JWT_TOKEN}' \
    -H 'Content-Type: application/json' \
    -d '{"title":"The power of vulnerability","author":"Brene Brown","dateMonth":"January 2021","viewCount":1800000,"likeCount":159000,"link":"https://www.ted.com/talks/brene_brown_the_power_of_vulnerability"}'	
```

To update a knowledge entry you can use the below API. The {id} parameter is the primary key value of the knowledge entity to be updated. The API requires the user to have 'WRITE' authority.

```sh
curl -v -X PUT 'http://localhost:8080/api/knowledge/{id}' -H 'Authorization: Bearer {JWT_TOKEN}' \
    -H 'Content-Type: application/json' \
    -d '{"title":"The power of vulnerability","author":"Brene Brown","dateMonth":"January 2021","viewCount":1800000,"likeCount":159000,"link":"https://www.ted.com/talks/brene_brown_the_power_of_vulnerability"}'	
```

To delete a knowledge entry you can use the below API. The {id} parameter is the primary key value of the knowledge entity to be updated. The API requires the user to have 'WRITE' authority.

```sh
curl -v -X DELETE 'http://localhost:8080/api/knowledge/{id}' -H 'Authorization: Bearer {JWT_TOKEN}'
```

