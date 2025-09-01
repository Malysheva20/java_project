# -------- Stage 1: Build --------
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy pom and source (expects your Step 3 project here)
COPY pom.xml ./
COPY src ./src

# Build a fat jar (skip tests to speed up container build)
RUN mvn -q -DskipTests package

# -------- Stage 2: Run --------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy built jar from builder
COPY --from=builder /app/target/*.jar /app/app.jar

# Environment: use H2 profile in Step 4
ENV SPRING_PROFILES_ACTIVE=pg
EXPOSE 8080

# Optional JVM flags can be overridden at run time
ENV JAVA_OPTS=""

ENTRYPOINT ["sh","-c","java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-pg} $JAVA_OPTS -jar /app/app.jar"]
