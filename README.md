# RMS Analytics System - Complete Setup & Running Guide

Restaurant Management System (RMS) Data Analytics Tool for Uber Eats Corporation

## 📋 Project Overview

A comprehensive web-based analytics system built with:
- **Backend**: Java Spring Boot with clean architecture, SOLID principles, and design patterns
- **Frontend**: React.js with Bootstrap and custom CSS
- **Color Scheme**: #003366 (Navy), #AAB8C2 (Slate), #F0F4F8 (Light Blue)

## 🏗️ Architecture & Design Patterns

### Backend Design Patterns Implemented:
1. **Factory Pattern** - `FilterStrategyFactory` for creating filter strategies
2. **Strategy Pattern** - Multiple filter implementations (`CompletedOrdersFilter`, `PriceRangeFilter`)
3. **Observer Pattern** - Event-driven data processing with `DataProcessingObserver` and `LoggingObserver`
4. **Singleton Pattern** - `DeadLetterQueue` and `EventManager` for centralized management
5. **Decorator Pattern** - Extensible filter chains
6. **Facade Pattern** - `AnalyticsServiceFacade` unifies all analytics services

### SOLID Principles:
- **S**ingle Responsibility - Each class has one reason to change
- **O**pen/Closed - Open for extension, closed for modification
- **L**iskov Substitution - Proper inheritance hierarchies
- **I**nterface Segregation - Focused interfaces
- **D**ependency Inversion - Depend on abstractions

## 📊 Analytics Features (7 Requirements)

1. **Peak Dining Analysis** - Hourly, daily, monthly peaks per outlet
2. **Customer Demographics** - Gender, age group, loyalty segmentation
3. **Seasonal Behavior** - Visit frequency and spending patterns
4. **Menu Analysis** - Top items, combinations, order flow
5. **Revenue Analysis** - Daily/weekly sales, payment methods, comparative charts
6. **Anomaly Detection** - Order count, cancellations, revenue anomalies
7. **Branch Performance** - Regional dashboards, ranking, underperformers

## 🚀 Quick Start

### Prerequisites
- Java 11+
- Maven 3.6+
- Node.js 14+
- npm or yarn

### Backend Setup

1. **Navigate to backend directory**
   ```bash
   cd rms-analytics-system/backend
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   Backend will start on `http://localhost:8080`

   **API Endpoints:**
   - POST `/api/analytics/load-data?filePath=<path>` - Load CSV data
   - GET `/api/analytics/report` - Get complete analytics report
   - GET `/api/analytics/peak-dining` - Peak dining analysis
   - GET `/api/analytics/customer-demographics` - Customer analysis
   - GET `/api/analytics/revenue-analysis` - Revenue data
   - GET `/api/analytics/branch-performance?metric=revenue` - Branch stats
   - GET `/api/analytics/anomalies?type=ordercount` - Anomaly detection
   - GET `/api/analytics/dead-letters` - Dead letter records
   - GET `/api/analytics/status` - System status

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd rms-analytics-system/frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start development server**
   ```bash
   npm start
   ```

   Frontend will open on `http://localhost:3000`

## 📥 Loading Data

1. Open the application in browser
2. Click "Upload Data"
3. Enter the full path to your CSV file:
   ```
   r:\HND-23 CSD\4th SEMESTER\APDP\restaurant_dataset.csv
   ```
4. Click "Load Data"
5. Once loaded, all analytics tabs become available

## 🗂️ Project Structure

```
rms-analytics-system/
├── backend/
│   ├── src/main/java/com/rms/analytics/
│   │   ├── model/              # Data models (Order, Customer, etc.)
│   │   ├── factory/            # Factory pattern implementations
│   │   ├── strategy/           # Strategy pattern implementations
│   │   ├── observer/           # Observer pattern implementations
│   │   ├── service/            # Analytics services & Facade
│   │   ├── controller/         # REST API endpoints
│   │   ├── util/               # Utilities (CSV loader, Singletons)
│   │   └── RMSAnalyticsApplication.java
│   ├── pom.xml
│   └── src/main/resources/application.properties
│
├── frontend/
│   ├── src/
│   │   ├── components/         # React components
│   │   │   ├── analytics/      # Analytics view components
│   │   │   ├── Navigation.js
│   │   │   ├── Dashboard.js
│   │   │   ├── DataUpload.js
│   │   │   └── Alert.js
│   │   ├── App.js              # Main app component
│   │   ├── App.css             # Global styles
│   │   └── index.js
│   ├── public/
│   │   └── index.html
│   └── package.json
│
└── restaurant_dataset.csv      # Sample data
```

