# Airbnb-Style Hotel Booking Platform

A production-style RESTful backend for a hotel booking system, inspired by Airbnb. Built with Spring Boot and deployed on AWS with a fully automated CI/CD pipeline.

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue)](https://www.postgresql.org/)
[![AWS](https://img.shields.io/badge/AWS-Elastic%20Beanstalk-orange)](https://aws.amazon.com/elasticbeanstalk/)

## Overview

This project implements the core backend of a hotel booking platform — hotel and room management, real-time inventory tracking, dynamic pricing, bookings, guest management, and payments — secured with JWT authentication and deployed to production on AWS.

## Features

- **Authentication & Authorization** — JWT-based auth with role-based access control (Guest / Hotel Manager)
- **Hotel & Room Management** — CRUD operations for hotels, rooms, and amenities
- **Inventory System** — Date-based room inventory with booked/reserved/total count tracking and surge factor
- **Dynamic Pricing Engine** — Strategy + Decorator pattern combining:
  - Base pricing
  - Surge pricing
  - Occupancy-based pricing
  - Holiday pricing
  - Urgency (last-minute) pricing
- **Booking Flow** — Multi-step booking (reserve → add guests → pay → confirm) with status tracking and booking expiry
- **Guest Management** — Add and manage guest details per booking
- **Payments** — Razorpay integration with payment verification and signature validation
- **API Documentation** — Interactive Swagger / OpenAPI UI

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.9, Spring Security, Spring Data JPA |
| Database | PostgreSQL (AWS RDS) |
| Migrations | Flyway |
| Auth | JWT |
| Payments | Razorpay |
| API Docs | Springdoc OpenAPI / Swagger UI |
| Build | Maven |
| Cloud | AWS (Elastic Beanstalk, RDS, IAM) |
| CI/CD | AWS CodePipeline + CodeBuild |

## Architecture

```
Controller → Service → Repository → PostgreSQL (RDS)
     │
     ├── JWTAuthFilter (Spring Security)
     ├── PricingService (Strategy + Decorator pattern)
     └── GlobalExceptionHandler (centralized error responses)
```

**Pricing strategies** are composed at runtime — a booking's final price is calculated by layering `SurgePricingStrategy`, `OccupancyPricingStrategy`, `HolidayPricingStrategy`, and `UrgencyPricingStrategy` on top of `BasePricingStrategy`, each implementing a common `PricingStrategy` interface.

## Getting Started

### Prerequisites
- Java 21
- Maven
- PostgreSQL (local instance for development)

### Local Setup

1. Clone the repository
   ```bash
   git clone https://github.com/piyushstack1/Spring-Boot-Project.git
   cd Spring-Boot-Project/airbnb
   ```

2. Create a local PostgreSQL database matching the name in `application-dev.properties`

3. Set the following environment variables (in your IDE run configuration or shell):
   ```
   DB_USERNAME=<your_db_username>
   DB_PASSWORD=<your_db_password>
   JWT_SECRET=<your_jwt_secret>
   RAZORPAY_KEY_ID=<your_razorpay_key_id>
   RAZORPAY_KEY_SECRET=<your_razorpay_key_secret>
   FRONTEND_URL=<your_frontend_url>
   ```

4. Run the application
   ```bash
   ./mvnw spring-boot:run
   ```

5. Open Swagger UI
   ```
   http://localhost:8081/api/v1/swagger-ui/index.html
   ```

### Spring Profiles

| Profile | Purpose | Schema Management |
|---|---|---|
| `dev` | Local development | Hibernate `ddl-auto=update` |
| `prod` | AWS deployment | Flyway-managed migrations, Hibernate `ddl-auto=validate` |

## Deployment

The application is deployed on **AWS Elastic Beanstalk** with a **PostgreSQL RDS** database, fully provisioned and connected via environment-specific configuration.

### CI/CD Pipeline

Every push to `main` triggers an automated pipeline:

```
GitHub push → CodePipeline → CodeBuild (mvn build) → Elastic Beanstalk (auto-deploy)
```

- **Source**: GitHub (via GitHub App connection)
- **Build**: AWS CodeBuild — compiles and packages the Spring Boot JAR (`buildspec.yml`)
- **Deploy**: AWS Elastic Beanstalk — automatically deploys the new build with zero manual intervention

### Database Migrations

Schema changes in production are managed via **Flyway**, versioned under `src/main/resources/db/migration/`. The initial schema was baselined from the existing production database, and all future schema changes go through versioned, auditable migration scripts rather than auto-DDL.

## API Documentation

Once running, full interactive API documentation is available via Swagger UI at:
```
/api/v1/swagger-ui/index.html
```

## License

This project is for educational and portfolio purposes.
