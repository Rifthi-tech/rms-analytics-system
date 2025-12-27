package com.ubereats.rms.repository;

import com.ubereats.rms.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;

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