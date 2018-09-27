package com.darkxell.common.item;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.common.item.Item.ItemCategory;
import com.darkxell.common.item.effects.TeachedMoveItemEffect;
import com.darkxell.common.item.effects.TeachesMoveItemEffect;
import com.darkxell.common.item.effects.TeachesMoveRenewableItemEffect;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;

/** Holds all Items. */
public final class ItemRegistry
{

	private static HashMap<Integer, Item> items = new HashMap<Integer, Item>();

	/** @return The Item with the input ID. */
	public static Item find(int id)
	{
		if (!items.containsKey(id)) Logger.e("Item ID " + id + " doesnt exist!");
		return items.get(id);
	}

	/** @return All Items. */
	public static ArrayList<Item> list()
	{
		ArrayList<Item> list = new ArrayList<>(items.values());
		list.sort(Comparator.naturalOrder());
		return list;
	}

	/** Loads this Registry for the Client. */
	public static void load()
	{
		Logger.instance().debug("Loading Items...");

		Element root = XMLUtils.read(ItemRegistry.class.getResourceAsStream("/data/items.xml"));
		for (Element e : root.getChildren())
		{
			Item item = new Item(e);
			register(item);

			String extra = XMLUtils.getAttribute(e, "extra", null);

			if (extra != null)
			{
				int effect = item.effectID;
				String[] data = extra.split(":");
				if (data[0].equals("tm"))
				{
					new TeachesMoveItemEffect(effect, Integer.parseInt(data[1]));
					new TeachedMoveItemEffect(-effect, Integer.parseInt(data[1]));

					Item used = new Item(-1 * item.id, ItemCategory.OTHERS, 0, 1, -effect, 95, false, false);
					register(used);

				} else if (data[0].equals("hm")) new TeachesMoveRenewableItemEffect(effect, Integer.parseInt(data[1]));
			}

		}
	}

	public static void register(Item item)
	{
		items.put(item.id, item);
	}

	public static void save(File file)
	{
		Element xml = new Element("items");
		for (Item item : items.values())
			xml.addContent(item.toXML());
		XMLUtils.saveFile(file, xml);
	}

	/** Saves this Registry for the Client. */
	public static void saveClient()
	{
		save(new File("resources/data/items.xml"));
	}

	public static void unregister(int id)
	{
		items.remove(id);
	}

}
