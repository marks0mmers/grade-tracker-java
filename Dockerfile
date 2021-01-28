FROM gradle AS build
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME

COPY . .
RUN gradle clean build

FROM adoptopenjdk/openjdk11:alpine-jre
ENV ARTIFACT_NAME=gradetracker.jar
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME
COPY --from=build $APP_HOME/build/libs/$ARTIFACT_NAME .

EXPOSE 8080
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}