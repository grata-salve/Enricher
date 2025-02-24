# Trade Data Enrichment Service

## 📌 Overview
This service enriches trade data by mapping product IDs to product names using a static dataset. It validates trade dates, logs missing product names, and supports handling large datasets efficiently.

## 🛠️ Tech Stack
- **Java 17**
- **Spring Boot 3.2.1**
- **Maven**
- **Redis Cache**
- **JUnit & Mockito**
- **Postman (for API testing)**

## 🚀 How to Run

### Prerequisites
- Install **Java 17**
- Install **Maven**
- Install **Redis** (ensure it is running on `localhost:6379`)

### Steps to Run the Application
```bash
# Clone the repository
git clone <repository-url>
cd trade-data-enrichment

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

## 🔥 API Usage

### 1️⃣ Enrich Trade Data (CSV)
#### **Request**
- **Method**: `POST`
- **Endpoint**: `http://localhost:8080/api/v1/enrich`
- **Content-Type**: `multipart/form-data`
- **Body**:
  - `file`: Upload the CSV file (e.g., `trade.csv`)

#### **Using Postman**
1. Open **Postman**.
2. Set **Method** to `POST`.
3. Enter `http://localhost:8080/api/v1/enrich` as the URL.
4. Go to the **Body** tab.
5. Select **form-data**.
6. Add a new key: `file` (Type: `File`).
7. Upload your `trade.csv` file.
8. Click **Send**.

#### **Response Example**
```csv
date,productName,currency,price
20160101,Treasury Bills Domestic,EUR,10
20160101,Corporate Bonds Domestic,EUR,20.1
20160101,REPO Domestic,EUR,30.34
20160101,Missing Product Name,EUR,35.34
```

## ✅ Test Coverage
- **Class Coverage**: 100% (5/5)
- **Method Coverage**: 92% (13/14)
- **Line Coverage**: 85% (61/71)

### Running Tests
```bash
mvn test
```

## 📸 Screenshots
### API Testing in Postman
[Postman Request](https://github.com/grata-salve/Enricher/issues/1)

### Test Execution
![Test Results](https://github.com/grata-salve/Enricher/issues/3)

### Code Coverage
![Coverage Report](https://github.com/grata-salve/Enricher/issues/2)

## 📌 Completed Tasks

### 1️⃣ Mandatory Requirements
- Implemented API for trade data enrichment.
- Used Redis for caching.
- Handled large datasets efficiently.
- Logged missing product names.
- Implemented robust validation for trade dates.

### 3️⃣ Async Processing
- Used `CompletableFuture` for asynchronous processing to improve performance.

### 4️⃣ Reactive Data Streaming
- Implemented Java Streams to process large trade files efficiently.

## 🚀 Future Enhancements
- **Support for JSON & XML Input Formats**
- **Improve Error Logging & Monitoring**
- **Optimize Memory Usage for Large Files**
- **Enhanced Validation:** Add stricter validation (e.g., numeric checks for price, currency validation).
