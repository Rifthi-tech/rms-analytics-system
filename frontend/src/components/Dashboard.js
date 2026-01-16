import React from 'react';

function Dashboard({ dataLoaded, reportData, onUploadClick }) {
  if (!dataLoaded) {
    return (
      <div className="container">
        <div className="row justify-content-center mt-5">
          <div className="col-md-8">
            <div className="card">
              <div className="card-body text-center py-5">
                <h1 className="mb-4">Welcome to RMS Analytics System</h1>
                <p className="lead mb-4">
                  Restaurant Management System Data Analytics Tool for Uber Eats Corporation
                </p>
                <p className="text-muted mb-4">
                  Upload your restaurant dataset to begin analyzing peak dining hours, customer behavior,
                  revenue patterns, menu popularity, and more.
                </p>
                <button className="btn btn-primary btn-lg" onClick={onUploadClick}>
                  📤 Upload Dataset Now
                </button>
              </div>
            </div>

            <div className="row mt-5">
              <div className="col-md-6 mb-3">
                <div className="card">
                  <div className="card-body">
                    <h5 className="card-title">📊 Peak Dining Analysis</h5>
                    <p className="card-text">Identify peak hours, days, and months for optimal staffing and resource allocation.</p>
                  </div>
                </div>
              </div>
              <div className="col-md-6 mb-3">
                <div className="card">
                  <div className="card-body">
                    <h5 className="card-title">👥 Customer Segmentation</h5>
                    <p className="card-text">Analyze demographics and loyalty segments to understand your customer base.</p>
                  </div>
                </div>
              </div>
              <div className="col-md-6 mb-3">
                <div className="card">
                  <div className="card-body">
                    <h5 className="card-title">💰 Revenue Analytics</h5>
                    <p className="card-text">Track daily/weekly sales, payment methods, and revenue trends across outlets.</p>
                  </div>
                </div>
              </div>
              <div className="col-md-6 mb-3">
                <div className="card">
                  <div className="card-body">
                    <h5 className="card-title">🏪 Branch Performance</h5>
                    <p className="card-text">Compare branch performance, identify underperformers, and optimize operations.</p>
                  </div>
                </div>
              </div>
              <div className="col-md-6 mb-3">
                <div className="card">
                  <div className="card-body">
                    <h5 className="card-title">⚠️ Anomaly Detection</h5>
                    <p className="card-text">Automatically detect operational issues and unusual patterns in your data.</p>
                  </div>
                </div>
              </div>
              <div className="col-md-6 mb-3">
                <div className="card">
                  <div className="card-body">
                    <h5 className="card-title">🍽️ Menu Analytics</h5>
                    <p className="card-text">Analyze popular items, combinations, and customer preferences by category.</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="container-fluid">
      <h1 className="mb-4">Analytics Dashboard</h1>

      {reportData && (
        <>
          <div className="stats-grid">
            <div className="stat-box">
              <div className="label">Total Orders</div>
              <div className="value">{reportData.totalOrderCount?.toLocaleString() || 0}</div>
            </div>
            <div className="stat-box">
              <div className="label">Total Revenue</div>
              <div className="value">₹{(reportData.totalRevenue?.toFixed(0) || 0).toLocaleString()}</div>
            </div>
            <div className="stat-box secondary">
              <div className="label">Peak Hour</div>
              <div className="value">{reportData.peakHourAnalysis?.peakHour}:00</div>
            </div>
            <div className="stat-box secondary">
              <div className="label">Orders in Peak Hour</div>
              <div className="value">{reportData.peakHourAnalysis?.orderCount || 0}</div>
            </div>
            <div className="stat-box">
              <div className="label">Order Count Anomalies</div>
              <div className="value">{reportData.orderCountAnomalies?.anomalyCount || 0}</div>
            </div>
            <div className="stat-box">
              <div className="label">Cancellation Anomalies</div>
              <div className="value">{reportData.cancellationAnomalies?.anomalyCount || 0}</div>
            </div>
          </div>

          {reportData.branchRanking && reportData.branchRanking.length > 0 && (
            <div className="card mb-4">
              <div className="card-header">🏆 Top Performing Branches</div>
              <div className="card-body">
                <table className="table">
                  <thead>
                    <tr>
                      <th>Branch ID</th>
                      <th>Total Orders</th>
                      <th>Revenue</th>
                      <th>Avg Order Value</th>
                      <th>Completion Rate</th>
                    </tr>
                  </thead>
                  <tbody>
                    {reportData.branchRanking.slice(0, 5).map((branch, idx) => (
                      <tr key={idx}>
                        <td>{branch.branchId}</td>
                        <td>{branch.totalOrders}</td>
                        <td>₹{branch.totalRevenue.toFixed(0)}</td>
                        <td>₹{branch.avgOrderValue.toFixed(0)}</td>
                        <td>{branch.completionRate.toFixed(1)}%</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {reportData.topMenuItems && reportData.topMenuItems.length > 0 && (
            <div className="card">
              <div className="card-header">🍽️ Top 10 Menu Items</div>
              <div className="card-body">
                <table className="table">
                  <thead>
                    <tr>
                      <th>Item Name</th>
                      <th>Order Count</th>
                    </tr>
                  </thead>
                  <tbody>
                    {reportData.topMenuItems.map((item, idx) => (
                      <tr key={idx}>
                        <td>{item.itemName}</td>
                        <td>
                          <span className="badge badge-primary">{item.orderCount}</span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default Dashboard;
