FROM openjdk:8-jdk-alpine

EXPOSE 8761

ENV SPRING_PROFILES_ACTIVE=dev \
    HOME=/root

COPY target/eureka-provider-0.0.1-SNAPSHOT.jar ${HOME}/app.jar

WORKDIR ${HOME}

CMD ["java", "-jar", "app.jar"]