package com.darkxell.common.dungeon;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.FloorData;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.util.Message;

/** Describes a Dungeon: floors, Pokémon, items... */
public class Dungeon
{
	// TODO make lists private when data is gathered.
	public static final boolean UP = false, DOWN = true;
	public static final String XML_ROOT = "dungeon";

	/** Whether this Dungeon goes up or down. See {@link Dungeon#UP} */
	public final boolean direction;
	/** The number of Floors in this Dungeon. */
	public final int floorCount;
	/** Describes this Dungeon's Floors' data. */
	public ArrayList<FloorData> floorData;
	/** This Dungeon's ID. */
	public final int id;
	/** Lists the Items found in this Dungeon. */
	public ArrayList<DungeonItem> items;
	/** The chance of a Room to be a Monster House in this Dungeon. */
	public final double monsterHouseChance;
	/** Lists the Pokémon found in this Dungeon. */
	public ArrayList<DungeonEncounter> pokemon;
	/** Number of Items the entering team is allowed to carry. -1 for no limit. */
	// public final int teamItems;
	/** Level the entering team is set to. -1 for no change. */
	// public final int teamLevel;
	/** Number of members the entering team is allowed to have. */
	// public final int teamMembers;
	/** Amount of Money the entering team is allowed to carry. -1 for no limit. */
	// public final int teamMoney;
	/** Lists the Traps found in this Dungeon. */
	public ArrayList<DungeonTrap> traps;

	public Dungeon(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.floorCount = Integer.parseInt(xml.getAttributeValue("floors"));
		this.direction = xml.getAttribute("down") != null;
		this.monsterHouseChance = xml.getAttribute("mhouse") == null ? 0 : Double.parseDouble(xml.getAttributeValue("mhouse"));
		/* this.teamItems = xml.getAttribute("t-items") == null ? -1 : Integer.parseInt(xml.getAttributeValue("t-items")); this.teamLevel = xml.getAttribute("t-level") == null ? -1 : Integer.parseInt(xml.getAttributeValue("t-level")); this.teamMembers = xml.getAttribute("t-members") == null ? 4 :
		 * Integer.parseInt(xml.getAttributeValue("t-members")); this.teamMoney = xml.getAttribute("t-money") == null ? -1 : Integer.parseInt(xml.getAttributeValue("t-money")); */

		this.pokemon = new ArrayList<DungeonEncounter>();
		for (Element pokemon : xml.getChild("encounters").getChildren(DungeonEncounter.XML_ROOT))
			this.pokemon.add(new DungeonEncounter(pokemon));

		this.items = new ArrayList<DungeonItem>();
		for (Element item : xml.getChild("items").getChildren(DungeonItem.XML_ROOT))
			this.items.add(new DungeonItem(item));

		this.traps = new ArrayList<DungeonTrap>();
		for (Element trap : xml.getChild("traps").getChildren(DungeonTrap.XML_ROOT))
			this.traps.add(new DungeonTrap(trap));

		this.floorData = new ArrayList<FloorData>();
		for (Element data : xml.getChild("data").getChildren(FloorData.XML_ROOT))
			this.floorData.add(new FloorData(data));
	}

	public Dungeon(int id, int floorCount, boolean direction, double monsterHouseChance, // int teamItems, int teamLevel, int teamMoney,int teamMembers,
			ArrayList<DungeonEncounter> pokemon, ArrayList<DungeonItem> items, ArrayList<DungeonTrap> traps, ArrayList<FloorData> floorData)
	{
		this.id = id;
		this.floorCount = floorCount;
		this.direction = direction;
		this.monsterHouseChance = monsterHouseChance;
		/* this.teamItems = teamItems; this.teamLevel = teamLevel; this.teamMembers = teamMembers; this.teamMoney = teamMoney; */
		this.pokemon = pokemon;
		this.items = items;
		this.traps = traps;
		this.floorData = floorData;
	}

	/** @return The Data of the input floor. */
	public FloorData getData(int floor)
	{
		for (FloorData data : this.floorData)
			if (data.floors.contains(floor)) return data;
		return this.floorData.get(0);
	}

	public boolean hasTraps()
	{
		return !this.traps.isEmpty();
	}

	/** @return This Dungeon's name. */
	public Message name()
	{
		return new Message("dungeon." + this.id);
	}

	/** @return A new instance of this Dungeon. */
	public DungeonInstance newInstance()
	{
		return new DungeonInstance(this.id, new Random());
	}

	/** @return A random Item for the input floor. */
	public ItemStack randomItem(Random random, int floor)
	{
		ArrayList<DungeonItem> candidates = new ArrayList<DungeonItem>();
		candidates.addAll(this.items);
		candidates.removeIf(new Predicate<DungeonItem>()
		{
			@Override
			public boolean test(DungeonItem i)
			{
				return !i.floors.contains(floor);
			}
		});
		return candidates.get(random.nextInt(candidates.size())).generate(random);
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("floors", Integer.toString(this.floorCount));
		if (this.direction) root.setAttribute("down", "true");
		if (this.monsterHouseChance != 0) root.setAttribute("mhouse", Double.toString(this.monsterHouseChance));
		/* if (this.teamItems != -1) root.setAttribute("t-items", Integer.toString(this.teamItems)); if (this.teamLevel != -1) root.setAttribute("t-level", Integer.toString(this.teamLevel)); if (this.teamMembers != 4) root.setAttribute("t-members", Integer.toString(this.teamMembers)); if
		 * (this.teamMoney != -1) root.setAttribute("t-money", Integer.toString(this.teamMoney)); */

		Element pokemon = new Element("encounters");
		for (DungeonEncounter poke : this.pokemon)
			pokemon.addContent(poke.toXML());
		root.addContent(pokemon);

		Element items = new Element("items");
		for (DungeonItem item : this.items)
			items.addContent(item.toXML());
		root.addContent(items);

		Element traps = new Element("traps");
		for (DungeonTrap trap : this.traps)
			traps.addContent(trap.toXML());
		root.addContent(traps);

		Element data = new Element("data");
		for (FloorData d : this.floorData)
			data.addContent(d.toXML());
		root.addContent(data);

		return root;
	}

}
