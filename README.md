# 🛍️ Mini E-Commerce Platform (Spring Boot Monolith)

> **Full-featured backend project** built for showcasing enterprise-level backend development using **Spring Boot 3**, **PostgreSQL**, **Redis**, **RabbitMQ**, **Cloudinary**, and **Docker**.  
> Designed as a real-world **E-Commerce system** that includes authentication, payments, caching, event-driven communication, and deploy-ready setup.

---

## 🚀 Features Overview

### 🧩 Core Features
- **Authentication & Authorization**
  - JWT (Access + Refresh token)
  - Login via Google & GitHub (OAuth2)
  - Forgot password, OTP email verification
- **User Management**
  - Register, login, role-based authorization (`@PreAuthorize`)
  - Audit tracking (created_by, updated_by)
- **Product Management**
  - CRUD with DTO & Mapper
  - Image upload via **Cloudinary**
  - Pagination, sorting, and filtering
- **Shopping Cart & Orders**
  - Add-to-cart, checkout flow, order tracking
  - Stock reservation & transaction consistency
- **Payments**
  - Integrated **VNPAY Sandbox**
  - Async callback verification & event publishing
- **Mail Notifications**
  - HTML email sender (order confirmation, OTP)
  - Event listener triggered mails
- **Redis Caching**
  - Cache product lists & categories
  - TTL & eviction strategies
- **RabbitMQ / Kafka**
  - Event-driven communication (order events, email jobs)
- **Scheduler**
  - Cron/fixed-rate jobs (e.g., clean expired orders)
- **WebSocket**
  - Realtime order notifications for Admin Dashboard
- **I18N**
  - Vietnamese 🇻🇳 + English 🇬🇧 resource bundles
- **API Docs**
  - Swagger UI + Bearer JWT auth
- **Healthcheck & Monitoring**
  - Actuator endpoints `/health`, `/info`, `/metrics`
- **Logging**
  - Centralized `SLF4J` logging
  - Audit logs & async mail events
- **Dockerized**
  - Ready-to-run with `docker-compose` (Postgres, Redis, RabbitMQ, App)
- **Profiles**
  - `dev`, `test`, `prod` environments (YAML-based config)

---

## 🏗️ Tech Stack

| Layer | Technology |
|-------|-------------|
| **Backend** | Spring Boot 3, Spring Data JPA, Spring Security |
| **Database** | PostgreSQL + Flyway migrations |
| **Cache** | Redis |
| **Message Broker** | RabbitMQ (or Kafka optional) |
| **Storage** | Cloudinary (images), AWS S3 optional |
| **Auth** | JWT + OAuth2 (Google, GitHub) |
| **Testing** | JUnit, Mockito |
| **Documentation** | Swagger / OpenAPI |
| **Deployment** | Docker, Docker Compose |
| **Mailing** | Spring MailSender (SMTP / Gmail) |
| **Monitoring** | Spring Boot Actuator |
| **Realtime** | WebSocket (STOMP protocol) |

---

## 🗂️ Project Structure

```text
src/main/java/com/chubecommerce
┣ auth/                 # JWT, OAuth2, OTP
┣ user/                 # User, role management
┣ product/              # Product, category, image
┣ cart/                 # Cart & cart items
┣ order/                # Order, order items
┣ payment/              # VNPAY integration
┣ common/               # Constants, utils, exception handler
┣ scheduler/            # Cron jobs
┣ event/                # Publisher & listener (RabbitMQ)
┣ websocket/            # Realtime notification
┣ config/               # Security, Redis, Flyway, Cloudinary, etc.
┗ ChubeCommerceApplication.java
```

----

## 🧱 Database Design (PostgreSQL)

Main tables:
- `users`, `roles`, `user_roles`
- `products`, `categories`, `product_images`
- `carts`, `cart_items`
- `orders`, `order_items`, `payments`
- `refresh_tokens`, `otp_verifications`
- `coupons`, `coupon_redemptions`
- `audit_log`

→ See `docs/ERD.png` or `src/main/resources/db/migration/V1__init.sql` for schema.

---

## ⚙️ Environment Configuration

### application.yml (multi-profile)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true

vnpay:
  tmn-code: YOUR_TMN_CODE
  secret-key: YOUR_SECRET_KEY
  pay-url: https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
  return-url: http://localhost:8080/api/payments/vnpay/return
cloudinary:
  cloud-name: your-cloud
  api-key: your-key
  api-secret: your-secret
jwt:
  secret: your-super-secret-key
  expiration-minutes: 15
  refresh-expiration-days: 7
🐳 Docker Setup

Run everything with one command:

docker-compose up -d


Services included:

app: Spring Boot application

postgres: Database

redis: Caching

rabbitmq: Event broker

💳 VNPAY Integration


Sandbox environment: https://sandbox.vnpayment.vn

Configurable in application.yml

Payment flow:

Create order → call /api/payments/vnpay/pay

Redirect to VNPAY sandbox

After payment, callback /api/payments/vnpay/return

Verify checksum → update order status → emit PaymentSuccessEvent

📬 Email Notification Example

OTP Verification

Order Confirmation

Forgot Password

Template folder: src/main/resources/templates/email/

🌍 Internationalization (I18N)

Resources:

src/main/resources/i18n/
 ┣ messages_en.properties
 ┗ messages_vi.properties


Usage:

messageSource.getMessage("order.success", null, LocaleContextHolder.getLocale());

🧪 Testing

Run all tests:

mvn test


Includes:

Unit tests for services

Integration tests with Testcontainers (optional)

MockMvc for controller testing

📜 API Documentation

After running the app:

Swagger UI: http://localhost:8080/swagger-ui.html

OpenAPI JSON: http://localhost:8080/v3/api-docs

🩺 Health Check

Provided by Spring Boot Actuator:

/actuator/health
/actuator/info
/actuator/metrics

🧑‍💻 Author

Tran Quang Huy (chube-coder-2k4)
🎓 FPT University — Backend Developer (Spring Boot)
📧 Email: your_email@gmail.com

🌐 GitHub: https://github.com/chube-coder-2k4

🏁 Future Enhancements

Payment integration with Stripe / PayPal

Product recommendation system

Elasticsearch for full-text search

CI/CD pipeline via GitHub Actions

Deployment to AWS ECS / EC2 + RDS + S3

“Build it as if it’s production.”
This project demonstrates a complete backend skill set for an enterprise-level e-commerce system, ready for real deployment or intern interviews.
