from PySide6.QtWidgets import QMainWindow, QTabWidget, QWidget, QVBoxLayout, QLabel, QMessageBox, QPushButton
from PySide6.QtCore import Qt, QTimer
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
        QTimer.singleShot(1000, self.check_backend)

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

        # Status indicator
        self.status_label = QLabel("⏳ Checking backend connection...")
        self.status_label.setStyleSheet("padding: 10px; background-color: #f39c12; color: white; font-weight: bold;")
        self.status_label.setAlignment(Qt.AlignCenter)
        layout.addWidget(self.status_label)

        self.tabs = QTabWidget()
        self.tabs.addTab(PeakDiningTab(), "Peak Dining Analysis")
        self.tabs.addTab(CustomerSegmentTab(), "Customer Segmentation")
        self.tabs.addTab(SeasonalTab(), "Seasonal Behavior")
        self.tabs.addTab(MenuAnalysisTab(), "Menu Item Analysis")
        self.tabs.addTab(RevenueTab(), "Revenue Analysis")

        layout.addWidget(self.tabs)

        footer = QLabel("Backend: http://localhost:8080/api/analytics")
        footer.setStyleSheet("padding: 10px; background-color: #ecf0f1;")
        layout.addWidget(footer)

    def check_backend(self):
        if APIService.check_health():
            self.status_label.setText("✅ Backend Connected")
            self.status_label.setStyleSheet("padding: 10px; background-color: #27ae60; color: white; font-weight: bold;")
        else:
            self.status_label.setText("❌ Backend Not Running - Start backend first!")
            self.status_label.setStyleSheet("padding: 10px; background-color: #e74c3c; color: white; font-weight: bold;")

            msg = QMessageBox(self)
            msg.setIcon(QMessageBox.Critical)
            msg.setWindowTitle("Backend Connection Failed")
            msg.setText("Cannot connect to backend server!")
            msg.setInformativeText(
                "The Java backend is not running.\n\n"
                "To start the backend:\n"
                "1. Open a terminal\n"
                "2. cd backend\n"
                "3. mvn spring-boot:run\n"
                "4. Wait for 'Started RestaurantAnalyticsApplication'\n"
                "5. Restart this frontend\n\n"
                "Backend URL: http://localhost:8080"
            )
            msg.setStandardButtons(QMessageBox.Retry | QMessageBox.Close)

            result = msg.exec()
            if result == QMessageBox.Retry:
                QTimer.singleShot(1000, self.check_backend)