FROM public.ecr.aws/docker/library/openjdk:21-jdk

RUN mkdir /opt/summary-api

RUN microdnf update && microdnf upgrade -y
RUN microdnf install -y openssl

WORKDIR /opt/summary-api

ADD ./build/libs/api-0.0.1-SNAPSHOT.jar summary.jar



CMD ["java", "-jar", "summary.jar"]

EXPOSE 8080