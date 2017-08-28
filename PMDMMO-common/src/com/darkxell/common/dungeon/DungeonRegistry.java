package com.darkxell.common.dungeon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

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
	public static void loadClient()
	{
		System.out.println("Loading Dungeons...");

		File file = new File("resources/data/dungeons.xml");
		SAXBuilder builder = new SAXBuilder();
		try
		{
			Element root = builder.build(file).getRootElement();
			for (Element e : root.getChildren("dungeon"))
			{
				Dungeon dungeon = new Dungeon(e);
				dungeons.put(dungeon.id, dungeon);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** Saves this Registry for the Client. */
	public static void saveClient()
	{
		Element xml = new Element("dungeons");
		for (Dungeon dungeon : dungeons.values())
			xml.addContent(dungeon.toXML());
		try
		{
			new XMLOutputter(Format.getPrettyFormat()).output(new Document(xml), new FileOutputStream("resources/data/dungeons.xml"));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
