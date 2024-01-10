FROM openjdk:17-jre-slim

WORKDIR /opt/sanhab-inquiry

EXPOSE 7030

CMD ["java", "-jar", "sanhab-inquiry-1.0.0-SNAPSHOT.jar"]