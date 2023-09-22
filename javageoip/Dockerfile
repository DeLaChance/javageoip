# Build stage
FROM eclipse-temurin:20-jdk-alpine as build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# RUN ./mvnw dependency:go-offline
COPY ../../../.m2/ /root/.m2/

COPY src src

RUN ./mvnw install -DskipTests=true -Dskip.integration.tests=true

USER root
RUN mvn package

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


# Application stage.
FROM eclipse-temurin:20-jre-alpine AS javageoip
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency

# Copy the maven artifacts from the build stage.
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app/classes

EXPOSE 8080

ENTRYPOINT ["java","-cp","app:app/lib/*", "-Dspring.profiles.active=prd", "nl.lucien.Application" ]
