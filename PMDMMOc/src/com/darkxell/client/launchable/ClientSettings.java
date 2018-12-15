package com.darkxell.client.launchable;

import com.darkxell.common.util.Logger;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class ClientSettings {
    public enum Setting {
        /**
         * Movement direction keys.
         */
        KEY_UP("key.up", KeyEvent.VK_UP),
        KEY_DOWN("key.down", KeyEvent.VK_DOWN),
        KEY_LEFT("key.left", KeyEvent.VK_LEFT),
        KEY_RIGHT("key.right", KeyEvent.VK_RIGHT),

        KEY_ATTACK("key.attack", KeyEvent.VK_D),
        KEY_ROTATE("key.rotate", KeyEvent.VK_S),
        KEY_RUN("key.run", KeyEvent.VK_SHIFT),
        KEY_DIAGONAL("key.diagonal", KeyEvent.VK_R),
        KEY_MENU("key.menu", KeyEvent.VK_ESCAPE),

        /**
         * Attack keys.
         */
        KEY_MOVE_1("key.move1", KeyEvent.VK_1),
        KEY_MOVE_2("key.move2", KeyEvent.VK_2),
        KEY_MOVE_3("key.move3", KeyEvent.VK_3),
        KEY_MOVE_4("key.move4", KeyEvent.VK_4),

        KEY_ITEM_1("key.item1", KeyEvent.VK_5),
        KEY_ITEM_2("key.item2", KeyEvent.VK_6),
        KEY_INVENTORY("key.inventory", KeyEvent.VK_I),
        KEY_MAP_UP("key.map.up", KeyEvent.VK_NUMPAD8),
        KEY_MAP_DOWN("key.map.down", KeyEvent.VK_NUMPAD2),
        KEY_MAP_LEFT("key.map.left", KeyEvent.VK_NUMPAD4),
        KEY_MAP_RIGHT("key.map.right", KeyEvent.VK_NUMPAD6),
        KEY_MAP_RESET("key.map.reset", KeyEvent.VK_NUMPAD5),
        KEY_PARTY("key.party", KeyEvent.VK_PAGE_UP),
        KEY_PAGE_LEFT("key.page.left", KeyEvent.VK_PAGE_DOWN),
        KEY_PAGE_RIGHT("key.page.right", KeyEvent.VK_P),

        /**
         * Server address - will be prefixed with {@code ws://}.
         */
        SERVER_ADDRESS("server.address", "localhost:8080/PMDMMOServer/"),

        /**
         * User login name.
         */
        LOGIN("login", "Unknown user"),
        HP_BARS("hp_bars", "true");

        private String key;
        private String value;

        private static final HashMap<String, Setting> values = new HashMap<>();

        static {
            for (Setting k : Setting.values()) {
                values.put(k.key, k);
            }
        }

        Setting(String key, String value) {
            this.key = key;
            this.value = value;
        }

        Setting(String key, int value) {
            this(key, Integer.toString(value));
        }

        public static Setting getByKey(String name) {
            return values.get(name);
        }
    }

    private static final String PROPERTIES_PATH = "settings.properties";
    private static Properties settings;

    public static boolean getBooleanSetting(Setting setting) {
        if (setting == null) {
            return false;
        }

        String value = getSetting(setting);
        return Boolean.parseBoolean(value);
    }

    private static String getSetting(String setting) {
        if (!settings.containsKey(setting)) {
            resetSetting(setting);
        }
        return settings.getProperty(setting);
    }

    public static String getSetting(Setting setting) {
        if (setting == null) {
            return null;
        }
        return getSetting(setting.key);
    }

    public static void load() {
        settings = new Properties();
        File f = new File(PROPERTIES_PATH);
        if (f.exists()) {
            try {
                settings.load(new FileInputStream(f));
            } catch (IOException e) {
                Logger.e("Could not load from settings.properties: " + e);
            }
        }
    }

    /**
     * Resets the input setting to its default value.
     */
    private static void resetSetting(String setting) {
        settings.put(setting, Setting.getByKey(setting).value);
    }

    public static void resetSetting(Setting setting) {
        if (setting != null) {
            settings.put(setting, setting.value);
        }
    }

    public static void save() {
        try {
            settings.store(new FileOutputStream(new File(PROPERTIES_PATH)), null);
        } catch (IOException e) {
            Logger.e("Could not save to settings.properties: " + e);
        }
    }

    public static void setSetting(Setting setting, boolean value) {
        if (setting != null) {
            settings.put(setting.key, String.valueOf(value));
        }
    }

    private static void setSetting(String setting, String value) {
        settings.put(setting, value);
    }

    public static void setSetting(Setting setting, String value) {
        if (setting != null && value != null) {
            setSetting(setting.key, value);
        }
    }
}
