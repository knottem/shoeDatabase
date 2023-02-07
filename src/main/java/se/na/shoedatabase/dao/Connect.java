package se.na.shoedatabase.dao;


import java.io.IOException;
import java.sql.Connection;

import static java.sql.DriverManager.getConnection;
import static se.na.shoedatabase.main.InitShoeDatabase.propertiesLoader;

public class Connect {

    public Connection getConnectionDB() throws IOException {
        String url = propertiesLoader.loadProperties().getProperty("mysql");
        String username = propertiesLoader.loadProperties().getProperty("username");
        String password = propertiesLoader.loadProperties().getProperty("password");
        try {
            return getConnection(url, username, password);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
