package com.darkxell.common.dungeon.data;

import com.darkxell.common.Registries;
import com.darkxell.common.trap.Trap;
import com.darkxell.common.util.XMLUtils;
import org.jdom2.Element;

/** Describes how a Traps appear in a Dungeon. */
public class DungeonTrapGroup
{
	public static final String XML_ROOT = "trap";

	/** The weight of each Trap. Represents how likely it is to appear compared to other traps on this floor. */
	public final int[] chances;
	/** The floors this Trap can appear on. */
	public final FloorSet floors;
	/** The Traps IDs. */
	public final int[] ids;

	public DungeonTrapGroup(Element xml)
	{
		this.ids = XMLUtils.readIntArray(xml.getChild("ids", xml.getNamespace()));
		if (xml.getChild("chances", xml.getNamespace()) == null)
		{
			this.chances = new int[this.ids.length];
			for (int i = 0; i < this.chances.length; ++i)
				this.chances[i] = 1;
		} else this.chances = XMLUtils.readIntArray(xml.getChild("chances", xml.getNamespace()));
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT, xml.getNamespace()));
	}

	public DungeonTrapGroup(int[] ids, int[] chances, FloorSet floors)
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

	public Trap[] traps()
	{
		Trap[] traps = new Trap[this.ids.length];
		for (int i = 0; i < traps.length; ++i)
			traps[i] = Registries.traps().find(this.ids[i]);
		return traps;
	}

}
