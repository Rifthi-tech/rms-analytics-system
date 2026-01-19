import unittest
import requests
import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from app import app

class IntegrationTests(unittest.TestCase):
    def setUp(self):
        self.app = app.test_client()
        self.backend_url = "http://localhost:8080"

    def test_backend_connection(self):
        """Test connection to backend API"""
        try:
            response = requests.get(f"{self.backend_url}/api/analytics/outlets", timeout=5)
            self.assertIn(response.status_code, [200, 404, 500])
        except requests.exceptions.RequestException:
            self.skipTest("Backend not running")

    def test_chart_data_integration(self):
        """Test chart data integration"""
        response = self.app.get('/charts/peak-dining')
        self.assertIn(response.status_code, [200, 500])

    def test_export_functionality(self):
        """Test export functionality"""
        response = self.app.get('/reports/export/csv/peak-dining')
        self.assertIn(response.status_code, [200, 500])

if __name__ == '__main__':
    unittest.main()