FROM openjdk:11
COPY build/libs/car-booking-service-*.jar car-booking-service.jar
EXPOSE 8080
CMD ["java", "-jar", "car-booking-service.jar"]