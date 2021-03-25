FROM adoptopenjdk/openjdk11
COPY build/libs/car-booking-service-*-all.jar car-booking-service.jar
EXPOSE 8080
CMD ["java", "-jar", "car-booking-service.jar"]