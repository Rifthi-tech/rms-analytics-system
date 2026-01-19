import unittest
import time
import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from app import app

class PerformanceTests(unittest.TestCase):
    def setUp(self):
        self.app = app.test_client()

    def test_page_load_time(self):
        """Test page load performance"""
        start_time = time.time()
        response = self.app.get('/')
        end_time = time.time()
        
        load_time = end_time - start_time
        self.assertLess(load_time, 2.0)  # Should load within 2 seconds
        self.assertEqual(response.status_code, 200)

    def test_multiple_requests(self):
        """Test handling multiple requests"""
        start_time = time.time()
        for _ in range(10):
            response = self.app.get('/dashboard')
            self.assertEqual(response.status_code, 200)
        end_time = time.time()
        
        total_time = end_time - start_time
        self.assertLess(total_time, 5.0)  # 10 requests within 5 seconds

if __name__ == '__main__':
    unittest.main()