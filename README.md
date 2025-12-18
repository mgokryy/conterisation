## 1.Installation
La version Java JDK 21 etait deja installee sur ma machine.

## 2. Tester le programme sans docker

Builder le projet : 

```bash
./gradlew build
```

Tester le projet :
```bash
java -jar build/libs/RentalService-0.0.1-SNAPSHOT.jar
```

### 3. Creation du fichier Dockerfile 
Creer le fichier Dockerfile dans le dossier RentalService

```bash
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/RentalService-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-Xmx300m", "-Xms300m", "-XX:TieredStopAtLevel=1", "-noverify", "-jar", "app.jar"]
```

## 4. Creation de l'image Docker
Lancer la création de l’image Docker :

```bash
docker build –t rentalservice .
```

## 5. Tester le programme avec Docker 

```bash
docker run –p 8080:8080 rentalservice
```

Vérifier dans votre navigateur à l’adresse : 

[http://localhost:8080/bonjour]

## 6. Publier l'image dans Docker Hub

Se connecter au Docker Hub

```bash
docker login
```

Faire un tag de l'image :

```bash
docker tag rentalservice mgokry/rentalservice:latest
```



Faire un push de l'image dans le Hub:

```bash
docker push mgokry/rentalservice:latest
```
