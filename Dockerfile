# -----------------------------------------------------------------------------
# Stage 1: Build (Compila o código usando uma imagem Maven completa)
# -----------------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copia apenas o pom.xml primeiro para aproveitar o cache de dependências do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e faz o build
COPY src ./src
RUN mvn clean package -DskipTests

# -----------------------------------------------------------------------------
# Stage 2: Runtime (Roda a aplicação usando apenas o JRE, muito mais leve)
# -----------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Cria um usuário não-root por segurança (Best Practice vital)
RUN addgroup -S pismo && adduser -S pismo -G pismo
USER pismo:pismo

# Copia o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Configurações de execução
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]