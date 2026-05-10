FROM gradle:8.6-jdk21 AS builder
WORKDIR /home/gradle/project

COPY . /home/gradle/project

RUN chmod +x ./gradlew || true
RUN ./gradlew --no-daemon bootJar -x test

FROM eclipse-temurin:21-jre
WORKDIR /app

# copy built jar
COPY --from=builder /home/gradle/project/build/libs/*.jar /app/banking.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/banking.jar"]
