from PySide6.QtWidgets import QWidget, QVBoxLayout, QLabel, QPushButton, QTextEdit
from PySide6.QtCore import Qt
from services.api_service import APIService
import json

class PeakDiningTab(QWidget):
    def __init__(self):
        super().__init__()
        self.init_ui()

    def init_ui(self):
        layout = QVBoxLayout()

        title = QLabel("Peak Dining Analysis")
        title.setStyleSheet("font-size: 18px; font-weight: bold; margin: 10px;")
        layout.addWidget(title)

        self.load_btn = QPushButton("Load Peak Dining Data")
        self.load_btn.clicked.connect(self.load_data)
        layout.addWidget(self.load_btn)

        self.result_text = QTextEdit()
        self.result_text.setReadOnly(True)
        layout.addWidget(self.result_text)

        self.setLayout(layout)

    def load_data(self):
        self.result_text.setText("Loading data...")
        data = APIService.get_peak_dining()

        if data:
            output = "=== PEAK DINING ANALYSIS ===\n\n"

            output += f"Peak Hour: {data.get('peakHour', 'N/A')}:00\n"
            output += f"Peak Day: {data.get('peakDay', 'N/A')}\n\n"

            output += "Hourly Distribution:\n"
            hourly = data.get('hourly', {})
            for hour in sorted(hourly.keys()):
                output += f"  {hour}:00 - {hourly[hour]} orders\n"

            output += "\nDaily Distribution:\n"
            daily = data.get('daily', {})
            for day, count in daily.items():
                output += f"  {day}: {count} orders\n"

            output += "\nMonthly Distribution:\n"
            monthly = data.get('monthly', {})
            for month in sorted(monthly.keys()):
                output += f"  Month {month}: {monthly[month]} orders\n"

            self.result_text.setText(output)
        else:
            self.result_text.setText("Failed to load data. Check backend connection.")