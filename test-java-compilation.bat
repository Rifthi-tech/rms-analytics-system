@echo off
echo Testing Java Backend Compilation...

echo Checking Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Java not found. Please install Java 17+ first.
    exit /b 1
)

echo Checking Maven...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Maven not found. Please install Maven first.
    exit /b 1
)

echo Compiling backend...
cd backend
mvn clean compile
if %errorlevel% neq 0 (
    echo Compilation failed. Check error messages above.
    cd ..
    exit /b 1
)

echo Compilation successful!
cd ..
echo Backend is ready to run with: mvn spring-boot:run