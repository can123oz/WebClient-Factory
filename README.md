# WebClient Factory with Circuit Breaker

## Overview
This application is a small Java-based web client factory designed to handle web service requests using `Spring WebClient`. Its main goal is to catch `5xx` server-side errors (e.g., 500, 502) and manage failures using a custom-built circuit breaker to avoid overloading unstable services. It also supports `4xx` client-side error handling.

## Features
- **Unit Test**: %100 Unit Test Coverage in the Webclient and CircuitBreaker classes.
- **HTTP Client**: Utilizes `Spring WebClient` to make HTTP requests to external services.
- **Custom Circuit Breaker**: Monitors service failures and short-circuits requests when a service is down, preventing further requests for a configured period of time.
- **Error Handling**: Automatically handles `4xx` and `5xx` HTTP response codes, logging and propagating errors as custom exceptions.
- **Configurable**: Base URL, authentication token, and username can be configured via application properties.

## Circuit Breaker Functionality
The custom circuit breaker:
- Tracks service availability by URL.
- If a service returns a `5xx` response, it is considered down, and subsequent requests to the same URL are blocked temporarily.
- Successful responses reset the service status, allowing new requests to be sent.

## Getting Started
### Prerequisites

Before you begin, ensure you have met the following requirements:

- [Docker](https://www.docker.com/) installed and running



### Installation
To install the project, follow these steps:

1. Navigate to the project directory: `cd project-directory`
2. First, Build the Docker Image. Run the following command to build your Docker image:
   `docker build -t <your-image-name> .`
3. Once the image is built, run the Docker Container.
`docker run -d -p 8080:8080 --name webclient-factory <your-image-name>`
4. Open a web browser and navigate to: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

### Configuration
The application relies on certain properties that can be configured in your `application.yml` file:
