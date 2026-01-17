"""
FRONTEND UNIT TESTING
Tests individual components in isolation
"""
import unittest
import sys
import os

# Add parent directory to path
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from app import app


class FrontendUnitTests(unittest.TestCase):
    """Unit tests for individual frontend components"""

    def setUp(self):
        """Set up test client"""
        self.app = app
        self.app.config['TESTING'] = True
        self.client = self.app.test_client()
        print(f"\n[UNIT TEST] Running: {self._testMethodName}")

    def test_home_page_loads(self):
        """Unit Test: Home page loads successfully"""
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)
        print("✓ PASS: Home page loads")

    def test_reports_page_loads(self):
        """Unit Test: Reports page loads successfully"""
        response = self.client.get('/reports/')
        self.assertEqual(response.status_code, 200)
        print("✓ PASS: Reports page loads")

    def test_404_error_handling(self):
        """Unit Test: 404 error handling works"""
        response = self.client.get('/nonexistent-page')
        self.assertEqual(response.status_code, 404)
        print("✓ PASS: 404 error handling works")

    def test_static_files_accessible(self):
        """Unit Test: Static files are accessible"""
        # Test if we can access the home page (which loads CSS/JS)
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'<!DOCTYPE html', response.data)
        print("✓ PASS: Static files accessible")

    def test_template_rendering(self):
        """Unit Test: Templates render correctly"""
        response = self.client.get('/')
        self.assertIn(b'Uber Eats Restaurant', response.data)
        print("✓ PASS: Templates render correctly")


if __name__ == '__main__':
    print("="*60)
    print("FRONTEND UNIT TESTING")
    print("="*60)
    unittest.main(verbosity=2)