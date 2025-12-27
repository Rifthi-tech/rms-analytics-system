# RMS Analytics System

A desktop application for Uber Eats Corporation's restaurant chain that automates large-scale data analytics.

## Features

### Backend (Java Spring Boot)
- RESTful API endpoints for analytics modules
- Modular pipeline architecture for data processing
- CSV data ingestion with chunk-based processing
- Dead letter queue for error handling
- Extensible design patterns (Strategy, Factory, Builder, Adapter)

### Frontend (Python PySide6)
- Modern tabbed desktop interface
- Interactive charts and visualizations
- File upload and parameter selection
- Export capabilities (CSV, PNG, PDF)

### Analytics Modules
1. **Peak Dining Analysis** - Identify busy hours/days
2. **Customer Segmentation** - Analyze demographics and loyalty
3. **Seasonal Behavior** - Festival vs regular patterns
4. **Menu Item Analysis** - Top items and combos
5. **Revenue Analysis** - Totals and reconciliation
6. **Anomaly Detection** - Spikes/drops identification
7. **Branch Performance** - Outlet rankings

## Prerequisites

### Backend
- Java 17 or higher
- Maven 3.6+
- IntelliJ IDEA (recommended)

### Frontend
- Python 3.8+
- pip package manager

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd rms-analytics-system