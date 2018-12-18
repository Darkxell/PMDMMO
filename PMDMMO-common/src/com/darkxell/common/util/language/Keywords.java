package com.darkxell.common.util.language;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Properties;

import com.darkxell.common.util.Logger;

/** Manages Keywords for moves/items descriptions. */
public class Keywords
{

	/** Comparator for Keywords. */
	public static final Comparator<String> comparator = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2)
		{
			return o1.toLowerCase().compareTo(o2.toLowerCase());
		}
	};

	/** Matches a Keyword's possible values with its ID. */
	private static final HashMap<String, String> dictionnary = new HashMap<String, String>();

	/** @param text - A Text to analyze.
	 * @return The List of Keyword values found in the input Text. */
	public static ArrayList<String> findKeywords(String text)
	{
		// Removing non-word components
		text = text.toLowerCase();
		text = text.replaceAll("( [0-9]* )|(<.*?>)", "");
		text = text.replaceAll("(?!-)\\p{Punct}", "");
		while (text.contains("  "))
			text = text.replaceAll("  ", " ");

		// Finding Keywords
		ArrayList<String> keywords = new ArrayList<String>();
		for (String keyword : dictionnary.keySet())
			if (text.contains(keyword.toLowerCase())) keywords.add(keyword);

		return keywords;
	}

	/** @return The Keyword matching the input Keyword value. null if matches no Keyword. */
	public static final String getKeyword(String keywordValue)
	{
		return dictionnary.get(keywordValue);
	}

	/** @return The Keyword matching the input Keyword value. null if matches no Keyword. */
	public static final ArrayList<String> getKeywords(Collection<String> keywordValues)
	{
		ArrayList<String> keywords = new ArrayList<String>();
		for (String value : keywordValues)
			if (isKeyword(value)) keywords.add(getKeyword(value));
		keywords.sort(comparator);
		return keywords;
	}

	/** @return True if the input value is a Keyword. */
	public static boolean isKeyword(String value)
	{
		return dictionnary.containsKey(value);
	}

	/** Reloads the keywords after switching language. */
	public static void updateKeywords()
	{
		dictionnary.clear();
		try
		{
			Properties data = new Properties();
			InputStream stream = Localization.class.getResourceAsStream("/lang/" + Localization.getLanguage().id + "_keywords.properties");
			if (stream == null) return;
			data.load(stream);

			for (Object keyword : data.keySet())
			{
				String key = (String) keyword;
				try
				{
					String[] values = ((String) data.getProperty(key)).split(",");
					for (String value : values)
						dictionnary.put(value, key);
				} catch (Exception e)
				{
					Logger.e("Keywords.updateKeywords(): Wrong format for keyword: " + key);
				}
			}

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
