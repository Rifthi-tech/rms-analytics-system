from flask import Flask, render_template, request, jsonify, send_file
import requests
import json
from routes.dashboard import dashboard_bp
from routes.reports import reports_bp
from routes.charts import charts_bp
from data_processor import data_processor

app = Flask(__name__)
app.secret_key = 'restaurant-analytics-secret-key'

# Register blueprints
app.register_blueprint(dashboard_bp, url_prefix='/dashboard')
app.register_blueprint(reports_bp, url_prefix='/reports')
app.register_blueprint(charts_bp, url_prefix='/charts')

@app.route('/')
def index():
    """Main dashboard page - redirect to dashboard overview"""
    try:
        outlets = data_processor.get_outlets()
        return render_template('dashboard_overview.html', outlets=outlets)
    except Exception as e:
        return render_template('dashboard_overview.html', outlets=[], error=str(e))

@app.route('/dashboard-overview')
def dashboard_overview():
    """Dashboard overview page"""
    try:
        outlets = data_processor.get_outlets()
        return render_template('dashboard_overview.html', outlets=outlets)
    except Exception as e:
        return render_template('dashboard_overview.html', outlets=[], error=str(e))

@app.route('/analysis')
def analysis():
    """Analysis page"""
    try:
        outlets = data_processor.get_outlets()
        return render_template('analysis.html', outlets=outlets)
    except Exception as e:
        return render_template('analysis.html', outlets=[], error=str(e))

@app.route('/reports')
def reports():
    """Reports page"""
    try:
        outlets = data_processor.get_outlets()
        return render_template('reports.html', outlets=outlets)
    except Exception as e:
        return render_template('reports.html', outlets=[], error=str(e))

@app.route('/api/outlets')
def get_outlets():
    """Get list of outlets from real data"""
    try:
        outlets = data_processor.get_outlets()
        return jsonify(outlets)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/api/analytics/<analysis_type>')
def get_analytics(analysis_type):
    """Get analytics data from real dataset"""
    try:
        # Get query parameters
        outlet_id = request.args.get('outletId')
        season = request.args.get('season')
        festival = request.args.get('festival')
        
        # Route to appropriate analysis function
        if analysis_type == 'peak-dining':
            data = data_processor.get_peak_dining_analysis(outlet_id, season, festival)
        elif analysis_type == 'customer-demographics':
            data = data_processor.get_customer_demographics(outlet_id, season, festival)
        elif analysis_type == 'customer-seasonal':
            data = data_processor.get_seasonal_behavior(outlet_id, season, festival)
        elif analysis_type == 'menu-analysis':
            data = data_processor.get_menu_analysis(outlet_id, season, festival)
        elif analysis_type == 'revenue-analysis':
            data = data_processor.get_revenue_analysis(outlet_id, season, festival)
        elif analysis_type == 'anomaly-detection':
            data = data_processor.get_anomaly_detection(outlet_id, season, festival)
        elif analysis_type == 'branch-performance':
            data = data_processor.get_branch_performance(outlet_id, season, festival)
        elif analysis_type == 'outlets':
            data = data_processor.get_outlets()
        else:
            return jsonify({'error': 'Invalid analysis type'}), 400
        
        return jsonify(data)
            
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/api/forecast')
def get_forecast():
    """Get 6-month forecast"""
    try:
        outlet_id = request.args.get('outletId')
        forecast = data_processor.get_6_month_forecast(outlet_id)
        return jsonify(forecast)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)