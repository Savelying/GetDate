package ru.savelying.getdate.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public class ConfigFileUtils {
    private final static Properties CONFIG = new Properties();

    static {
        loadConfig();
    }

    public static String getConfig(String key) {
        return CONFIG.getProperty(key);
    }

    public static Boolean getFeatureFlag(String key) {
        return Boolean.parseBoolean(getConfig("app.ff." + key));
    }

    private static void loadConfig() {
        try (InputStream in = ConfigFileUtils.class.getClassLoader().getResourceAsStream("application.properties")) {
            CONFIG.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
