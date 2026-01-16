import React from 'react';

function CustomerDemographics({ data }) {
  if (!data) return <div>Loading...</div>;

  return (
    <div className="container-fluid">
      <h1 className="mb-4">👥 Customer Demographics & Segmentation</h1>

      <div className="row">
        <div className="col-md-6">
          <div className="card mb-4">
            <div className="card-header">👫 Gender Distribution</div>
            <div className="card-body">
              <table className="table">
                <thead>
                  <tr>
                    <th>Gender</th>
                    <th>Count</th>
                    <th>Percentage</th>
                  </tr>
                </thead>
                <tbody>
                  {data.genderDistribution && Object.entries(data.genderDistribution).map(([gender, count], idx) => {
                    const total = Object.values(data.genderDistribution).reduce((a, b) => a + b, 0);
                    const percentage = ((count / total) * 100).toFixed(1);
                    return (
                      <tr key={idx}>
                        <td>{gender}</td>
                        <td>{count}</td>
                        <td>{percentage}%</td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div className="col-md-6">
          <div className="card mb-4">
            <div className="card-header">🎂 Age Group Distribution</div>
            <div className="card-body">
              <table className="table">
                <thead>
                  <tr>
                    <th>Age Group</th>
                    <th>Count</th>
                    <th>Percentage</th>
                  </tr>
                </thead>
                <tbody>
                  {data.ageGroupDistribution && Object.entries(data.ageGroupDistribution).map(([ageGroup, count], idx) => {
                    const total = Object.values(data.ageGroupDistribution).reduce((a, b) => a + b, 0);
                    const percentage = ((count / total) * 100).toFixed(1);
                    return (
                      <tr key={idx}>
                        <td>{ageGroup}</td>
                        <td>{count}</td>
                        <td>{percentage}%</td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      {data.loyaltySegmentation && (
        <div className="card">
          <div className="card-header">💳 Loyalty Segmentation</div>
          <div className="card-body">
            <table className="table">
              <thead>
                <tr>
                  <th>Loyalty Group</th>
                  <th>Customer Count</th>
                  <th>Avg Spending (₹)</th>
                </tr>
              </thead>
              <tbody>
                {data.loyaltySegmentation.segmentCounts && 
                  Object.entries(data.loyaltySegmentation.segmentCounts).map(([segment, count], idx) => (
                    <tr key={idx}>
                      <td>
                        <span className="badge badge-primary">{segment}</span>
                      </td>
                      <td>{count}</td>
                      <td>₹{(data.loyaltySegmentation.avgSpendingBySegment?.[segment] || 0).toFixed(0)}</td>
                    </tr>
                  ))
                }
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}

export default CustomerDemographics;
