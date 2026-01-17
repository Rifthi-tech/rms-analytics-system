@echo off
cls
echo.
echo ============================================================
echo    GENERATING COMPREHENSIVE TEST REPORT
echo ============================================================
echo.

echo [Step 1/4] Running Frontend Unit Tests...
cd frontend
python -m pytest tests/test_app.py -v --tb=short > ../test-results-frontend-unit.txt 2>&1
echo    ✓ Frontend unit tests completed

echo [Step 2/4] Running Frontend Integration Tests...
python -m pytest tests/test_integration.py -v --tb=short > ../test-results-frontend-integration.txt 2>&1
echo    ✓ Frontend integration tests completed

echo [Step 3/4] Running Frontend Performance Tests...
python -m pytest tests/test_performance.py -v --tb=short > ../test-results-frontend-performance.txt 2>&1
echo    ✓ Frontend performance tests completed

echo [Step 4/4] Generating Coverage Report...
python -m pytest tests/ --cov=. --cov-report=html --cov-report=term > ../test-results-coverage.txt 2>&1
echo    ✓ Coverage report generated

cd ..

echo.
echo ============================================================
echo    TEST REPORT GENERATED
echo ============================================================
echo.
echo Test result files created:
echo   - test-results-frontend-unit.txt
echo   - test-results-frontend-integration.txt
echo   - test-results-frontend-performance.txt
echo   - test-results-coverage.txt
echo   - frontend/htmlcov/index.html (Coverage HTML report)
echo.
echo ============================================================
echo    OPENING COVERAGE REPORT
echo ============================================================
echo.

if exist "frontend\htmlcov\index.html" (
    start frontend\htmlcov\index.html
    echo Coverage report opened in browser
) else (
    echo Coverage report not found
)

echo.
echo ============================================================
pause
