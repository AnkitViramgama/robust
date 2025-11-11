FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy parent pom
COPY backend/pom.xml .

# Copy all module poms
COPY backend/core/pom.xml ./core/
COPY backend/user-management/pom.xml ./user-management/
COPY backend/role-management/pom.xml ./role-management/
COPY backend/menu-management/pom.xml ./menu-management/
COPY backend/plugin-loader/pom.xml ./plugin-loader/
COPY backend/main-app/pom.xml ./main-app/
COPY backend/example-product-plugin/pom.xml ./example-product-plugin/

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY backend/ .

# Build the application
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built jar
COPY --from=build /app/main-app/target/*.jar app.jar

# Create plugins directory
RUN mkdir -p /app/plugins

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
