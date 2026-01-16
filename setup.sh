#!/bin/bash
# RMS Analytics System - Linux/Mac Setup Script

echo ""
echo "============================================"
echo "  RMS Analytics System - Setup & Launch"
echo "============================================"
echo ""

# Check Java
if ! command -v java &> /dev/null; then
    echo "ERROR: Java not found. Please install Java 11+"
    exit 1
fi

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven not found. Please install Maven"
    exit 1
fi

# Check Node
if ! command -v node &> /dev/null; then
    echo "ERROR: Node.js not found. Please install Node.js 14+"
    exit 1
fi

echo "✓ All prerequisites found"
echo ""

# Build Backend
echo "[1/4] Building Backend..."
cd backend
mvn clean install -q
if [ $? -ne 0 ]; then
    echo "ERROR: Backend build failed"
    exit 1
fi
cd ..
echo "✓ Backend built successfully"

echo ""
echo "[2/4] Installing Frontend Dependencies..."
cd frontend
npm install -q
if [ $? -ne 0 ]; then
    echo "ERROR: Frontend installation failed"
    exit 1
fi
cd ..
echo "✓ Frontend dependencies installed"

echo ""
echo "[3/4] Starting Backend Server..."
cd backend
mvn spring-boot:run &
BACKEND_PID=$!
cd ..
echo "✓ Backend starting on http://localhost:8080"

echo ""
sleep 5
echo "[4/4] Starting Frontend Server..."
cd frontend
npm start &
FRONTEND_PID=$!
cd ..
echo "✓ Frontend starting on http://localhost:3000"

echo ""
echo "============================================"
echo "  ✓ Setup Complete!"
echo "============================================"
echo ""
echo "Frontend:  http://localhost:3000"
echo "Backend:   http://localhost:8080"
echo ""
echo "To load data, use file path:"
echo "r:\HND-23 CSD\4th SEMESTER\APDP\restaurant_dataset.csv"
echo ""
echo "Press Ctrl+C to stop servers"
echo ""

wait
