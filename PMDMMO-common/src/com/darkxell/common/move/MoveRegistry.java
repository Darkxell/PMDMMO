package com.darkxell.common.move;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;

/** Holds all Moves. */
public final class MoveRegistry
{
	public static Move ATTACK;

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
	public static void load()
	{
		Logger.instance().debug("Loading Moves...");

		Element root = XMLUtils.read(MoveRegistry.class.getResourceAsStream("/data/moves.xml"));
		for (Element e : root.getChildren())
		{
			Move move = new Move(e);
			moves.put(move.id, move);
		}

		ATTACK = find(0);
	}

	/** Saves this Registry for the Client. */
	public static void saveClient()
	{
		Element xml = new Element("moves");
		for (Move move : moves.values())
			if (move != ATTACK) xml.addContent(move.toXML());
		XMLUtils.saveFile(new File("resources/data/moves.xml"), xml);
	}

}
