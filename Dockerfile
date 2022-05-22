FROM openjdk:11
EXPOSE 8080
ADD build/libs/kumbang-api-server-0.0.1-SNAPSHOT.jar kumbang-api-server.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "kumbang-api-server.jar"]