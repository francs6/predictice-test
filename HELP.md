# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.4/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.4/maven-plugin/reference/html/#build-image)
* [Spring Data Elasticsearch (Access+Driver)](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#data.nosql.elasticsearch)
* [Spring Data ES (Repo API)](https://docs.spring.io/spring-data/elasticsearch/reference/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#web)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Build Docker image

```shell
docker compose up --build --force-recreate
```

### Swagger

[Swagger API documentation](http://localhost:8080/swagger-ui/index.html#/)

### Request sample

GET http://127.0.0.1:8080/albums

GET http://127.0.0.1:8080/albums?year=1998&searchText=your life

GET http://127.0.0.1:8080/albums/year/facet

GET http://127.0.0.1:8080/album/9ac00c65-2380-4e71-b07a-72c537894d00
