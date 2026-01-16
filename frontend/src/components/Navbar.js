import React from 'react';

function Navbar({ currentPage, onNavigate }) {
  const navItems = [
    { id: 'dashboard', label: '📊 Dashboard', icon: '📊' },
    { id: 'peak-dining', label: '🕐 Peak Dining', icon: '🕐' },
    { id: 'revenue', label: '💰 Revenue', icon: '💰' },
    { id: 'demographics', label: '👥 Demographics', icon: '👥' },
    { id: 'branch', label: '🏪 Branches', icon: '🏪' },
    { id: 'anomalies', label: '⚠️ Anomalies', icon: '⚠️' }
  ];

  return (
    <nav className="gradient-primary text-white shadow-lg sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center space-x-3">
            <div className="text-3xl">🍽️</div>
            <div>
              <h1 className="text-2xl font-bold">RMS Analytics</h1>
              <p className="text-sm text-purple-100">Restaurant Management System</p>
            </div>
          </div>
          
          <div className="hidden md:flex space-x-1">
            {navItems.map(item => (
              <button
                key={item.id}
                onClick={() => onNavigate(item.id)}
                className={`px-4 py-2 rounded-lg transition-all duration-300 font-medium ${
                  currentPage === item.id
                    ? 'bg-white text-purple-600 shadow-lg'
                    : 'hover:bg-white hover:bg-opacity-20 text-white'
                }`}
              >
                {item.icon} {item.label}
              </button>
            ))}
          </div>

          {/* Mobile menu button */}
          <div className="md:hidden">
            <button className="p-2 rounded-lg hover:bg-white hover:bg-opacity-20">
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
