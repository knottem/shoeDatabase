package se.na.shoedatabase.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public class Connect {

    public Connection getConnectionDB() throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/main/resources/settings.properties"));
        String url = prop.getProperty("mysql");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        try {
            return getConnection(url, username, password);
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
