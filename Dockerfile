FROM maven:3.9-amazoncorretto-21-alpine AS builder

WORKDIR /build

COPY ./.mvn/wrapper ./.mvn/wrapper
COPY ./src ./src
COPY ./pom.xml ./

RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine

ARG SPRING_DATASOURCE_URL

WORKDIR /home/hire4j

COPY --from=builder /build/target/hire4j-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "hire4j-0.0.1-SNAPSHOT.jar", "--spring.datasource.url=${SPRING_DATASOURCE_URL}"]
