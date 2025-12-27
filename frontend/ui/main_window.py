from PySide6.QtWidgets import QMainWindow, QTabWidget, QWidget, QVBoxLayout, QLabel, QMessageBox
from PySide6.QtCore import Qt
from ui.tabs.peak_dining_tab import PeakDiningTab
from ui.tabs.customer_segment_tab import CustomerSegmentTab
from ui.tabs.seasonal_tab import SeasonalTab
from ui.tabs.menu_analysis_tab import MenuAnalysisTab
from ui.tabs.revenue_tab import RevenueTab
from services.api_service import APIService

class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.init_ui()
        self.check_backend()

    def init_ui(self):
        self.setWindowTitle("Restaurant Analytics System")
        self.setGeometry(100, 100, 1200, 800)

        central_widget = QWidget()
        self.setCentralWidget(central_widget)

        layout = QVBoxLayout()
        central_widget.setLayout(layout)

        header = QLabel("Restaurant Analytics Dashboard")
        header.setStyleSheet("font-size: 24px; font-weight: bold; padding: 20px; background-color: #2c3e50; color: white;")
        header.setAlignment(Qt.AlignCenter)
        layout.addWidget(header)

        self.tabs = QTabWidget()
        self.tabs.addTab(PeakDiningTab(), "Peak Dining Analysis")
        self.tabs.addTab(CustomerSegmentTab(), "Customer Segmentation")
        self.tabs.addTab(SeasonalTab(), "Seasonal Behavior")
        self.tabs.addTab(MenuAnalysisTab(), "Menu Item Analysis")
        self.tabs.addTab(RevenueTab(), "Revenue Analysis")

        layout.addWidget(self.tabs)

        footer = QLabel("Backend: http://localhost:8080")
        footer.setStyleSheet("padding: 10px; background-color: #ecf0f1;")
        layout.addWidget(footer)

    def check_backend(self):
        if not APIService.check_health():
            QMessageBox.warning(
                self,
                "Backend Connection",
                "Cannot connect to backend server at http://localhost:8080\n\n"
                "Please ensure the Java backend is running."
            )