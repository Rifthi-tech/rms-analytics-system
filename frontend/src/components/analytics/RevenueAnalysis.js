import React from 'react';

function RevenueAnalysis({ data }) {
  if (!data) return <div>Loading...</div>;

  return (
    <div className="container-fluid">
      <h1 className="mb-4">💰 Revenue Analysis</h1>

      <div className="stats-grid">
        <div className="stat-box">
          <div className="label">Total Revenue</div>
          <div className="value">₹{(data.totalRevenue || 0).toFixed(0).toLocaleString()}</div>
        </div>
        <div className="stat-box">
          <div className="label">Total Orders</div>
          <div className="value">{(data.totalOrderCount || 0).toLocaleString()}</div>
        </div>
        <div className="stat-box secondary">
          <div className="label">Avg Daily Revenue</div>
          <div className="value">₹{(data.dailySalesSummary?.avgDailyRevenue || 0).toFixed(0).toLocaleString()}</div>
        </div>
      </div>

      <div className="row">
        <div className="col-md-6">
          <div className="card mb-4">
            <div className="card-header">💳 Revenue by Payment Method</div>
            <div className="card-body">
              <table className="table">
                <thead>
                  <tr>
                    <th>Payment Method</th>
                    <th>Revenue (₹)</th>
                  </tr>
                </thead>
                <tbody>
                  {data.revenueByPaymentMethod && Object.entries(data.revenueByPaymentMethod).map(([method, revenue], idx) => (
                    <tr key={idx}>
                      <td>{method}</td>
                      <td>₹{revenue.toFixed(0).toLocaleString()}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div className="col-md-6">
          <div className="card mb-4">
            <div className="card-header">🏪 Orders by Outlet</div>
            <div className="card-body">
              <table className="table">
                <thead>
                  <tr>
                    <th>Outlet ID</th>
                    <th>Order Count</th>
                  </tr>
                </thead>
                <tbody>
                  {data.ordersByOutlet && Object.entries(data.ordersByOutlet).map(([outlet, count], idx) => (
                    <tr key={idx}>
                      <td>{outlet}</td>
                      <td>{count}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <div className="card">
        <div className="card-header">📊 Revenue by Outlet</div>
        <div className="card-body">
          <table className="table">
            <thead>
              <tr>
                <th>Outlet ID</th>
                <th>Revenue (₹)</th>
              </tr>
            </thead>
            <tbody>
              {data.revenueByOutlet && Object.entries(data.revenueByOutlet).map(([outlet, revenue], idx) => (
                <tr key={idx}>
                  <td>{outlet}</td>
                  <td>₹{revenue.toFixed(0).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default RevenueAnalysis;
