from PySide6.QtWidgets import QWidget, QVBoxLayout, QLabel, QPushButton, QTextEdit
from services.api_service import APIService

class RevenueTab(QWidget):
    def __init__(self):
        super().__init__()
        self.init_ui()

    def init_ui(self):
        layout = QVBoxLayout()

        title = QLabel("Revenue Analysis")
        title.setStyleSheet("font-size: 18px; font-weight: bold; margin: 10px;")
        layout.addWidget(title)

        self.load_btn = QPushButton("Load Revenue Data")
        self.load_btn.clicked.connect(self.load_data)
        layout.addWidget(self.load_btn)

        self.result_text = QTextEdit()
        self.result_text.setReadOnly(True)
        layout.addWidget(self.result_text)

        self.setLayout(layout)

    def load_data(self):
        self.result_text.setText("Loading data...")
        data = APIService.get_revenue()

        if data:
            output = "=== REVENUE ANALYSIS ===\n\n"

            total = data.get('totalRevenue', 0)
            output += f"Total Revenue: LKR {total:,.2f}\n"

            orders = data.get('totalOrders', 0)
            output += f"Total Orders: {orders}\n"

            avg = data.get('avgOrderValue', 0)
            output += f"Average Order Value: LKR {avg:,.2f}\n\n"

            output += "Revenue by Payment Method:\n"
            payment = data.get('paymentMethodRevenue', {})
            for method, rev in payment.items():
                percentage = (rev / total * 100) if total > 0 else 0
                output += f"  {method}: LKR {rev:,.2f} ({percentage:.1f}%)\n"

            self.result_text.setText(output)
        else:
            self.result_text.setText("Failed to load data. Check backend connection.")