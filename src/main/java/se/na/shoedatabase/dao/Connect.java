package se.na.shoedatabase.dao;


import java.io.IOException;
import java.sql.Connection;

import static java.sql.DriverManager.getConnection;
import static se.na.shoedatabase.main.InitShoeDatabase.properties;

public class Connect {

    public Connection getConnectionDB() throws IOException {
        try {
            return getConnection(properties.getMysql(), properties.getUsername(), properties.getPassword());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
