import unittest
import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from app import app

class UnitTests(unittest.TestCase):
    def setUp(self):
        self.app = app.test_client()
        self.app.testing = True

    def test_app_creation(self):
        """Test Flask app creation"""
        self.assertIsNotNone(app)

    def test_home_route(self):
        """Test home page route"""
        response = self.app.get('/')
        self.assertEqual(response.status_code, 200)

    def test_dashboard_route(self):
        """Test dashboard route"""
        response = self.app.get('/dashboard')
        self.assertEqual(response.status_code, 200)

    def test_reports_route(self):
        """Test reports route"""
        response = self.app.get('/reports')
        self.assertEqual(response.status_code, 200)

    def test_analysis_route(self):
        """Test analysis route"""
        response = self.app.get('/analysis')
        self.assertEqual(response.status_code, 200)

if __name__ == '__main__':
    unittest.main()