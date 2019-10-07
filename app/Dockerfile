FROM azul/zulu-openjdk-alpine:11.0.4

RUN apk update && \
    apk upgrade

RUN apk add --update tzdata
ENV TZ=Europe/Zurich

# Clean APK cache
RUN rm -rf /var/cache/apk/*

RUN mkdir /opt/app/

COPY build/libs/app-all.jar /opt/app/app.jar

CMD ["java", "-jar" ,"/opt/app/app.jar"]