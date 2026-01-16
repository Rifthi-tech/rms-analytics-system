import React from 'react';

function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-gray-900 text-white mt-20 border-t border-gray-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8 mb-8">
          {/* Brand */}
          <div>
            <h3 className="text-2xl font-bold mb-4 flex items-center space-x-2">
              <span>🍽️</span>
              <span>RMS Analytics</span>
            </h3>
            <p className="text-gray-400">
              Advanced analytics for restaurant management systems
            </p>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="text-lg font-semibold mb-4">Analytics</h4>
            <ul className="space-y-2 text-gray-400">
              <li><a href="#dashboard" className="hover:text-white transition">Dashboard</a></li>
              <li><a href="#peak-dining" className="hover:text-white transition">Peak Dining</a></li>
              <li><a href="#revenue" className="hover:text-white transition">Revenue</a></li>
              <li><a href="#demographics" className="hover:text-white transition">Demographics</a></li>
            </ul>
          </div>

          {/* More Links */}
          <div>
            <h4 className="text-lg font-semibold mb-4">Features</h4>
            <ul className="space-y-2 text-gray-400">
              <li><a href="#branch" className="hover:text-white transition">Branch Analysis</a></li>
              <li><a href="#anomalies" className="hover:text-white transition">Anomalies</a></li>
              <li><a href="#" className="hover:text-white transition">Reports</a></li>
              <li><a href="#" className="hover:text-white transition">Settings</a></li>
            </ul>
          </div>

          {/* Stats */}
          <div>
            <h4 className="text-lg font-semibold mb-4">Statistics</h4>
            <div className="space-y-3">
              <div className="flex justify-between items-center">
                <span className="text-gray-400">Orders Tracked:</span>
                <span className="font-bold">6,960+</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-gray-400">Customers:</span>
                <span className="font-bold">2,150+</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-gray-400">Branches:</span>
                <span className="font-bold">5</span>
              </div>
            </div>
          </div>
        </div>

        {/* Bottom */}
        <div className="border-t border-gray-800 pt-8">
          <div className="flex flex-col md:flex-row justify-between items-center">
            <p className="text-gray-400 text-sm">
              &copy; {currentYear} RMS Analytics System. All rights reserved.
            </p>
            <div className="flex space-x-6 mt-4 md:mt-0">
              <a href="#" className="text-gray-400 hover:text-white transition text-sm">Privacy Policy</a>
              <a href="#" className="text-gray-400 hover:text-white transition text-sm">Terms of Service</a>
              <a href="#" className="text-gray-400 hover:text-white transition text-sm">Contact</a>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
