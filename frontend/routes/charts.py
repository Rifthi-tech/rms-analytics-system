from flask import Blueprint, jsonify, request
import plotly.graph_objs as go
import plotly.utils
import json
import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from data_processor import data_processor

charts_bp = Blueprint('charts', __name__)

@charts_bp.route('/peak-dining')
def peak_dining_charts():
    """Generate charts for peak dining analysis"""
    try:
        outlet_id = request.args.get('outletId')
        season = request.args.get('season')
        festival = request.args.get('festival')
        
        data = data_processor.get_peak_dining_analysis(outlet_id, season, festival)
        charts = create_peak_dining_charts(data)
        
        return jsonify(charts)
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@charts_bp.route('/customer-demographics')
def customer_demographics_charts():
    """Generate charts for customer demographics"""
    try:
        outlet_id = request.args.get('outletId')
        season = request.args.get('season')
        festival = request.args.get('festival')
        
        data = data_processor.get_customer_demographics(outlet_id, season, festival)
        charts = create_customer_demographics_charts(data)
        
        return jsonify(charts)
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@charts_bp.route('/menu-analysis')
def menu_analysis_charts():
    """Generate charts for menu analysis"""
    try:
        outlet_id = request.args.get('outletId')
        season = request.args.get('season')
        festival = request.args.get('festival')
        
        data = data_processor.get_menu_analysis(outlet_id, season, festival)
        charts = create_menu_analysis_charts(data)
        
        return jsonify(charts)
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@charts_bp.route('/revenue-analysis')
def revenue_analysis_charts():
    """Generate charts for revenue analysis"""
    try:
        outlet_id = request.args.get('outletId')
        season = request.args.get('season')
        festival = request.args.get('festival')
        
        data = data_processor.get_revenue_analysis(outlet_id, season, festival)
        charts = create_revenue_analysis_charts(data)
        
        return jsonify(charts)
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@charts_bp.route('/branch-performance')
def branch_performance_charts():
    """Generate charts for branch performance"""
    try:
        outlet_id = request.args.get('outletId')
        season = request.args.get('season')
        festival = request.args.get('festival')
        
        data = data_processor.get_branch_performance(outlet_id, season, festival)
        charts = create_branch_performance_charts(data)
        
        return jsonify(charts)
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@charts_bp.route('/seasonal-behavior')
def seasonal_behavior_charts():
    """Generate charts for seasonal behavior analysis"""
    try:
        outlet_id = request.args.get('outletId')
        season = request.args.get('season')
        festival = request.args.get('festival')
        
        data = data_processor.get_seasonal_behavior(outlet_id, season, festival)
        charts = create_seasonal_behavior_charts(data)
        
        return jsonify(charts)
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@charts_bp.route('/customer-seasonal')
def customer_seasonal_charts():
    """Generate charts for customer seasonal analysis (alias for seasonal-behavior)"""
    try:
        outlet_id = request.args.get('outletId')
        season = request.args.get('season')
        festival = request.args.get('festival')
        
        data = data_processor.get_seasonal_behavior(outlet_id, season, festival)
        charts = create_seasonal_behavior_charts(data)
        
        return jsonify(charts)
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@charts_bp.route('/anomaly-detection')
def anomaly_detection_charts():
    """Generate charts for anomaly detection"""
    try:
        outlet_id = request.args.get('outletId')
        season = request.args.get('season')
        festival = request.args.get('festival')
        
        data = data_processor.get_anomaly_detection(outlet_id, season, festival)
        charts = create_anomaly_detection_charts(data)
        
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
        
        # Loyalty distribution chart
        if 'loyaltyDistribution' in data and data['loyaltyDistribution']:
            loyalty_data = data['loyaltyDistribution']
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
        
        # Loyalty segmentation chart
        if 'loyaltySegmentation' in data and data['loyaltySegmentation']:
            segmentation = data['loyaltySegmentation']
            groups = list(segmentation.keys())
            avg_spent = [segmentation[group]['avgSpent'] for group in groups]
            
            segmentation_chart = go.Figure(data=go.Bar(
                x=groups,
                y=avg_spent,
                marker_color='#6c757d'
            ))
            segmentation_chart.update_layout(
                title='Average Spending by Loyalty Group',
                xaxis_title='Loyalty Group',
                yaxis_title='Average Spent (LKR)',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['loyalty_segmentation'] = json.dumps(segmentation_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
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
        if 'categoryAnalysis' in data and data['categoryAnalysis']:
            category_data = data['categoryAnalysis']
            categories = [item['category'] for item in category_data]
            revenues = [item['totalRevenue'] for item in category_data]
            
            category_chart = go.Figure(data=go.Pie(
                labels=categories,
                values=revenues,
                marker_colors=['#4a90e2', '#6c757d', '#e9ecef', '#dee2e6', '#f8f9fa']
            ))
            category_chart.update_layout(
                title='Revenue by Menu Category',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['category_analysis'] = json.dumps(category_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Spice level preferences
        if 'spicePreferences' in data and data['spicePreferences']:
            spice_data = data['spicePreferences']
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
        
        # Vegetarian vs Non-vegetarian
        if 'vegetarianAnalysis' in data and data['vegetarianAnalysis']:
            veg_data = data['vegetarianAnalysis']
            labels = ['Vegetarian' if k == 'True' else 'Non-Vegetarian' for k in veg_data.keys()]
            values = list(veg_data.values())
            
            veg_chart = go.Figure(data=go.Pie(
                labels=labels,
                values=values,
                marker_colors=['#4a90e2', '#6c757d']
            ))
            veg_chart.update_layout(
                title='Vegetarian vs Non-Vegetarian Orders',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['vegetarian_analysis'] = json.dumps(veg_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Item combinations chart
        if 'itemCombinations' in data and data['itemCombinations']:
            combinations = data['itemCombinations'][:5]  # Top 5 combinations
            combo_labels = [f"{combo['item1']} + {combo['item2']}" for combo in combinations]
            frequencies = [combo['frequency'] for combo in combinations]
            
            combo_chart = go.Figure(data=go.Bar(
                x=frequencies,
                y=combo_labels,
                orientation='h',
                marker_color='#6c757d'
            ))
            combo_chart.update_layout(
                title='Top Item Combinations',
                xaxis_title='Frequency',
                yaxis_title='Item Combination',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['item_combinations'] = json.dumps(combo_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
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
        if 'paymentMethods' in data and data['paymentMethods']:
            payment_data = data['paymentMethods']
            methods = list(payment_data.keys())
            revenues = list(payment_data.values())
            
            payment_chart = go.Figure(data=go.Pie(
                labels=methods,
                values=revenues,
                marker_colors=['#4a90e2', '#6c757d', '#e9ecef', '#dee2e6']
            ))
            payment_chart.update_layout(
                title='Revenue by Payment Method',
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['payment_methods'] = json.dumps(payment_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Outlet revenue comparison
        if 'outletRevenue' in data and data['outletRevenue']:
            outlet_data = data['outletRevenue']
            outlets = [outlet['outletName'] for outlet in outlet_data]
            revenues = [outlet['revenue'] for outlet in outlet_data]
            
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
        
        # Revenue growth chart (if growth rate is available)
        if 'revenueSummary' in data and data['revenueSummary'].get('revenueGrowthRate') != 'N/A':
            growth_rate = data['revenueSummary']['revenueGrowthRate']
            
            growth_chart = go.Figure(data=go.Indicator(
                mode = "gauge+number+delta",
                value = growth_rate,
                domain = {'x': [0, 1], 'y': [0, 1]},
                title = {'text': "Revenue Growth Rate (%)"},
                delta = {'reference': 0},
                gauge = {
                    'axis': {'range': [None, 50]},
                    'bar': {'color': "#4a90e2"},
                    'steps': [
                        {'range': [0, 25], 'color': "#f8f9fa"},
                        {'range': [25, 50], 'color': "#e9ecef"}],
                    'threshold': {
                        'line': {'color': "#6c757d", 'width': 4},
                        'thickness': 0.75,
                        'value': 30}}
            ))
            growth_chart.update_layout(
                height=400,
                plot_bgcolor='white',
                paper_bgcolor='white'
            )
            charts['revenue_growth'] = json.dumps(growth_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
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

def create_seasonal_behavior_charts(data):
    """Create charts for seasonal behavior analysis"""
    charts = {}
    
    try:
        # Monthly trends chart
        if 'monthlyOrders' in data and 'order_id' in data['monthlyOrders']:
            monthly_data = data['monthlyOrders']['order_id']
            months = list(monthly_data.keys())
            counts = list(monthly_data.values())
            
            if months and counts:
                monthly_chart = go.Figure(data=go.Bar(
                    x=months,
                    y=counts,
                    marker_color='#4a90e2'
                ))
                monthly_chart.update_layout(
                    title='Monthly Order Trends',
                    xaxis_title='Month',
                    yaxis_title='Order Count',
                    height=400,
                    plot_bgcolor='white',
                    paper_bgcolor='white',
                    font={'color': '#6c757d'}
                )
                charts['monthly_trends'] = json.dumps(monthly_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Monthly revenue chart
        if 'monthlyOrders' in data and 'revenue' in data['monthlyOrders']:
            revenue_data = data['monthlyOrders']['revenue']
            months = list(revenue_data.keys())
            revenues = list(revenue_data.values())
            
            if months and revenues:
                revenue_chart = go.Figure(data=go.Scatter(
                    x=months,
                    y=revenues,
                    mode='lines+markers',
                    line=dict(color='#4a90e2', width=3),
                    marker=dict(color='#6c757d', size=8)
                ))
                revenue_chart.update_layout(
                    title='Monthly Revenue Trends',
                    xaxis_title='Month',
                    yaxis_title='Revenue (LKR)',
                    height=400,
                    plot_bgcolor='white',
                    paper_bgcolor='white',
                    font={'color': '#6c757d'}
                )
                charts['monthly_revenue'] = json.dumps(revenue_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Seasonal retention chart
        if 'seasonalRetention' in data and data['seasonalRetention']:
            retention_data = data['seasonalRetention']
            groups = list(retention_data.keys())
            counts = list(retention_data.values())
            
            if groups and counts:
                retention_chart = go.Figure(data=go.Pie(
                    labels=groups,
                    values=counts,
                    marker_colors=['#4a90e2', '#6c757d', '#e9ecef', '#dee2e6']
                ))
                retention_chart.update_layout(
                    title='Customer Retention by Loyalty Group',
                    height=400,
                    plot_bgcolor='white',
                    paper_bgcolor='white',
                    font={'color': '#6c757d'}
                )
                charts['seasonal_retention'] = json.dumps(retention_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Seasonal orders chart
        if 'seasonalOrders' in data and data['seasonalOrders']:
            seasonal_data = data['seasonalOrders']
            seasons = list(seasonal_data.keys())
            counts = list(seasonal_data.values())
            
            if seasons and counts:
                seasonal_chart = go.Figure(data=go.Bar(
                    x=seasons,
                    y=counts,
                    marker_color='#6c757d'
                ))
                seasonal_chart.update_layout(
                    title='Orders by Season',
                    xaxis_title='Season',
                    yaxis_title='Order Count',
                    height=400,
                    plot_bgcolor='white',
                    paper_bgcolor='white',
                    font={'color': '#6c757d'}
                )
                charts['seasonal_orders'] = json.dumps(seasonal_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Seasonal revenue chart
        if 'seasonalRevenue' in data and data['seasonalRevenue']:
            seasonal_revenue = data['seasonalRevenue']
            seasons = list(seasonal_revenue.keys())
            revenues = list(seasonal_revenue.values())
            
            if seasons and revenues:
                seasonal_revenue_chart = go.Figure(data=go.Pie(
                    labels=seasons,
                    values=revenues,
                    hole=0.3,
                    marker_colors=['#4a90e2', '#6c757d', '#e9ecef', '#dee2e6']
                ))
                seasonal_revenue_chart.update_layout(
                    title='Revenue Distribution by Season',
                    height=400,
                    plot_bgcolor='white',
                    paper_bgcolor='white',
                    font={'color': '#6c757d'}
                )
                charts['seasonal_revenue'] = json.dumps(seasonal_revenue_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
    except Exception as e:
        print(f"Error creating seasonal behavior charts: {e}")
    
    return charts

def create_anomaly_detection_charts(data):
    """Create charts for anomaly detection"""
    charts = {}
    
    try:
        # Alert severity distribution
        if 'alertLogs' in data and data['alertLogs']:
            alerts = data['alertLogs']
            severity_counts = {}
            for alert in alerts:
                severity = alert.get('severity', 'UNKNOWN')
                severity_counts[severity] = severity_counts.get(severity, 0) + 1
            
            if severity_counts:
                severities = list(severity_counts.keys())
                counts = list(severity_counts.values())
                
                severity_chart = go.Figure(data=go.Pie(
                    labels=severities,
                    values=counts,
                    marker_colors=['#dc3545', '#6c757d', '#4a90e2']
                ))
                severity_chart.update_layout(
                    title='Alert Distribution by Severity',
                    height=400,
                    plot_bgcolor='white',
                    paper_bgcolor='white'
                )
                charts['alert_severity'] = json.dumps(severity_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
        # Alert types chart
        if 'alertLogs' in data and data['alertLogs']:
            alerts = data['alertLogs']
            type_counts = {}
            for alert in alerts:
                alert_type = alert.get('type', 'Unknown')
                type_counts[alert_type] = type_counts.get(alert_type, 0) + 1
            
            if type_counts:
                types = list(type_counts.keys())
                counts = list(type_counts.values())
                
                type_chart = go.Figure(data=go.Bar(
                    x=types,
                    y=counts,
                    marker_color='#6c757d'
                ))
                type_chart.update_layout(
                    title='Alert Distribution by Type',
                    xaxis_title='Alert Type',
                    yaxis_title='Count',
                    height=400,
                    plot_bgcolor='white',
                    paper_bgcolor='white'
                )
                charts['alert_types'] = json.dumps(type_chart, cls=plotly.utils.PlotlyJSONEncoder)
        
    except Exception as e:
        print(f"Error creating anomaly detection charts: {e}")
    
    return charts