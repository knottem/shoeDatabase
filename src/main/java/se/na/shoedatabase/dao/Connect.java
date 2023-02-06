package se.na.shoedatabase.dao;

import se.na.shoedatabase.main.InitShoeDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import java.util.stream.Stream;

import static java.sql.DriverManager.getConnection;

public class Connect {

    public Connection getConnectionDB() throws IOException {
        String url = InitShoeDatabase.propertiesLoader.loadProperties().getProperty("mysql");
        String username = InitShoeDatabase.propertiesLoader.loadProperties().getProperty("username");
        String password = InitShoeDatabase.propertiesLoader.loadProperties().getProperty("password");
        try {
            return getConnection(url, username, password);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
