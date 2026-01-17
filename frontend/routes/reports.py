from flask import Blueprint, render_template, request, send_file, jsonify
import requests
import pandas as pd
import io
from reportlab.lib.pagesizes import letter
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.lib import colors
import json

reports_bp = Blueprint('reports', __name__)
BACKEND_URL = 'http://localhost:8080/api/analytics'

@reports_bp.route('/')
def reports_home():
    """Reports home page"""
    try:
        outlets_response = requests.get(f'{BACKEND_URL}/outlets')
        outlets = outlets_response.json() if outlets_response.status_code == 200 else []
        
        return render_template('reports.html', outlets=outlets)
    except Exception as e:
        return render_template('reports.html', outlets=[], error=str(e))

@reports_bp.route('/export/csv/<analysis_type>')
def export_csv(analysis_type):
    """Export analysis data as CSV"""
    try:
        # Get query parameters
        params = dict(request.args)
        
        # Get data from backend
        response = requests.get(f'{BACKEND_URL}/{analysis_type}', params=params)
        
        if response.status_code != 200:
            return jsonify({'error': 'Failed to fetch data'}), 400
        
        data = response.json()
        
        # Convert to CSV based on analysis type
        csv_data = convert_to_csv(data, analysis_type)
        
        # Create CSV file
        output = io.StringIO()
        csv_data.to_csv(output, index=False)
        output.seek(0)
        
        # Create file-like object for download
        csv_file = io.BytesIO()
        csv_file.write(output.getvalue().encode('utf-8'))
        csv_file.seek(0)
        
        filename = f"{analysis_type}_report.csv"
        
        return send_file(
            csv_file,
            mimetype='text/csv',
            as_attachment=True,
            download_name=filename
        )
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@reports_bp.route('/export/pdf/<analysis_type>')
def export_pdf(analysis_type):
    """Export analysis data as PDF"""
    try:
        # Get query parameters
        params = dict(request.args)
        
        # Get data from backend
        response = requests.get(f'{BACKEND_URL}/{analysis_type}', params=params)
        
        if response.status_code != 200:
            return jsonify({'error': 'Failed to fetch data'}), 400
        
        data = response.json()
        
        # Create PDF
        pdf_buffer = create_pdf_report(data, analysis_type)
        
        filename = f"{analysis_type}_report.pdf"
        
        return send_file(
            pdf_buffer,
            mimetype='application/pdf',
            as_attachment=True,
            download_name=filename
        )
        
    except Exception as e:
        return jsonify({'error': str(e)}), 500

def convert_to_csv(data, analysis_type):
    """Convert analysis data to CSV format"""
    if analysis_type == 'peak-dining':
        return convert_peak_dining_to_csv(data)
    elif analysis_type == 'customer-demographics':
        return convert_customer_demographics_to_csv(data)
    elif analysis_type == 'menu-analysis':
        return convert_menu_analysis_to_csv(data)
    elif analysis_type == 'revenue-analysis':
        return convert_revenue_analysis_to_csv(data)
    elif analysis_type == 'branch-performance':
        return convert_branch_performance_to_csv(data)
    else:
        # Generic conversion
        return pd.DataFrame([data])

def convert_peak_dining_to_csv(data):
    """Convert peak dining data to CSV"""
    rows = []
    
    if 'peakHourTables' in data and 'overallPeakHours' in data['peakHourTables']:
        for hour_data in data['peakHourTables']['overallPeakHours']:
            rows.append({
                'Type': 'Peak Hour',
                'Hour': hour_data.get('hour', ''),
                'Order Count': hour_data.get('orderCount', 0),
                'Time Range': hour_data.get('timeRange', '')
            })
    
    if 'branchSummaries' in data:
        for outlet_id, summary in data['branchSummaries'].items():
            rows.append({
                'Type': 'Branch Summary',
                'Outlet ID': outlet_id,
                'Outlet Name': summary.get('outletName', ''),
                'Total Orders': summary.get('totalOrders', 0),
                'Total Revenue': summary.get('totalRevenue', 0),
                'Average Order Value': summary.get('averageOrderValue', 0)
            })
    
    return pd.DataFrame(rows)

def convert_customer_demographics_to_csv(data):
    """Convert customer demographics data to CSV"""
    rows = []
    
    if 'ageDistribution' in data:
        for age_group, count in data['ageDistribution'].items():
            rows.append({
                'Category': 'Age Distribution',
                'Group': age_group,
                'Count': count
            })
    
    if 'genderDistribution' in data:
        for gender, count in data['genderDistribution'].items():
            rows.append({
                'Category': 'Gender Distribution',
                'Group': gender,
                'Count': count
            })
    
    if 'loyaltyGroupAnalysis' in data and 'distribution' in data['loyaltyGroupAnalysis']:
        for loyalty_group, count in data['loyaltyGroupAnalysis']['distribution'].items():
            rows.append({
                'Category': 'Loyalty Distribution',
                'Group': loyalty_group,
                'Count': count
            })
    
    return pd.DataFrame(rows)

def convert_menu_analysis_to_csv(data):
    """Convert menu analysis data to CSV"""
    rows = []
    
    if 'popularItems' in data:
        for item in data['popularItems']:
            rows.append({
                'Item Name': item.get('itemName', ''),
                'Order Count': item.get('orderCount', 0),
                'Total Revenue': item.get('totalRevenue', 0),
                'Category': item.get('category', ''),
                'Price': item.get('price', 0),
                'Is Vegetarian': item.get('isVegetarian', False),
                'Spice Level': item.get('spiceLevel', '')
            })
    
    return pd.DataFrame(rows)

def convert_revenue_analysis_to_csv(data):
    """Convert revenue analysis data to CSV"""
    rows = []
    
    if 'revenueSummary' in data:
        summary = data['revenueSummary']
        rows.append({
            'Metric': 'Total Revenue',
            'Value': summary.get('totalRevenue', 0)
        })
        rows.append({
            'Metric': 'Average Order Value',
            'Value': summary.get('averageOrderValue', 0)
        })
        rows.append({
            'Metric': 'Total Orders',
            'Value': summary.get('totalOrders', 0)
        })
    
    if 'dailyRevenue' in data:
        for date, revenue in data['dailyRevenue'].items():
            rows.append({
                'Metric': 'Daily Revenue',
                'Date': date,
                'Value': revenue
            })
    
    return pd.DataFrame(rows)

def convert_branch_performance_to_csv(data):
    """Convert branch performance data to CSV"""
    rows = []
    
    if 'branchRankings' in data:
        for branch in data['branchRankings']:
            rows.append({
                'Outlet ID': branch.get('outletId', ''),
                'Branch Name': branch.get('branchName', ''),
                'Borough': branch.get('borough', ''),
                'Revenue': branch.get('revenue', 0),
                'Order Count': branch.get('orderCount', 0),
                'Average Order Value': branch.get('averageOrderValue', 0),
                'Customer Count': branch.get('customerCount', 0)
            })
    
    return pd.DataFrame(rows)

def create_pdf_report(data, analysis_type):
    """Create PDF report from analysis data"""
    buffer = io.BytesIO()
    doc = SimpleDocTemplate(buffer, pagesize=letter)
    styles = getSampleStyleSheet()
    story = []
    
    # Title
    title = f"{analysis_type.replace('-', ' ').title()} Report"
    story.append(Paragraph(title, styles['Title']))
    story.append(Spacer(1, 12))
    
    # Add content based on analysis type
    if analysis_type == 'peak-dining':
        add_peak_dining_content(story, data, styles)
    elif analysis_type == 'customer-demographics':
        add_customer_demographics_content(story, data, styles)
    elif analysis_type == 'menu-analysis':
        add_menu_analysis_content(story, data, styles)
    elif analysis_type == 'revenue-analysis':
        add_revenue_analysis_content(story, data, styles)
    elif analysis_type == 'branch-performance':
        add_branch_performance_content(story, data, styles)
    
    doc.build(story)
    buffer.seek(0)
    return buffer

