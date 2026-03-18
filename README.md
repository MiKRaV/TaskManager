# Task Manager

Java: 21 (Eclipse Temurin 21.0.7)

Spring Boot: 3.5.11

## Run

### Database

* Go to resources folder:
```
cd src/main/resources
```

* Add .env file (you can copy .env.sample)

* Run docker:
```
docker-compose up -d
```

### API

```
mvn clean install
mvn spring-boot:run
```

Swagger: http://localhost:8080/api/swagger-ui/index.html

### Tests

```
mvn test
```

## Postman collection

Collection is placed in `/postman_collection` folder.