package com.darkxell.client.launchable;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.darkxell.common.util.Logger;

public class ClientSettings
{
	public static final String KEY_UP = "key.up", KEY_DOWN = "key.down", KEY_LEFT = "key.left", KEY_RIGHT = "key.right", KEY_ATTACK = "key.attack",
			KEY_ROTATE = "key.rotate", KEY_RUN = "key.run", KEY_DIAGONAL = "key.diagonal", KEY_MENU = "key.menu", KEY_MOVE_1 = "key.move1",
			KEY_MOVE_2 = "key.move2", KEY_MOVE_3 = "key.move3", KEY_MOVE_4 = "key.move4", KEY_ITEM_1 = "key.item1", KEY_ITEM_2 = "key.item2",
			KEY_INVENTORY = "key.inventory", KEY_MAP_UP = "key.map.up", KEY_MAP_DOWN = "key.map.down", KEY_MAP_LEFT = "key.map.left",
			KEY_MAP_RIGHT = "key.map.right", KEY_MAP_RESET = "key.map.reset", KEY_PARTY = "key.party", KEY_PAGE_LEFT = "key.page.left",
			KEY_PAGE_RIGHT = "key.page.right", SERVER_ADDRESS = "server.address", LOGIN = "login", HP_BARS = "hp_bars";

	private static Properties settings;

	public static String defaultSetting(String setting)
	{
		switch (setting)
		{
			case KEY_UP:
				return Integer.toString(KeyEvent.VK_UP);
			case KEY_DOWN:
				return Integer.toString(KeyEvent.VK_DOWN);
			case KEY_LEFT:
				return Integer.toString(KeyEvent.VK_LEFT);
			case KEY_RIGHT:
				return Integer.toString(KeyEvent.VK_RIGHT);
			case KEY_ATTACK:
				return Integer.toString(KeyEvent.VK_D);
			case KEY_ROTATE:
				return Integer.toString(KeyEvent.VK_S);
			case KEY_RUN:
				return Integer.toString(KeyEvent.VK_SHIFT);
			case KEY_DIAGONAL:
				return Integer.toString(KeyEvent.VK_R);
			case KEY_MENU:
				return Integer.toString(KeyEvent.VK_ESCAPE);
			case KEY_MOVE_1:
				return Integer.toString(KeyEvent.VK_1);
			case KEY_MOVE_2:
				return Integer.toString(KeyEvent.VK_2);
			case KEY_MOVE_3:
				return Integer.toString(KeyEvent.VK_3);
			case KEY_MOVE_4:
				return Integer.toString(KeyEvent.VK_4);
			case KEY_ITEM_1:
				return Integer.toString(KeyEvent.VK_5);
			case KEY_ITEM_2:
				return Integer.toString(KeyEvent.VK_6);
			case KEY_INVENTORY:
				return Integer.toString(KeyEvent.VK_I);
			case KEY_MAP_UP:
				return Integer.toString(KeyEvent.VK_NUMPAD8);
			case KEY_MAP_DOWN:
				return Integer.toString(KeyEvent.VK_NUMPAD2);
			case KEY_MAP_LEFT:
				return Integer.toString(KeyEvent.VK_NUMPAD4);
			case KEY_MAP_RIGHT:
				return Integer.toString(KeyEvent.VK_NUMPAD6);
			case KEY_MAP_RESET:
				return Integer.toString(KeyEvent.VK_NUMPAD5);
			case KEY_PAGE_LEFT:
				return Integer.toString(KeyEvent.VK_PAGE_UP);
			case KEY_PAGE_RIGHT:
				return Integer.toString(KeyEvent.VK_PAGE_DOWN);
			case KEY_PARTY:
				return Integer.toString(KeyEvent.VK_P);
			case SERVER_ADDRESS:
				return "localhost:8080/PMDMMOServer/";
			case LOGIN:
				return "Unknown user";
			case HP_BARS:
				return "true";
			default:
				return null;
		}
	}

	public static boolean getBooleanSetting(String setting)
	{
		String value = getSetting(setting);
		return value != null && value.equals("true");
	}

	public static String getSetting(String setting)
	{
		if (!settings.containsKey(setting)) resetSetting(setting);
		return settings.getProperty(setting);
	}

	public static void load()
	{
		settings = new Properties();
		File f = new File("settings.properties");
		if (f.exists()) try
		{
			settings.load(new FileInputStream(f));
		} catch (FileNotFoundException e)
		{
			Logger.e("Could not find settings.properties : " + e);
		} catch (IOException e)
		{
			Logger.e("Could not open settings.properties : " + e);
		}
	}

	/** Resets the input setting to its default value. */
	public static void resetSetting(String setting)
	{
		settings.put(setting, defaultSetting(setting));
	}

	public static void save()
	{
		try
		{
			settings.store(new FileOutputStream(new File("settings.properties")), null);
		} catch (FileNotFoundException e)
		{
			Logger.e("Could not find settings.properties : " + e);
		} catch (IOException e)
		{
			Logger.e("Could not open settings.properties : " + e);
		}
	}

	public static void setSetting(String setting, String value)
	{
		settings.put(setting, value);
	}

}
