package com.darkxell.common.item;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/** Holds all Items. */
public final class ItemRegistry
{

	private static HashMap<Integer, Item> items = new HashMap<Integer, Item>();

	/** @return The Item with the input ID. */
	public static Item find(int id)
	{
		return items.get(id);
	}

	/** @return All Items. */
	public static Collection<Item> list()
	{
		return items.values();
	}

	/** Loads this Registry for the Client. */
	public static void loadClient()
	{
		System.out.println("Loading Items...");

		File file = new File("resources/data/items.xml");
		SAXBuilder builder = new SAXBuilder();
		try
		{
			Element root = builder.build(file).getRootElement();
			for (Element e : root.getChildren("item"))
			{
				String className = e.getAttributeValue("type");
				Class<?> c = Class.forName("com.darkxell.common.item.Item" + className);
				Item item = (Item) c.getConstructor(new Class[0]).newInstance(e);
				items.put(item.id, item);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
