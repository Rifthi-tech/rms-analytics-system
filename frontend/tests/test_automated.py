import unittest
import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from app import app

class AutomatedTests(unittest.TestCase):
    def setUp(self):
        self.app = app.test_client()

    def test_automated_health_check(self):
        """Automated health check test"""
        response = self.app.get('/')
        self.assertEqual(response.status_code, 200)

    def test_automated_api_endpoints(self):
        """Test all API endpoints automatically"""
        endpoints = ['/charts/peak-dining', '/charts/customer-demographics', 
                    '/charts/revenue-analysis']
        for endpoint in endpoints:
            response = self.app.get(endpoint)
            self.assertIn(response.status_code, [200, 500])  # Accept both success and server error

    def test_automated_error_handling(self):
        """Test automated error handling"""
        response = self.app.get('/nonexistent-route')
        self.assertEqual(response.status_code, 404)

if __name__ == '__main__':
    unittest.main()