
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
