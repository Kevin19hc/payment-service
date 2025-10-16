FROM openjdk:17-slim

COPY target/payment-service-0.0.1-SNAPSHOT.jar app.jar

#Ejecución de la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]