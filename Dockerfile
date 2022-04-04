FROM openjdk:17-alpine
COPY ./* /barbershop_web_platform/
WORKDIR /barbershop_web_platform
RUN chmod +x ./gradlew
RUN ./gradlew :bootjar
WORKDIR /barbershop_web_platform/build/libs
RUN mv *.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]