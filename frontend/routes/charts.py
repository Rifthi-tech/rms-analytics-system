from flask import Blueprint, jsonify, request
import requests
import plotly.graph_objs as go
import plotly.utils
import json

charts_bp = Blueprint('charts', __name__)
BACKEND_URL = 'http://localhost:8080/api/analytics'

@charts_bp.route('/peak-dining')
def peak_dining_charts():
    """Generate charts for peak dining analysis"""
    try:
        params = dict(request.args)
        response = requests.get(f'{BACKEND_URL}/peak-dining', params=params)
        
        if response.status_code != 200:
            return jsonify({'error': 'Failed to fetch data'}), 400
        
        data = response.json()
        charts = create_peak_dining_charts(data)
        
        return jsonify(charts)
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@charts_bp.route('/customer-demographics')
def customer_demographics_charts():
    """Generate charts for customer demographics"""
    try:
        params = dict(request.args)
        response = requests.get(f'{BACKEND_URL}/customer-demographics', params=params)
        
        if response.status_code != 200:
            return jsonify({'error': 'Failed to fetch data'}), 400
        
        data = response.json()
        charts = create_customer_demographics_charts(data)
        
        return jsonify(charts)
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@charts_bp.route('/menu-analysis')
def menu_analysis_charts():
    """Generate charts for menu analysis"""
    try:
        params = dict(request.args)
        response = requests.get(f'{BACKEND_URL}/menu-analysis', params=params)
        
        if response.status_code != 200:
            return jsonify({'error': 'Failed to fetch data'}), 400
        
        data = response.json()
        charts = create_menu_analysis_charts(data)
        
        return jsonify(charts)
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@charts_bp.route('/revenue-analysis')
def revenue_analysis_charts():
    """Generate charts for revenue analysis"""
    try:
        params = dict(request.args)
        response = requests.get(f'{BACKEND_URL}/revenue-analysis', params=params)
        
        if response.status_code != 200:
            return jsonify({'error': 'Failed to fetch data'}), 400
        
        data = response.json()
        charts = create_revenue_analysis_charts(data)
        
        return jsonify(charts)
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@charts_bp.route('/branch-performance')
def branch_performance_charts():
    """Generate charts for branch performance"""
    try:
        params = dict(request.args)
        response = requests.get(f'{BACKEND_URL}/branch-performance', params=params)
        
        if response.status_code != 200:
            return jsonify({'error': 'Failed to fetch data'}), 400
        
        data = response.json()
        charts = create_branch_performance_charts(data)
        
        return jsonify(charts)
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

