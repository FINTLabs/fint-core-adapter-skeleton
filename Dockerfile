FROM gradle:8.6-jdk17 as builder
USER root
COPY . .
RUN gradle --no-daemon build

FROM eclipse-temurin:17-jre
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/libs/fint-core-adapter-skeleton-*.jar /data/app.jar
ENTRYPOINT ["java", "-jar", "/data/app.jar"]
