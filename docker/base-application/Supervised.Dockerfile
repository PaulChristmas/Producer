FROM python:3.7 AS downloader
# configure download
ARG SPV_VERSION=0.1.4
ARG ART_USER
ARG ART_PASS
ENV ART_REPO=https://services.crplab.ru/artifactory/buldozer/${SPV_VERSION}/supervisor-linux-amd64
# download supervisor binary
RUN wget --http-user ${ART_USER} --http-password ${ART_PASS} -O /opt/supervisor "${ART_REPO}"
RUN chmod +x /opt/supervisor

#final image
FROM maven:3-jdk-8 as builder
ADD pom.xml /pom.xml
ADD src /src
RUN ["mvn", "install"]

FROM openjdk:8
COPY --from=builder /target /target
COPY --from=downloader /opt/supervisor /opt/supervisor

ENTRYPOINT ["/opt/supervisor"]
CMD ["--bin", "java", "--args", "-jar,target/base-application-0.0.1-SNAPSHOT.jar"]