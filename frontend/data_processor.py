import pandas as pd
import numpy as np
from datetime import datetime, timedelta
import json
import os

class RestaurantDataProcessor:
    def __init__(self, csv_path=None):
        # Try multiple possible paths for the CSV file
        possible_paths = [
            'restaurant_dataset_combined.csv',
            '../restaurant_dataset_combined.csv',
            os.path.join(os.path.dirname(os.path.dirname(__file__)), 'restaurant_dataset_combined.csv')
        ]
        
        self.csv_path = csv_path
        if not self.csv_path:
            for path in possible_paths:
                if os.path.exists(path):
                    self.csv_path = path
                    break
        
        if not self.csv_path:
            self.csv_path = possible_paths[0]  # Default fallback
            
        self.df = None
        self.load_data()
    
    def load_data(self):
        """Load and preprocess the restaurant dataset"""
        try:
            self.df = pd.read_csv(self.csv_path)
            self.df['order_placed'] = pd.to_datetime(self.df['order_placed'])
            self.df['served_time'] = pd.to_datetime(self.df['served_time'])
            self.df['join_date'] = pd.to_datetime(self.df['join_date'])
            
            # Add derived columns
            self.df['hour'] = self.df['order_placed'].dt.hour
            self.df['day_of_week'] = self.df['order_placed'].dt.day_name()
            self.df['month'] = self.df['order_placed'].dt.month
            self.df['date'] = self.df['order_placed'].dt.date
            
            print(f"Loaded {len(self.df)} records from dataset")
        except Exception as e:
            print(f"Error loading data: {e}")
            self.df = pd.DataFrame()
    
    def get_outlets(self):
        """Get list of unique outlets"""
        if self.df.empty:
            return []
        
        outlets = self.df.groupby(['outlet_id', 'name_y']).first().reset_index()
        return [
            {
                'id': row['outlet_id'],
                'name': row['name_y'],
                'borough': row['borough'],
                'capacity': row['capacity']
            }
            for _, row in outlets.iterrows()
        ]
    
    def get_peak_dining_analysis(self, outlet_id=None, season=None, festival=None):
        """Analyze peak dining patterns"""
        df_filtered = self.filter_data(outlet_id, season, festival)
        
        if df_filtered.empty:
            return {'error': 'No data available for selected filters'}
        
        # Daily patterns
        daily_patterns = df_filtered.groupby('day_of_week').size().to_dict()
        
        # Hourly patterns
        hourly_patterns = df_filtered.groupby('hour').size().to_dict()
        
        # Peak hours by outlet
        peak_by_outlet = df_filtered.groupby(['name_y', 'hour']).size().reset_index(name='order_count')
        peak_by_outlet = peak_by_outlet.loc[peak_by_outlet.groupby('name_y')['order_count'].idxmax()]
        
        return {
            'dailyPatterns': daily_patterns,
            'hourlyPatterns': hourly_patterns,
            'peakByOutlet': peak_by_outlet.to_dict('records'),
            'totalOrders': len(df_filtered)
        }
    
    def get_customer_demographics(self, outlet_id=None, season=None, festival=None):
        """Analyze customer demographics"""
        df_filtered = self.filter_data(outlet_id, season, festival)
        
        if df_filtered.empty:
            return {'error': 'No data available for selected filters'}
        
        # Get unique customers
        customers = df_filtered.drop_duplicates('customer_id')
        
        # Age distribution
        age_bins = [0, 25, 35, 45, 55, 100]
        age_labels = ['18-25', '26-35', '36-45', '46-55', '55+']
        customers['age_group'] = pd.cut(customers['age'], bins=age_bins, labels=age_labels, right=False)
        age_distribution = customers['age_group'].value_counts().to_dict()
        
        # Gender distribution
        gender_distribution = customers['gender'].value_counts().to_dict()
        
        # Loyalty distribution
        loyalty_distribution = customers['loyalty_group'].value_counts().to_dict()
        
        return {
            'ageDistribution': age_distribution,
            'genderDistribution': gender_distribution,
            'loyaltyDistribution': loyalty_distribution,
            'totalCustomers': len(customers)
        }
    
    def get_seasonal_behavior(self, outlet_id=None, season=None, festival=None):
        """Analyze seasonal customer behavior"""
        df_filtered = self.filter_data(outlet_id, season, festival)
        
        if df_filtered.empty:
            return {'error': 'No data available for selected filters'}
        
        # Monthly trends
        monthly_orders = df_filtered.groupby('month').agg({
            'order_id': 'count',
            'total_price_lkr': 'sum'
        }).to_dict()
        
        # Seasonal retention (simplified)
        seasonal_retention = df_filtered.groupby('loyalty_group')['customer_id'].nunique().to_dict()
        
        return {
            'monthlyOrders': monthly_orders,
            'seasonalRetention': seasonal_retention
        }
    
    def get_menu_analysis(self, outlet_id=None, season=None, festival=None):
        """Analyze menu performance"""
        df_filtered = self.filter_data(outlet_id, season, festival)
        
        if df_filtered.empty:
            return {'error': 'No data available for selected filters'}
        
        # Popular items
        popular_items = df_filtered.groupby('name').agg({
            'quantity': 'sum',
            'order_id': 'nunique'
        }).reset_index()
        popular_items.columns = ['itemName', 'totalQuantity', 'orderCount']
        popular_items = popular_items.sort_values('orderCount', ascending=False).head(10)
        
        # Category analysis
        category_analysis = df_filtered.groupby('category').agg({
            'quantity': 'sum',
            'price_lkr_y': 'sum'
        }).reset_index()
        
        # Spice level preferences
        spice_preferences = df_filtered['spice_level'].value_counts().to_dict()
        
        # Vegetarian analysis
        veg_analysis = df_filtered['is_vegetarian'].value_counts().to_dict()
        
        return {
            'popularItems': popular_items.to_dict('records'),
            'categoryAnalysis': category_analysis.to_dict('records'),
            'spicePreferences': spice_preferences,
            'vegetarianAnalysis': veg_analysis
        }
    
    def get_revenue_analysis(self, outlet_id=None, season=None, festival=None):
        """Analyze revenue metrics"""
        df_filtered = self.filter_data(outlet_id, season, festival)
        
        if df_filtered.empty:
            return {'error': 'No data available for selected filters'}
        
        # Revenue summary
        total_revenue = df_filtered['total_price_lkr'].sum()
        total_orders = df_filtered['order_id'].nunique()
        avg_order_value = total_revenue / total_orders if total_orders > 0 else 0
        
        # Daily revenue
        daily_revenue = df_filtered.groupby('date')['total_price_lkr'].sum().to_dict()
        daily_revenue = {str(k): v for k, v in daily_revenue.items()}
        
        # Payment method analysis
        payment_methods = df_filtered.groupby('payment_method')['total_price_lkr'].sum().to_dict()
        
        # Outlet revenue comparison
        outlet_revenue = df_filtered.groupby('name_y').agg({
            'total_price_lkr': 'sum',
            'order_id': 'nunique'
        }).reset_index()
        outlet_revenue.columns = ['outletName', 'revenue', 'orderCount']
        outlet_revenue['avgOrderValue'] = outlet_revenue['revenue'] / outlet_revenue['orderCount']
        
        return {
            'revenueSummary': {
                'totalRevenue': total_revenue,
                'totalOrders': total_orders,
                'averageOrderValue': avg_order_value
            },
            'dailyRevenue': daily_revenue,
            'paymentMethods': payment_methods,
            'outletRevenue': outlet_revenue.to_dict('records')
        }
    
    def get_branch_performance(self, outlet_id=None, season=None, festival=None):
        """Analyze branch performance"""
        df_filtered = self.filter_data(outlet_id, season, festival)
        
        if df_filtered.empty:
            return {'error': 'No data available for selected filters'}
        
        # Branch rankings
        branch_performance = df_filtered.groupby('name_y').agg({
            'total_price_lkr': 'sum',
            'order_id': 'nunique',
            'customer_id': 'nunique'
        }).reset_index()
        
        branch_performance.columns = ['branchName', 'revenue', 'orderCount', 'customerCount']
        branch_performance['averageOrderValue'] = branch_performance['revenue'] / branch_performance['orderCount']
        branch_performance = branch_performance.sort_values('revenue', ascending=False)
        
        return {
            'branchRankings': branch_performance.to_dict('records')
        }
    
    def get_anomaly_detection(self, outlet_id=None, season=None, festival=None):
        """Simple anomaly detection"""
        df_filtered = self.filter_data(outlet_id, season, festival)
        
        if df_filtered.empty:
            return {'error': 'No data available for selected filters'}
        
        alerts = []
        
        # Check for unusual order volumes
        daily_orders = df_filtered.groupby('date').size()
        mean_orders = daily_orders.mean()
        std_orders = daily_orders.std()
        
        for date, count in daily_orders.items():
            if count > mean_orders + 2 * std_orders:
                alerts.append({
                    'type': 'High Order Volume',
                    'message': f'Unusually high order volume on {date}: {count} orders',
                    'severity': 'HIGH',
                    'date': str(date)
                })
            elif count < mean_orders - 2 * std_orders:
                alerts.append({
                    'type': 'Low Order Volume',
                    'message': f'Unusually low order volume on {date}: {count} orders',
                    'severity': 'MEDIUM',
                    'date': str(date)
                })
        
        return {
            'alertLogs': alerts[-10:]  # Return last 10 alerts
        }
    
    def filter_data(self, outlet_id=None, season=None, festival=None):
        """Filter data based on parameters"""
        df_filtered = self.df.copy()
        
        if outlet_id:
            df_filtered = df_filtered[df_filtered['outlet_id'] == outlet_id]
        
        if season:
            season_months = {
                'spring': [3, 4, 5],
                'summer': [6, 7, 8],
                'autumn': [9, 10, 11],
                'winter': [12, 1, 2]
            }
            if season in season_months:
                df_filtered = df_filtered[df_filtered['month'].isin(season_months[season])]
        
        # Festival filtering would require additional date mapping
        # For now, we'll skip festival filtering
        
        return df_filtered
    
    def get_6_month_forecast(self, outlet_id=None):
        """Generate 6-month forecast based on historical data"""
        df_filtered = self.filter_data(outlet_id)
        
        if df_filtered.empty:
            return {'error': 'No data available for forecasting'}
        
        # Simple trend-based forecast
        monthly_revenue = df_filtered.groupby('month')['total_price_lkr'].sum()
        
        # Calculate average growth rate
        if len(monthly_revenue) > 1:
            growth_rate = monthly_revenue.pct_change().mean()
        else:
            growth_rate = 0.05  # Default 5% growth
        
        # Generate 6-month forecast
        last_month_revenue = monthly_revenue.iloc[-1] if len(monthly_revenue) > 0 else 100000
        forecast = []
        
        for i in range(1, 7):
            forecasted_revenue = last_month_revenue * (1 + growth_rate) ** i
            forecast.append({
                'month': f'Month +{i}',
                'forecastedRevenue': round(forecasted_revenue, 2),
                'confidence': max(0.6, 0.9 - i * 0.05)  # Decreasing confidence
            })
        
        return {
            'forecast': forecast,
            'growthRate': growth_rate,
            'baseRevenue': last_month_revenue
        }

# Global instance
data_processor = RestaurantDataProcessor()