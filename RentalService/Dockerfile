FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY build/libs/RentalService-0.0.1-SNAPSHOT.jar app.jar
RUN jar tf app.jar | grep TestController || true
EXPOSE 8080
CMD ["java","-jar","app.jar"]
