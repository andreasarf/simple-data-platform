FROM maven:3.9.6-amazoncorretto-21 AS maven

## Build the-app
FROM maven AS build-local
COPY . /sources
RUN curl -L "https://drive.usercontent.google.com/download?id=1QLBGFOoKw_3-iM58q4unWfwHmPqfnrYr&confirm=yes" -o /sources/dataset.parquet
WORKDIR /build
RUN mvn -q -f /sources/pom.xml clean verify
RUN cp /sources/target/*.jar ./application.jar
# print test coverage
RUN awk -F"," '{ instructions += $4 + $5; covered += $5 } END { print covered, "/", instructions, " instructions covered"; print 100*covered/instructions, "% covered" }' /sources/target/site/jacoco/jacoco.csv

## Extract built jar file into layers
FROM build-local AS extractor
WORKDIR /build/layers
RUN java -Djarmode=layertools -jar /build/application.jar extract

## Create final docker image.
FROM gcr.io/distroless/java21-debian12
WORKDIR /app
COPY --from=extractor sources/dataset.parquet ./
COPY --from=extractor build/layers/dependencies/ ./
COPY --from=extractor build/layers/spring-boot-loader ./
COPY --from=extractor build/layers/snapshot-dependencies/ ./
COPY --from=extractor build/layers/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
