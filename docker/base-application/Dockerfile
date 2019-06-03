FROM maven:3-jdk-8 as builder

ADD pom.xml /pom.xml

ADD src /src

RUN ["mvn", "install"]

FROM openjdk:8

COPY --from=builder /target /target

COPY docker/base-application/entrypoint.sh /target

COPY templates /target/templates

RUN chmod +x target/entrypoint.sh

ENTRYPOINT ["target/entrypoint.sh"]
