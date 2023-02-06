package se.na.shoedatabase.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    Properties prop = new Properties();

    public Properties loadProperties() {

        try {
            InputStream inputFile = PropertiesLoader.class.getClassLoader().getResourceAsStream("settings.properties");
            if (inputFile != null) {
                prop.load(inputFile);
                inputFile.close();
            } else {
                System.out.println("Misslyckades hitta properties");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
