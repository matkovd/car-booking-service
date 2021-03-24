## Description
Service is running on port 8080, swagger-ui is available under /swagger-ui.html
All controllers are shown there

## Running locally
### Everything from docker registry
```
docker-compose up
```
### MySQL Only
```
docker-compose up -f docker-compose-mysql.yml
```

## Building docker image
### With Gradle and custom dockerfile
```
./gradlew docker
```
### Spring boot way with Spring base image
```
./gradlew bootBuildImage --imageName 
```

## Env variables



## Improvements:
1. Start reusing customer details by phone number / Id number
2. Restrict person with same contact info to pick to cars at the same time
3. Make booking period configurable not only through env variable
4. Replace schema.sql with liquibase 
5. Make start and end time optional params
## Questionable places:
1. Same code for getAllCars in both external / internal api. Personally prefer redundancy over creating dependency between different package
2. Create static fromDto / toDto constructors for models. Might be controversial  