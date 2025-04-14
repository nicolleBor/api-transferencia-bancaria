# Use a imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Defina o diretório de trabalho
WORKDIR /app

# Copie o JAR gerado para dentro do container
COPY target/api-transferencia-bancaria.jar /app/api-transferencia-bancaria.jar

# Comando para rodar a aplicação
CMD ["java", "-jar", "api-transferencia-bancaria.jar"]
