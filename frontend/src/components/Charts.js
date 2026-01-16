import React from 'react';
import {
  LineChart,
  Line,
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from 'recharts';

export function RevenueChart({ data }) {
  const chartData = [
    { name: 'Monday', revenue: 12400, orders: 2400 },
    { name: 'Tuesday', revenue: 13300, orders: 2210 },
    { name: 'Wednesday', revenue: 20000, orders: 2290 },
    { name: 'Thursday', revenue: 22780, orders: 2000 },
    { name: 'Friday', revenue: 29000, orders: 2181 },
    { name: 'Saturday', revenue: 39490, orders: 2500 },
    { name: 'Sunday', revenue: 35000, orders: 2100 }
  ];

  return (
    <div className="chart-container">
      <h5 style={{ marginBottom: '1rem', fontWeight: '700', color: '#003366' }}>📊 Revenue Trend</h5>
      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={chartData}>
          <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
          <XAxis dataKey="name" stroke="#666" />
          <YAxis stroke="#666" />
          <Tooltip 
            contentStyle={{ backgroundColor: '#fff', border: '1px solid #e0e0e0', borderRadius: '8px' }}
            cursor={{ stroke: '#003366', strokeWidth: 2 }}
          />
          <Legend />
          <Line 
            type="monotone" 
            dataKey="revenue" 
            stroke="#003366" 
            strokeWidth={3}
            dot={{ fill: '#003366', r: 5 }}
            activeDot={{ r: 8 }}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}

export function OrdersChart({ data }) {
  const chartData = [
    { name: '12 AM', orders: 120 },
    { name: '3 AM', orders: 80 },
    { name: '6 AM', orders: 200 },
    { name: '9 AM', orders: 450 },
    { name: '12 PM', orders: 890 },
    { name: '3 PM', orders: 750 },
    { name: '6 PM', orders: 1200 },
    { name: '9 PM', orders: 980 },
    { name: '12 AM', orders: 500 }
  ];

  return (
    <div className="chart-container">
      <h5 style={{ marginBottom: '1rem', fontWeight: '700', color: '#003366' }}>🕐 Hourly Orders</h5>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={chartData}>
          <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
          <XAxis dataKey="name" stroke="#666" />
          <YAxis stroke="#666" />
          <Tooltip 
            contentStyle={{ backgroundColor: '#fff', border: '1px solid #e0e0e0', borderRadius: '8px' }}
            cursor={{ fill: 'rgba(0, 51, 102, 0.1)' }}
          />
          <Bar dataKey="orders" fill="#004d99" radius={[8, 8, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}

export function CategoryChart({ data }) {
  const chartData = [
    { name: 'Pizza', value: 35 },
    { name: 'Burgers', value: 25 },
    { name: 'Salads', value: 15 },
    { name: 'Desserts', value: 15 },
    { name: 'Beverages', value: 10 }
  ];

  const COLORS = ['#003366', '#004d99', '#AAB8C2', '#8A9BA8', '#F0F4F8'];

  return (
    <div className="chart-container">
      <h5 style={{ marginBottom: '1rem', fontWeight: '700', color: '#003366' }}>🍽️ Category Distribution</h5>
      <ResponsiveContainer width="100%" height={300}>
        <PieChart>
          <Pie
            data={chartData}
            cx="50%"
            cy="50%"
            labelLine={false}
            label={({ name, value }) => `${name}: ${value}%`}
            outerRadius={100}
            fill="#8884d8"
            dataKey="value"
          >
            {chartData.map((entry, index) => (
              <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
            ))}
          </Pie>
          <Tooltip 
            contentStyle={{ backgroundColor: '#fff', border: '1px solid #e0e0e0', borderRadius: '8px' }}
            formatter={(value) => `${value}%`}
          />
        </PieChart>
      </ResponsiveContainer>
    </div>
  );
}

export function BranchPerformanceChart({ data }) {
  const chartData = [
    { name: 'Downtown', revenue: 45000, orders: 1200 },
    { name: 'Mall', revenue: 38000, orders: 1050 },
    { name: 'Airport', revenue: 52000, orders: 1400 },
    { name: 'Suburbs', revenue: 28000, orders: 800 },
    { name: 'University', revenue: 35000, orders: 950 }
  ];

  return (
    <div className="chart-container">
      <h5 style={{ marginBottom: '1rem', fontWeight: '700', color: '#003366' }}>🏪 Branch Performance</h5>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={chartData}>
          <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
          <XAxis dataKey="name" stroke="#666" />
          <YAxis stroke="#666" />
          <Tooltip 
            contentStyle={{ backgroundColor: '#fff', border: '1px solid #e0e0e0', borderRadius: '8px' }}
            cursor={{ fill: 'rgba(0, 51, 102, 0.1)' }}
          />
          <Legend />
          <Bar dataKey="revenue" fill="#003366" radius={[8, 8, 0, 0]} />
          <Bar dataKey="orders" fill="#AAB8C2" radius={[8, 8, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}

export function CustomerAgeChart({ data }) {
  const chartData = [
    { name: '18-25', customers: 450 },
    { name: '26-35', customers: 620 },
    { name: '36-45', customers: 480 },
    { name: '46-55', customers: 380 },
    { name: '55+', customers: 220 }
  ];

  return (
    <div className="chart-container">
      <h5 style={{ marginBottom: '1rem', fontWeight: '700', color: '#003366' }}>👥 Customer Age Distribution</h5>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={chartData}>
          <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
          <XAxis dataKey="name" stroke="#666" />
          <YAxis stroke="#666" />
          <Tooltip 
            contentStyle={{ backgroundColor: '#fff', border: '1px solid #e0e0e0', borderRadius: '8px' }}
            cursor={{ fill: 'rgba(170, 184, 194, 0.1)' }}
          />
          <Bar dataKey="customers" fill="#AAB8C2" radius={[8, 8, 0, 0]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}

export function SalesGrowthChart({ data }) {
  const chartData = [
    { month: 'Jan', sales: 65000 },
    { month: 'Feb', sales: 72000 },
    { month: 'Mar', sales: 68500 },
    { month: 'Apr', sales: 82000 },
    { month: 'May', sales: 95000 },
    { month: 'Jun', sales: 108000 },
    { month: 'Jul', sales: 125000 },
    { month: 'Aug', sales: 118000 },
    { month: 'Sep', sales: 130000 },
    { month: 'Oct', sales: 145000 },
    { month: 'Nov', sales: 158000 },
    { month: 'Dec', sales: 175000 }
  ];

  return (
    <div className="chart-container">
      <h5 style={{ marginBottom: '1rem', fontWeight: '700', color: '#003366' }}>📈 Monthly Sales Growth</h5>
      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={chartData}>
          <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
          <XAxis dataKey="month" stroke="#666" />
          <YAxis stroke="#666" />
          <Tooltip 
            contentStyle={{ backgroundColor: '#fff', border: '1px solid #e0e0e0', borderRadius: '8px' }}
            formatter={(value) => `Rs. ${value.toLocaleString()}`}
            cursor={{ stroke: '#003366', strokeWidth: 2 }}
          />
          <Line 
            type="monotone" 
            dataKey="sales" 
            stroke="#004d99" 
            strokeWidth={3}
            dot={{ fill: '#003366', r: 5 }}
            activeDot={{ r: 8 }}
            isAnimationActive={true}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}