def create_peak_dining_charts(data):
    """Create charts for peak dining analysis"""
    charts = {}
    
    try:
        # Hourly heatmap
        if 'hourlyHeatmap' in data and data['hourlyHeatmap']:
            heatmap_data = data['hourlyHeatmap']
            outlets = list(heatmap_data.keys())
            hours = list(range(24))
            z_data = []
            
            for outlet in outlets:
                outlet_data = heatmap_data[outlet]
                row = [outlet_data.get(str(hour), 0) for hour in hours]
                z_data.append(row)
            
            heatmap = go.Figure(data=go.Heatmap(
                z=z_data,
                x=hours,
                y=outlets,
                colorscale=[[0, '#f8f9fa'], [0.5, '#6c757d'], [1, '#4a90e2']],
                hoverongaps=False
            ))
            heatmap.update_layout(
                title='Order Volume Heatmap by Hour',
                xaxis_title='Hour of Day',
                yaxis_title='Outlet',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['heatmap'] = json.dumps(heatmap, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Daily patterns bar chart
        if 'dailyPatterns' in data and data['dailyPatterns']:
            daily_data = data['dailyPatterns']
            days = list(daily_data.keys())
            counts = list(daily_data.values())
            
            daily_chart = go.Figure(data=go.Bar(
                x=days,
                y=counts,
                marker_color='#4a90e2'
            ))
            daily_chart.update_layout(
                title='Orders by Day of Week',
                xaxis_title='Day',
                yaxis_title='Order Count',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['daily_patterns'] = json.dumps(daily_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Peak hours table chart
        if 'peakHourTables' in data and 'overallPeakHours' in data['peakHourTables']:
            peak_hours = data['peakHourTables']['overallPeakHours']
            hours = [str(item['hour']) for item in peak_hours]
            counts = [item['orderCount'] for item in peak_hours]
            
            peak_chart = go.Figure(data=go.Bar(
                x=hours,
                y=counts,
                marker_color='#6c757d'
            ))
            peak_chart.update_layout(
                title='Top Peak Hours',
                xaxis_title='Hour',
                yaxis_title='Order Count',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['peak_hours'] = json.dumps(peak_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
    except Exception as e:
        print(f"Error creating peak dining charts: {e}")
    
    return charts

def create_customer_demographics_charts(data):
    """Create charts for customer demographics"""
    charts = {}
    
    try:
        # Age distribution pie chart
        if 'ageDistribution' in data and data['ageDistribution']:
            age_data = data['ageDistribution']
            labels = list(age_data.keys())
            values = list(age_data.values())
            
            age_chart = go.Figure(data=go.Pie(
                labels=labels,
                values=values,
                hole=0.3,
                marker_colors=['#4a90e2', '#6c757d', '#f8f9fa', '#e9ecef', '#dee2e6']
            ))
            age_chart.update_layout(
                title='Customer Age Distribution',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['age_distribution'] = json.dumps(age_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Gender distribution bar chart
        if 'genderDistribution' in data and data['genderDistribution']:
            gender_data = data['genderDistribution']
            genders = list(gender_data.keys())
            counts = list(gender_data.values())
            
            gender_chart = go.Figure(data=go.Bar(
                x=genders,
                y=counts,
                marker_color=['#4a90e2', '#6c757d', '#e9ecef']
            ))
            gender_chart.update_layout(
                title='Customer Gender Distribution',
                xaxis_title='Gender',
                yaxis_title='Count',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['gender_distribution'] = json.dumps(gender_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Loyalty group analysis
        if 'loyaltyGroupAnalysis' in data and 'distribution' in data['loyaltyGroupAnalysis']:
            loyalty_data = data['loyaltyGroupAnalysis']['distribution']
            groups = list(loyalty_data.keys())
            counts = list(loyalty_data.values())
            
            loyalty_chart = go.Figure(data=go.Pie(
                labels=groups,
                values=counts,
                marker_colors=['#4a90e2', '#6c757d', '#e9ecef', '#dee2e6']
            ))
            loyalty_chart.update_layout(
                title='Customer Loyalty Distribution',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['loyalty_distribution'] = json.dumps(loyalty_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
    except Exception as e:
        print(f"Error creating customer demographics charts: {e}")
    
    return charts

def create_menu_analysis_charts(data):
    """Create charts for menu analysis"""
    charts = {}
    
    try:
        # Popular items bar chart
        if 'popularItems' in data and data['popularItems']:
            items = data['popularItems'][:10]  # Top 10 items
            item_names = [item['itemName'] for item in items]
            order_counts = [item['orderCount'] for item in items]
            
            popular_chart = go.Figure(data=go.Bar(
                x=order_counts,
                y=item_names,
                orientation='h',
                marker_color='#4a90e2'
            ))
            popular_chart.update_layout(
                title='Top 10 Popular Menu Items',
                xaxis_title='Order Count',
                yaxis_title='Menu Item',
                height=500,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['popular_items'] = json.dumps(popular_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Category analysis pie chart
        if 'categoryAnalysis' in data and 'ordersByCategory' in data['categoryAnalysis']:
            category_data = data['categoryAnalysis']['ordersByCategory']
            categories = list(category_data.keys())
            counts = list(category_data.values())
            
            category_chart = go.Figure(data=go.Pie(
                labels=categories,
                values=counts,
                marker_colors=['#4a90e2', '#6c757d', '#e9ecef', '#dee2e6', '#f8f9fa']
            ))
            category_chart.update_layout(
                title='Orders by Menu Category',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['category_analysis'] = json.dumps(category_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Spice level preferences
        if 'spiceLevelPreferences' in data and data['spiceLevelPreferences']:
            spice_data = data['spiceLevelPreferences']
            spice_levels = list(spice_data.keys())
            counts = list(spice_data.values())
            
            spice_chart = go.Figure(data=go.Bar(
                x=spice_levels,
                y=counts,
                marker_color='#6c757d'
            ))
            spice_chart.update_layout(
                title='Spice Level Preferences',
                xaxis_title='Spice Level',
                yaxis_title='Order Count',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['spice_preferences'] = json.dumps(spice_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
    except Exception as e:
        print(f"Error creating menu analysis charts: {e}")
    
    return charts

def create_revenue_analysis_charts(data):
    """Create charts for revenue analysis"""
    charts = {}
    
    try:
        # Daily revenue line chart
        if 'dailyRevenue' in data and data['dailyRevenue']:
            daily_revenue = data['dailyRevenue']
            dates = list(daily_revenue.keys())
            revenues = list(daily_revenue.values())
            
            daily_chart = go.Figure(data=go.Scatter(
                x=dates,
                y=revenues,
                mode='lines+markers',
                line=dict(color='#4a90e2')
            ))
            daily_chart.update_layout(
                title='Daily Revenue Trend',
                xaxis_title='Date',
                yaxis_title='Revenue (LKR)',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['daily_revenue'] = json.dumps(daily_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Payment method analysis
        if 'paymentMethodAnalysis' in data and 'ordersByPaymentMethod' in data['paymentMethodAnalysis']:
            payment_data = data['paymentMethodAnalysis']['ordersByPaymentMethod']
            methods = list(payment_data.keys())
            counts = list(payment_data.values())
            
            payment_chart = go.Figure(data=go.Pie(
                labels=methods,
                values=counts,
                marker_colors=['#4a90e2', '#6c757d', '#e9ecef', '#dee2e6']
            ))
            payment_chart.update_layout(
                title='Orders by Payment Method',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['payment_methods'] = json.dumps(payment_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Outlet revenue comparison
        if 'outletRevenue' in data and data['outletRevenue']:
            outlet_data = data['outletRevenue']
            outlets = list(outlet_data.keys())
            revenues = [outlet_data[outlet]['revenue'] for outlet in outlets]
            
            outlet_chart = go.Figure(data=go.Bar(
                x=outlets,
                y=revenues,
                marker_color='#6c757d'
            ))
            outlet_chart.update_layout(
                title='Revenue by Outlet',
                xaxis_title='Outlet',
                yaxis_title='Revenue (LKR)',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['outlet_revenue'] = json.dumps(outlet_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
    except Exception as e:
        print(f"Error creating revenue analysis charts: {e}")
    
    return charts

def create_branch_performance_charts(data):
    """Create charts for branch performance"""
    charts = {}
    
    try:
        # Branch rankings bar chart
        if 'branchRankings' in data and data['branchRankings']:
            rankings = data['branchRankings'][:10]  # Top 10 branches
            branch_names = [branch.get('branchName', branch.get('outletId', '')) for branch in rankings]
            revenues = [branch['revenue'] for branch in rankings]
            
            ranking_chart = go.Figure(data=go.Bar(
                x=branch_names,
                y=revenues,
                marker_color='#4a90e2'
            ))
            ranking_chart.update_layout(
                title='Top 10 Branch Performance by Revenue',
                xaxis_title='Branch',
                yaxis_title='Revenue (LKR)',
                height=400,
                xaxis_tickangle=-45,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['branch_rankings'] = json.dumps(ranking_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Performance metrics comparison
        if 'performanceMetrics' in data and data['performanceMetrics']:
            metrics_data = data['performanceMetrics']
            outlets = list(metrics_data.keys())
            avg_order_values = [metrics_data[outlet].get('averageOrderValue', 0) for outlet in outlets]
            
            aov_chart = go.Figure(data=go.Bar(
                x=outlets,
                y=avg_order_values,
                marker_color='#6c757d'
            ))
            aov_chart.update_layout(
                title='Average Order Value by Branch',
                xaxis_title='Outlet',
                yaxis_title='Average Order Value (LKR)',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['aov_comparison'] = json.dumps(aov_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
    except Exception as e:
        print(f"Error creating branch performance charts: {e}")
    
    return charts