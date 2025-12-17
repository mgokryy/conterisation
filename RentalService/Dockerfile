FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/RentalService-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-Xmx300m", "-Xms300m", "-XX:TieredStopAtLevel=1", "-noverify", "-jar", "app.jar"]