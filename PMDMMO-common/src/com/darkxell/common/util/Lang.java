package com.darkxell.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Lang
{

	public static enum Language
	{
		ENGLISH("en", "English");

		public final String id;
		public final String name;

		private Language(String id, String name)
		{
			this.id = id;
			this.name = name;
		}
	}

	private static HashMap<String, String> dictionnary = new HashMap<String, String>();
	private static Language selected = Language.ENGLISH;

	public static Language getLanguage()
	{
		return selected;
	}

	public static void setLanguage(Language language)
	{
		if (language == selected) return;
		selected = language;
		updateTranslations();
	}

	public static String translate(String id)
	{
		if (dictionnary.containsKey(id)) return dictionnary.get(id);
		return id;
	}

	/** Reloads the translations after switching language. */
	private static void updateTranslations()
	{
		dictionnary.clear();
		Properties lang = new Properties();
		try
		{
			lang.load(new FileInputStream(new File("resources/lang/" + selected.id + ".properties")));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		for (String id : lang.stringPropertyNames())
			dictionnary.put(id, lang.getProperty(id));
	}

}
