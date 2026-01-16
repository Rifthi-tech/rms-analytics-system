@echo off
REM RMS Analytics System - Windows Setup Script

echo.
echo ============================================
echo  RMS Analytics System - Setup & Launch
echo ============================================
echo.

REM Check Java
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java not found. Please install Java 11+
    pause
    exit /b 1
)

REM Check Maven
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven not found. Please install Maven
    pause
    exit /b 1
)

REM Check Node
node -v >nul 2>&1
if errorlevel 1 (
    echo ERROR: Node.js not found. Please install Node.js 14+
    pause
    exit /b 1
)

echo ✓ All prerequisites found
echo.

REM Build Backend
echo [1/4] Building Backend...
cd backend
call mvn clean install -q
if errorlevel 1 (
    echo ERROR: Backend build failed
    pause
    exit /b 1
)
cd ..
echo ✓ Backend built successfully

echo.
echo [2/4] Installing Frontend Dependencies...
cd frontend
call npm install -q
if errorlevel 1 (
    echo ERROR: Frontend installation failed
    pause
    exit /b 1
)
cd ..
echo ✓ Frontend dependencies installed

echo.
echo [3/4] Starting Backend Server...
start "RMS Backend" cmd /k "cd backend && mvn spring-boot:run"
echo ✓ Backend starting on http://localhost:8080

echo.
timeout /t 5 /nobreak
echo [4/4] Starting Frontend Server...
start "RMS Frontend" cmd /k "cd frontend && npm start"
echo ✓ Frontend starting on http://localhost:3000

echo.
echo ============================================
echo  ✓ Setup Complete!
echo ============================================
echo.
echo Frontend:  http://localhost:3000
echo Backend:   http://localhost:8080
echo.
echo To load data, use file path:
echo r:\HND-23 CSD\4th SEMESTER\APDP\restaurant_dataset.csv
echo.
pause
