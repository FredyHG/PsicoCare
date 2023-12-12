FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8080

COPY --from=build /app/build/libs/PsicoCare-1.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]