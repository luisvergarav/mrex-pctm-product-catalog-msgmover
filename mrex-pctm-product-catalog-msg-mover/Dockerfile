FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=/build/libs/mrex-pctm-product-catalog-msg-mover-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} mrex-pctm-product-catalog-msg-mover-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mrex-pctm-product-catalog-msg-mover-0.0.1-SNAPSHOT.jar"]