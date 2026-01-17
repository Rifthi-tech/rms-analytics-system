# Restaurant Analytics System - Installation Guide

## Quick Start (Python-only with Mock Data)

If you want to see the system interface immediately with mock data:

1. Run `setup-python-only.bat`
2. Run `start-frontend-only.bat`
3. Open http://localhost:5000

This will show you the interface with sample data, but won't have the full analytics capabilities.

## Full Installation (Recommended)

For complete functionality with real data processing:

### Prerequisites

#### 1. Java 17 or Higher
- **Download**: https://adoptium.net/
- **Installation**: 
  - Download the Windows x64 MSI installer
  - Run the installer and follow the setup wizard
  - Make sure "Add to PATH" is checked during installation
- **Verify**: Open Command Prompt and run `java -version`

#### 2. Apache Maven 3.6+
- **Download**: https://maven.apache.org/download.cgi
- **Installation**:
  - Download the Binary zip archive (apache-maven-3.x.x-bin.zip)
  - Extract to a folder like `C:\Program Files\Apache\maven`
  - Add `C:\Program Files\Apache\maven\bin` to your system PATH
- **Verify**: Open Command Prompt and run `mvn -version`

#### 3. Python 3.8+
- **Download**: https://www.python.org/downloads/
- **Installation**:
  - Download the Windows installer
  - **IMPORTANT**: Check "Add Python to PATH" during installation
  - Choose "Install for all users" if you have admin rights
- **Verify**: Open Command Prompt and run `python --version`

### Setup Steps

1. **Run Setup**
   ```bash
   setup.bat
   ```
   This will:
   - Check all prerequisites
   - Install Python dependencies
   - Compile the Java backend
   - Verify everything is working

2. **Start the System**
   ```bash
   run-system.bat
   ```
   This will start both backend and frontend services automatically.

3. **Access the System**
   - Frontend Dashboard: http://localhost:5000
   - Backend API: http://localhost:8080

### Manual Setup (Alternative)

If the automated scripts don't work:

1. **Install Python Dependencies**
   ```bash
   cd frontend
   pip install -r requirements.txt
   cd ..
   ```

2. **Compile Java Backend**
   ```bash
   cd backend
   mvn compile
   cd ..
   ```

3. **Start Backend** (in one terminal)
   ```bash
   cd backend
   mvn spring-boot:run
   ```

4. **Start Frontend** (in another terminal, after backend is running)
   ```bash
   cd frontend
   python app.py
   ```

## Troubleshooting

### Common Issues

#### "Java is not recognized"
- Java is not installed or not in PATH
- Solution: Install Java 17+ from https://adoptium.net/
- Make sure to add Java to PATH during installation

#### "mvn is not recognized"
- Maven is not installed or not in PATH
- Solution: Install Maven and add to PATH
- Guide: https://maven.apache.org/install.html

#### "python is not recognized"
- Python is not installed or not in PATH
- Solution: Install Python and check "Add to PATH"
- Alternative: Use `py` instead of `python` command

#### Backend won't start
- Check Java version: `java -version` (should be 17+)
- Check Maven: `mvn -version`
- Check port 8080 is not in use
- Look for error messages in the console

#### Frontend connection errors
- Make sure backend is running first
- Check backend is accessible at http://localhost:8080
- Verify Python dependencies are installed

#### Data loading issues
- Ensure `restaurant_dataset_combined.csv` is in the backend directory
- Check file permissions
- Look for error messages in backend console

### System Requirements

- **Operating System**: Windows 10/11
- **RAM**: Minimum 4GB, Recommended 8GB+
- **Disk Space**: 2GB free space
- **Java**: Version 17 or higher
- **Python**: Version 3.8 or higher
- **Maven**: Version 3.6 or higher

### Port Usage

- **Backend**: Port 8080
- **Frontend**: Port 5000

Make sure these ports are not in use by other applications.

## Features Available

### With Full Installation
- ✅ All 7 analytics modules
- ✅ Real-time data processing
- ✅ Interactive charts and visualizations
- ✅ CSV/PDF export functionality
- ✅ Advanced filtering (season, festival, outlet)
- ✅ Anomaly detection
- ✅ Large file processing (5GB+)

### With Python-only (Mock Data)
- ✅ User interface demonstration
- ✅ Basic chart examples
- ❌ Real data processing
- ❌ Export functionality
- ❌ Advanced analytics

## Next Steps

After successful installation:

1. **Explore the Dashboard**: Navigate through different analytics modules
2. **Try Filtering**: Use the outlet, season, and festival filters
3. **Export Reports**: Generate CSV and PDF reports
4. **Upload Data**: Use the CSV upload feature for your own data
5. **API Integration**: Use the REST APIs for custom integrations

## Support

If you encounter issues:

1. Check this troubleshooting guide
2. Verify all prerequisites are correctly installed
3. Check the console output for error messages
4. Try the Python-only version first to verify basic functionality

## System Architecture

```
CSV Data → Java Backend (Spring Boot) → REST APIs → Python Frontend (Flask) → Web Dashboard
```

The system is designed to be modular and scalable, with clear separation between data processing (Java) and presentation (Python/Web).