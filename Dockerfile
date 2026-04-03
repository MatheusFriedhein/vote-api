FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src

# roda testes e gera o jar; se falhar, a imagem não é criada
RUN mvn -B clean package

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /app/target/voting-api-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
