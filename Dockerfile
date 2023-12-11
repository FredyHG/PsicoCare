FROM gradle:latest

WORKDIR /app

COPY build.gradle.kts .
RUN gradle build --no-daemon || echo "Gradle build failed!"

ADD . .

ENTRYPOINT ["gradle", "run"]