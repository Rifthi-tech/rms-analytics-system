from PySide6.QtWidgets import QWidget, QVBoxLayout, QLabel, QPushButton, QTextEdit
from services.api_service import APIService

class MenuAnalysisTab(QWidget):
    def __init__(self):
        super().__init__()
        self.init_ui()

    def init_ui(self):
        layout = QVBoxLayout()

        title = QLabel("Menu Item Analysis")
        title.setStyleSheet("font-size: 18px; font-weight: bold; margin: 10px;")
        layout.addWidget(title)

        self.load_btn = QPushButton("Load Menu Analysis")
        self.load_btn.clicked.connect(self.load_data)
        layout.addWidget(self.load_btn)

        self.result_text = QTextEdit()
        self.result_text.setReadOnly(True)
        layout.addWidget(self.result_text)

        self.setLayout(layout)

    def load_data(self):
        self.result_text.setText("Loading data...")
        data = APIService.get_menu_items()

        if data:
            output = "=== MENU ITEM ANALYSIS ===\n\n"

            output += "Top 10 Items by Order Count:\n"
            top_items = data.get('topItems', {})
            for i, (item, count) in enumerate(top_items.items(), 1):
                output += f"  {i}. {item}: {count} orders\n"

            output += "\nTop 10 Items by Revenue:\n"
            item_revenue = data.get('itemRevenue', {})
            for i, (item, rev) in enumerate(item_revenue.items(), 1):
                output += f"  {i}. {item}: LKR {rev:,.2f}\n"

            output += "\nCategory Distribution:\n"
            categories = data.get('categoryDistribution', {})
            for cat, count in categories.items():
                output += f"  {cat}: {count} orders\n"

            self.result_text.setText(output)
        else:
            self.result_text.setText("Failed to load data. Check backend connection.")