#  Payment Service

API for managing payments and communicating events through RabbitMQ.  
It allows registering, querying, updating, and notifying changes in payment statuses.

---

##  **Main Features**

- Register new payments.
- Retrieve individual or all payments.
- Update a payment’s status.
- Notify payment status changes via **RabbitMQ**.

---

##  **REST Endpoints**

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `POST` | `/api/v1/payments` | Creates a new payment. |
| `GET` | `/api/v1/payments/{id}` | Retrieves a payment by its ID. |
| `GET` | `/api/v1/payments` | Lists all registered payments. |
| `PATCH` | `/api/v1/payments/{id}` | Updates the status of an existing payment. |

---

## **Tech Stack & Requirements**
This project requires the following environment and dependencies:

- Java 17
- Spring Boot 3.2.0+
- MongoDB 6.0+
- RabbitMQ 3.12+
- Docker & Docker Compose
- JUnit 5 & Mockito
- Swagger OpenAPI

---

##  **Database Schema**

Payments are stored in a MongoDB collection.  
Each document follows the structure below:

```
/docs/payment_db.json
```
---

##  **RabbitMQ Configuration**

### **Exchange**
| Property | Value |
|-----------|--------|
| **Name** | `payment.exchange` |
| **Type** | `topic` |
| **Durable** | `true` |
| **Auto Delete** | `false` |
| **Description** | Main exchange where payment-related events are published. |


### **Queues**
| Queue | Durable | Auto Delete | Routing Key | Description |
|--------|----------|--------------|------------|--------------|
| `payment.status.queue` | `true` | `false` |  | Queue that receives notifications when a payment status changes. |

We can consult the definition in the following document:
```
/docs/rabbit_definitions.json
```

---

##  **Project Execution**

### **Run with Docker Compose**

1️.- **Build and Package the Project:**

From the project root directory, run:
```bash
mvn clean package -DskipTests
```
This command compiles the project and creates the JAR file inside the target/ directory, skipping test execution to speed up the build.

2️.- **Start containers:**

Once the package is ready, start all required containers:
```bash
docker-compose up --build
```
This command will:

- Build the Docker image for the payment-service.

- Start the following services:
  - MongoDB (Database)
  - RabbitMQ (Message Broker)
  - Payment-Service (Spring Boot microservice)

3.- **Verify Running Containers**

To make sure all containers are running:
```bash
docker ps
```
You should see entries for:
- payment_service
- mongodb
- rabbitmq2
---

##  **Health Check**

Once the container is running, you can verify that the service and its dependencies are working properly by accessing the built-in Spring Boot Actuator health endpoint:

**[http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)**

---

##  **Swagger UI**

Once the container is running, interactive documentation is available at:

 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

---

##  **Testing**

- Unit tests use **JUnit 5** and **Mockito**.
- Integration tests verify payment creation, status updates, and RabbitMQ event publishing.

Run all tests:
```bash
mvn clean test
```

---

##  **Postman Collection**

A Postman collection is included to test the REST endpoints:

```
/docs/Payment-Service-Collection.postman_collection.json
```

Import this file into Postman and use the `Development` environment.


