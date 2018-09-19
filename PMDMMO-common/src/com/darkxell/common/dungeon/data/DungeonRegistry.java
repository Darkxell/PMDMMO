package com.darkxell.common.dungeon.data;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;

/** Holds all Dungeons. */
public final class DungeonRegistry
{

	private static HashMap<Integer, Dungeon> dungeons = new HashMap<Integer, Dungeon>();

	/** @return The Dungeon with the input ID. */
	public static Dungeon find(int id)
	{
		return dungeons.get(id);
	}

	/** @return All Dungeons. */
	public static Collection<Dungeon> list()
	{
		return dungeons.values();
	}

	/** Loads this Registry for the Client. */
	public static void load()
	{
		Logger.i("Loading Dungeons...");

		Element root = XMLUtils.read(PokemonRegistry.class.getResourceAsStream("/data/dungeons.xml"));
		for (Element e : root.getChildren("dungeon", root.getNamespace()))
		{
			Dungeon dungeon = new Dungeon(e);
			dungeons.put(dungeon.id, dungeon);
		}
	}

	/** Saves this Registry for the Client. */
	public static void saveClient()
	{
		Element xml = new Element("dungeons");
		for (Dungeon dungeon : dungeons.values())
			xml.addContent(dungeon.toXML());
		XMLUtils.saveFile(new File("resources/data/dungeons.xml"), xml);
	}

}
