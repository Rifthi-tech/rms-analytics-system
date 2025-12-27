from PySide6.QtWidgets import QWidget, QVBoxLayout, QLabel, QPushButton, QTextEdit
from services.api_service import APIService

class CustomerSegmentTab(QWidget):
    def __init__(self):
        super().__init__()
        self.init_ui()

    def init_ui(self):
        layout = QVBoxLayout()

        title = QLabel("Customer Segmentation")
        title.setStyleSheet("font-size: 18px; font-weight: bold; margin: 10px;")
        layout.addWidget(title)

        self.load_btn = QPushButton("Load Customer Segments")
        self.load_btn.clicked.connect(self.load_data)
        layout.addWidget(self.load_btn)

        self.result_text = QTextEdit()
        self.result_text.setReadOnly(True)
        layout.addWidget(self.result_text)

        self.setLayout(layout)

    def load_data(self):
        self.result_text.setText("Loading data...")
        data = APIService.get_customer_segment()

        if data:
            output = "=== CUSTOMER SEGMENTATION ===\n\n"

            output += "Gender Distribution:\n"
            gender = data.get('genderDistribution', {})
            for g, count in gender.items():
                output += f"  {g}: {count} orders\n"

            output += "\nAge Distribution:\n"
            age = data.get('ageDistribution', {})
            for group, count in age.items():
                output += f"  {group}: {count} orders\n"

            output += "\nLoyalty Group Distribution:\n"
            loyalty = data.get('loyaltyDistribution', {})
            for tier, count in loyalty.items():
                output += f"  {tier}: {count} customers\n"

            output += "\nAverage Spending by Loyalty Group:\n"
            spending = data.get('loyaltyAvgSpending', {})
            for tier, avg in spending.items():
                output += f"  {tier}: LKR {avg:.2f}\n"

            self.result_text.setText(output)
        else:
            self.result_text.setText("Failed to load data. Check backend connection.")