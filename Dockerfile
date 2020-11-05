FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/minesweeper-api-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} minesweeper-api.jar
CMD java -jar minesweeper-api.jar

