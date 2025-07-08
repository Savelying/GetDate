package ru.savelying.getdate.utils;

import lombok.experimental.UtilityClass;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.util.Collections.singletonMap;
import static ru.savelying.getdate.utils.StringUtils.isBlank;

@UtilityClass
public class ConfigFileYaml {
    private final static Map<String,String> CONFIG = new HashMap<>();

    static {
        loadConfig();
    }

    public static String getConfig(String key) {
        return CONFIG.get(key);
    }

    private static void loadConfig() {
        try (InputStream in = ConfigFileYaml.class.getClassLoader().getResourceAsStream("application.yml")) {
            Map<String, Object> loaded = new Yaml().load(in);
            CONFIG.putAll(getFlattenedMap(loaded));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> getFlattenedMap(Map<String, Object> loaded) {
        Map<String, String> flattened = new LinkedHashMap<>();
        buildFlattenedMap(flattened, loaded, null);
        return flattened;
    }

    private static void buildFlattenedMap(Map<String, String> flattened, Map<String, Object> loaded, String prefix) {
        loaded.forEach((key, value) -> {
            if (!isBlank(prefix)) key = prefix +(key.startsWith("[") ? key : '.' + key);
            if (value instanceof String) {
                flattened.put(key, value.toString());
            } else if (value instanceof Map) {
              buildFlattenedMap(flattened, (Map<String, Object>) value, key);
            } else if (value instanceof Collection) {
                int count = 0;
                for (Object item : (Collection<?>) value) buildFlattenedMap(flattened, singletonMap("[" + (count++) + "]", item), key);
            } else {
                flattened.put(key, value != null ? "" + value : "");
            }
        });
    }
}
