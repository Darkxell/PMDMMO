package com.darkxell.common.dungeon;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.util.Message;

/** Describes a Dungeon: floors, Pokémon, items... */
public class Dungeon
{
	public static final boolean UP = false, DOWN = true;
	public static final String XML_ROOT = "dungeon";

	/** Lists the Items found in this Dungeon. */
	private ArrayList<DungeonItem> buriedItems;
	/** Lists this Dungeon's floors that are not random. */
	private ArrayList<Integer> cutsceneFloors;
	/** Whether this Dungeon goes up or down. See {@link Dungeon#UP} */
	public final boolean direction;
	/** The number of Floors in this Dungeon. */
	public final int floorCount;
	/** True if this Dungeon has Monster Houses. */
	public final boolean hasMonsterHouses;
	/** True if this Dungeon has Traps. */
	public final boolean hasTraps;
	/** This Dungeon's ID. */
	public final int id;
	/** Lists the Items found in this Dungeon. */
	private ArrayList<DungeonItem> items;
	/** Lists the Items found in this Dungeon. */
	private ArrayList<DungeonItem> monsterHouseItems;
	/** Lists the Pokémon found in this Dungeon. */
	private ArrayList<DungeonPokemon> pokemon;
	/** Number of Items the entering team is allowed to carry. -1 for no limit. */
	public final int teamItems;
	/** Level the entering team is set to. -1 for no change. */
	public final int teamLevel;
	/** Number of members the entering team is allowed to have. */
	public final int teamMembers;
	/** Amount of Money the entering team is allowed to carry. -1 for no limit. */
	public final int teamMoney;
	/** Lists the Traps found in this Dungeon. */
	private ArrayList<DungeonTrap> traps;

	public Dungeon(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.floorCount = Integer.parseInt(xml.getAttributeValue("floors"));
		this.direction = xml.getAttribute("down") != null;
		this.hasMonsterHouses = xml.getAttribute("mhouse") != null;
		this.hasTraps = xml.getAttribute("traps") != null;
		this.teamItems = xml.getAttribute("t-items") == null ? -1 : Integer.parseInt(xml.getAttributeValue("t-items"));
		this.teamLevel = xml.getAttribute("t-level") == null ? -1 : Integer.parseInt(xml.getAttributeValue("t-level"));
		this.teamMembers = xml.getAttribute("t-members") == null ? 4 : Integer.parseInt(xml.getAttributeValue("t-members"));
		this.teamMoney = xml.getAttribute("t-money") == null ? -1 : Integer.parseInt(xml.getAttributeValue("t-money"));

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

	public Dungeon(int id, int floorCount, boolean direction, boolean hasMonsterHouses, boolean hasTraps, int teamItems, int teamLevel, int teamMoney,
			int teamMembers, ArrayList<Integer> cutsceneFloors, ArrayList<DungeonPokemon> pokemon, ArrayList<DungeonItem> items,
			ArrayList<DungeonItem> monsterHouseItems, ArrayList<DungeonItem> buriedItems, ArrayList<DungeonTrap> traps)
	{
		this.id = id;
		this.floorCount = floorCount;
		this.direction = direction;
		this.hasMonsterHouses = hasMonsterHouses;
		this.hasTraps = hasTraps;
		this.teamItems = teamItems;
		this.teamLevel = teamLevel;
		this.teamMembers = teamMembers;
		this.teamMoney = teamMoney;
		this.cutsceneFloors = cutsceneFloors;
		this.pokemon = pokemon;
		this.items = items;
		this.monsterHouseItems = monsterHouseItems;
		this.buriedItems = buriedItems;
		this.traps = traps;
	}

	/** @return This Dungeon's name. */
	public Message name()
	{
		return new Message("dungeon." + this.id);
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("floors", Integer.toString(this.floorCount));
		if (this.direction) root.setAttribute("down", "true");
		if (this.hasMonsterHouses) root.setAttribute("mhouse", "true");
		if (this.hasTraps) root.setAttribute("traps", "true");
		if (this.teamItems != -1) root.setAttribute("t-items", Integer.toString(this.teamItems));
		if (this.teamLevel != -1) root.setAttribute("t-level", Integer.toString(this.teamLevel));
		if (this.teamMembers != 4) root.setAttribute("t-members", Integer.toString(this.teamMembers));
		if (this.teamMoney != -1) root.setAttribute("t-money", Integer.toString(this.teamMoney));

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
