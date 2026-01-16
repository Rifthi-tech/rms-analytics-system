import React, { useEffect, useState } from 'react';
import {
  LineChart, Line, BarChart, Bar, PieChart, Pie, Cell,
  XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, AreaChart, Area
} from 'recharts';

const revenueData = [
  { month: 'Jan', revenue: 65000, orders: 450 },
  { month: 'Feb', revenue: 72000, orders: 480 },
  { month: 'Mar', revenue: 68500, orders: 420 },
  { month: 'Apr', revenue: 82000, orders: 550 },
  { month: 'May', revenue: 95000, orders: 620 },
  { month: 'Jun', revenue: 108000, orders: 720 },
];

const hourlyData = [
  { time: '6 AM', orders: 45 },
  { time: '9 AM', orders: 120 },
  { time: '12 PM', orders: 480 },
  { time: '3 PM', orders: 280 },
  { time: '6 PM', orders: 890 },
  { time: '9 PM', orders: 650 },
  { time: '12 AM', orders: 150 },
];

const categoryData = [
  { name: 'Pizza', value: 35 },
  { name: 'Burgers', value: 25 },
  { name: 'Salads', value: 15 },
  { name: 'Desserts', value: 15 },
  { name: 'Beverages', value: 10 },
];

const COLORS = ['#667eea', '#764ba2', '#f093fb', '#f5576c', '#feca57'];

export function Dashboard({ dataLoaded }) {
  return (
    <div className="min-h-screen bg-linear-to-br from-gray-50 to-gray-100">
      {/* Hero Section */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="gradient-primary text-white rounded-xl shadow-lg p-8 mb-12">
          <h1 className="text-4xl font-bold mb-4">Welcome to RMS Analytics Dashboard</h1>
          <p className="text-lg text-purple-100">
            Real-time insights into your restaurant's performance and customer behavior
          </p>
        </div>

        {/* KPI Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-12">
          <div className="metric-card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm font-semibold">Total Revenue</p>
                <h3 className="text-3xl font-bold text-gray-900 mt-2">Rs. 490K</h3>
                <p className="text-green-600 text-sm mt-2">↑ 12% from last month</p>
              </div>
              <div className="text-5xl">💰</div>
            </div>
          </div>

          <div className="metric-card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm font-semibold">Total Orders</p>
                <h3 className="text-3xl font-bold text-gray-900 mt-2">3,820</h3>
                <p className="text-green-600 text-sm mt-2">↑ 8% from last month</p>
              </div>
              <div className="text-5xl">📦</div>
            </div>
          </div>

          <div className="metric-card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm font-semibold">Avg Order Value</p>
                <h3 className="text-3xl font-bold text-gray-900 mt-2">Rs. 1,280</h3>
                <p className="text-green-600 text-sm mt-2">↑ 5% from last month</p>
              </div>
              <div className="text-5xl">💵</div>
            </div>
          </div>

          <div className="metric-card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-gray-600 text-sm font-semibold">Active Customers</p>
                <h3 className="text-3xl font-bold text-gray-900 mt-2">1,245</h3>
                <p className="text-green-600 text-sm mt-2">↑ 15% from last month</p>
              </div>
              <div className="text-5xl">👥</div>
            </div>
          </div>
        </div>

        {/* Charts Section */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8">
          {/* Revenue Trend */}
          <div className="chart-container">
            <h3 className="text-xl font-bold text-gray-900 mb-6 flex items-center space-x-2">
              <span>📈</span>
              <span>Revenue Trend</span>
            </h3>
            <ResponsiveContainer width="100%" height={300}>
              <AreaChart data={revenueData}>
                <defs>
                  <linearGradient id="colorRevenue" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#667eea" stopOpacity={0.8}/>
                    <stop offset="95%" stopColor="#667eea" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
                <XAxis dataKey="month" stroke="#666" />
                <YAxis stroke="#666" />
                <Tooltip contentStyle={{ backgroundColor: '#fff', border: '1px solid #e0e0e0', borderRadius: '8px' }} />
                <Area type="monotone" dataKey="revenue" stroke="#667eea" fillOpacity={1} fill="url(#colorRevenue)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>

          {/* Category Distribution */}
          <div className="chart-container">
            <h3 className="text-xl font-bold text-gray-900 mb-6 flex items-center space-x-2">
              <span>🍽️</span>
              <span>Category Distribution</span>
            </h3>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={categoryData}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, value }) => `${name}: ${value}%`}
                  outerRadius={100}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {categoryData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip formatter={(value) => `${value}%`} />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Key Insights */}
        <div className="gradient-secondary text-white rounded-xl shadow-lg p-8">
          <h3 className="text-2xl font-bold mb-6">🎯 Key Insights</h3>
          <ul className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <li className="flex items-start space-x-3">
              <span className="text-2xl">✓</span>
              <span>Peak dining hours between 6 PM - 9 PM with 890 orders</span>
            </li>
            <li className="flex items-start space-x-3">
              <span className="text-2xl">✓</span>
              <span>Pizza is the most popular category (35% of orders)</span>
            </li>
            <li className="flex items-start space-x-3">
              <span className="text-2xl">✓</span>
              <span>Revenue increased 12% month-over-month</span>
            </li>
            <li className="flex items-start space-x-3">
              <span className="text-2xl">✓</span>
              <span>Average order value is Rs. 1,280 per transaction</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
}

