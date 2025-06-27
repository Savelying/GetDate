package ru.savelying.getdate.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class WordBundle {
    private final ResourceBundle resourceBundle;

    public WordBundle(String lang) {
//        Locale locale = Locale.getDefault();
        Locale locale = Locale.of("en");
        if (lang.equals("ru")) {
            locale = Locale.of("ru");
        }
        this.resourceBundle = ResourceBundle.getBundle("words", locale);
    }

    public String getWord(String key) {
        String result;
        try {
            result = resourceBundle.getString(key.toLowerCase());
        } catch (MissingResourceException | ClassCastException e) {
            String[] pathArray = key.split("\\.");
            result = pathArray[pathArray.length - 1];
        } catch (Exception e) {
            result = "* empty *";
        }
        return result;
    }
}
