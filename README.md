# Uber Eats Restaurant Analytics

## 🚀 Quick Start Options

### Option 1: Immediate Demo (Python-only with Mock Data)
```bash
setup-python-only.bat
start-frontend-only.bat
```
Then open http://localhost:5000

### Option 2: Full System (Requires Java + Maven)
```bash
setup.bat
run-system.bat
```
Then open http://localhost:5000

## 📋 Prerequisites for Full System

- **Java 17+**: Download from [Adoptium](https://adoptium.net/)
- **Maven 3.6+**: Download from [Apache Maven](https://maven.apache.org/download.cgi)
- **Python 3.8+**: Download from [Python.org](https://www.python.org/downloads/)

## 🏗️ System Architecture

### Backend (Java Spring Boot)
- **Modular Pipeline**: Ingestion → Transformation → Analytics → API
- **Source-Agnostic Ingestion**: CSV, JSON, Database, APIs
- **Chunk-Based Processing**: Handles large files (5GB+)
- **Error Handling**: Dead-letter queue for failed operations
- **REST APIs**: JSON endpoints for analytics results

### Frontend (Python Flask)
- **Web GUI**: Interactive management dashboard
- **Visualizations**: Plotly/Seaborn/Matplotlib charts
- **Report Export**: CSV, PDF formats
- **Filtering**: Branch selector, time filters, seasonal analysis

## 📊 Analytics Modules

### 1. Peak Dining Analysis
- **Heatmaps**: Order volume by hour and outlet
- **Peak Hour Tables**: Top performing time slots
- **Branch Summaries**: Performance metrics per location
- **Daily/Weekly Patterns**: Temporal analysis

### 2. Customer Demographics & Segmentation
- **Age Distribution**: Customer age group analysis
- **Gender Analysis**: Gender-based patterns
- **Loyalty Groups**: Customer segmentation by loyalty
- **Spending Patterns**: Behavioral analysis
- **RFM Analysis**: Recency, Frequency, Monetary segmentation

### 3. Customer Seasonal Behavior
- **Seasonal Retention**: Customer retention across seasons
- **Loyalty Index**: Customer loyalty scoring
- **Seasonal Spending**: Spending patterns by season
- **Customer Lifecycle**: Lifespan analysis

### 4. Popular Menu & Order Flow Analysis
- **Top Items**: Most popular menu items
- **Category Analysis**: Performance by food category
- **Item Combos**: Frequently ordered combinations
- **Sankey Diagrams**: Order flow visualization
- **Spice Level Preferences**: Customer taste preferences
- **Vegetarian Analysis**: Dietary preference insights

### 5. Ticket Counting & Revenue Analysis
- **Revenue Summary**: Total revenue and growth metrics
- **Daily/Monthly Revenue**: Time-based revenue trends
- **Average Order Value**: AOV analysis by various dimensions
- **Payment Method Analysis**: Payment preference insights
- **Outlet Revenue Comparison**: Branch performance comparison

### 6. Service Anomaly Detection
- **Preparation Time Anomalies**: Unusual cooking times
- **Order Volume Anomalies**: Unexpected order patterns
- **Revenue Anomalies**: Unusual revenue patterns
- **Customer Behavior Anomalies**: Unusual spending patterns
- **Alert Logs**: Automated alert system

### 7. Branch Performance Analysis
- **Branch Dashboards**: Comprehensive branch metrics
- **Branch Rankings**: Performance-based rankings
- **Efficiency Analysis**: Operational efficiency metrics
- **Customer Satisfaction**: Satisfaction indicators

## 🎯 Key Features

### Interactive Dashboard
- **Real-time Analytics**: Live data analysis and visualization
- **Filtering System**: Multi-dimensional filtering (outlet, season, festival)
- **Responsive Design**: Works on desktop, tablet, and mobile
- **Interactive Charts**: Plotly-powered interactive visualizations

### Advanced Analytics
- **Statistical Analysis**: Z-score based anomaly detection
- **Time Series Analysis**: Temporal pattern recognition
- **Customer Segmentation**: RFM analysis and loyalty scoring
- **Performance Metrics**: KPI tracking and benchmarking

### Export Capabilities
- **CSV Export**: Raw data for further analysis
- **PDF Reports**: Formatted reports with charts and tables
- **Customizable Filters**: Export specific data subsets

### Seasonal & Festival Filtering
- **Seasons**: Spring, Summer, Autumn, Winter
- **Festivals**: Christmas, New Year, Valentine's Day, Easter, Diwali, Vesak
- **Custom Periods**: Flexible date range filtering

## 🔧 Installation & Setup

See [INSTALLATION_GUIDE.md](INSTALLATION_GUIDE.md) for detailed setup instructions.

## 📁 Project Structure
```
restaurant-analytics/
├── backend/                    # Java Spring Boot
│   ├── src/main/java/com/restaurant/analytics/
│   │   ├── ingestion/         # Data ingestion module
│   │   ├── transform/         # Data transformation
│   │   ├── analytics/         # Analytics engines
│   │   ├── api/              # REST API controllers
│   │   └── model/            # Data models
│   ├── pom.xml               # Maven configuration
│   └── restaurant_dataset_combined.csv
├── frontend/                   # Python Flask
│   ├── app.py                 # Main Flask application
│   ├── app-mock.py           # Mock data version
│   ├── routes/
│   │   ├── dashboard.py       # Dashboard routes
│   │   ├── reports.py         # Report generation
│   │   └── charts.py          # Chart endpoints
│   ├── templates/             # HTML templates
│   └── requirements.txt
├── setup.bat                  # Full system setup
├── setup-python-only.bat     # Python-only setup
├── run-system.bat            # Start full system
├── start-frontend-only.bat   # Start frontend only
└── INSTALLATION_GUIDE.md     # Detailed installation guide
```

## 🌐 API Endpoints

### Analytics Endpoints
- `GET /api/analytics/peak-dining` - Peak dining analysis
- `GET /api/analytics/customer-demographics` - Customer demographics
- `GET /api/analytics/customer-seasonal` - Seasonal behavior
- `GET /api/analytics/menu-analysis` - Menu analysis
- `GET /api/analytics/revenue-analysis` - Revenue analysis
- `GET /api/analytics/anomaly-detection` - Anomaly detection
- `GET /api/analytics/branch-performance` - Branch performance
- `GET /api/analytics/outlets` - List of outlets

### Query Parameters
- `outletId` - Filter by specific outlet
- `season` - Filter by season (spring, summer, autumn, winter)
- `festival` - Filter by festival period

### Export Endpoints
- `GET /reports/export/csv/{analysis_type}` - Export as CSV
- `GET /reports/export/pdf/{analysis_type}` - Export as PDF

## 🚨 Troubleshooting

### Common Issues

1. **Java/Maven not found**: Install prerequisites from links above
2. **Port conflicts**: Ensure ports 8080 and 5000 are available
3. **Python dependencies**: Run `pip install -r frontend/requirements.txt`
4. **Data loading**: Ensure CSV file is in backend directory

### System Requirements
- **OS**: Windows 10/11
- **RAM**: 4GB minimum, 8GB+ recommended
- **Storage**: 2GB free space
- **Ports**: 8080 (backend), 5000 (frontend)

## 🔮 Future Enhancements

- Real-time data streaming
- Machine learning predictions
- Advanced forecasting models
- Multi-tenant support
- Role-based access control
- Mobile application
- Advanced dashboard customization

## 📄 License

This project is designed for educational and commercial use in restaurant analytics.

---

**Note**: The system includes both a full-featured version (requires Java/Maven) and a demo version (Python-only with mock data) to accommodate different setup requirements.