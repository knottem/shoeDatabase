package se.na.shoedatabase.utility;

import se.na.shoedatabase.dao.Encrypt;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    public Properties properties = new Properties();

    public PropertiesLoader() {
        try {
            InputStream inputFile = PropertiesLoader.class.getClassLoader().getResourceAsStream("settings.properties");
            if (inputFile != null) {
                properties.load(inputFile);
                inputFile.close();
            } else {
                System.out.println("Misslyckades hitta properties");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMysql() {
        return properties.getProperty("mysql");
    }

    public String getUsername() {
        return properties.getProperty("username");
    }

    public String getPassword() {
        return properties.getProperty("password");
    }

    public Boolean getTesting(){
        return Boolean.parseBoolean(properties.getProperty("testing"));
    }

    public String getTestLoginAdmin(){
        return properties.getProperty("loginAdmin");
    }
    public String getTestLoginAdminPass(){
        return Encrypt.encryptSHA3(properties.getProperty("loginAdminPass"));
    }

    public Long getTestLoginUser(){
        return Long.valueOf(properties.getProperty("loginUser"));
    }
    public String getTestLoginUserPass(){
        return Encrypt.encryptSHA3(properties.getProperty("loginUserPass"));
    }



}
