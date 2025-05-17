FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8081
ENV JAVA_OPTS="-Xmx2048m"
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar