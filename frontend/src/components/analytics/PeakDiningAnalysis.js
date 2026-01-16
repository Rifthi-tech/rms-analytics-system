import React from 'react';

function PeakDiningAnalysis({ data }) {
  if (!data) return <div>Loading...</div>;

  const hourLabels = Array.from({length: 24}, (_, i) => `${i}:00`);
  const hourValues = hourLabels.map((_, i) => data.peakHourAnalysis?.hourlyData?.[i] || 0);

  const dayLabels = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  const dayValues = dayLabels.map((_, i) => data.dailyPeaks?.[i + 1] || 0);

  const monthLabels = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  const monthValues = monthLabels.map((_, i) => data.monthlyPeaks?.[i + 1] || 0);

  return (
    <div className="container-fluid">
      <h1 className="mb-4">📊 Peak Dining Analysis</h1>

      <div className="row">
        <div className="col-md-4">
          <div className="card mb-4">
            <div className="card-body">
              <h5>Peak Hour</h5>
              <div className="metric-value">{data.peakHourAnalysis?.peakHour}:00</div>
              <p className="metric-label">Orders: {data.peakHourAnalysis?.orderCount}</p>
            </div>
          </div>
        </div>
      </div>

      <div className="card mb-4">
        <div className="card-header">📈 Hourly Orders Distribution</div>
        <div className="card-body">
          <div className="table-responsive">
            <table className="table">
              <thead>
                <tr>
                  <th>Hour</th>
                  <th>Orders</th>
                </tr>
              </thead>
              <tbody>
                {hourLabels.map((hour, idx) => (
                  <tr key={idx}>
                    <td>{hour}</td>
                    <td>{hourValues[idx]}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div className="row">
        <div className="col-md-6">
          <div className="card mb-4">
            <div className="card-header">📅 Daily Peak Hours</div>
            <div className="card-body">
              <table className="table">
                <thead>
                  <tr>
                    <th>Day</th>
                    <th>Orders</th>
                  </tr>
                </thead>
                <tbody>
                  {dayLabels.map((day, idx) => (
                    <tr key={idx}>
                      <td>{day}</td>
                      <td>{dayValues[idx]}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div className="col-md-6">
          <div className="card mb-4">
            <div className="card-header">📆 Monthly Peak Hours</div>
            <div className="card-body">
              <table className="table">
                <thead>
                  <tr>
                    <th>Month</th>
                    <th>Orders</th>
                  </tr>
                </thead>
                <tbody>
                  {monthLabels.map((month, idx) => (
                    <tr key={idx}>
                      <td>{month}</td>
                      <td>{monthValues[idx]}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      {data.branchPeakData && (
        <div className="card">
          <div className="card-header">🏪 Branch-Level Peak Hours</div>
          <div className="card-body">
            <table className="table">
              <thead>
                <tr>
                  <th>Branch ID</th>
                  <th>Total Orders</th>
                  <th>Peak Hour</th>
                  <th>Peak Hour Orders</th>
                </tr>
              </thead>
              <tbody>
                {Object.entries(data.branchPeakData).map(([id, branch]) => (
                  <tr key={id}>
                    <td>{branch.branchId}</td>
                    <td>{branch.totalOrders}</td>
                    <td>{branch.peakHour}:00</td>
                    <td>{branch.peakOrderCount}</td>
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

export default PeakDiningAnalysis;