## 🔧 Configuration

### Backend (application.properties)
```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:testdb
app.data.chunk-size=10000
app.data.max-file-size=524288000  # 500MB
```

### Frontend (.env - optional)
```
REACT_APP_API_URL=http://localhost:8080/api
```

## 📊 Data Format Expected

CSV must contain:
- Order fields: order_id, customer_id, outlet_id, order_placed, status, total_price_lkr
- Customer fields: contact_no, gender, age, join_date, loyalty_group
- Item fields: item_id, name, category, price_lkr_y, is_vegetarian, spice_level
- Outlet fields: outlet_id, borough, capacity, opened

## 🎨 UI Color Scheme

- **Primary**: #003366 (Navy Blue) - Headers, buttons, highlights
- **Secondary**: #AAB8C2 (Slate Gray) - Secondary elements
- **Background**: #F0F4F8 (Very Light Blue) - Page background
- **Text**: Dark gray for readability

## 📈 Data Processing Features

### Chunk-Based Processing
- Handles large files (500MB+) in 10,000 record chunks
- Memory-efficient streaming
- Progress tracking with Observer pattern

### Error Handling
- Dead Letter Queue for failed records
- Automatic error logging
- Graceful degradation

### Performance Metrics
- Processing time tracking
- Event notifications
- System status monitoring

## 🧪 Testing

### Backend Unit Tests
```bash
cd backend
mvn test
```

### Frontend Component Tests
```bash
cd frontend
npm test
```

## 🛠️ Troubleshooting

### Backend Won't Start
1. Check Java version: `java -version` (should be 11+)
2. Check port 8080 is available
3. Clear Maven cache: `mvn clean install`

### Frontend Won't Connect
1. Ensure backend is running on `http://localhost:8080`
2. Check CORS is enabled in `RMSAnalyticsApplication.java`
3. Check browser console for errors

### Data Won't Load
1. Verify CSV file path is correct
2. Check file format matches expected structure
3. Check dead-letter queue: `/api/analytics/dead-letters`

## 📝 Code Examples

### Using the Facade
```java
AnalyticsServiceFacade facade = new AnalyticsServiceFacade();
facade.loadData(orders, customers, items, outlets);
CompleteAnalyticsReport report = facade.generateCompleteReport();
```

### Creating Filters
```java
FilterStrategy filter = FilterStrategyFactory.createFilter(
    FilterStrategyFactory.FilterType.COMPLETED_ORDERS
);
List<Order> filtered = filter.filter(orders);
```

### Subscribing to Events
```java
EventManager.getInstance().subscribe(new LoggingObserver());
```

## 📚 Key Classes

| Class | Purpose |
|-------|---------|
| `AnalyticsServiceFacade` | Unified interface for all analytics |
| `PeakDiningAnalysisService` | Peak hour/day/month analysis |
| `CustomerDemographicsService` | Customer segmentation |
| `RevenueAnalysisService` | Sales and revenue tracking |
| `BranchPerformanceService` | Branch comparison and ranking |
| `AnomalyDetectionService` | Anomaly identification |
| `CSVDataLoader` | Chunk-based CSV loading |
| `DeadLetterQueue` | Error record management |

## 🔐 Security Considerations

- Input validation on all endpoints
- CORS configuration for frontend
- Dead letter queue prevents data loss
- Error messages don't expose sensitive info

## 📱 Responsive Design

- Mobile-friendly Bootstrap layout
- Collapsible sidebar for smaller screens
- Responsive tables and charts
- Touch-friendly buttons

## 🚀 Deployment

### Docker (Optional)
```dockerfile
# Backend
FROM openjdk:11
COPY target/rms-analytics-system.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# Frontend
FROM node:14
COPY frontend /app
WORKDIR /app
RUN npm install && npm run build
```

## 📞 Support

For issues or questions:
1. Check dead-letter queue for processing errors
2. Review application logs in backend console
3. Inspect browser console for frontend errors

## 📄 License

Educational project - RMS Analytics System for Uber Eats Corporation

---

**Built with Clean Architecture, SOLID Principles, and Design Patterns ✨**