def add_peak_dining_content(story, data, styles):
    """Add peak dining content to PDF"""
    story.append(Paragraph("Peak Dining Analysis", styles['Heading1']))
    story.append(Spacer(1, 12))
    
    if 'peakHourTables' in data and 'overallPeakHours' in data['peakHourTables']:
        story.append(Paragraph("Top Peak Hours", styles['Heading2']))
        
        table_data = [['Hour', 'Order Count', 'Time Range']]
        for hour_data in data['peakHourTables']['overallPeakHours']:
            table_data.append([
                str(hour_data.get('hour', '')),
                str(hour_data.get('orderCount', 0)),
                hour_data.get('timeRange', '')
            ])
        
        table = Table(table_data)
        table.setStyle(TableStyle([
            ('BACKGROUND', (0, 0), (-1, 0), colors.grey),
            ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
            ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
            ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
            ('FONTSIZE', (0, 0), (-1, 0), 14),
            ('BOTTOMPADDING', (0, 0), (-1, 0), 12),
            ('BACKGROUND', (0, 1), (-1, -1), colors.beige),
            ('GRID', (0, 0), (-1, -1), 1, colors.black)
        ]))
        
        story.append(table)
        story.append(Spacer(1, 12))

def add_customer_demographics_content(story, data, styles):
    """Add customer demographics content to PDF"""
    story.append(Paragraph("Customer Demographics Analysis", styles['Heading1']))
    story.append(Spacer(1, 12))
    
    # Age distribution
    if 'ageDistribution' in data:
        story.append(Paragraph("Age Distribution", styles['Heading2']))
        table_data = [['Age Group', 'Count']]
        for age_group, count in data['ageDistribution'].items():
            table_data.append([age_group, str(count)])
        
        table = Table(table_data)
        table.setStyle(TableStyle([
            ('BACKGROUND', (0, 0), (-1, 0), colors.grey),
            ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
            ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
            ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
            ('GRID', (0, 0), (-1, -1), 1, colors.black)
        ]))
        
        story.append(table)
        story.append(Spacer(1, 12))

def add_menu_analysis_content(story, data, styles):
    """Add menu analysis content to PDF"""
    story.append(Paragraph("Menu Analysis", styles['Heading1']))
    story.append(Spacer(1, 12))
    
    if 'popularItems' in data:
        story.append(Paragraph("Popular Items", styles['Heading2']))
        table_data = [['Item Name', 'Order Count', 'Revenue', 'Category']]
        
        for item in data['popularItems'][:10]:  # Top 10 items
            table_data.append([
                item.get('itemName', ''),
                str(item.get('orderCount', 0)),
                f"${item.get('totalRevenue', 0):.2f}",
                item.get('category', '')
            ])
        
        table = Table(table_data)
        table.setStyle(TableStyle([
            ('BACKGROUND', (0, 0), (-1, 0), colors.grey),
            ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
            ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
            ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
            ('GRID', (0, 0), (-1, -1), 1, colors.black)
        ]))
        
        story.append(table)

def add_revenue_analysis_content(story, data, styles):
    """Add revenue analysis content to PDF"""
    story.append(Paragraph("Revenue Analysis", styles['Heading1']))
    story.append(Spacer(1, 12))
    
    if 'revenueSummary' in data:
        summary = data['revenueSummary']
        story.append(Paragraph("Revenue Summary", styles['Heading2']))
        
        summary_text = f"""
        Total Revenue: ${summary.get('totalRevenue', 0):,.2f}
        Total Orders: {summary.get('totalOrders', 0):,}
        Average Order Value: ${summary.get('averageOrderValue', 0):.2f}
        """
        
        story.append(Paragraph(summary_text, styles['Normal']))
        story.append(Spacer(1, 12))

def add_branch_performance_content(story, data, styles):
    """Add branch performance content to PDF"""
    story.append(Paragraph("Branch Performance Analysis", styles['Heading1']))
    story.append(Spacer(1, 12))
    
    if 'branchRankings' in data:
        story.append(Paragraph("Branch Rankings", styles['Heading2']))
        table_data = [['Branch Name', 'Revenue', 'Orders', 'AOV']]
        
        for branch in data['branchRankings'][:10]:  # Top 10 branches
            table_data.append([
                branch.get('branchName', ''),
                f"${branch.get('revenue', 0):,.2f}",
                str(branch.get('orderCount', 0)),
                f"${branch.get('averageOrderValue', 0):.2f}"
            ])
        
        table = Table(table_data)
        table.setStyle(TableStyle([
            ('BACKGROUND', (0, 0), (-1, 0), colors.grey),
            ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
            ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
            ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
            ('GRID', (0, 0), (-1, -1), 1, colors.black)
        ]))
        
        story.append(table)