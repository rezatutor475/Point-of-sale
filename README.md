# POS System (Java + Spring Boot)

A professional, modular Point-of-Sale (POS) system built in Java using Spring Boot. This project supports inventory management, order processing, customer handling, and seamless integration with Sadad and Sep payment gateways.

## 🚀 Features

* 📦 Product & Inventory Management
* 🧾 Order Creation & Tracking
* 👥 Customer Profiles & Loyalty
* 💳 Payment Gateway Integration (Sadad & Sep)
* 📈 Sales Reports (Top Products, Revenues)
* 🔌 REST API & CLI Testing Support
* 🗃️ H2 / MySQL database ready
* 🔒 Bean Validation and Logging

## 🛠 Tech Stack

* Java 17
* Spring Boot 3.2.4
* Spring Data JPA
* MySQL & H2 Database
* SLF4J & Logback
* Swagger / OpenAPI
* Maven Build Tool

## 📂 Project Structure

```
com.pos
├── App.java                      # CLI entry point
├── config                       # App & PSP configuration
├── controller                   # REST Controllers (Product, Order, Customer)
├── model                        # Entity classes
├── repository                   # Data Access Layer
├── service                      # Business logic (Inventory, Orders, Reports)
├── service/payment              # Payment services (Sadad, Sep)
├── util                         # Utility classes (HTTP, Printing, Validation)
├── api                          # Spring Boot REST endpoints
└── resources
    └── application.properties   # Config file
```

## ⚙️ Setup Instructions

1. **Clone the repository:**

   ```bash
   git clone https://github.com/your-org/pos-system.git
   cd pos-system
   ```

2. **Configure application.properties:**
   Set your DB credentials and PSP API keys in `src/main/resources/application.properties`

3. **Build & Run the App:**

   ```bash
   mvn spring-boot:run
   ```

4. **Access Swagger UI:**
   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 📑 Sample Endpoints

* `GET /api/products`
* `POST /api/orders`
* `POST /api/pay`

## 🧪 Testing

```bash
mvn test
```

## 🔐 Payment Gateway Notes


# POS QR System

A professional Point of Sale (POS) system built using Java and Spring Boot. Includes QR code generation for sales receipts, product tracking, and more.

---

## 📦 Features

- ✅ RESTful API for managing products and sales
- 📷 QR code generation for receipts
- 📊 H2 in-memory database for testing
- 🧩 Swagger/OpenAPI integration
- 📁 File upload and static storage for QR images
- 🧪 JUnit and Spring Boot testing framework

---

## 🏗️ Project Structure

```
pos-qr-java/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/example/posqr/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   └── PosQrApplication.java
│   │   └── resources/
│   │       ├── static/qrcodes/
│   │       └── application.properties
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+

### Run Application
```bash
mvn spring-boot:run
```

### Build JAR
```bash
mvn clean package
java -jar target/pos-qr-system-1.0.0.jar
```

---

## 🔧 API Docs
Access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Running Tests
```bash
mvn test
```

---

## 📁 QR Code Output
Generated QR images are stored in:
```
src/main/resources/static/qrcodes/
```

---

## 📜 License
MIT License. See `LICENSE` file for details.

---

## 🤝 Contributions
Pull requests and feature suggestions are welcome!

* **Sadad**: [https://sadad.shaparak.ir](https://sadad.shaparak.ir)
* **Sep**: [https://sep.shaparak.ir](https://sep.shaparak.ir)

## 📄 License

MIT License © 2025 \[Your Name or Organization Here]
