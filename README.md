# Premier Service

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

# Deuxieme Service

## 1. Creer un dossier PrenomService

Créer un nouveau dossier nommé PrenomService à la racine du projet.

```bash
mkdir PrenomService
cd PrenomService
```

## 2. Création du service php

Créer un fichier index.php qui retourne mon prénom via une requête HTTP GET.

```bash
<?php
echo "Bonjour, je m'appelle Marie-grace"; 
```

## 3. Création du Dockerfile PHP

Créer un fichier Dockerfile dans le dossier PrenomService

```bash
FROM php:8.2-apache

COPY index.php /var/www/html/index.php

EXPOSE 80
```

## 4. Creation de l'image Docker

Lancer la création de l’image Docker :

```bash
docker build –t prenomservice .
```

## 5. Tester le programme avec Docker

```bash
docker run -p 8082:80 prenomservice
```

Vérifier dans votre navigateur à l’adresse :

[http://localhost:8082]

## 6. Publier l'image dans Docker Hub

Se connecter au Docker Hub

```bash
docker login
```

Faire un tag de l'image :

```bash
docker tag prenomservice mgokry/prenomservice:latest
```

Faire un push de l'image dans le Hub:

```bash
docker push mgokry/prenomservice:latest
```

# kubernetes-minikube  

Minikube est un outil permettant d’exécuter Kubernetes localement.  
Il permet de lancer un cluster Kubernetes mono-nœud sur un ordinateur personnel (Windows, macOS ou Linux) afin de tester Kubernetes ou de travailler en environnement de développement.

## Présentation du projet

Ce projet contient **deux microservices** :

- **rentalservice** : microservice Java (Spring Boot)
- **prenomservice** : microservice PHP

Le microservice `rentalservice` communique avec `prenomservice` via HTTP.  
L’objectif est de déployer ces microservices avec Docker, puis avec Kubernetes, et enfin de mettre en place une **gateway** avec **Ingress (NGINX)**.

---

## Communication entre microservices avec Docker Compose

Un fichier `docker-compose.yml` est utilisé pour lancer les deux microservices et permettre leur communication.

Le microservice `rentalservice` appelle le microservice `prenomservice` via son nom de service Docker.

Exemple d’URL utilisée dans le fichier `application.properties` :

```
customer.service.url=http://prenomservice
```

Lancer les services avec :

```bash
docker-compose up
```

Tester :

```
http://localhost:8080/customer/Jean
```

---

## Création des images Docker

### Compilation du projet Java

Sous Linux :
```bash
./gradlew build
```

Sous Windows :
```powershell
.\gradlew build
```

### Construction des images Docker

```bash
docker build -t mgokry/rentalservice ./RentalService
docker build -t mgokry/prenomservice ./PrenomService
```

---

## Publication des images sur Docker Hub

Connexion à Docker Hub :

```bash
docker login
```

Publication des images :

```bash
docker push mgokry/rentalservice:latest
docker push mgokry/prenomservice:latest
```

---

## Déploiement Kubernetes avec des fichiers YAML

Les commandes Kubernetes sont remplacées par des fichiers YAML.

Le fichier Kubernetes contient :

- un **Deployment** pour chaque microservice
- un **Service** pour chaque microservice

Application :

```bash
kubectl apply -f k8s-deployments-services.yaml
```

Vérification :

```bash
kubectl get pods
kubectl get svc
```

---

## Communication entre microservices dans Kubernetes

Dans Kubernetes, la communication se fait via le **nom du Service**.

Dans `application.properties` :

```
customer.service.url=http://prenomservice:80
```

Test depuis un pod :

```bash
kubectl exec -it <pod-rentalservice> -- curl http://prenomservice
```

La communication entre les microservices est fonctionnelle.

---

## Gateway : routage des requêtes avec Ingress (NGINX)

Une gateway est mise en place afin de fournir un **point d’entrée unique** devant les microservices.

Ingress permet de router les requêtes HTTP vers les services Kubernetes.

---

## Installation de NGINX Ingress Controller

Activation de l’addon Ingress :

```bash
minikube addons enable ingress
```

Vérification :

```bash
kubectl get pods -n ingress-nginx
```

---

## Fichier Ingress

Un fichier `ingress.yaml` est ajouté au projet.  
Il définit les règles de routage suivantes :

- `/` → `rentalservice`
- `/prenom` → `prenomservice`

Application :

```bash
kubectl apply -f ingress.yaml
```

Vérification :

```bash
kubectl get ingress
kubectl describe ingress
```

---

## Accès à la gateway avec Minikube

Sous Windows, l’accès externe à l’Ingress est réalisé via Minikube.

Commande utilisée :

```bash
minikube service ingress-nginx-controller -n ingress-nginx
```

Cette commande expose le contrôleur Ingress via un **NodePort**.  
Les requêtes HTTP sont ensuite routées vers les microservices selon les règles définies dans le fichier `ingress.yaml`.

---

## Conclusion

Ce projet met en œuvre :

- la communication entre microservices
- Docker et Docker Compose
- Kubernetes avec des fichiers YAML
- des Services Kubernetes
- une gateway basée sur **NGINX Ingress**

Les microservices sont accessibles via un point d’entrée unique, assurant le routage des requêtes HTTP dans le cluster Kubernetes.

---

## Suppression des ressources

```bash
kubectl delete -f k8s-deployments-services.yaml
kubectl delete -f ingress.yaml
```
