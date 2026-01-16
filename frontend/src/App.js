import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import {
  Dashboard,
  PeakDiningAnalysis,
  RevenueAnalysis,
  CustomerDemographics,
  BranchPerformance,
  AnomalyDetection
} from './pages/AnalyticsPages';

const API_BASE_URL = 'http://localhost:8080/api';

function App() {
  const [currentPage, setCurrentPage] = useState('dashboard');
  const [dataLoaded, setDataLoaded] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // Auto-load the dataset
      const filePath = 'r:\\HND-23 CSD\\4th SEMESTER\\APDP\\rms-analytics-system\\restaurant_dataset.csv';
      
      const response = await axios.post(`${API_BASE_URL}/analytics/load-data`, null, {
        params: { filePath }
      });

      if (response.data.success) {
        setDataLoaded(true);
        console.log(`✅ Data loaded: ${response.data.ordersLoaded} orders`);
      } else {
        throw new Error('Failed to load data');
      }
    } catch (err) {
      console.error('Error loading data:', err.message);
      setError('Could not load restaurant data. Using sample data for demo.');
      setDataLoaded(true); // Show pages with sample data
    } finally {
      setLoading(false);
    }
  };

  const renderPage = () => {
    switch (currentPage) {
      case 'dashboard':
        return <Dashboard dataLoaded={dataLoaded} />;
      case 'peak-dining':
        return <PeakDiningAnalysis />;
      case 'revenue':
        return <RevenueAnalysis />;
      case 'demographics':
        return <CustomerDemographics />;
      case 'branch':
        return <BranchPerformance />;
      case 'anomalies':
        return <AnomalyDetection />;
      default:
        return <Dashboard dataLoaded={dataLoaded} />;
    }
  };

  return (
    <div className="flex flex-col min-h-screen bg-gray-50">
      <Navbar currentPage={currentPage} onNavigate={setCurrentPage} />

      {/* Loading State */}
      {loading && (
        <div className="flex-1 flex items-center justify-center">
          <div className="text-center">
            <div className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-gradient-to-r from-purple-500 to-pink-500 mb-4">
              <svg className="animate-spin h-8 w-8 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
            </div>
            <h2 className="text-2xl font-bold text-gray-900 mb-2">Loading Analytics</h2>
            <p className="text-gray-600">Initializing data...</p>
          </div>
        </div>
      )}

      {/* Error Banner */}
      {error && (
        <div className="bg-blue-50 border-l-4 border-blue-500 p-4 mx-4 mt-4 rounded">
          <p className="text-blue-800">ℹ️ {error}</p>
        </div>
      )}

      {/* Main Content */}
      {!loading && (
        <main className="flex-1">
          {renderPage()}
        </main>
      )}

      {/* Footer */}
      <Footer />
    </div>
  );
}

export default App;
