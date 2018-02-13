package com.darkxell.common.item;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;

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
		Logger.instance().debug("Loading Items...");

		Element root = XMLUtils.readFile(new File("resources/data/items.xml"));
		for (Element e : root.getChildren("item", root.getNamespace()))
			try
			{
				String className = e.getAttributeValue("type");
				Class<?> c = Class.forName(Item.class.getName() + className);
				Item item = (Item) c.getConstructor(Element.class).newInstance(e);
				items.put(item.id, item);
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
	}

	/** Saves this Registry for the Client. */
	public static void saveClient()
	{
		Element xml = new Element("items");
		for (Item item : items.values())
			xml.addContent(item.toXML());
		XMLUtils.saveFile(new File("resources/data/items.xml"), xml);
	}

}
