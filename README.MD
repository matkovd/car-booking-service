## Description
Service is running on port 8080, swagger-ui is available under /swagger-ui.html
All controllers are shown there  

Service is running
## Running locally
### Everything from docker registry
**NB** It will require authorization into Github Packages
1. Create personal access token with rights to read:packages
2. ``` docker login docker.pkg.github.com --username your_username``` and use github access token as a password
```
docker-compose up
```
### MySQL Only
```
docker-compose -f docker-compose-mysql.yml up
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

## Pushing docker image
### With Gradle and custom dockerfile
```
./gradlew dockerTagsPush
```


## Env variables
|Variable   | Usage  |
|---|---|
| MYSQL_HOST  |  MySQL host  | 
| MYSQL_DB  | MySQL database  |
| MYSQL_USERNAME |MySQL username   |
| MYSQL_PASSWORD  | MySQL password  |
| BOOKING_PERIOD | Booking period in ISO8601 format   |


## Improvements:
1. Start reusing customer details by phone number / Id number
2. Restrict person with same contact info to pick to cars at the same time
3. Make booking period configurable not only through env variable
4. Replace schema.sql with liquibase 
5. Make start and end time optional params
## Questionable places:
1. Same code for getAllCars in both external / internal api. Personally prefer redundancy over creating dependency between different package
2. Create static fromDto / toDto constructors for models. Might be controversial  