package com.darkxell.common.util.language;

import java.io.*;
import java.util.Properties;

import com.darkxell.common.util.Logger;

public class Lang {
    public enum Language {
        ENGLISH("en", "English");

        public final String id;
        public final String name;

        Language(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    private static Properties dictionnary = new Properties();
    private static Language selected;

    public static boolean containsKey(String id) {
        return dictionnary.containsKey(id);
    }

    public static Language getLanguage() {
        return selected;
    }

    public static void load(boolean forceLoad) {
        setLanguage(Language.ENGLISH);
        if (forceLoad) {
            updateTranslations();
            Keywords.updateKeywords();
        }
    }

    public static void setLanguage(Language language) {
        if (language == selected) {
            return;
        }
        Logger.instance().info("Language set to " + language.name + ".");
        selected = language;
        updateTranslations();
        Keywords.updateKeywords();
    }

    public static String translate(String id) {
        if (dictionnary.containsKey(id)) {
            return dictionnary.getProperty(id);
        }
        return id;
    }

    /**
     * Reloads the translations after switching language.
     */
    private static void updateTranslations() {
        dictionnary.clear();
        try {
            InputStream stream = Lang.class.getResourceAsStream("/lang/" + selected.id + ".properties");
            if (stream != null) {
                dictionnary.load(stream);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
