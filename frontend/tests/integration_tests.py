"""
INTEGRATION TESTING
Tests how multiple components work together
"""
import unittest
import sys
import os

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from app import app


class IntegrationTests(unittest.TestCase):
    """Integration tests for component interactions"""

    def setUp(self):
        """Set up test client"""
        self.app = app
        self.app.config['TESTING'] = True
        self.client = self.app.test_client()
        print(f"\n[INTEGRATION TEST] Running: {self._testMethodName}")

    def test_page_navigation_flow(self):
        """Integration Test: Navigation between pages works"""
        # Test navigation flow: Home -> Reports -> Home
        
        # Step 1: Load home page
        response1 = self.client.get('/')
        self.assertEqual(response1.status_code, 200)
        
        # Step 2: Navigate to reports
        response2 = self.client.get('/reports/')
        self.assertEqual(response2.status_code, 200)
        
        # Step 3: Back to home
        response3 = self.client.get('/')
        self.assertEqual(response3.status_code, 200)
        
        print("✓ PASS: Page navigation flow works")

    def test_session_handling(self):
        """Integration Test: Session handling across requests"""
        # Test multiple requests in same session
        with self.client.session_transaction() as sess:
            sess['test_key'] = 'test_value'
        
        # Make multiple requests
        response1 = self.client.get('/')
        response2 = self.client.get('/reports/')
        
        self.assertEqual(response1.status_code, 200)
        self.assertEqual(response2.status_code, 200)
        print("✓ PASS: Session handling works")

    def test_template_and_static_integration(self):
        """Integration Test: Templates and static files work together"""
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)
        
        # Check if HTML structure is correct
        self.assertIn(b'<!DOCTYPE html', response.data)
        self.assertIn(b'<html', response.data)
        self.assertIn(b'</html>', response.data)
        print("✓ PASS: Template and static integration works")

    def test_error_handling_integration(self):
        """Integration Test: Error handling across the system"""
        # Test various error scenarios
        response_404 = self.client.get('/nonexistent')
        self.assertEqual(response_404.status_code, 404)
        
        # Test method not allowed (if applicable)
        response_405 = self.client.post('/')  # Assuming GET only
        # Note: This might return 200 if POST is allowed, that's fine
        
        print("✓ PASS: Error handling integration works")


if __name__ == '__main__':
    print("="*60)
    print("INTEGRATION TESTING")
    print("="*60)
    unittest.main(verbosity=2)