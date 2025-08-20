# Tinder Service ❤️🔥

A Spring Boot microservice that powers the **Tinder-style deck generation and matching system**.  
This service handles user profiles, swipes, matches, and deck generation logic.

---

## 📌 Features
- User profile management (with geolocation support)
- Nearby user discovery (within configurable radius)
- Deck generation based on **gender & sexual preference**
- Swipe (like/dislike) tracking
- Match creation when two users swipe right
- Kafka integration for event-driven communication between services
- AWS S3 support for profile picture storage
- Aurora RDS for persistence

---

## 🛠️ Tech Stack
- **Java 17**
- **Spring Boot 3+**
- **Spring Security 6**
- **Spring Data JPA / Hibernate**
- **Aurora RDS**
- **Apache Kafka**
- **AWS S3**
- **Docker & Docker Compose**
- **Gradle/Maven** (depending on your setup)

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven or Gradle
- Docker (for local DB/Kafka if needed)
- AWS credentials (if testing with S3)

### Setup & Run

```bash
# Clone repository
git clone https://github.com/your-org/tinder-service.git
cd tinder-service


# or Build (Gradle)
./gradlew build


