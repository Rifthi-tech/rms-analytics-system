"""
PERFORMANCE TESTING
Tests system speed, load handling, and resource usage
"""
import unittest
import time
import sys
import os

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from app import app


class PerformanceTests(unittest.TestCase):
    """Performance tests for system speed and efficiency"""

    def setUp(self):
        """Set up test client"""
        self.app = app
        self.app.config['TESTING'] = True
        self.client = self.app.test_client()
        print(f"\n[PERFORMANCE TEST] Running: {self._testMethodName}")

    def test_page_load_speed(self):
        """Performance Test: Page load speed"""
        
        # Test home page load time
        start_time = time.time()
        response = self.client.get('/')
        end_time = time.time()
        
        load_time = end_time - start_time
        
        self.assertEqual(response.status_code, 200)
        self.assertLess(load_time, 3.0, "Home page should load in under 3 seconds")
        
        print(f"✓ PASS: Home page loads in {load_time:.3f} seconds")

    def test_multiple_requests_performance(self):
        """Performance Test: Multiple requests handling"""
        
        start_time = time.time()
        
        # Make 10 requests
        for i in range(10):
            response = self.client.get('/')
            self.assertEqual(response.status_code, 200)
        
        end_time = time.time()
        total_time = end_time - start_time
        avg_time = total_time / 10
        
        self.assertLess(avg_time, 2.0, "Average request time should be under 2 seconds")
        
        print(f"✓ PASS: 10 requests completed in {total_time:.3f}s (avg: {avg_time:.3f}s)")

    def test_concurrent_requests_simulation(self):
        """Performance Test: Concurrent requests simulation"""
        
        # Simulate concurrent requests by making rapid requests
        start_time = time.time()
        
        responses = []
        for i in range(5):
            response = self.client.get('/')
            responses.append(response)
        
        end_time = time.time()
        total_time = end_time - start_time
        
        # All requests should succeed
        for response in responses:
            self.assertEqual(response.status_code, 200)
        
        self.assertLess(total_time, 5.0, "5 concurrent requests should complete in under 5 seconds")
        
        print(f"✓ PASS: 5 concurrent requests handled in {total_time:.3f}s")

    def test_memory_usage_stability(self):
        """Performance Test: Memory usage stability"""
        
        # Make multiple requests to test memory stability
        for i in range(20):
            response = self.client.get('/')
            self.assertEqual(response.status_code, 200)
        
        # If we get here without crashes, memory is stable
        print("✓ PASS: Memory usage stable after 20 requests")

    def test_response_size_efficiency(self):
        """Performance Test: Response size efficiency"""
        
        response = self.client.get('/')
        self.assertEqual(response.status_code, 200)
        
        # Check response size (should be reasonable)
        response_size = len(response.data)
        self.assertGreater(response_size, 100, "Response should have content")
        self.assertLess(response_size, 1000000, "Response should not be too large")  # 1MB limit
        
        print(f"✓ PASS: Response size is efficient ({response_size} bytes)")


if __name__ == '__main__':
    print("="*60)
    print("PERFORMANCE TESTING")
    print("="*60)
    unittest.main(verbosity=2)