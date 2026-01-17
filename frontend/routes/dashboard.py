from flask import Blueprint, render_template, request, jsonify
import requests
import plotly.graph_objs as go
import plotly.utils
import json

dashboard_bp = Blueprint('dashboard', __name__)
BACKEND_URL = 'http://localhost:8080/api/analytics'

@dashboard_bp.route('/')
def dashboard_home():
    """Main dashboard view"""
    try:
        # Get outlets for filters
        outlets_response = requests.get(f'{BACKEND_URL}/outlets')
        outlets = outlets_response.json() if outlets_response.status_code == 200 else []
        
        return render_template('dashboard.html', outlets=outlets)
    except Exception as e:
        return render_template('dashboard.html', outlets=[], error=str(e))

@dashboard_bp.route('/peak-dining')
def peak_dining_dashboard():
    """Peak dining analysis dashboard"""
    try:
        outlet_id = request.args.get('outletId')
        season = request.args.get('season')
        festival = request.args.get('festival')
        
        params = {}
        if outlet_id:
            params['outletId'] = outlet_id
        if season:
            params['season'] = season
        if festival:
            params['festival'] = festival
        
        response = requests.get(f'{BACKEND_URL}/peak-dining', params=params)
        
        if response.status_code == 200:
            data = response.json()
            
            # Create visualizations
            charts = create_peak_dining_charts(data)
            
            return render_template('peak_dining.html', 
                                 data=data, 
                                 charts=charts,
                                 selected_outlet=outlet_id,
                                 selected_season=season,
                                 selected_festival=festival)
        else:
            return render_template('peak_dining.html', error='Failed to load data')
            
    except Exception as e:
        return render_template('peak_dining.html', error=str(e))

@dashboard_bp.route('/customer-analytics')
def customer_analytics_dashboard():
    """Customer analytics dashboard"""
    try:
        outlet_id = request.args.get('outletId')
        season = request.args.get('season')
        
        params = {}
        if outlet_id:
            params['outletId'] = outlet_id
        if season:
            params['season'] = season
        
        # Get demographics data
        demographics_response = requests.get(f'{BACKEND_URL}/customer-demographics', params=params)
        seasonal_response = requests.get(f'{BACKEND_URL}/customer-seasonal', params=params)
        
        demographics_data = demographics_response.json() if demographics_response.status_code == 200 else {}
        seasonal_data = seasonal_response.json() if seasonal_response.status_code == 200 else {}
        
        # Create visualizations
        charts = create_customer_analytics_charts(demographics_data, seasonal_data)
        
        return render_template('customer_analytics.html',
                             demographics_data=demographics_data,
                             seasonal_data=seasonal_data,
                             charts=charts,
                             selected_outlet=outlet_id,
                             selected_season=season)
        
    except Exception as e:
        return render_template('customer_analytics.html', error=str(e))

def create_peak_dining_charts(data):
    """Create charts for peak dining analysis"""
    charts = {}
    
    try:
        # Hourly heatmap
        if 'hourlyHeatmap' in data:
            heatmap_data = data['hourlyHeatmap']
            if heatmap_data:
                # Convert to format suitable for heatmap
                outlets = list(heatmap_data.keys())
                hours = list(range(24))
                z_data = []
                
                for outlet in outlets:
                    outlet_data = heatmap_data[outlet]
                    row = [outlet_data.get(hour, 0) for hour in hours]
                    z_data.append(row)
                
                heatmap = go.Figure(data=go.Heatmap(
                    z=z_data,
                    x=hours,
                    y=outlets,
                    colorscale='Viridis'
                ))
                heatmap.update_layout(
                    title='Order Volume Heatmap by Hour',
                    xaxis_title='Hour of Day',
                    yaxis_title='Outlet'
                )
                charts['heatmap'] = json.dumps(heatmap, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Daily patterns
        if 'dailyPatterns' in data:
            daily_data = data['dailyPatterns']
            if daily_data:
                days = list(daily_data.keys())
                counts = list(daily_data.values())
                
                daily_chart = go.Figure(data=go.Bar(x=days, y=counts))
                daily_chart.update_layout(
                    title='Orders by Day of Week',
                    xaxis_title='Day',
                    yaxis_title='Order Count'
                )
                charts['daily_patterns'] = json.dumps(daily_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
    except Exception as e:
        print(f"Error creating peak dining charts: {e}")
    
    return charts

def create_customer_analytics_charts(demographics_data, seasonal_data):
    """Create charts for customer analytics"""
    charts = {}
    
    try:
        # Age distribution
        if 'ageDistribution' in demographics_data:
            age_data = demographics_data['ageDistribution']
            if age_data:
                age_groups = list(age_data.keys())
                counts = list(age_data.values())
                
                age_chart = go.Figure(data=go.Pie(labels=age_groups, values=counts))
                age_chart.update_layout(title='Customer Age Distribution')
                charts['age_distribution'] = json.dumps(age_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Gender distribution
        if 'genderDistribution' in demographics_data:
            gender_data = demographics_data['genderDistribution']
            if gender_data:
                genders = list(gender_data.keys())
                counts = list(gender_data.values())
                
                gender_chart = go.Figure(data=go.Bar(x=genders, y=counts))
                gender_chart.update_layout(
                    title='Customer Gender Distribution',
                    xaxis_title='Gender',
                    yaxis_title='Count'
                )
                charts['gender_distribution'] = json.dumps(gender_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Loyalty analysis
        if 'loyaltyGroupAnalysis' in demographics_data:
            loyalty_data = demographics_data['loyaltyGroupAnalysis']
            if 'distribution' in loyalty_data:
                loyalty_dist = loyalty_data['distribution']
                groups = list(loyalty_dist.keys())
                counts = list(loyalty_dist.values())
                
                loyalty_chart = go.Figure(data=go.Pie(labels=groups, values=counts))
                loyalty_chart.update_layout(title='Customer Loyalty Distribution')
                charts['loyalty_distribution'] = json.dumps(loyalty_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
    except Exception as e:
        print(f"Error creating customer analytics charts: {e}")
    
    return charts