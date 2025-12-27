from PySide6.QtWidgets import QWidget, QVBoxLayout, QLabel, QPushButton, QTextEdit
from services.api_service import APIService

class SeasonalTab(QWidget):
    def __init__(self):
        super().__init__()
        self.init_ui()

    def init_ui(self):
        layout = QVBoxLayout()

        title = QLabel("Seasonal Behavior Analysis")
        title.setStyleSheet("font-size: 18px; font-weight: bold; margin: 10px;")
        layout.addWidget(title)

        self.load_btn = QPushButton("Load Seasonal Data")
        self.load_btn.clicked.connect(self.load_data)
        layout.addWidget(self.load_btn)

        self.result_text = QTextEdit()
        self.result_text.setReadOnly(True)
        layout.addWidget(self.result_text)

        self.setLayout(layout)

    def load_data(self):
        self.result_text.setText("Loading data...")
        data = APIService.get_seasonal()

        if data:
            output = "=== SEASONAL BEHAVIOR ===\n\n"

            output += "Monthly Revenue:\n"
            revenue = data.get('monthlyRevenue', {})
            for month, rev in revenue.items():
                output += f"  {month}: LKR {rev:,.2f}\n"

            output += "\nMonthly Order Count:\n"
            orders = data.get('monthlyOrders', {})
            for month, count in orders.items():
                output += f"  {month}: {count} orders\n"

            self.result_text.setText(output)
        else:
            self.result_text.setText("Failed to load data. Check backend connection.")