package se.na.shoedatabase.dao;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import java.util.stream.Stream;

import static java.sql.DriverManager.getConnection;

public class Connect {

    public Connection getConnectionDB() throws IOException {
        Properties prop = new Properties();
        prop.load(getClass().getClassLoader().getResourceAsStream("settings.properties"));
        String[] temp = Stream.of("mysql", "username", "password").map(prop::getProperty).toArray(String[]::new);
        try {
            return getConnection(temp[0], temp[1], temp[2]);
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
