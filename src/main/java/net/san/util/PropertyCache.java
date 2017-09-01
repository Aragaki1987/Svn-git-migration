package net.san.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by AnNN1 on 5/26/2017.
 */
public class PropertyCache {
    private static Properties properties = new Properties();

    public static void load(String fileLoc) {
        properties.clear();
        try {
            properties.load(new FileInputStream(fileLoc));
        } catch (IOException e) {
            System.out.println("ERROR : Property file is not found");
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
