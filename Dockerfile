FROM eclipse-temurin:25-jdk AS builder

WORKDIR /workspace

COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
RUN ./gradlew --version

COPY src ./src
RUN ./gradlew bootJar --no-daemon

FROM gcr.io/distroless/java25-debian13

WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar

USER nonroot:nonroot
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
