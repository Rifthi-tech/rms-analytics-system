# Restaurant Analytics System - Complete Guide

## System Overview

This hybrid restaurant analytics system provides comprehensive data analysis capabilities with:

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

## Analytics Modules

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

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Python 3.8+
- pip (Python package manager)

### Setup Instructions

1. **Run Setup**
   ```bash
   setup.bat
   ```

2. **Start the System**
   ```bash
   run-system.bat
   ```
   
   Or start services individually:
   ```bash
   # Terminal 1 - Backend
   start-backend.bat
   
   # Terminal 2 - Frontend (after backend is running)
   start-frontend.bat
   ```

3. **Access the System**
   - Frontend Dashboard: http://localhost:5000
   - Backend API: http://localhost:8080

## System Architecture

### Data Flow
```
CSV Data → Ingestion Service → Transformation Service → Analytics Engines → REST APIs → Flask Frontend → Interactive Dashboard
```

### Key Components

#### Backend Services
- **DataIngestionService**: Handles CSV/JSON/API data ingestion with chunking
- **DataTransformationService**: Cleans and validates data, applies filters
- **Analytics Services**: 7 specialized analytics modules
- **AnalyticsController**: REST API endpoints
- **Error Handling**: Dead-letter queue for failed operations

#### Frontend Components
- **Flask App**: Main web application
- **Dashboard Routes**: Interactive dashboard pages
- **Charts Routes**: Plotly chart generation
- **Reports Routes**: CSV/PDF export functionality
- **Templates**: Responsive HTML templates with Bootstrap

## API Endpoints

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
- `festival` - Filter by festival period (christmas, new_year, valentine, easter, diwali, vesak)

### Export Endpoints
- `GET /reports/export/csv/{analysis_type}` - Export as CSV
- `GET /reports/export/pdf/{analysis_type}` - Export as PDF

## Features

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

### Error Handling
- **Robust Processing**: Handles malformed data gracefully
- **Dead Letter Queue**: Failed records are logged for review
- **Validation**: Data quality checks and cleaning

## Data Schema

The system processes restaurant order data with the following key fields:
- Order information (ID, timestamps, status, payment method)
- Customer data (demographics, loyalty, spending history)
- Outlet information (location, capacity, performance metrics)
- Menu items (categories, prices, dietary information)

## Performance Features

### Scalability
- **Chunk Processing**: Handles large files (5GB+) efficiently
- **Async Processing**: Non-blocking data ingestion
- **Memory Management**: Optimized for large datasets

### Caching
- **Data Caching**: Reduces processing time for repeated queries
- **Chart Caching**: Faster visualization rendering

## Troubleshooting

### Common Issues

1. **Backend won't start**
   - Check Java version (requires Java 17+)
   - Verify Maven installation
   - Check port 8080 availability

2. **Frontend connection errors**
   - Ensure backend is running first
   - Check Python dependencies are installed
   - Verify port 5000 availability

3. **Data loading issues**
   - Verify CSV file is in backend directory
   - Check file format matches expected schema
   - Review error logs for data validation issues

### Log Locations
- Backend logs: Console output from Spring Boot
- Frontend logs: Flask console output
- Error logs: Check browser developer console for frontend errors

## Development

### Adding New Analytics Modules
1. Create new service class in `backend/src/main/java/com/restaurant/analytics/analytics/`
2. Add endpoint in `AnalyticsController`
3. Create corresponding frontend route in `frontend/routes/`
4. Add visualization in `frontend/routes/charts.py`

### Customizing Visualizations
- Modify chart creation functions in `frontend/routes/charts.py`
- Update templates in `frontend/templates/`
- Add new chart types using Plotly.js

### Database Integration
- Update `application.properties` for database connection
- Modify `DataIngestionService` for database queries
- Add JPA repositories for data persistence

## Security Considerations

- **CORS Configuration**: Configured for localhost development
- **Input Validation**: Data validation and sanitization
- **Error Handling**: Secure error messages without data exposure

## Future Enhancements

- Real-time data streaming
- Machine learning predictions
- Advanced forecasting models
- Multi-tenant support
- Role-based access control
- Mobile application
- Advanced dashboard customization

## Support

For technical support or questions:
1. Check this documentation
2. Review error logs
3. Verify system requirements
4. Check API endpoint responses

The system is designed to be modular and extensible, allowing for easy addition of new analytics modules and visualization types.