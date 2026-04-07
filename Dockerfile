# ===== Stage 1: Build =====
FROM gradle:8.8-jdk21 AS builder

WORKDIR /workspace

COPY . .

RUN gradle :app:bootJar --no-daemon

# ===== Stage 2: Runtime =====
FROM eclipse-temurin:21-jre

WORKDIR /app

RUN addgroup --system spring && adduser --system spring --ingroup spring

COPY --from=builder /workspace/app/build/libs/*.jar app.jar

RUN mkdir -p /app/logs && chown -R spring:spring /app

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
