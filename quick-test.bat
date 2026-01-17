@echo off
cls
echo.
echo ============================================================
echo              QUICK TEST - UBER EATS RESTAURANT
echo ============================================================
echo.

cd frontend

echo Testing your website...
echo.

python -m pytest tests/test_app.py::TestFlaskApp::test_home_page -v --tb=no
if %ERRORLEVEL% EQU 0 (
    echo ✓ Home page works
) else (
    echo ✗ Home page broken
)

python -m pytest tests/test_app.py::TestFlaskApp::test_reports_page -v --tb=no
if %ERRORLEVEL% EQU 0 (
    echo ✓ Reports page works
) else (
    echo ✗ Reports page broken
)

python -m pytest tests/test_app.py::TestFlaskApp::test_404_error -v --tb=no
if %ERRORLEVEL% EQU 0 (
    echo ✓ Error handling works
) else (
    echo ✗ Error handling broken
)

echo.
echo ============================================================
echo Test complete!
echo ============================================================

cd ..
pause