# Invext Request Distributor

## Overview

The **Invext Request Distributor** is a Spring Boot application designed to manage and distribute client requests to the appropriate teams within a customer service center. The application ensures that each agent handles a maximum of three simultaneous requests and manages request queues when agents are busy.

## Features

- **RESTful API** for submitting and managing client requests.
- **Dynamic Assignment** of requests to agents based on their availability and team specialization.
- **Request Queuing** when all agents in a team are busy.
- **Concurrency Handling** to manage multiple requests simultaneously.
- **Validation** of input data to ensure data integrity.
- **Exception Handling** with meaningful error responses.
- **Logging** for monitoring application behavior.
- **API Documentation** using Swagger/OpenAPI for easy API exploration.

## Technologies Used

- **Java 17 (Amazon Corretto 17)**
- **Spring Boot 3.3.4**
- **Gradle with Groovy DSL**
- **Spring Web**
- **Spring Validation**
- **Spring Boot Actuator**
- **Lombok**
- **SLF4J and Logback**
- **JUnit 5 and Mockito**
- **Swagger/OpenAPI**

## Prerequisites

- **Java 17** installed on your machine.
- **Gradle** installed (if not using the Gradle wrapper).
- An IDE like **IntelliJ IDEA** (recommended) or any other Java IDE.

## Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/MayLovatto/invext.git
   cd invext-request-distributor
   ```

2. **Build the Project**

   Using the Gradle wrapper included in the project:

   ```bash
   ./gradlew clean build
   ```

   Or, if you have Gradle installed globally:

   ```bash
   gradle clean build
   ```

## Running the Application

### Using Gradle

```bash
./gradlew bootRun
```

### Using the JAR File

After building the project, run the JAR file:

```bash
java -jar build/libs/invext-request-distributor-1.0.0.jar
```

The application will start on **`http://localhost:8080`**.

## Usage

### API Endpoints

#### **Submit a Request**

- **URL:** `POST /api/requests`
- **Description:** Submits a new client request to be processed.
- **Request Body:**

  ```json
  {
    "id": "R1",
    "subject": "Problemas com cartão"
  }
  ```

- **Responses:**
    - **200 OK:** Request submitted successfully.
    - **400 Bad Request:** Validation errors.

#### **Complete a Request**

- **URL:** `POST /api/requests/complete`
- **Description:** Marks a request as completed by an agent.
- **Query Parameters:**
    - `agentId` (string, required): The ID of the agent completing the request.
- **Responses:**
    - **200 OK:** Agent has completed a request.
    - **404 Not Found:** Agent not found.

### Accessing the API Documentation

The application uses Swagger/OpenAPI for API documentation.

- **URL:** `http://localhost:8080/swagger-ui/index.html`

## Testing

The project includes unit and integration tests to ensure functionality and reliability.

### Running Tests

Using Gradle:

```bash
./gradlew test
```

### Test Coverage

Tests cover the following aspects:

- **Service Layer:** Validates the request dispatching logic and agent assignment.
- **Controller Layer:** Ensures API endpoints respond correctly to various inputs.
- **Exception Handling:** Checks that errors are handled gracefully.

## Project Structure

```
invext-request-distributor/
├── build.gradle
├── settings.gradle
├── src
    ├── main
    │   ├── java
    │   │   └── com.invext.requestdistributor
    │   │       ├── Application.java
    │   │       ├── api
    │   │       │   ├── controller
    │   │       │   │   └── RequestController.java
    │   │       │   └── dto
    │   │       │       ├── RequestDTO.java
    │   │       │       └── ResponseDTO.java
    │   │       ├── domain
    │   │       │   ├── entity
    │   │       │   │   ├── Agent.java
    │   │       │   │   ├── Request.java
    │   │       │   │   └── Team.java
    │   │       │   └── service
    │   │       │       ├── DispatcherService.java
    │   │       │       └── impl
    │   │       │           └── DispatcherServiceImpl.java
    │   │       ├── infrastructure
    │   │       │   ├── config
    │   │       │   │   └── AppConfig.java
    │   │       │   └── exception
    │   │       │       ├── GlobalExceptionHandler.java
    │   │       │       └── AgentNotFoundException.java
    │   │       └── util
    │   │           └── mapper
    │   │               └── RequestMapper.java
    │   └── resources
    │       └── application.properties
    └── test
        └── java
            └── com.invext.requestdistributor
                ├── api.controller
                │   └── RequestControllerTest.java
                └── domain.service
                    └── DispatcherServiceTest.java
```

## Design Decisions

- **Domain-Driven Design (DDD):** Organized the project into logical layers (API, Domain, Infrastructure, Util) for better maintainability and scalability.
- **SOLID Principles:** Applied object-oriented design principles to enhance code quality.
- **DTOs and Validation:** Used Data Transfer Objects and validation annotations to ensure data integrity and decouple the API layer from the domain layer.
- **Exception Handling:** Implemented a global exception handler to manage errors gracefully and provide meaningful responses.
- **Testing:** Wrote comprehensive unit and integration tests to ensure functionality and reliability.
- **API Documentation:** Used Swagger/OpenAPI for interactive API documentation.
