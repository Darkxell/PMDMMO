package com.darkxell.common.dungeon;

import org.jdom2.Element;

import com.darkxell.common.util.XMLUtils;

/** Describes how a Traps appear in a Dungeon. */
public class DungeonTrap
{
	public static final String XML_ROOT = "trap";

	/** The weight of each Trap. Represents how likely it is to appear compared to other traps on this floor. */
	public final int[] chances;
	/** The floors this Trap can appear on. */
	public final FloorSet floors;
	/** The Traps IDs. */
	public final int[] ids;

	public DungeonTrap(Element xml)
	{
		this.ids = XMLUtils.readIntArray(xml.getChild("ids"));
		if (xml.getChild("chances") == null)
		{
			this.chances = new int[this.ids.length];
			for (int i = 0; i < this.chances.length; ++i)
				this.chances[i] = 1;
		} else this.chances = XMLUtils.readIntArray(xml.getChild("chances"));
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT));
	}

	public DungeonTrap(int[] ids, int[] chances, FloorSet floors)
	{
		this.ids = ids;
		this.chances = chances;
		this.floors = floors;
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.addContent(XMLUtils.toXML("ids", this.ids));
		root.addContent(this.floors.toXML());

		boolean chances = false;
		for (int c : this.chances)
			if (c != 1)
			{
				chances = true;
				break;
			}
		if (chances) root.addContent(XMLUtils.toXML("chances", this.chances));

		return root;
	}

}
