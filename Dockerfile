FROM gradle AS build
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME

COPY . .
RUN gradle clean assemble

FROM adoptopenjdk/openjdk11:alpine-jre
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME
COPY --from=build $APP_HOME/build/libs/gradetracker.jar .

EXPOSE 8080
CMD java -jar gradetracker.jar