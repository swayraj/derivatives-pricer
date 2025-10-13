#Stage1:Creating a runnable jar

#Using an official Maven image that include java-21
FROM maven:3.9-eclipse-temurin-21 AS build

#Setup the working directory
WORKDIR /app

#Copy the Maven project file to leverage Docker's layer caching
COPY pom.xml .

#Copy the maven wrapper files
COPY .mvn .mvn

#Copy the application's source code
COPY src src

#Run the maven command to build the project and create an executable .jar file
RUN mvn package -DskipTests


#Stage2: Executing the jar
FROM eclipse-temurin:21-jre-jammy

# Set the working directory.
WORKDIR /app

# Copy only the compiled .jar file from the 'build' stage into this final image.
# The path to the jar file is standard for Spring Boot projects.
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080, which is the default port for Spring Boot applications.
EXPOSE 8080

# The command to run when the container starts.
ENTRYPOINT ["java", "-jar", "app.jar"]



