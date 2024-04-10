FROM public.ecr.aws/docker/library/openjdk:21-jdk

RUN mkdir /opt/summary-worker



WORKDIR /opt/summary-worker

ADD ./build/libs/worker-0.0.1-SNAPSHOT.jar worker.jar



CMD ["java", "-jar", "worker.jar"]
