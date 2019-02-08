package com.darkxell.common.util.language;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.darkxell.common.util.Logger;

public class Localization {
    public enum Language {
        ENGLISH("en", "English"),
        FRENCH("fr", "Francais");

        public final String id;
        public final String name;

        Language(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public static Language get(String value) {
            for (Language l : values())
                if (l.id.equals(value))
                    return l;
            return ENGLISH;
        }
    }

    private static Properties dictionary = new Properties();
    private static Language selected;

    private static void forceLoad() {
        updateTranslations();
        Keywords.updateKeywords();
    }

    public static boolean containsKey(String id) {
        return dictionary.containsKey(id);
    }

    public static Language getLanguage() {
        return selected;
    }

    public static void load(boolean forceLoad) {
        setLanguage(Language.ENGLISH);
        if (forceLoad)
            forceLoad();
    }

    public static void setLanguage(Language language) {
        if (language == selected)
            return;
        Logger.instance().info("Language set to " + language.name + ".");
        selected = language;
        forceLoad();
    }

    public static String translate(String id) {
        if (dictionary.containsKey(id))
            return dictionary.getProperty(id);
        return id;
    }

    /** Reloads the translations after switching language. */
    private static void updateTranslations() {
        dictionary.clear();
        try {
            InputStream stream = Localization.class.getResourceAsStream("/lang/" + selected.id + ".properties");
            if (stream != null)
                dictionary.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
