package com.darkxell.common.move;

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

/** Holds all Moves. */
public final class MoveRegistry
{

	private static HashMap<Integer, Move> moves = new HashMap<Integer, Move>();

	/** @return The Moves with the input ID. */
	public static Move find(int id)
	{
		return moves.get(id);
	}

	/** @return All Moves. */
	public static Collection<Move> list()
	{
		return moves.values();
	}

	/** Loads this Registry for the Client. */
	public static void loadClient()
	{
		System.out.println("Loading Moves...");

		File file = new File("resources/data/moves.xml");
		SAXBuilder builder = new SAXBuilder();
		try
		{
			Element root = builder.build(file).getRootElement();
			for (Element e : root.getChildren("move"))
			{
				String className = e.getAttributeValue("movetype");
				if (className == null) className = "";
				Class<?> c = Class.forName(Move.class.getName() + className);
				Move move = (Move) c.getConstructor(Element.class).newInstance(e);
				moves.put(move.id, move);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** Saves this Registry for the Client. */
	public static void saveClient()
	{
		Element xml = new Element("moves");
		for (Move move : moves.values())
			xml.addContent(move.toXML());
		try
		{
			new XMLOutputter(Format.getPrettyFormat()).output(new Document(xml), new FileOutputStream("resources/data/moves.xml"));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
