FROM openjdk:8-jdk-alpine
VOLUME /tmp
RUN apk add --no-cache maven
COPY ./ /opt/backend/cookiejar/
WORKDIR /opt/backend/cookiejar
RUN chown -R 1000 /opt/backend/cookiejar
USER 1000
RUN mvn clean package spring-boot:repackage -Dmaven.test.skip=true
WORKDIR /opt/backend/cookiejar/target
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","cookiejar-0.0.1-SNAPSHOT.jar"]