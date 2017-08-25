package com.darkxell.common.dungeon;

import java.util.ArrayList;

import org.jdom2.Element;

/** Describes a Dungeon: floors, Pokémon, items... */
public class Dungeon
{
	public static final String XML_ROOT = "dungeon";

	/** Lists the Items found in this Dungeon. */
	private ArrayList<DungeonItem> buriedItems;
	/** Lists this Dungeon's floors that are not random. */
	private ArrayList<Integer> cutsceneFloors;
	/** The number of Floors in this Dungeon. */
	public final int floorCount;
	/** This Dungeon's ID. */
	public final int id;
	/** Lists the Items found in this Dungeon. */
	private ArrayList<DungeonItem> items;
	/** Lists the Items found in this Dungeon. */
	private ArrayList<DungeonItem> monsterHouseItems;
	/** Lists the Pokémon found in this Dungeon. */
	private ArrayList<DungeonPokemon> pokemon;
	/** Lists the Traps found in this Dungeon. */
	private ArrayList<DungeonTrap> traps;

	public Dungeon(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.floorCount = Integer.parseInt(xml.getAttributeValue("floors"));

		this.cutsceneFloors = new ArrayList<Integer>();
		String value = xml.getChildText("cutscene-floors");
		if (value != null) for (String floor : value.split(","))
			this.cutsceneFloors.add(Integer.parseInt(floor));

		this.pokemon = new ArrayList<DungeonPokemon>();
		for (Element pokemon : xml.getChild("encounters").getChildren(DungeonPokemon.XML_ROOT))
			this.pokemon.add(new DungeonPokemon(pokemon));

		this.items = new ArrayList<DungeonItem>();
		for (Element item : xml.getChild("items").getChildren(DungeonItem.XML_ROOT))
			this.items.add(new DungeonItem(item));

		this.monsterHouseItems = new ArrayList<DungeonItem>();
		for (Element item : xml.getChild("items-mhouse").getChildren(DungeonItem.XML_ROOT))
			this.monsterHouseItems.add(new DungeonItem(item));

		this.buriedItems = new ArrayList<DungeonItem>();
		for (Element item : xml.getChild("items-buried").getChildren(DungeonItem.XML_ROOT))
			this.buriedItems.add(new DungeonItem(item));

		this.traps = new ArrayList<DungeonTrap>();
		for (Element trap : xml.getChild("traps").getChildren(DungeonTrap.XML_ROOT))
			this.traps.add(new DungeonTrap(trap));
	}

	public Dungeon(int id, int floorCount, ArrayList<Integer> cutsceneFloors, ArrayList<DungeonPokemon> pokemon, ArrayList<DungeonItem> items,
			ArrayList<DungeonItem> monsterHouseItems, ArrayList<DungeonItem> buriedItems, ArrayList<DungeonTrap> traps)
	{
		this.id = id;
		this.floorCount = floorCount;
		this.cutsceneFloors = cutsceneFloors;
		this.pokemon = pokemon;
		this.items = items;
		this.monsterHouseItems = monsterHouseItems;
		this.buriedItems = buriedItems;
		this.traps = traps;
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("floors", Integer.toString(this.floorCount));

		if (this.cutsceneFloors.size() != 0)
		{
			String floors = "";
			for (Integer floor : this.cutsceneFloors)
				if (floors.equals("")) floors += floor;
				else floors += "," + floor;
			root.addContent(new Element("cutscene-floors").setText(floors));
		}

		Element pokemon = new Element("encounters");
		for (DungeonPokemon poke : this.pokemon)
			pokemon.addContent(poke.toXML());
		root.addContent(pokemon);

		Element items = new Element("items");
		for (DungeonItem item : this.items)
			items.addContent(item.toXML());
		root.addContent(items);

		Element mhouse = new Element("items-mhouse");
		for (DungeonItem item : this.monsterHouseItems)
			mhouse.addContent(item.toXML());
		root.addContent(mhouse);

		Element buried = new Element("items-buried");
		for (DungeonItem item : this.buriedItems)
			buried.addContent(item.toXML());
		root.addContent(buried);

		Element traps = new Element("traps");
		for (DungeonTrap trap : this.traps)
			traps.addContent(trap.toXML());
		root.addContent(traps);

		return root;
	}

}
