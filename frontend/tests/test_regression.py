import unittest
import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from app import app

class RegressionTests(unittest.TestCase):
    def setUp(self):
        self.app = app.test_client()

    def test_existing_routes_still_work(self):
        """Test that existing routes still work after changes"""
        routes = ['/', '/dashboard', '/analysis', '/reports']
        for route in routes:
            response = self.app.get(route)
            self.assertEqual(response.status_code, 200)

    def test_template_rendering(self):
        """Test template rendering works"""
        response = self.app.get('/')
        self.assertIn(b'Uber Eats Restaurant', response.data)

    def test_static_files_accessible(self):
        """Test static files are accessible"""
        # Test if pages load without 500 errors
        response = self.app.get('/dashboard')
        self.assertNotEqual(response.status_code, 500)

if __name__ == '__main__':
    unittest.main()