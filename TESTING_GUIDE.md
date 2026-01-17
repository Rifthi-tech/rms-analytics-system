# Uber Eats Restaurant Analytics - Testing Guide

## Overview
This document describes the comprehensive testing strategy for the Uber Eats Restaurant Analytics system, covering both frontend (Python/Flask) and backend (Java/Spring Boot) components.

## Test Types Implemented

### 1. Unit Testing
**Purpose**: Test individual components in isolation

**Backend (Java/Spring Boot)**:
- Location: `backend/src/test/java/com/restaurant/`
- Framework: JUnit 5, Spring Boot Test
- Tests:
  - `AnalyticsApplicationTests.java` - Application context loading
  - `AnalyticsControllerTest.java` - Controller endpoints
  - `AnalyticsServiceTest.java` - Business logic

**Frontend (Python/Flask)**:
- Location: `frontend/tests/test_app.py`
- Framework: pytest, unittest
- Tests:
  - Home page rendering
  - Analytics page loading
  - Route handling
  - Error handling (404)

### 2. Integration Testing
**Purpose**: Test interaction between components

**Backend**:
- Location: `backend/src/test/java/com/restaurant/integration/`
- Tests:
  - `AnalyticsIntegrationTest.java` - End-to-end API workflows
  - Full analytics workflow
  - Revenue analytics pipeline
  - Customer analytics pipeline

**Frontend**:
- Location: `frontend/tests/test_integration.py`
- Tests:
  - Navigation flow between pages
  - Complete analytics workflow
  - Reports generation workflow
  - Data consistency across pages

### 3. System Testing
**Purpose**: Test the complete system as a whole

**Covered by**:
- Integration tests that span multiple components
- End-to-end workflow tests
- Full user journey tests

### 4. Performance Testing
**Purpose**: Ensure system meets performance requirements

**Frontend**:
- Location: `frontend/tests/test_performance.py`
- Tests:
  - Page load time (< 2 seconds for home, < 3 seconds for analytics)
  - Concurrent request handling
  - Memory usage monitoring
  - Average response time

**Metrics**:
- Home page: < 2 seconds
- Analytics pages: < 3 seconds
- Concurrent requests: < 1 second average
- No memory leaks after 20 requests

### 5. Regression Testing
**Purpose**: Ensure new changes don't break existing functionality

**Implementation**:
- All unit and integration tests serve as regression tests
- Run automatically before each deployment
- Baseline test suite maintained

### 6. Automated Testing
**Purpose**: Enable continuous testing and CI/CD

**Tools**:
- Maven for backend tests
- pytest for frontend tests
- Batch scripts for automated execution

## Running Tests

### Quick Test (Recommended for Development)
```bash
run-quick-tests.bat
```

### Comprehensive Test Suite
```bash
run-all-tests.bat
```

### Individual Test Suites

#### Backend Tests Only
```bash
cd backend
mvn test
```

#### Frontend Tests Only
```bash
cd frontend
python -m pytest tests/ -v
```

#### Specific Test File
```bash
cd frontend
python -m pytest tests/test_app.py -v
```

#### With Coverage Report
```bash
cd frontend
python -m pytest tests/ --cov=. --cov-report=html
```

## Test Results Interpretation

### Success Indicators
- ✓ All tests pass
- No errors in console output
- Exit code 0
- "All tests passed!" message

### Failure Indicators
- ✗ Test failures shown
- Error stack traces
- Non-zero exit code
- "Some tests failed" message

## Test Coverage

### Backend Coverage
- Controllers: API endpoints
- Services: Business logic
- Integration: End-to-end workflows

### Frontend Coverage
- Routes: All Flask routes
- Templates: Page rendering
- Navigation: User flows
- Performance: Load times

## Continuous Integration

### Pre-Commit Testing
Run quick tests before committing:
```bash
run-quick-tests.bat
```

### Pre-Deployment Testing
Run full test suite before deployment:
```bash
run-all-tests.bat
```

## Test Maintenance

### Adding New Tests

**Backend**:
1. Create test class in `backend/src/test/java/com/restaurant/`
2. Use `@Test` annotation
3. Follow naming convention: `Test*.java` or `*Test.java`

**Frontend**:
1. Create test file in `frontend/tests/`
2. Use `test_*.py` naming convention
3. Import unittest or use pytest fixtures

### Updating Tests
- Update tests when functionality changes
- Maintain test documentation
- Keep test data current

## Best Practices

1. **Write tests first** (TDD approach when possible)
2. **Keep tests independent** - No test should depend on another
3. **Use descriptive names** - Test names should explain what they test
4. **Test edge cases** - Include boundary conditions
5. **Mock external dependencies** - Isolate unit tests
6. **Run tests frequently** - Before commits and deployments
7. **Maintain test data** - Keep test datasets up to date
8. **Document test failures** - Record and track issues

## Troubleshooting

### Common Issues

**Backend Tests Fail**:
- Check Java version (requires Java 17)
- Verify Maven installation
- Check database connectivity
- Review application-test.properties

**Frontend Tests Fail**:
- Check Python version (requires Python 3.8+)
- Install test dependencies: `pip install -r requirements.txt`
- Verify Flask app configuration
- Check test data availability

**Performance Tests Fail**:
- System may be under load
- Check network connectivity
- Verify resource availability
- Review timeout thresholds

## Test Reports

### Generating Reports

**Backend (Maven Surefire)**:
```bash
cd backend
mvn test
# Reports in: target/surefire-reports/
```

**Frontend (pytest with coverage)**:
```bash
cd frontend
python -m pytest tests/ --cov=. --cov-report=html
# Reports in: htmlcov/index.html
```

## Contact & Support

For testing issues or questions:
- Review test output logs
- Check TESTING_GUIDE.md
- Consult development team

---

**Last Updated**: January 2025
**Version**: 1.0.0
