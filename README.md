# 🏦 Banking Management System – Microservices Architecture

A secure and modular **Java Spring Boot Banking System** built with **Microservices Architecture**. It handles **User registration/login**, **Account management**, and **Transaction processing**, all protected by **JWT-based authentication** and routed via **Spring Cloud Gateway**.

---

## 📌 Modules in the Project

- 🔐 **User Service** – Handles registration, login, and token generation.
- 💼 **Account Service** – Manages bank accounts linked to users.
- 💸 **Transaction Service** – Handles send/receive operations and transaction history.
- 🚪 **API Gateway** – Routes all requests and applies global security filters.
- 🔍 **Eureka Server** – Service discovery for all microservices.

---

## 🔐 Security Overview

- JWT-based authentication
- Passwords encrypted using BCrypt
- Spring Security filters on User, Account, and Transaction services
- Gateway checks for valid token before routing requests

---

## 🛠 Technology Stack

| Layer             | Technology           |
|------------------|----------------------|
| Language          | Java 17              |
| Framework         | Spring Boot, Spring Cloud |
| Security          | Spring Security + JWT |
| Service Registry  | Eureka Server        |
| Gateway           | Spring Cloud Gateway |
| Database          | MySQL                |
| Build Tool        | Maven                |
| Communication     | REST (via Feign or RestTemplate) |

---

## 🔁 Microservices Communication Flow

```text
[Client] 
   ↓
[API Gateway] 
   ↓
→ [User Service] (Login/Register, JWT Issued)
→ [Account Service] (Requires Valid JWT)
→ [Transaction Service] (Requires Valid JWT)

