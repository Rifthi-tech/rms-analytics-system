import sys
import json
import requests
import pandas as pd
from datetime import datetime, timedelta
from typing import Dict, Any, Optional
from pathlib import Path

from PySide6.QtWidgets import (
    QApplication, QMainWindow, QWidget, QVBoxLayout, QHBoxLayout,
    QTabWidget, QPushButton, QLabel, QLineEdit, QComboBox,
    QDateEdit, QFileDialog, QMessageBox, QGroupBox,
    QTableWidget, QTableWidgetItem, QTextEdit, QProgressBar,
    QSpinBox, QDoubleSpinBox, QCheckBox, QSplitter
)
from PySide6.QtCore import Qt, QDate, QThread, Signal
from PySide6.QtGui import QFont, QPalette, QColor

import matplotlib.pyplot as plt
from matplotlib.backends.backend_qt5agg import FigureCanvasQTAgg as FigureCanvas
from matplotlib.figure import Figure
import plotly.graph_objects as go
from plotly.subplots import make_subplots


class AnalysisWorker(QThread):
    """Worker thread for running analysis in background"""
    progress = Signal(int)
    result_ready = Signal(dict)
    error_occurred = Signal(str)

    def __init__(self, analysis_type: str, params: Dict):
        super().__init__()
        self.analysis_type = analysis_type
        self.params = params
        self.api_base_url = "http://localhost:8080/api/analytics"

    def run(self):
        try:
            self.progress.emit(10)

            # Map analysis type to API endpoint
            endpoints = {
                "peak_dining": "/peak-dining",
                "customer_segmentation": "/customer-segmentation",
                "revenue_analysis": "/revenue-analysis",
                "menu_analysis": "/menu-analysis",
                "seasonal_behavior": "/seasonal-behavior",
                "anomaly_detection": "/anomaly-detection",
                "branch_performance": "/branch-performance"
            }

            endpoint = endpoints.get(self.analysis_type)
            if not endpoint:
                self.error_occurred.emit(f"Unknown analysis type: {self.analysis_type}")
                return

            url = f"{self.api_base_url}{endpoint}"

            self.progress.emit(30)

            # Prepare request payload
            payload = {
                "startDate": self.params.get("start_date"),
                "endDate": self.params.get("end_date"),
                "outletId": self.params.get("outlet_id"),
                "analysisType": self.analysis_type
            }

            self.progress.emit(50)

            # Send request to backend
            response = requests.post(url, json=payload, timeout=30)
            self.progress.emit(70)

            if response.status_code == 200:
                result = response.json()
                self.progress.emit(90)
                self.result_ready.emit(result)
            else:
                self.error_occurred.emit(f"API Error: {response.status_code} - {response.text}")

            self.progress.emit(100)

        except requests.exceptions.RequestException as e:
            self.error_occurred.emit(f"Connection error: {str(e)}")
        except Exception as e:
            self.error_occurred.emit(f"Unexpected error: {str(e)}")


class AnalyticsTab(QWidget):
    """Base class for all analytics tabs"""

    def __init__(self, tab_name: str):
        super().__init__()
        self.tab_name = tab_name
        self.results = None
        self.init_ui()

    def init_ui(self):
        self.layout = QVBoxLayout()

        # Control panel
        self.control_panel = self.create_control_panel()
        self.layout.addWidget(self.control_panel)

        # Results area
        self.results_splitter = QSplitter(Qt.Vertical)

        # Chart area
        self.chart_widget = QWidget()
        self.chart_layout = QVBoxLayout(self.chart_widget)
        self.results_splitter.addWidget(self.chart_widget)

        # Data table area
        self.table_widget = QTableWidget()
        self.results_splitter.addWidget(self.table_widget)

        self.layout.addWidget(self.results_splitter)

        self.setLayout(self.layout)

    def create_control_panel(self) -> QGroupBox:
        """Create control panel with date pickers, filters, etc."""
        group = QGroupBox("Analysis Controls")
        layout = QVBoxLayout()

        # Date range
        date_layout = QHBoxLayout()
        date_layout.addWidget(QLabel("Start Date:"))
        self.start_date = QDateEdit()
        self.start_date.setDate(QDate.currentDate().addMonths(-1))
        self.start_date.setCalendarPopup(True)
        date_layout.addWidget(self.start_date)

        date_layout.addWidget(QLabel("End Date:"))
        self.end_date = QDateEdit()
        self.end_date.setDate(QDate.currentDate())
        self.end_date.setCalendarPopup(True)
        date_layout.addWidget(self.end_date)
        layout.addLayout(date_layout)

        # Outlet selection
        outlet_layout = QHBoxLayout()
        outlet_layout.addWidget(QLabel("Outlet:"))
        self.outlet_combo = QComboBox()
        self.outlet_combo.addItem("All Outlets", "")
        # TODO: Load outlets from API
        outlet_layout.addWidget(self.outlet_combo)
        layout.addLayout(outlet_layout)

        # Run button
        self.run_button = QPushButton("Run Analysis")
        self.run_button.clicked.connect(self.run_analysis)
        layout.addWidget(self.run_button)

        # Progress bar
        self.progress_bar = QProgressBar()
        self.progress_bar.setVisible(False)
        layout.addWidget(self.progress_bar)

        group.setLayout(layout)
        return group

    def run_analysis(self):
        """Run analysis - to be implemented by subclasses"""
        pass

    def display_results(self, results: Dict):
        """Display analysis results"""
        self.results = results
        self.update_charts()
        self.update_table()

    def update_charts(self):
        """Update charts with results - to be implemented by subclasses"""
        pass

    def update_table(self):
        """Update data table with results"""
        if not self.results:
            return

        data = self.results.get("data", {})
        if not data:
            return

        # Convert dict to list of rows for table display
        rows = []
        for key, value in data.items():
            if isinstance(value, dict):
                for subkey, subvalue in value.items():
                    rows.append([f"{key}.{subkey}", str(subvalue)])
            else:
                rows.append([key, str(value)])

        self.table_widget.setRowCount(len(rows))
        self.table_widget.setColumnCount(2)
        self.table_widget.setHorizontalHeaderLabels(["Metric", "Value"])

        for i, row in enumerate(rows):
            for j, cell in enumerate(row):
                self.table_widget.setItem(i, j, QTableWidgetItem(cell))


