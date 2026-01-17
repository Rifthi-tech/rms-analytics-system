@echo off
echo ============================================================
echo QUICK TEST SUITE - UBER EATS RESTAURANT ANALYTICS
echo ============================================================
echo.

echo [1/2] Running Frontend Tests...
echo ============================================================
cd frontend
python tests/test_app.py
echo.

echo [2/2] Running Backend Tests...
echo ============================================================
cd ../backend
call mvn test -q
cd ..
echo.

echo ============================================================
echo QUICK TEST COMPLETED
echo ============================================================
pause
