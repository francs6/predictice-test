# Utiliser une image de base avec Java et Maven préinstallés
FROM maven:3.8.4-openjdk-17-slim AS builder

# Copier les fichiers de code source dans le conteneur
COPY . /app

# Définir le répertoire de travail à utiliser pour la construction
WORKDIR /app

# Compiler et packager l'application avec Maven
RUN mvn clean package -DskipTests -Pdocker

FROM amazoncorretto:17-alpine-jdk
MAINTAINER fbedier.com
#COPY target/*.jar demo.jar
#ENTRYPOINT ["java","-jar","demo.jar"]
COPY --from=builder /app/target/*.jar /usr/local/bin/demo.jar
# Commande pour exécuter l'application lorsque le conteneur démarre
CMD ["java", "-Dspring.profiles.active=docker", "-jar", "/usr/local/bin/demo.jar"]
#EXPOSE 8080
