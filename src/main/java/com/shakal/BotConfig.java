package com.shakal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class BotConfig {
    private static Properties prop;

    static {
        buildProperties();
    }

    private static void buildProperties() {
        try (InputStream input = BotConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            prop = new Properties();
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    public static String getProperty(String key){
        return prop.getProperty(key, "undefined");
    }

}
