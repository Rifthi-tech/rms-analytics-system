import requests
from typing import Dict, Any

class APIService:
    BASE_URL = "http://localhost:8081/api/api/analytics"

    @staticmethod
    def get_peak_dining() -> Dict[str, Any]:
        try:
            response = requests.get(f"{APIService.BASE_URL}/peak-dining", timeout=10)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.ConnectionError:
            print(f"ERROR: Cannot connect to {APIService.BASE_URL}")
            print("Make sure backend is running on http://localhost:8080")
            return {"error": "Connection refused - Backend not running"}
        except requests.exceptions.Timeout:
            print("ERROR: Request timed out")
            return {"error": "Request timeout"}
        except requests.exceptions.RequestException as e:
            print(f"ERROR: {str(e)}")
            return {"error": str(e)}

    @staticmethod
    def get_customer_segment() -> Dict[str, Any]:
        try:
            response = requests.get(f"{APIService.BASE_URL}/customer-segment", timeout=10)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            print(f"Error fetching customer segment data: {e}")
            return {}

    @staticmethod
    def get_seasonal() -> Dict[str, Any]:
        try:
            response = requests.get(f"{APIService.BASE_URL}/seasonal", timeout=10)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            print(f"Error fetching seasonal data: {e}")
            return {}

    @staticmethod
    def get_menu_items() -> Dict[str, Any]:
        try:
            response = requests.get(f"{APIService.BASE_URL}/menu-items", timeout=10)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            print(f"Error fetching menu items data: {e}")
            return {}

    @staticmethod
    def get_revenue() -> Dict[str, Any]:
        try:
            response = requests.get(f"{APIService.BASE_URL}/revenue", timeout=10)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            print(f"Error fetching revenue data: {e}")
            return {}

    @staticmethod
    def get_branch_performance() -> Dict[str, Any]:
        try:
            response = requests.get(f"{APIService.BASE_URL}/branch-performance", timeout=10)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            print(f"Error fetching branch performance data: {e}")
            return {}

    @staticmethod
    def check_health() -> bool:
        try:
            response = requests.get(f"{APIService.BASE_URL}/health", timeout=5)
            return response.status_code == 200
        except:
            return False