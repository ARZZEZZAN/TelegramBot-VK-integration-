FROM adoptopenjdk/openjdk16:ubi
RUN mkdir /opt/app
ENV BOT_NAME=JavaArzbot
ENV BOT_TOKEN=6158289507:AAFRYqGyjLtNgGHQcfNP3d4tD22jyRgJybg
COPY japp.jar /opt/app
ENTRYPOINT ["java", "-Dbot.username=${BOT_NAME}", "-Dbot.token=${BOT_TOKEN}", "-jar", "/app.jar"]
