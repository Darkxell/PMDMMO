package com.darkxell.common.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Predicate;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.util.Message;

/** Describes a Dungeon: floors, Pokémon, items... */
public class Dungeon
{
	public static final boolean UP = false, DOWN = true;
	public static final String XML_ROOT = "dungeon";

	/** Lists the Items found in this Dungeon. */
	private ArrayList<DungeonItem> buriedItems;
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
	/** Lists this Dungeon's Floors' layouts. */
	private HashMap<Integer, FloorSet> layouts;
	/** Lists the Items found in this Dungeon. */
	private ArrayList<DungeonItem> monsterHouseItems;
	/** Lists the Pokémon found in this Dungeon. */
	private ArrayList<DungeonEncounter> pokemon;
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

		this.pokemon = new ArrayList<DungeonEncounter>();
		for (Element pokemon : xml.getChild("encounters").getChildren(DungeonEncounter.XML_ROOT))
			this.pokemon.add(new DungeonEncounter(pokemon));

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

		this.layouts = new HashMap<Integer, FloorSet>();
		for (Element layout : xml.getChild("layouts").getChildren("layout"))
			this.layouts.put(Integer.parseInt(layout.getAttributeValue("id")), new FloorSet(layout.getChild(FloorSet.XML_ROOT)));
	}

	public Dungeon(int id, int floorCount, boolean direction, boolean hasMonsterHouses, boolean hasTraps, int teamItems, int teamLevel, int teamMoney,
			int teamMembers, ArrayList<DungeonEncounter> pokemon, ArrayList<DungeonItem> items, ArrayList<DungeonItem> monsterHouseItems,
			ArrayList<DungeonItem> buriedItems, ArrayList<DungeonTrap> traps, HashMap<Integer, FloorSet> layouts)
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
		this.pokemon = pokemon;
		this.items = items;
		this.monsterHouseItems = monsterHouseItems;
		this.buriedItems = buriedItems;
		this.traps = traps;
		this.layouts = layouts;
	}

	/** @return The Layout to use for the input floor. */
	public Layout getLayout(int floor)
	{
		for (Integer layout : this.layouts.keySet())
			if (this.layouts.get(layout).contains(floor)) return Layout.find(layout);
		return null;
	}

	/** @return This Dungeon's name. */
	public Message name()
	{
		return new Message("dungeon." + this.id);
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
		if (this.hasMonsterHouses) root.setAttribute("mhouse", "true");
		if (this.hasTraps) root.setAttribute("traps", "true");
		if (this.teamItems != -1) root.setAttribute("t-items", Integer.toString(this.teamItems));
		if (this.teamLevel != -1) root.setAttribute("t-level", Integer.toString(this.teamLevel));
		if (this.teamMembers != 4) root.setAttribute("t-members", Integer.toString(this.teamMembers));
		if (this.teamMoney != -1) root.setAttribute("t-money", Integer.toString(this.teamMoney));

		Element pokemon = new Element("encounters");
		for (DungeonEncounter poke : this.pokemon)
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

		Element layouts = new Element("layouts");
		for (Integer layout : this.layouts.keySet())
			layouts.addContent(new Element("layout").setAttribute("id", Integer.toString(layout)).addContent(this.layouts.get(layout).toXML()));
		root.addContent(layouts);

		return root;
	}

}
