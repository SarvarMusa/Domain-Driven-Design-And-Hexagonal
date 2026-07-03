# 🍔 Food Ordering System — DDD & Hexagonal Architecture

> A production-grade microservices backend implementing **Domain-Driven Design (DDD)** and **Hexagonal (Ports & Adapters) Architecture** for a food ordering platform.

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.6.7-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-7.0.1-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)](https://kafka.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-20.10.7-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Maven](https://img.shields.io/badge/Maven-3.8.1-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](./LICENSE)

---

## 📌 Description

This project solves the challenge of building a **scalable, loosely-coupled food ordering system** where different business domains (ordering, payment, restaurant approval) must communicate reliably without tight coupling.

**Key problems solved:**
- **Distributed transactions** handled via the **Saga pattern** (no 2PC needed)
- **Reliable messaging** via the **Outbox pattern** (no message loss on crash)
- **Domain isolation** via Hexagonal Architecture (domain logic has zero infrastructure dependencies)
- **Event-driven communication** via Apache Kafka

---

## 🏗️ Architecture Overview

```
food-ordering-system/
├── common/                          # Shared domain primitives & events
│   ├── common-domain/               # Base entities, value objects
│   └── common-application/          # Shared DTOs, exceptions
│
├── order-service/                   # Core ordering bounded context
│   ├── order-domain/
│   │   ├── order-domain-core/       # Pure domain: entities, events, rules
│   │   └── order-application-service/  # Use cases (ports in)
│   ├── order-application/           # REST API (adapter in)
│   ├── order-dataaccess/            # JPA repositories (adapter out)
│   ├── order-messaging/             # Kafka producers/consumers (adapter out)
│   └── order-container/             # Spring Boot bootstrap
│
├── payment-service/                 # Payment bounded context
│   ├── payment-domain/
│   ├── payment-dataaccess/
│   ├── payment-messaging/
│   └── payment-container/
│
├── restaurant-service/              # Restaurant approval bounded context
│   ├── restaurant-domain/
│   ├── restaurant-dataaccess/
│   ├── restaurant-messaging/
│   └── restaurant-container/
│
├── customer-service/                # Customer bounded context
│
└── infrastructure/                  # Cross-cutting infrastructure
    ├── kafka/                       # Kafka config, producer, consumer
    ├── saga/                        # Saga step interfaces
    ├── outbox/                      # Outbox pattern base
    └── docker-compose/              # Local dev environment
```

### 🔄 Hexagonal Architecture (per service)

```
┌─────────────────────────────────────────┐
│              DOMAIN CORE                │
│  (Entities, Value Objects, Events,      │
│   Domain Services, Business Rules)      │
│                                         │
│  ← Ports (interfaces defined here)      │
└──────────────┬──────────────────────────┘
               │
    ┌──────────┴──────────┐
    │                     │
 [Adapters IN]       [Adapters OUT]
 REST Controller     JPA Repository
 Kafka Consumer      Kafka Producer
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 2.6.7 |
| Build Tool | Maven 3.8.1 |
| Messaging | Apache Kafka (Confluent 7.0.1) |
| Database | PostgreSQL 13 |
| Containerization | Docker + Docker Compose |
| Coordination | Apache ZooKeeper |
| Patterns | DDD, Hexagonal, Saga, Outbox |

---

## ⚙️ Setup Instructions

### Prerequisites
- Java 17+
- Docker & Docker Compose
- Maven 3.8+

### 1. Clone the repository
```bash
git clone git@github.com:SarvarMusa/Domain-Driven-Design-And-Hexagonal.git
cd Domain-Driven-Design-And-Hexagonal
```

### 2. Start infrastructure (Kafka + ZooKeeper + PostgreSQL)
```bash
cd infrastructure/docker-compose

# Start ZooKeeper first
docker-compose -f zookeeper.yml up -d

# Start Kafka cluster
docker-compose -f kafka_cluster.yml up -d

# Initialize Kafka topics
docker-compose -f init_kafka.yml up

# Start common services (PostgreSQL, etc.)
docker-compose -f common.yml up -d
```

### 3. Build the project
```bash
cd ../../
mvn clean install -DskipTests
```

### 4. Run each service
```bash
# Order Service
cd order-service/order-container
mvn spring-boot:run

# Payment Service
cd payment-service/payment-container
mvn spring-boot:run

# Restaurant Service
cd restaurant-service/restaurant-container
mvn spring-boot:run
```

---

## 🔐 Environment Variables

Create a `.env` file based on `.env.example`:

```bash
cp infrastructure/docker-compose/.env.example infrastructure/docker-compose/.env
```

| Variable | Description | Default |
|----------|-------------|---------|
| `KAFKA_VERSION` | Confluent Kafka image version | `7.0.1` |
| `GLOBAL_NETWORK` | Docker network name | `food-ordering-system` |
| `GROUP_ID` | Kafka consumer group prefix | `com.food.order.system` |

> ⚠️ Never commit `.env` files with real credentials!

---

## 📡 API Endpoints

### Order Service (`http://localhost:8181`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/orders` | Create a new food order |
| `GET` | `/orders/{trackingId}` | Track an order by tracking ID |

**Create Order Request:**
```json
POST /orders
{
  "customerId": "d215b5f8-0249-4dc5-89a3-51fd148cfb41",
  "restaurantId": "d215b5f8-0249-4dc5-89a3-51fd148cfb45",
  "address": {
    "street": "street_1",
    "postalCode": "1000AB",
    "city": "Amsterdam"
  },
  "price": 200.00,
  "items": [
    {
      "productId": "d215b5f8-0249-4dc5-89a3-51fd148cfb48",
      "quantity": 1,
      "price": 50.00,
      "subTotal": 50.00
    }
  ]
}
```

**Create Order Response:**
```json
{
  "orderTrackingId": "uuid",
  "orderStatus": "PENDING",
  "message": "Order created successfully"
}
```

---

## 🔄 Event Flow

```
Customer → POST /orders → OrderService
                              │
                    [Kafka: payment-request]
                              │
                         PaymentService
                              │
                    [Kafka: payment-response]
                              │
                         OrderService
                              │
                [Kafka: restaurant-approval-request]
                              │
                       RestaurantService
                              │
                [Kafka: restaurant-approval-response]
                              │
                         OrderService → APPROVED ✅
```

---

## 🔒 Design Patterns

| Pattern | Purpose |
|---------|---------|
| **Saga** | Distributed transaction management without 2PC |
| **Outbox** | Reliable event publishing (at-least-once delivery) |
| **CQRS** | Separate read/write models |
| **Hexagonal** | Domain isolation from infrastructure |
| **DDD** | Rich domain models aligned with business |

---

## 🤝 Contributing

1. Fork the project
2. Create your feature branch: `git checkout -b feat/amazing-feature`
3. Commit using conventional commits: `git commit -m 'feat: add amazing feature'`
4. Push: `git push origin feat/amazing-feature`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](./LICENSE) file for details.

---

<p align="center">Built with ❤️ by <a href="https://github.com/SarvarMusa">SarvarMusa</a></p>
