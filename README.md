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

* **Sadad**: [https://sadad.shaparak.ir](https://sadad.shaparak.ir)
* **Sep**: [https://sep.shaparak.ir](https://sep.shaparak.ir)

## 📄 License

MIT License © 2025 \[Your Name or Organization Here]
