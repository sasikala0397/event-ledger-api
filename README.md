# event-ledger-api

## 📖 Overview
This project implements an **Event Ledger API** that processes financial transaction events.
It demonstrates:
- Idempotency (duplicate events are ignored)
- Out-of-order event handling
- Balance computation
- Input validation

Built with **Spring Boot** and **H2 in-memory database** for easy local setup.

## ⚙️ Prerequisites
- Java 17+
- Git

## 📦 Install Dependencies
Dependencies are managed with Maven. Use the included Maven wrapper to install dependencies and build the project:

```bash
./mvnw clean install -DskipTests
```

## 🚀 Start the Application
Start the application with:

```bash
./mvnw spring-boot:run
```

Then open or call the API at:

```text
http://localhost:8080
```

You can also build and run the jar directly:

```bash
./mvnw clean package
java -jar target/event-ledger-api-0.0.1-SNAPSHOT.jar
```

## 🧪 Run Tests
Run tests with:

```bash
./mvnw test
```

To generate the coverage report, run:

```bash
./mvnw clean verify
```

Then open the coverage report at:

```text
target/site/jacoco/index.html
```
