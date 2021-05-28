# ------------------------------- START OF THE DEPENDENCY STAGE ------------------------------- #
FROM adoptopenjdk/maven-openjdk11 AS MAVEN_DEPS

WORKDIR /app

COPY application/spring/pom.xml application/spring/pom.xml
COPY frontend/pom.xml frontend/pom.xml
COPY deployment/pom.xml deployment/pom.xml
COPY releases/pom.xml releases/pom.xml
COPY resources/pom.xml resources/pom.xml
COPY pom.xml .


RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline

# ------------------------------- END OF DEPS STAGE - START OF THE BUILD STAGE ------------------------------- #
FROM adoptopenjdk/maven-openjdk11 AS MAVEN_BUILD

COPY --from=MAVEN_DEPS /root/.m2 /root/.m2
COPY --from=MAVEN_DEPS /app /build/app

COPY application/spring/src /build/app/application/spring/src
COPY frontend/src /build/app/frontend/src
COPY deployment/src /build/app/deployment/src
COPY releases/src /build/app/releases/src
COPY resources/src /build/app/resources/src

WORKDIR /build/app/

RUN mvn -B -e -o package

# ------------------------------- END OF BUILD STAGE - START OF THE RUNNABLE STAGE ------------------------------- #
FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

COPY --from=MAVEN_BUILD /build/app/application/spring/target/infra-services-app.jar /app

ARG spring_boot_profile=dev
ENV spring.profiles.active=${spring_boot_profile}

ARG java_opts
ENV JAVA_OPTS=${java_opts}

CMD java $JAVA_OPTS -jar ./infra-services-app.jar
