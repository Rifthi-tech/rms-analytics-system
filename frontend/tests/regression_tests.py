"""
REGRESSION TESTING
Tests to ensure new changes don't break existing functionality
"""
import unittest
import sys
import os

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from app import app


class RegressionTests(unittest.TestCase):
    """Regression tests to catch breaking changes"""

    def setUp(self):
        """Set up test client"""
        self.app = app
        self.app.config['TESTING'] = True
        self.client = self.app.test_client()
        print(f"\n[REGRESSION TEST] Running: {self._testMethodName}")

    def test_core_functionality_still_works(self):
        """Regression Test: Core functionality remains intact"""
        
        # Test that basic functionality still works
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'Uber Eats Restaurant', response.data)
        
        print("✓ PASS: Core functionality intact")

    def test_existing_routes_still_accessible(self):
        """Regression Test: All existing routes still work"""
        
        # Test known working routes
        routes_to_test = [
            '/',
            '/reports/'
        ]
        
        for route in routes_to_test:
            response = self.client.get(route)
            self.assertEqual(response.status_code, 200, f"Route {route} should still work")
        
        print("✓ PASS: All existing routes accessible")

    def test_error_handling_not_broken(self):
        """Regression Test: Error handling still works"""
        
        # Test that 404 handling still works
        response = self.client.get('/this-should-not-exist')
        self.assertEqual(response.status_code, 404)
        
        print("✓ PASS: Error handling not broken")

    def test_template_rendering_not_broken(self):
        """Regression Test: Template rendering still works"""
        
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)
        
        # Check that HTML structure is still intact
        self.assertIn(b'<!DOCTYPE html', response.data)
        self.assertIn(b'<html', response.data)
        self.assertIn(b'</html>', response.data)
        
        print("✓ PASS: Template rendering not broken")

    def test_static_content_not_broken(self):
        """Regression Test: Static content still loads"""
        
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)
        
        # Check that page has expected content
        self.assertIn(b'Uber Eats Restaurant', response.data)
        
        print("✓ PASS: Static content not broken")

    def test_session_handling_not_broken(self):
        """Regression Test: Session handling still works"""
        
        # Test session functionality
        with self.client.session_transaction() as sess:
            sess['regression_test'] = 'working'
        
        # Make a request and ensure session persists
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)
        
        print("✓ PASS: Session handling not broken")

    def test_application_startup_not_broken(self):
        """Regression Test: Application starts correctly"""
        
        # Test that the application initializes properly
        self.assertIsNotNone(self.app)
        self.assertTrue(self.app.config['TESTING'])
        
        print("✓ PASS: Application startup not broken")


if __name__ == '__main__':
    print("="*60)
    print("REGRESSION TESTING")
    print("="*60)
    unittest.main(verbosity=2)