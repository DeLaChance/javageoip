# Build stage
FROM maven:3.8.7-openjdk-18-slim as build

WORKDIR /workspace/app

WORKDIR /build

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src/ /build/src/

USER root
RUN mvn install -DskipTests=true -Dskip.integration.tests=true

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


# Application stage.
FROM eclipse-temurin:18-jre-alpine AS javageoip
VOLUME /tmp

ARG DEPENDENCY=/build/target/dependency

# Copy the maven artifacts from the build stage.
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app/classes

EXPOSE 8080

ENTRYPOINT ["java","-cp","/app/classes:/app/lib/*", "-Dspring.profiles.active=prd", "nl.lucien.Application" ]
