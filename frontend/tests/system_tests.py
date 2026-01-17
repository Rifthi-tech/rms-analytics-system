"""
SYSTEM TESTING
Tests the complete system end-to-end
"""
import unittest
import sys
import os

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from app import app


class SystemTests(unittest.TestCase):
    """System tests for complete end-to-end workflows"""

    def setUp(self):
        """Set up test client"""
        self.app = app
        self.app.config['TESTING'] = True
        self.client = self.app.test_client()
        print(f"\n[SYSTEM TEST] Running: {self._testMethodName}")

    def test_complete_user_journey(self):
        """System Test: Complete user journey through the system"""
        
        # Step 1: User visits home page
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'Uber Eats Restaurant', response.data)
        
        # Step 2: User navigates to reports
        response = self.client.get('/reports/')
        self.assertEqual(response.status_code, 200)
        
        # Step 3: User goes back to home
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)
        
        print("✓ PASS: Complete user journey works")

    def test_system_availability(self):
        """System Test: System is available and responsive"""
        
        # Test multiple endpoints
        endpoints = ['/', '/reports/']
        
        for endpoint in endpoints:
            response = self.client.get(endpoint)
            self.assertEqual(response.status_code, 200)
        
        print("✓ PASS: System availability confirmed")

    def test_system_error_recovery(self):
        """System Test: System handles errors gracefully"""
        
        # Test invalid URLs
        invalid_urls = ['/invalid', '/nonexistent', '/bad-url']
        
        for url in invalid_urls:
            response = self.client.get(url)
            self.assertEqual(response.status_code, 404)
        
        # System should still work after errors
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)
        
        print("✓ PASS: System error recovery works")

    def test_system_data_flow(self):
        """System Test: Data flows correctly through the system"""
        
        # Test that pages load with expected content
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)
        
        # Check for key system elements
        self.assertIn(b'html', response.data.lower())
        
        print("✓ PASS: System data flow works")

    def test_system_performance_basic(self):
        """System Test: Basic system performance"""
        import time
        
        # Measure response time
        start_time = time.time()
        response = self.client.get('/')
        end_time = time.time()
        
        response_time = end_time - start_time
        
        self.assertEqual(response.status_code, 200)
        self.assertLess(response_time, 5.0, "Response should be under 5 seconds")
        
        print(f"✓ PASS: System performance OK ({response_time:.2f}s)")


if __name__ == '__main__':
    print("="*60)
    print("SYSTEM TESTING")
    print("="*60)
    unittest.main(verbosity=2)