export function PeakDiningAnalysis() {
  return (
    <div className="min-h-screen bg-liner-to-br from-gray-50 to-gray-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="gradient-primary text-white rounded-xl shadow-lg p-8 mb-12">
          <h1 className="text-4xl font-bold mb-2">🕐 Peak Dining Analysis</h1>
          <p className="text-purple-100">Understand when customers visit and order patterns throughout the day</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Hourly Orders */}
          <div className="chart-container">
            <h3 className="text-xl font-bold text-gray-900 mb-6">Hourly Order Distribution</h3>
            <ResponsiveContainer width="100%" height={350}>
              <BarChart data={hourlyData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
                <XAxis dataKey="time" stroke="#666" />
                <YAxis stroke="#666" />
                <Tooltip contentStyle={{ backgroundColor: '#fff', border: '1px solid #e0e0e0', borderRadius: '8px' }} />
                <Bar dataKey="orders" fill="#667eea" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>

          {/* Daily Breakdown */}
          <div className="metric-card">
            <h3 className="text-xl font-bold text-gray-900 mb-6">📊 Daily Summary</h3>
            <div className="space-y-4">
              <div className="p-4 bg-liner-to-br from-blue-50 to-blue-100 rounded-lg">
                <div className="flex justify-between items-center mb-2">
                  <span className="font-semibold text-gray-900">Breakfast (6-12 PM)</span>
                  <span className="text-blue-600 font-bold">165 orders</span>
                </div>
                <div className="w-full bg-blue-200 rounded-full h-2">
                  <div className="bg-blue-600 h-2 rounded-full" style={{width: '35%'}}></div>
                </div>
              </div>

              <div className="p-4 bg-liner-to-br from-purple-50 to-purple-100 rounded-lg">
                <div className="flex justify-between items-center mb-2">
                  <span className="font-semibold text-gray-900">Lunch (12-3 PM)</span>
                  <span className="text-purple-600 font-bold">280 orders</span>
                </div>
                <div className="w-full bg-purple-200 rounded-full h-2">
                  <div className="bg-purple-600 h-2 rounded-full" style={{width: '60%'}}></div>
                </div>
              </div>

              <div className="p-4 bg-liner-to-br from-pink-50 to-pink-100 rounded-lg">
                <div className="flex justify-between items-center mb-2">
                  <span className="font-semibold text-gray-900">Dinner (6-12 AM)</span>
                  <span className="text-pink-600 font-bold">540 orders</span>
                </div>
                <div className="w-full bg-pink-200 rounded-full h-2">
                  <div className="bg-pink-600 h-2 rounded-full" style={{width: '100%'}}></div>
                </div>
              </div>

              <div className="p-4 bg-liner-to-br from-yellow-50 to-yellow-100 rounded-lg">
                <div className="flex justify-between items-center mb-2">
                  <span className="font-semibold text-gray-900">Off-Hours (12-6 AM)</span>
                  <span className="text-yellow-600 font-bold">95 orders</span>
                </div>
                <div className="w-full bg-yellow-200 rounded-full h-2">
                  <div className="bg-yellow-600 h-2 rounded-full" style={{width: '18%'}}></div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Insights */}
        <div className="gradient-secondary text-white rounded-xl shadow-lg p-8 mt-8">
          <h3 className="text-2xl font-bold mb-4">💡 Key Findings</h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div>
              <h4 className="text-lg font-semibold mb-2">Peak Hour</h4>
              <p className="text-2xl font-bold">6 PM</p>
              <p className="text-sm text-pink-100 mt-1">890 orders expected</p>
            </div>
            <div>
              <h4 className="text-lg font-semibold mb-2">Busiest Period</h4>
              <p className="text-2xl font-bold">Dinner</p>
              <p className="text-sm text-pink-100 mt-1">41% of daily orders</p>
            </div>
            <div>
              <h4 className="text-lg font-semibold mb-2">Avg Daily Orders</h4>
              <p className="text-2xl font-bold">1,080</p>
              <p className="text-sm text-pink-100 mt-1">Orders per day</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export function RevenueAnalysis() {
  return (
    <div className="min-h-screen bg-liner-to-br from-gray-50 to-gray-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="gradient-primary text-white rounded-xl shadow-lg p-8 mb-12">
          <h1 className="text-4xl font-bold mb-2">💰 Revenue Analysis</h1>
          <p className="text-purple-100">Comprehensive revenue trends and financial performance metrics</p>
        </div>

        <div className="grid grid-cols-1 gap-8">
          {/* Revenue Line Chart */}
          <div className="chart-container">
            <h3 className="text-xl font-bold text-gray-900 mb-6">Monthly Revenue Growth</h3>
            <ResponsiveContainer width="100%" height={400}>
              <LineChart data={revenueData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
                <XAxis dataKey="month" stroke="#666" />
                <YAxis stroke="#666" />
                <Tooltip contentStyle={{ backgroundColor: '#fff', border: '1px solid #e0e0e0', borderRadius: '8px' }} formatter={(value) => `Rs. ${value.toLocaleString()}`} />
                <Legend />
                <Line type="monotone" dataKey="revenue" stroke="#667eea" strokeWidth={3} dot={{ fill: '#667eea', r: 6 }} activeDot={{ r: 8 }} />
                <Line type="monotone" dataKey="orders" stroke="#f5576c" strokeWidth={3} dot={{ fill: '#f5576c', r: 6 }} activeDot={{ r: 8 }} />
              </LineChart>
            </ResponsiveContainer>
          </div>

          {/* Revenue Metrics */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="metric-card">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-gray-600 text-sm font-semibold">Total Revenue (6 months)</p>
                  <h3 className="text-3xl font-bold text-gray-900 mt-2">Rs. 490.5K</h3>
                  <p className="text-green-600 text-sm mt-2">↑ 15% growth</p>
                </div>
                <div className="text-5xl">💎</div>
              </div>
            </div>

            <div className="metric-card">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-gray-600 text-sm font-semibold">Average Monthly Revenue</p>
                  <h3 className="text-3xl font-bold text-gray-900 mt-2">Rs. 81.75K</h3>
                  <p className="text-green-600 text-sm mt-2">Consistent growth</p>
                </div>
                <div className="text-5xl">📊</div>
              </div>
            </div>

            <div className="metric-card">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-gray-600 text-sm font-semibold">Peak Month Revenue</p>
                  <h3 className="text-3xl font-bold text-gray-900 mt-2">Rs. 108K</h3>
                  <p className="text-green-600 text-sm mt-2">June 2024</p>
                </div>
                <div className="text-5xl">🏆</div>
              </div>
            </div>
          </div>
        </div>

        {/* Payment Methods */}
        <div className="mt-8">
          <div className="chart-container">
            <h3 className="text-xl font-bold text-gray-900 mb-6">Payment Methods</h3>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={[
                { method: 'Cash', count: 45, revenue: 42 },
                { method: 'Card', count: 35, revenue: 52 },
                { method: 'Digital', count: 20, revenue: 6 },
              ]}>
                <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
                <XAxis dataKey="method" stroke="#666" />
                <YAxis stroke="#666" />
                <Tooltip contentStyle={{ backgroundColor: '#fff', border: '1px solid #e0e0e0', borderRadius: '8px' }} />
                <Legend />
                <Bar dataKey="count" fill="#667eea" radius={[8, 8, 0, 0]} />
                <Bar dataKey="revenue" fill="#764ba2" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>
    </div>
  );
}

export function CustomerDemographics() {
  const ageData = [
    { age: '18-25', count: 450 },
    { age: '26-35', count: 620 },
    { age: '36-45', count: 480 },
    { age: '46-55', count: 320 },
    { age: '55+', count: 180 },
  ];

  return (
    <div className="min-h-screen bg-liner-to-br from-gray-50 to-gray-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="gradient-primary text-white rounded-xl shadow-lg p-8 mb-12">
          <h1 className="text-4xl font-bold mb-2">👥 Customer Demographics</h1>
          <p className="text-purple-100">Understand your customer base and purchasing patterns</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8">
          {/* Age Distribution */}
          <div className="chart-container">
            <h3 className="text-xl font-bold text-gray-900 mb-6">Age Distribution</h3>
            <ResponsiveContainer width="100%" height={350}>
              <BarChart data={ageData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
                <XAxis dataKey="age" stroke="#666" />
                <YAxis stroke="#666" />
                <Tooltip contentStyle={{ backgroundColor: '#fff', border: '1px solid #e0e0e0', borderRadius: '8px' }} />
                <Bar dataKey="count" fill="#764ba2" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>

          {/* Gender Distribution */}
          <div className="chart-container">
            <h3 className="text-xl font-bold text-gray-900 mb-6">Gender Distribution</h3>
            <ResponsiveContainer width="100%" height={350}>
              <PieChart>
                <Pie
                  data={[
                    { name: 'Male', value: 58 },
                    { name: 'Female', value: 40 },
                    { name: 'Other', value: 2 },
                  ]}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, value }) => `${name}: ${value}%`}
                  outerRadius={100}
                  fill="#8884d8"
                  dataKey="value"
                >
                  <Cell fill="#667eea" />
                  <Cell fill="#f093fb" />
                  <Cell fill="#feca57" />
                </Pie>
                <Tooltip formatter={(value) => `${value}%`} />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Customer Metrics */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="metric-card">
            <h4 className="font-semibold text-gray-600 mb-2">Most Active Age Group</h4>
            <p className="text-3xl font-bold text-purple-600">26-35 years</p>
            <p className="text-sm text-gray-600 mt-2">620 customers (25.8%)</p>
          </div>

          <div className="metric-card">
            <h4 className="font-semibold text-gray-600 mb-2">Average Customer Age</h4>
            <p className="text-3xl font-bold text-purple-600">35.2 years</p>
            <p className="text-sm text-gray-600 mt-2">Well-balanced demographic</p>
          </div>

          <div className="metric-card">
            <h4 className="font-semibold text-gray-600 mb-2">Repeat Customers</h4>
            <p className="text-3xl font-bold text-purple-600">68%</p>
            <p className="text-sm text-gray-600 mt-2">Strong loyalty rate</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export function BranchPerformance() {
  const branchData = [
    { name: 'Downtown', revenue: 45000, orders: 1200, satisfaction: 95 },
    { name: 'Mall', revenue: 38000, orders: 1050, satisfaction: 88 },
    { name: 'Airport', revenue: 52000, orders: 1400, satisfaction: 92 },
    { name: 'Suburbs', revenue: 28000, orders: 800, satisfaction: 85 },
    { name: 'University', revenue: 35000, orders: 950, satisfaction: 90 },
  ];

  return (
    <div className="min-h-screen bg-liner-to-br from-gray-50 to-gray-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="gradient-primary text-white rounded-xl shadow-lg p-8 mb-12">
          <h1 className="text-4xl font-bold mb-2">🏪 Branch Performance</h1>
          <p className="text-purple-100">Compare performance metrics across all restaurant branches</p>
        </div>

        {/* Branch Comparison Chart */}
        <div className="chart-container mb-8">
          <h3 className="text-xl font-bold text-gray-900 mb-6">Revenue vs Orders by Branch</h3>
          <ResponsiveContainer width="100%" height={400}>
            <BarChart data={branchData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
              <XAxis dataKey="name" stroke="#666" />
              <YAxis stroke="#666" />
              <Tooltip contentStyle={{ backgroundColor: '#fff', border: '1px solid #e0e0e0', borderRadius: '8px' }} />
              <Legend />
              <Bar dataKey="revenue" fill="#667eea" radius={[8, 8, 0, 0]} />
              <Bar dataKey="orders" fill="#f5576c" radius={[8, 8, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* Branch Details */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {branchData.map((branch, idx) => (
            <div key={idx} className="metric-card">
              <h4 className="text-lg font-bold text-gray-900 mb-4">{branch.name}</h4>
              <div className="space-y-3">
                <div className="flex justify-between items-center">
                  <span className="text-gray-600">Revenue:</span>
                  <span className="font-bold text-purple-600">Rs. {(branch.revenue / 1000).toFixed(0)}K</span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="text-gray-600">Orders:</span>
                  <span className="font-bold text-purple-600">{branch.orders}</span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="text-gray-600">Satisfaction:</span>
                  <span className="font-bold text-green-600">{branch.satisfaction}%</span>
                </div>
                <div className="pt-3 border-t border-gray-200">
                  <div className="w-full bg-gray-200 rounded-full h-2">
                    <div 
                      className="bg-liner-to-br from-purple-500 to-pink-500 h-2 rounded-full" 
                      style={{width: `${(branch.revenue / 52000) * 100}%`}}
                    ></div>
                  </div>
                  <p className="text-xs text-gray-500 mt-2">Performance rank</p>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Top Performer */}
        <div className="gradient-secondary text-white rounded-xl shadow-lg p-8 mt-8">
          <h3 className="text-2xl font-bold mb-4">🏆 Top Performer</h3>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-lg">Airport Branch</p>
              <p className="text-sm text-pink-100 mt-1">Rs. 52,000 revenue with 1,400 orders</p>
            </div>
            <div className="text-5xl">⭐</div>
          </div>
        </div>
      </div>
    </div>
  );
}

export function AnomalyDetection() {
  const anomalies = [
    { date: '2024-06-15', type: 'Unusual Spike', severity: 'High', orders: 1800, description: 'Unexpected surge in evening orders' },
    { date: '2024-06-10', type: 'Low Performance', severity: 'Medium', orders: 420, description: 'Below average lunch time orders' },
    { date: '2024-06-05', type: 'Payment Error', severity: 'Critical', orders: 250, description: 'Billing system issue detected' },
    { date: '2024-05-28', type: 'Customer Complaint', severity: 'High', orders: 890, description: 'Multiple delivery delays reported' },
  ];

  return (
    <div className="min-h-screen bg-liner-to-br from-gray-50 to-gray-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="gradient-primary text-white rounded-xl shadow-lg p-8 mb-12">
          <h1 className="text-4xl font-bold mb-2">⚠️ Anomaly Detection</h1>
          <p className="text-purple-100">Identify unusual patterns and potential issues in your business</p>
        </div>

        {/* Anomalies List */}
        <div className="space-y-4">
          {anomalies.map((anomaly, idx) => (
            <div key={idx} className="bg-white rounded-lg shadow-md p-6 border-l-4 border-red-500 hover:shadow-lg transition">
              <div className="flex justify-between items-start mb-4">
                <div>
                  <h4 className="text-lg font-bold text-gray-900">{anomaly.type}</h4>
                  <p className="text-sm text-gray-600 mt-1">{anomaly.date}</p>
                </div>
                <span className={`px-3 py-1 rounded-full text-sm font-semibold ${
                  anomaly.severity === 'Critical' ? 'bg-red-100 text-red-800' :
                  anomaly.severity === 'High' ? 'bg-orange-100 text-orange-800' :
                  'bg-yellow-100 text-yellow-800'
                }`}>
                  {anomaly.severity}
                </span>
              </div>
              <p className="text-gray-700 mb-3">{anomaly.description}</p>
              <div className="flex items-center justify-between text-sm">
                <span className="text-gray-600">Orders Affected:</span>
                <span className="font-bold text-purple-600">{anomaly.orders}</span>
              </div>
            </div>
          ))}
        </div>

        {/* Summary Stats */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mt-12">
          <div className="metric-card">
            <p className="text-gray-600 text-sm font-semibold">Total Anomalies</p>
            <h3 className="text-3xl font-bold text-gray-900 mt-2">24</h3>
            <p className="text-red-600 text-sm mt-2">Last 30 days</p>
          </div>

          <div className="metric-card">
            <p className="text-gray-600 text-sm font-semibold">Critical Issues</p>
            <h3 className="text-3xl font-bold text-gray-900 mt-2">3</h3>
            <p className="text-red-600 text-sm mt-2">Require immediate action</p>
          </div>

          <div className="metric-card">
            <p className="text-gray-600 text-sm font-semibold">Resolution Rate</p>
            <h3 className="text-3xl font-bold text-gray-900 mt-2">87%</h3>
            <p className="text-green-600 text-sm mt-2">Issues resolved</p>
          </div>

          <div className="metric-card">
            <p className="text-gray-600 text-sm font-semibold">Avg Response Time</p>
            <h3 className="text-3xl font-bold text-gray-900 mt-2">2.4h</h3>
            <p className="text-green-600 text-sm mt-2">Time to resolution</p>
          </div>
        </div>
      </div>
    </div>
  );
}
