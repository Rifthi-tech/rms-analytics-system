import React from 'react';
import { RevenueChart, OrdersChart, CategoryChart, BranchPerformanceChart, CustomerAgeChart, SalesGrowthChart } from './Charts';

function DashboardWithCharts({ dataLoaded, reportData }) {
  return (
    <div className="dashboard-container">
      <div className="page-header">
        <h1 style={{ fontSize: '2.5rem', fontWeight: '800', color: '#003366', marginBottom: '0.5rem' }}>
          📊 Restaurant Analytics Dashboard
        </h1>
        <p style={{ color: '#666', fontSize: '1.1rem' }}>
          {dataLoaded ? 'Real-time analytics and insights' : 'Upload data to see analytics'}
        </p>
      </div>

      {!dataLoaded ? (
        <div style={{
          background: 'linear-gradient(135deg, #003366 0%, #004d99 100%)',
          color: 'white',
          padding: '3rem',
          borderRadius: '12px',
          textAlign: 'center',
          marginBottom: '2rem'
        }}>
          <h2 style={{ marginBottom: '1rem', fontSize: '1.8rem' }}>📤 No Data Loaded</h2>
          <p style={{ marginBottom: '1.5rem', fontSize: '1.05rem' }}>
            Please upload the restaurant dataset to view analytics and graphs
          </p>
          <button 
            className="btn btn-light"
            onClick={() => window.location.href = 'http://localhost:3001'}
            style={{
              padding: '0.75rem 2rem',
              fontSize: '1rem',
              fontWeight: '600',
              background: 'white',
              color: '#003366',
              border: 'none',
              borderRadius: '8px',
              cursor: 'pointer'
            }}
          >
            Upload Data
          </button>
        </div>
      ) : (
        <>
          {/* Key Metrics */}
          <div className="metrics-grid" style={{ 
            display: 'grid', 
            gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
            gap: '1.5rem',
            marginBottom: '3rem'
          }}>
            <div className="metric-card">
              <div className="metric-label">Total Revenue</div>
              <div className="metric-value">Rs. 175K</div>
              <div style={{ fontSize: '0.85rem', color: '#27ae60', marginTop: '0.5rem' }}>↑ 12% increase</div>
            </div>
            <div className="metric-card">
              <div className="metric-label">Total Orders</div>
              <div className="metric-value">8,950</div>
              <div style={{ fontSize: '0.85rem', color: '#27ae60', marginTop: '0.5rem' }}>↑ 8% increase</div>
            </div>
            <div className="metric-card">
              <div className="metric-label">Avg. Order Value</div>
              <div className="metric-value">Rs. 1,950</div>
              <div style={{ fontSize: '0.85rem', color: '#e74c3c', marginTop: '0.5rem' }}>↓ 2% decrease</div>
            </div>
            <div className="metric-card">
              <div className="metric-label">Active Customers</div>
              <div className="metric-value">2,150</div>
              <div style={{ fontSize: '0.85rem', color: '#27ae60', marginTop: '0.5rem' }}>↑ 15% increase</div>
            </div>
          </div>

          {/* Charts Section */}
          <div style={{ marginBottom: '3rem' }}>
            <h2 style={{ fontSize: '1.8rem', fontWeight: '700', color: '#003366', marginBottom: '2rem' }}>
              📈 Analytics Overview
            </h2>

            {/* Revenue and Orders Row */}
            <div style={{ 
              display: 'grid', 
              gridTemplateColumns: 'repeat(auto-fit, minmax(500px, 1fr))',
              gap: '2rem',
              marginBottom: '2rem'
            }}>
              <div style={{
                background: 'white',
                padding: '2rem',
                borderRadius: '12px',
                boxShadow: '0 4px 6px rgba(0, 51, 102, 0.1)',
                transition: 'all 0.3s ease'
              }}>
                <RevenueChart data={reportData} />
              </div>
              <div style={{
                background: 'white',
                padding: '2rem',
                borderRadius: '12px',
                boxShadow: '0 4px 6px rgba(0, 51, 102, 0.1)',
                transition: 'all 0.3s ease'
              }}>
                <OrdersChart data={reportData} />
              </div>
            </div>

            {/* Category and Branch Row */}
            <div style={{ 
              display: 'grid', 
              gridTemplateColumns: 'repeat(auto-fit, minmax(500px, 1fr))',
              gap: '2rem',
              marginBottom: '2rem'
            }}>
              <div style={{
                background: 'white',
                padding: '2rem',
                borderRadius: '12px',
                boxShadow: '0 4px 6px rgba(0, 51, 102, 0.1)',
                transition: 'all 0.3s ease'
              }}>
                <CategoryChart data={reportData} />
              </div>
              <div style={{
                background: 'white',
                padding: '2rem',
                borderRadius: '12px',
                boxShadow: '0 4px 6px rgba(0, 51, 102, 0.1)',
                transition: 'all 0.3s ease'
              }}>
                <BranchPerformanceChart data={reportData} />
              </div>
            </div>

            {/* Customer Age and Sales Growth Row */}
            <div style={{ 
              display: 'grid', 
              gridTemplateColumns: 'repeat(auto-fit, minmax(500px, 1fr))',
              gap: '2rem'
            }}>
              <div style={{
                background: 'white',
                padding: '2rem',
                borderRadius: '12px',
                boxShadow: '0 4px 6px rgba(0, 51, 102, 0.1)',
                transition: 'all 0.3s ease'
              }}>
                <CustomerAgeChart data={reportData} />
              </div>
              <div style={{
                background: 'white',
                padding: '2rem',
                borderRadius: '12px',
                boxShadow: '0 4px 6px rgba(0, 51, 102, 0.1)',
                transition: 'all 0.3s ease'
              }}>
                <SalesGrowthChart data={reportData} />
              </div>
            </div>
          </div>

          {/* Summary Stats */}
          <div style={{
            background: 'linear-gradient(135deg, #003366 0%, #004d99 100%)',
            color: 'white',
            padding: '2rem',
            borderRadius: '12px',
            marginTop: '2rem'
          }}>
            <h3 style={{ marginBottom: '1rem', fontSize: '1.5rem' }}>🎯 Key Insights</h3>
            <ul style={{ fontSize: '1rem', lineHeight: '1.8', marginLeft: '1.5rem' }}>
              <li>Peak dining hours are 6 PM - 9 PM with highest order volume</li>
              <li>Pizza is the most popular category (35% of total orders)</li>
              <li>Downtown branch shows highest revenue (Rs. 45,000)</li>
              <li>Age group 26-35 represents largest customer base (620 customers)</li>
              <li>Monthly sales growing consistently with 12% YoY increase</li>
            </ul>
          </div>
        </>
      )}
    </div>
  );
}

export default DashboardWithCharts;
