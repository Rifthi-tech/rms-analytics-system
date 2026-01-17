@echo off
echo ============================================================
echo UBER EATS RESTAURANT ANALYTICS - COMPREHENSIVE TEST SUITE
echo ============================================================
echo.

set TOTAL_TESTS=0
set PASSED_TESTS=0
set FAILED_TESTS=0

echo [1/6] BACKEND UNIT TESTS
echo ============================================================
cd backend
call mvn clean test -Dtest=AnalyticsApplicationTests,AnalyticsControllerTest,AnalyticsServiceTest
if %ERRORLEVEL% EQU 0 (
    echo [PASSED] Backend Unit Tests
    set /a PASSED_TESTS+=1
) else (
    echo [FAILED] Backend Unit Tests
    set /a FAILED_TESTS+=1
)
set /a TOTAL_TESTS+=1
cd ..
echo.

echo [2/6] BACKEND INTEGRATION TESTS
echo ============================================================
cd backend
call mvn test -Dtest=AnalyticsIntegrationTest
if %ERRORLEVEL% EQU 0 (
    echo [PASSED] Backend Integration Tests
    set /a PASSED_TESTS+=1
) else (
    echo [FAILED] Backend Integration Tests
    set /a FAILED_TESTS+=1
)
set /a TOTAL_TESTS+=1
cd ..
echo.

echo [3/6] FRONTEND UNIT TESTS
echo ============================================================
cd frontend
python -m pytest tests/test_app.py -v --tb=short
if %ERRORLEVEL% EQU 0 (
    echo [PASSED] Frontend Unit Tests
    set /a PASSED_TESTS+=1
) else (
    echo [FAILED] Frontend Unit Tests
    set /a FAILED_TESTS+=1
)
set /a TOTAL_TESTS+=1
cd ..
echo.

echo [4/6] FRONTEND INTEGRATION TESTS
echo ============================================================
cd frontend
python -m pytest tests/test_integration.py -v --tb=short
if %ERRORLEVEL% EQU 0 (
    echo [PASSED] Frontend Integration Tests
    set /a PASSED_TESTS+=1
) else (
    echo [FAILED] Frontend Integration Tests
    set /a FAILED_TESTS+=1
)
set /a TOTAL_TESTS+=1
cd ..
echo.

echo [5/6] FRONTEND PERFORMANCE TESTS
echo ============================================================
cd frontend
python -m pytest tests/test_performance.py -v --tb=short
if %ERRORLEVEL% EQU 0 (
    echo [PASSED] Frontend Performance Tests
    set /a PASSED_TESTS+=1
) else (
    echo [FAILED] Frontend Performance Tests
    set /a FAILED_TESTS+=1
)
set /a TOTAL_TESTS+=1
cd ..
echo.

echo [6/6] SYSTEM REGRESSION TESTS
echo ============================================================
echo Running regression test suite...
cd frontend
python tests/test_app.py > nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo [PASSED] System Regression Tests
    set /a PASSED_TESTS+=1
) else (
    echo [FAILED] System Regression Tests
    set /a FAILED_TESTS+=1
)
set /a TOTAL_TESTS+=1
cd ..
echo.

echo ============================================================
echo TEST SUMMARY
echo ============================================================
echo Total Test Suites: %TOTAL_TESTS%
echo Passed: %PASSED_TESTS%
echo Failed: %FAILED_TESTS%
echo.

if %FAILED_TESTS% EQU 0 (
    echo [SUCCESS] All tests passed! ✓
    echo.
    echo Your Uber Eats Restaurant Analytics system is ready for deployment.
) else (
    echo [WARNING] Some tests failed. Please review the output above.
)

echo.
echo ============================================================
pause
