import React from 'react';

function BranchPerformance({ data }) {
  if (!data) return <div>Loading...</div>;

  return (
    <div className="container-fluid">
      <h1 className="mb-4">🏪 Branch Performance Analysis</h1>

      <div className="card mb-4">
        <div className="card-header">🏆 Branch Performance Ranking</div>
        <div className="card-body">
          <table className="table">
            <thead>
              <tr>
                <th>Rank</th>
                <th>Branch ID</th>
                <th>Total Orders</th>
                <th>Total Revenue (₹)</th>
                <th>Avg Order Value (₹)</th>
                <th>Completion Rate (%)</th>
              </tr>
            </thead>
            <tbody>
              {data.branchRanking && data.branchRanking.map((branch, idx) => (
                <tr key={idx}>
                  <td>
                    <span className="badge badge-primary">{idx + 1}</span>
                  </td>
                  <td>{branch.branchId}</td>
                  <td>{branch.totalOrders}</td>
                  <td>₹{branch.totalRevenue.toFixed(0).toLocaleString()}</td>
                  <td>₹{branch.avgOrderValue.toFixed(0)}</td>
                  <td>
                    <div style={{
                      width: '100%',
                      backgroundColor: '#f0f4f8',
                      borderRadius: '4px',
                      overflow: 'hidden'
                    }}>
                      <div style={{
                        width: `${Math.min(branch.completionRate, 100)}%`,
                        backgroundColor: '#003366',
                        color: 'white',
                        textAlign: 'center',
                        padding: '4px'
                      }}>
                        {branch.completionRate.toFixed(1)}%
                      </div>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {data.underperformingBranches && data.underperformingBranches.length > 0 && (
        <div className="card">
          <div className="card-header alert-danger">⚠️ Underperforming Branches</div>
          <div className="card-body">
            <table className="table">
              <thead>
                <tr>
                  <th>Branch ID</th>
                  <th>Total Orders</th>
                  <th>Revenue (₹)</th>
                  <th>Completion Rate (%)</th>
                </tr>
              </thead>
              <tbody>
                {data.underperformingBranches.map((branch, idx) => (
                  <tr key={idx} style={{backgroundColor: 'rgba(220, 53, 69, 0.1)'}}>
                    <td>{branch.branchId}</td>
                    <td>{branch.totalOrders}</td>
                    <td>₹{branch.totalRevenue.toFixed(0)}</td>
                    <td>{branch.completionRate.toFixed(1)}%</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}

export default BranchPerformance;