class PeakDiningTab(AnalyticsTab):
    """Peak Dining Analysis Tab"""

    def __init__(self):
        super().__init__("Peak Dining Analysis")
        self.setup_charts()

    def setup_charts(self):
        # Create matplotlib figure
        self.figure = Figure(figsize=(10, 8))
        self.canvas = FigureCanvas(self.figure)
        self.chart_layout.addWidget(self.canvas)

    def run_analysis(self):
        params = {
            "start_date": self.start_date.date().toString("yyyy-MM-dd"),
            "end_date": self.end_date.date().toString("yyyy-MM-dd"),
            "outlet_id": self.outlet_combo.currentData()
        }

        self.worker = AnalysisWorker("peak_dining", params)
        self.worker.progress.connect(self.progress_bar.setValue)
        self.worker.result_ready.connect(self.display_results)
        self.worker.error_occurred.connect(self.show_error)

        self.progress_bar.setVisible(True)
        self.run_button.setEnabled(False)
        self.worker.start()

    def update_charts(self):
        if not self.results:
            return

        data = self.results.get("data", {})

        self.figure.clear()

        # Create subplots
        ax1 = self.figure.add_subplot(211)
        ax2 = self.figure.add_subplot(212)

        # Plot orders by hour
        orders_by_hour = data.get("ordersByHour", {})
        if orders_by_hour:
            hours = list(orders_by_hour.keys())
            counts = list(orders_by_hour.values())
            ax1.bar(hours, counts)
            ax1.set_xlabel("Hour of Day")
            ax1.set_ylabel("Number of Orders")
            ax1.set_title("Orders by Hour")
            ax1.grid(True, alpha=0.3)

        # Plot orders by day
        orders_by_day = data.get("ordersByDay", {})
        if orders_by_day:
            days = list(orders_by_day.keys())
            counts = list(orders_by_day.values())
            ax2.bar(days, counts)
            ax2.set_xlabel("Day of Week")
            ax2.set_ylabel("Number of Orders")
            ax2.set_title("Orders by Day")
            ax2.grid(True, alpha=0.3)
            ax2.tick_params(axis='x', rotation=45)

        self.figure.tight_layout()
        self.canvas.draw()


class CustomerSegmentationTab(AnalyticsTab):
    """Customer Segmentation Analysis Tab"""

    def __init__(self):
        super().__init__("Customer Segmentation")
        self.setup_charts()

    def setup_charts(self):
        # Create plotly widget (using web view)
        # Note: For production, use QWebEngineView to display plotly charts
        self.chart_label = QLabel("Segmentation charts will appear here")
        self.chart_label.setAlignment(Qt.AlignCenter)
        self.chart_layout.addWidget(self.chart_label)

    def run_analysis(self):
        params = {
            "start_date": self.start_date.date().toString("yyyy-MM-dd"),
            "end_date": self.end_date.date().toString("yyyy-MM-dd"),
            "outlet_id": self.outlet_combo.currentData()
        }

        self.worker = AnalysisWorker("customer_segmentation", params)
        self.worker.progress.connect(self.progress_bar.setValue)
        self.worker.result_ready.connect(self.display_results)
        self.worker.error_occurred.connect(self.show_error)

        self.progress_bar.setVisible(True)
        self.run_button.setEnabled(False)
        self.worker.start()


class MainWindow(QMainWindow):
    """Main application window"""

    def __init__(self):
        super().__init__()
        self.setWindowTitle("RMS Analytics System")
        self.setGeometry(100, 100, 1400, 900)
        self.init_ui()

    def init_ui(self):
        # Create central widget and layout
        central_widget = QWidget()
        self.setCentralWidget(central_widget)
        layout = QVBoxLayout(central_widget)

        # Create tab widget
        self.tab_widget = QTabWidget()

        # Add analytics tabs
        self.peak_dining_tab = PeakDiningTab()
        self.customer_seg_tab = CustomerSegmentationTab()

        # TODO: Add more tabs
        # self.seasonal_tab = SeasonalBehaviorTab()
        # self.menu_tab = MenuAnalysisTab()
        # self.revenue_tab = RevenueAnalysisTab()
        # self.anomaly_tab = AnomalyDetectionTab()
        # self.branch_tab = BranchPerformanceTab()

        self.tab_widget.addTab(self.peak_dining_tab, "Peak Dining")
        self.tab_widget.addTab(self.customer_seg_tab, "Customer Segmentation")

        layout.addWidget(self.tab_widget)

        # Status bar
        self.status_bar = self.statusBar()
        self.status_bar.showMessage("Ready")

    def show_error(self, error_message: str):
        QMessageBox.critical(self, "Error", error_message)
        self.status_bar.showMessage(f"Error: {error_message}")


def main():
    app = QApplication(sys.argv)

    # Set application style
    app.setStyle("Fusion")

    # Create and show main window
    window = MainWindow()
    window.show()

    sys.exit(app.exec())


if __name__ == "__main__":
    main()