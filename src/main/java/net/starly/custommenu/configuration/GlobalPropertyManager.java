package net.starly.custommenu.configuration;

import java.io.*;
import java.util.Properties;

public class GlobalPropertyManager {

    private static GlobalPropertyManager instance;

    public static GlobalPropertyManager getInstance() {
        if (instance == null) instance = new GlobalPropertyManager();
        return instance;
    }

    private GlobalPropertyManager() {}


    private final Properties properties = new Properties();

    public Properties getProperties() {
        return new Properties(properties);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public void removeProperty(String key) {
        properties.remove(key);
    }


    public void loadAll(File file) {
        try (
                FileInputStream fr = new FileInputStream(file);
                BufferedInputStream br = new BufferedInputStream(fr)
        ) {
            properties.load(br);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveAll(File file) {
        try (
                FileOutputStream fw = new FileOutputStream(file);
                BufferedOutputStream bw = new BufferedOutputStream(fw)
        ) {
            properties.store(bw, "ST-CustomMenu Properties.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
