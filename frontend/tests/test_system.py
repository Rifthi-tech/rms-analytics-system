import unittest
import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from app import app

class SystemTests(unittest.TestCase):
    def setUp(self):
        self.app = app.test_client()

    def test_complete_user_workflow(self):
        """Test complete user workflow"""
        # Navigate to dashboard
        response = self.app.get('/dashboard')
        self.assertEqual(response.status_code, 200)
        
        # Navigate to analysis
        response = self.app.get('/analysis')
        self.assertEqual(response.status_code, 200)
        
        # Navigate to reports
        response = self.app.get('/reports')
        self.assertEqual(response.status_code, 200)

    def test_navigation_flow(self):
        """Test navigation between pages"""
        pages = ['/', '/dashboard', '/analysis', '/reports']
        for page in pages:
            response = self.app.get(page)
            self.assertEqual(response.status_code, 200)

if __name__ == '__main__':
    unittest.main()