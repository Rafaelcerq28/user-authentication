# FROM openjdk:21
# COPY target/*.jar app.jar
# EXPOSE 5000
# ENTRYPOINT ["java","-jar","/app.jar"]

# Usar uma imagem base do Maven com o JDK 17 para construir a aplicação
# FROM maven:3.8.1-openjdk-21 AS build
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
# RUN mvn clean package

# # Usar uma imagem base do JDK 17 para rodar a aplicação
# FROM openjdk:21
# WORKDIR /app
# #Checar se no POM em <packaging> esta como jar
# COPY target/app-0.0.1-SNAPSHOT.jar app.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "app.jar"]

# Etapa de build
FROM maven:3.8.1-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de runtime com imagem leve
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
