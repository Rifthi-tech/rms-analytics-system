import React, { useState, useEffect } from 'react';

function DataUpload({ onDataLoaded }) {
  const [filePath, setFilePath] = useState('r:\\HND-23 CSD\\4th SEMESTER\\APDP\\rms-analytics-system\\restaurant_dataset.csv');
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    // Auto-load the data when component mounts
    const autoLoad = async () => {
      const defaultPath = 'r:\\HND-23 CSD\\4th SEMESTER\\APDP\\rms-analytics-system\\restaurant_dataset.csv';
      setLoading(true);
      try {
        await onDataLoaded(defaultPath);
        setSuccessMessage('✓ Dataset automatically loaded successfully!');
        setTimeout(() => setSuccessMessage(''), 4000);
      } catch (error) {
        console.error('Auto-load failed:', error);
        setSuccessMessage('');
      } finally {
        setLoading(false);
      }
    };
    
    autoLoad();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!filePath.trim()) {
      alert('Please enter a file path');
      return;
    }

    setLoading(true);
    setSuccessMessage('');
    try {
      await onDataLoaded(filePath);
      setSuccessMessage('✓ Dataset loaded successfully!');
      setTimeout(() => setSuccessMessage(''), 4000);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-lg-8">
          <div className="card mt-5" style={{ borderRadius: '12px' }}>
            <div className="card-header" style={{ background: 'linear-gradient(135deg, #003366 0%, #004d99 100%)', borderRadius: '12px 12px 0 0' }}>
              📤 Upload Restaurant Dataset
            </div>
            <div className="card-body" style={{ padding: '2rem' }}>
              {successMessage && (
                <div className="alert alert-success border-0" style={{ borderRadius: '8px', background: '#d4edda' }}>
                  {successMessage}
                </div>
              )}

              <form onSubmit={handleSubmit}>
                <div className="mb-4">
                  <label className="form-label" style={{ fontWeight: '600', fontSize: '1.05rem' }}>
                    CSV File Path
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Enter CSV file path"
                    value={filePath}
                    onChange={(e) => setFilePath(e.target.value)}
                    disabled={loading}
                    style={{ 
                      borderRadius: '8px', 
                      borderColor: '#e0e0e0',
                      padding: '0.75rem',
                      fontSize: '0.95rem'
                    }}
                  />
                  <small className="form-text text-muted d-block mt-2">
                    Current path: {filePath}
                  </small>
                </div>

                <button 
                  type="submit" 
                  className="btn btn-primary w-100"
                  disabled={loading}
                  style={{
                    padding: '0.875rem',
                    fontSize: '1rem',
                    fontWeight: '600',
                    borderRadius: '8px',
                    background: loading ? '#666' : 'linear-gradient(135deg, #003366 0%, #004d99 100%)',
                    border: 'none',
                    color: 'white'
                  }}
                >
                  {loading ? (
                    <>
                      <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                      Processing...
                    </>
                  ) : 'Load Data'}
                </button>
              </form>

              <div className="mt-5" style={{ borderTop: '2px solid #f0f4f8', paddingTop: '2rem' }}>
                <h5 style={{ fontWeight: '700', color: '#003366', marginBottom: '1rem' }}>📋 Expected Format</h5>
                <p style={{ color: '#666', marginBottom: '1rem' }}>Your CSV file should contain these columns:</p>
                <div style={{ background: '#f8f9fa', padding: '1.5rem', borderRadius: '8px', borderLeft: '4px solid #003366' }}>
                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', fontSize: '0.9rem', color: '#555' }}>
                    <div>
                      <strong style={{ color: '#003366' }}>Order Data:</strong>
                      <ul style={{ margin: '0.5rem 0', paddingLeft: '1.5rem' }}>
                        <li>order_id</li>
                        <li>customer_id</li>
                        <li>outlet_id</li>
                        <li>status</li>
                      </ul>
                    </div>
                    <div>
                      <strong style={{ color: '#003366' }}>Timing Data:</strong>
                      <ul style={{ margin: '0.5rem 0', paddingLeft: '1.5rem' }}>
                        <li>order_placed</li>
                        <li>order_confirmed</li>
                        <li>prep_started</li>
                        <li>prep_finished</li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>

              <div style={{ marginTop: '1.5rem', padding: '1rem', background: '#e3f2fd', borderRadius: '8px', fontSize: '0.9rem', color: '#003366' }}>
                <strong>💡 Tip:</strong> Place your <code>restaurant_dataset.csv</code> file in the rms-analytics-system folder for automatic loading.
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default DataUpload;
