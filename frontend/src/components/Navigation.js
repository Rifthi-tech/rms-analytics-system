import React from 'react';

function Navigation({ dataLoaded, onNavigate }) {
  return (
    <nav className="navbar">
      <div className="container-fluid">
        <div className="navbar-brand">🍽️ RMS Analytics System</div>
        <div className="navbar-nav">
          <a className="nav-link" onClick={() => onNavigate('dashboard')} style={{cursor: 'pointer'}}>
            Dashboard
          </a>
          {dataLoaded && (
            <>
              <a className="nav-link" onClick={() => onNavigate('peak-dining')} style={{cursor: 'pointer'}}>
                Peak Dining
              </a>
              <a className="nav-link" onClick={() => onNavigate('demographics')} style={{cursor: 'pointer'}}>
                Demographics
              </a>
              <a className="nav-link" onClick={() => onNavigate('revenue')} style={{cursor: 'pointer'}}>
                Revenue
              </a>
              <a className="nav-link" onClick={() => onNavigate('branch')} style={{cursor: 'pointer'}}>
                Performance
              </a>
              <a className="nav-link" onClick={() => onNavigate('anomalies')} style={{cursor: 'pointer'}}>
                Anomalies
              </a>
            </>
          )}
          <a className="nav-link" onClick={() => onNavigate('upload')} style={{cursor: 'pointer'}}>
            Upload Data
          </a>
        </div>
      </div>
    </nav>
  );
}

export default Navigation;
