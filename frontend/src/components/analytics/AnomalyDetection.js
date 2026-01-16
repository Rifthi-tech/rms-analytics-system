import React from 'react';

function AnomalyDetection({ data }) {
  if (!data) return <div>Loading...</div>;

  const renderAnomalyReport = (report) => {
    if (!report || !report.anomalies || report.anomalies.length === 0) {
      return <p className="text-success">✓ No anomalies detected</p>;
    }

    return (
      <table className="table">
        <thead>
          <tr>
            <th>Dimension</th>
            <th>Actual Value</th>
            <th>Expected Value</th>
            <th>Deviation (%)</th>
          </tr>
        </thead>
        <tbody>
          {report.anomalies.map((anomaly, idx) => (
            <tr key={idx} style={{backgroundColor: 'rgba(220, 53, 69, 0.1)'}}>
              <td>{anomaly.dimension}</td>
              <td>{anomaly.actualValue}</td>
              <td>{anomaly.expectedValue}</td>
              <td>
                <span className="badge badge-danger">
                  {anomaly.deviationPercent.toFixed(1)}%
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  return (
    <div className="container-fluid">
      <h1 className="mb-4">⚠️ Service Anomaly Detection</h1>

      {data.orderCountAnomalies && (
        <div className="card mb-4">
          <div className="card-header">📊 Order Count Anomalies</div>
          <div className="card-body">
            {data.orderCountAnomalies.anomalyCount > 0 ? (
              <>
                <p className="alert alert-warning">
                  Found <strong>{data.orderCountAnomalies.anomalyCount}</strong> anomalies
                </p>
                {renderAnomalyReport(data.orderCountAnomalies)}
              </>
            ) : (
              <p className="text-success">✓ No order count anomalies detected</p>
            )}
          </div>
        </div>
      )}

      {data.cancellationAnomalies && (
        <div className="card mb-4">
          <div className="card-header">❌ Cancellation Anomalies</div>
          <div className="card-body">
            {data.cancellationAnomalies.anomalyCount > 0 ? (
              <>
                <p className="alert alert-warning">
                  Found <strong>{data.cancellationAnomalies.anomalyCount}</strong> anomalies
                </p>
                {renderAnomalyReport(data.cancellationAnomalies)}
              </>
            ) : (
              <p className="text-success">✓ No cancellation anomalies detected</p>
            )}
          </div>
        </div>
      )}

      {data.revenueAnomalies && (
        <div className="card">
          <div className="card-header">💰 Revenue Anomalies</div>
          <div className="card-body">
            {data.revenueAnomalies.anomalyCount > 0 ? (
              <>
                <p className="alert alert-warning">
                  Found <strong>{data.revenueAnomalies.anomalyCount}</strong> anomalies
                </p>
                {renderAnomalyReport(data.revenueAnomalies)}
              </>
            ) : (
              <p className="text-success">✓ No revenue anomalies detected</p>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default AnomalyDetection;
