FROM adoptopenjdk/openjdk11
COPY build/libs/microservice-*-all.jar microservice.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-XX:+IdleTuningGcOnIdle", "-Xtune:virtualized", "-jar", "microservice.jar"]