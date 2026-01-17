from flask import Flask, render_template, request, jsonify, send_file
import requests
import json
from routes.dashboard import dashboard_bp
from routes.reports import reports_bp
from routes.charts import charts_bp

app = Flask(__name__)
app.secret_key = 'restaurant-analytics-secret-key'

# Backend API configuration
BACKEND_URL = 'http://localhost:8080/api/analytics'

# Register blueprints
app.register_blueprint(dashboard_bp, url_prefix='/dashboard')
app.register_blueprint(reports_bp, url_prefix='/reports')
app.register_blueprint(charts_bp, url_prefix='/charts')

@app.route('/')
def index():
    """Main dashboard page"""
    try:
        # Get outlets for dropdown
        outlets_response = requests.get(f'{BACKEND_URL}/outlets')
        outlets = outlets_response.json() if outlets_response.status_code == 200 else []
        
        return render_template('index.html', outlets=outlets)
    except Exception as e:
        return render_template('index.html', outlets=[], error=str(e))

@app.route('/api/outlets')
def get_outlets():
    """Get list of outlets"""
    try:
        response = requests.get(f'{BACKEND_URL}/outlets')
        return jsonify(response.json())
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/api/analytics/<analysis_type>')
def get_analytics(analysis_type):
    """Proxy endpoint for analytics data"""
    try:
        # Get query parameters
        params = dict(request.args)
        
        # Map analysis types to backend endpoints
        endpoint_mapping = {
            'peak-dining': 'peak-dining',
            'customer-demographics': 'customer-demographics',
            'customer-seasonal': 'customer-seasonal',
            'menu-analysis': 'menu-analysis',
            'revenue-analysis': 'revenue-analysis',
            'anomaly-detection': 'anomaly-detection',
            'branch-performance': 'branch-performance'
        }
        
        if analysis_type not in endpoint_mapping:
            return jsonify({'error': 'Invalid analysis type'}), 400
        
        endpoint = endpoint_mapping[analysis_type]
        response = requests.get(f'{BACKEND_URL}/{endpoint}', params=params)
        
        if response.status_code == 200:
            return jsonify(response.json())
        else:
            return jsonify({'error': 'Backend service error'}), response.status_code
            
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)