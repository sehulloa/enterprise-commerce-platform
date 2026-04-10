# --------- BUILD STAGE ---------
FROM gradle:8.7-jdk21 AS builder

WORKDIR /app
COPY . .

RUN gradle clean bootJar --no-daemon

# --------- RUNTIME STAGE ---------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copiar jar generado
COPY --from=builder /app/app/build/libs/*.jar app.jar

# Puerto estándar
EXPOSE 8080

# Variables por defecto (pueden ser sobreescritas)
ENV SPRING_PROFILES_ACTIVE=prod

# Healthcheck para contenedor
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de arranque
ENTRYPOINT ["java","-jar","app.jar"]