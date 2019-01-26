package com.darkxell.client.launchable;

import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Localization.Language;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ClientSettings {
	public enum Setting {
		/** Movement direction keys. */
		KEY_UP("key.up", KeyEvent.VK_UP),
		KEY_DOWN("key.down", KeyEvent.VK_DOWN),
		KEY_LEFT("key.left", KeyEvent.VK_LEFT),
		KEY_RIGHT("key.right", KeyEvent.VK_RIGHT),

		/** Attack keys. */
		KEY_MOVE_1("key.move1", KeyEvent.VK_1),
		KEY_MOVE_2("key.move2", KeyEvent.VK_2),
		KEY_MOVE_3("key.move3", KeyEvent.VK_3),
		KEY_MOVE_4("key.move4", KeyEvent.VK_4),

		/** Free roam camera keys. */
		KEY_MAP_UP("key.map.up", KeyEvent.VK_NUMPAD8),
		KEY_MAP_DOWN("key.map.down", KeyEvent.VK_NUMPAD2),
		KEY_MAP_LEFT("key.map.left", KeyEvent.VK_NUMPAD4),
		KEY_MAP_RIGHT("key.map.right", KeyEvent.VK_NUMPAD6),
		KEY_MAP_RESET("key.map.reset", KeyEvent.VK_NUMPAD5),

		KEY_ATTACK("key.attack", KeyEvent.VK_ENTER),
		KEY_DIAGONAL("key.diagonal", KeyEvent.VK_R),
		KEY_MENU("key.menu", KeyEvent.VK_ESCAPE),
		KEY_INVENTORY("key.inventory", KeyEvent.VK_I),
		KEY_ITEM_1("key.item1", KeyEvent.VK_5),
		KEY_ITEM_2("key.item2", KeyEvent.VK_6),
		KEY_PAGE_LEFT("key.page.left", KeyEvent.VK_PAGE_UP),
		KEY_PAGE_RIGHT("key.page.right", KeyEvent.VK_PAGE_DOWN),
		KEY_PARTY("key.party", KeyEvent.VK_P),
		KEY_ROTATE("key.rotate", KeyEvent.VK_S),
		KEY_RUN("key.run", KeyEvent.VK_SHIFT),

		/** Server address - will be prefixed with {@code ws://}. */
		SERVER_ADDRESS("server.address", "localhost:8080/PMDMMOServer/"),

		/** User login name. */
		LOGIN("login", "Unknown user"),
		HP_BARS("hp_bars", true),

		LANGUAGE("lang", "null");

		public final String key;
		public final String defaultValue;

		Setting(String key, String defaultValue) {
			this.key = key;
			this.defaultValue = defaultValue;
		}

		Setting(String key, int defaultValue) {
			this(key, Integer.toString(defaultValue));
		}

		Setting(String key, boolean defaultValue) {
			this(key, Boolean.toString(defaultValue));
		}
	}

	private static final String PROPERTIES_PATH = "settings.properties";
	private static Properties settings;

	private static String getSetting(String setting) {
		if (settings == null) {
			load();
		}
		return settings.getProperty(setting);
	}

	private static void setSetting(String setting, String value) {
		if (settings == null) {
			load();
		}
		if (setting.equals(Setting.LANGUAGE.key)) Localization.setLanguage(Language.get(value));
		settings.put(setting, value);
	}

	public static String getSetting(Setting setting) {
		if (setting == null) { return null; }
		if (!settings.containsKey(setting.key)) {
			resetSetting(setting);
		}
		return getSetting(setting.key);
	}

	public static boolean getBooleanSetting(Setting setting) {
		if (setting == null) { return false; }
		String value = getSetting(setting);
		return Boolean.parseBoolean(value);
	}

	public static void setSetting(Setting setting, String value) {
		setSetting(setting.key, String.valueOf(value));
	}

	public static void setSetting(Setting setting, boolean value) {
		setSetting(setting.key, String.valueOf(value));
	}

	public static void resetSetting(Setting setting) {
		setSetting(setting.key, setting.defaultValue);
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

		Localization.setLanguage(Language.get(getSetting(Setting.LANGUAGE)));
	}

	public static void save() {
		try {
			settings.store(new FileOutputStream(new File(PROPERTIES_PATH)), null);
		} catch (IOException e) {
			Logger.e("Could not save to settings.properties: " + e);
		}
	}
}
