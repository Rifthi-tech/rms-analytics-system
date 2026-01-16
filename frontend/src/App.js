import React, { useState } from 'react';
import axios from 'axios';
import './App.css';
import Navigation from './components/Navigation';
import Dashboard from './components/Dashboard';
import DashboardWithCharts from './components/DashboardWithCharts';
import DataUpload from './components/DataUpload';
import PeakDiningAnalysis from './components/analytics/PeakDiningAnalysis';
import CustomerDemographics from './components/analytics/CustomerDemographics';
import RevenueAnalysis from './components/analytics/RevenueAnalysis';
import BranchPerformance from './components/analytics/BranchPerformance';
import AnomalyDetection from './components/analytics/AnomalyDetection';
import Alert from './components/Alert';

const API_BASE_URL = 'http://localhost:8080/api';

function App() {
  const [currentPage, setCurrentPage] = useState('dashboard');
  const [dataLoaded, setDataLoaded] = useState(false);
  const [reportData, setReportData] = useState(null);
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(false);

  const showAlert = (message, type = 'info') => {
    const id = Date.now();
    setAlerts(prev => [...prev, { id, message, type }]);
    setTimeout(() => {
      setAlerts(prev => prev.filter(alert => alert.id !== id));
    }, 5000);
  };

  const handleDataUpload = async (filePath) => {
    setLoading(true);
    try {
      const response = await axios.post(`${API_BASE_URL}/analytics/load-data`, null, {
        params: { filePath }
      });
      
      if (response.data.success) {
        setDataLoaded(true);
        showAlert(`Data loaded successfully! ${response.data.ordersLoaded} orders processed.`, 'success');
        loadReport();
      }
    } catch (error) {
      showAlert(error.response?.data?.error || 'Error loading data', 'error');
    } finally {
      setLoading(false);
    }
  };

  const loadReport = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/analytics/report`);
      setReportData(response.data);
    } catch (error) {
      showAlert('Error loading report', 'error');
    }
  };

  const navigateTo = (page) => {
    setCurrentPage(page);
    window.scrollTo(0, 0);
  };

  return (
    <div className="app">
      <Navigation dataLoaded={dataLoaded} onNavigate={navigateTo} />
      
      <div className="alert-container">
        {alerts.map(alert => (
          <Alert key={alert.id} message={alert.message} type={alert.type} />
        ))}
      </div>

      <div className="main-content">
        {loading && (
          <div className="text-center py-5">
            <div className="loading-spinner mx-auto"></div>
            <p className="mt-3">Processing data...</p>
          </div>
        )}

        {!loading && (
          <>
            {currentPage === 'dashboard' && (
              <DashboardWithCharts 
                dataLoaded={dataLoaded} 
                reportData={reportData}
                onUploadClick={() => navigateTo('upload')}
              />
            )}
            {currentPage === 'upload' && (
              <DataUpload onDataLoaded={handleDataUpload} />
            )}
            {currentPage === 'peak-dining' && dataLoaded && (
              <PeakDiningAnalysis data={reportData} />
            )}
            {currentPage === 'demographics' && dataLoaded && (
              <CustomerDemographics data={reportData} />
            )}
            {currentPage === 'revenue' && dataLoaded && (
              <RevenueAnalysis data={reportData} />
            )}
            {currentPage === 'branch' && dataLoaded && (
              <BranchPerformance data={reportData} />
            )}
            {currentPage === 'anomalies' && dataLoaded && (
              <AnomalyDetection data={reportData} />
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default App;
