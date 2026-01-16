import React, { useState } from 'react';

function DataUpload({ onDataLoaded }) {
  const [filePath, setFilePath] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!filePath.trim()) {
      alert('Please enter a file path');
      return;
    }

    setLoading(true);
    try {
      await onDataLoaded(filePath);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-md-8">
          <div className="card mt-5">
            <div className="card-header">
              📤 Upload Dataset
            </div>
            <div className="card-body">
              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label className="form-label">CSV File Path</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="e.g., r:\HND-23 CSD\4th SEMESTER\APDP\restaurant_dataset.csv"
                    value={filePath}
                    onChange={(e) => setFilePath(e.target.value)}
                    disabled={loading}
                  />
                  <small className="form-text text-muted">
                    Enter the full path to your CSV file
                  </small>
                </div>

                <button 
                  type="submit" 
                  className="btn btn-primary w-100"
                  disabled={loading}
                >
                  {loading ? 'Loading...' : 'Load Data'}
                </button>
              </form>

              <div className="mt-4">
                <h5>Expected Format</h5>
                <p>Your CSV file should contain the following columns:</p>
                <ul>
                  <li>order_id, customer_id, outlet_id</li>
                  <li>order_placed, order_confirmed, prep_started, prep_finished, served_time</li>
                  <li>status, num_items, total_price_lkr, payment_method</li>
                  <li>item_id, quantity, price_lkr_x, name_x</li>
                  <li>contact_no, gender, age, join_date, loyalty_group</li>
                  <li>name_y, borough, capacity, opened, category, price_lkr_y, is_vegetarian, spice_level</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default DataUpload;
