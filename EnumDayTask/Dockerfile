FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn package -DskipTests -B

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

COPY --from=build /app/target/EnumDayTask-1.0-SNAPSHOT.jar app.jar

EXPOSE 9060
ENTRYPOINT ["java", "-jar", "app.jar"]
