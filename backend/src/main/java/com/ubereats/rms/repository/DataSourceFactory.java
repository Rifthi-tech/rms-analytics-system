package com.ubereats.rms.repository;

import org.springframework.stereotype.Component;

@Component
public class DataSourceFactory {

    public DataSource createDataSource(String filePath, String dataSourceType) {
        switch (dataSourceType.toUpperCase()) {
            case "CSV":
                return new CSVDataSource(filePath);
            // Add more data source types here (Database, API, etc.)
            default:
                throw new IllegalArgumentException("Unsupported data source type: " + dataSourceType);
        }
    }

    public interface DataSource {
        List<Order> loadOrders();
        List<Customer> loadCustomers();
        List<MenuItem> loadMenuItems();
        List<Outlet> loadOutlets();
    }
}