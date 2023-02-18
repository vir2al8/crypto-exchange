FROM arm64v8/eclipse-temurin:17-jre-ubi9-minimal

RUN mkdir /opt/app
WORKDIR /opt/app
COPY common/build/libs/common-*.jar app.jar
EXPOSE 8080

CMD ["java", "-XX:+UseContainerSupport", "-Dfile.encoding=UTF8", "-jar", "app.jar"]