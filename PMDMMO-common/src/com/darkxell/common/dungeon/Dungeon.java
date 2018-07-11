package com.darkxell.common.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Predicate;

import org.jdom2.Element;

import com.darkxell.common.dungeon.floor.FloorData;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.Weather;

/** Describes a Dungeon: floors, Pok�mon, items... */
public class Dungeon implements Comparable<Dungeon>
{
	public static final boolean UP = false, DOWN = true;
	public static final String XML_ROOT = "dungeon";

	/** Lists the buried Items found in this Dungeon. If empty, use the standard list. */
	private final ArrayList<DungeonItem> buriedItems;
	/** Whether this Dungeon goes up or down. See {@link Dungeon#UP} */
	public final boolean direction;
	/** The number of Floors in this Dungeon. */
	public final int floorCount;
	/** Describes this Dungeon's Floors' data. */
	private final ArrayList<FloorData> floorData;
	/** This Dungeon's ID. */
	public final int id;
	/** Lists the Items found in this Dungeon. */
	private final ArrayList<DungeonItem> items;
	/** ID of the Dungeon this Dungeon leads to (e.g. Mt. Blaze to Mt. Blaze Peak). -1 if no leading Dungeon. */
	public final int linkedTo;
	/** The x position in pixels of the dungeon on the dungeons world map. */
	public final int mapx;
	/** The y position in pixels of the dungeon on the dungeons world map. */
	public final int mapy;
	/** Lists the Pok�mon found in this Dungeon. */
	private final ArrayList<DungeonEncounter> pokemon;
	/** True if Pok�mon from this Dungeon can be recruited. */
	public final boolean recruitsAllowed;
	/** Lists the Items found in this Dungeon's shops. */
	private final ArrayList<DungeonItem> shopItems;
	/** The chance of an Item being Sticky in this Dungeon. */
	public final int stickyChance;
	/** The number of turns to spend on a single floor before being kicked. */
	public final int timeLimit;
	/** Lists the Traps found in this Dungeon. */
	private final ArrayList<DungeonTrap> traps;
	/** The Weather in this Dungeon (Weather ID -> floors). */
	private final HashMap<Integer, FloorSet> weather;

	public Dungeon(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.floorCount = Integer.parseInt(xml.getAttributeValue("floors"));
		this.direction = XMLUtils.getAttribute(xml, "down", false);
		this.recruitsAllowed = XMLUtils.getAttribute(xml, "recruits", true);
		this.timeLimit = XMLUtils.getAttribute(xml, "limit", 2000);
		this.linkedTo = XMLUtils.getAttribute(xml, "linked", -1);
		this.stickyChance = XMLUtils.getAttribute(xml, "sticky", 0);
		this.mapx = XMLUtils.getAttribute(xml, "mapx", -260) + 260;
		this.mapy = XMLUtils.getAttribute(xml, "mapy", -140) + 140;

		this.pokemon = new ArrayList<DungeonEncounter>();
		for (Element pokemon : xml.getChild("encounters", xml.getNamespace()).getChildren(DungeonEncounter.XML_ROOT, xml.getNamespace()))
			this.pokemon.add(new DungeonEncounter(pokemon));

		this.items = new ArrayList<DungeonItem>();
		for (Element item : xml.getChild("items", xml.getNamespace()).getChildren(DungeonItem.XML_ROOT, xml.getNamespace()))
			this.items.add(new DungeonItem(item));

		this.shopItems = new ArrayList<DungeonItem>();
		if (xml.getChild("shops", xml.getNamespace()) != null)
			for (Element item : xml.getChild("shops", xml.getNamespace()).getChildren(DungeonItem.XML_ROOT, xml.getNamespace()))
			this.shopItems.add(new DungeonItem(item));

		this.buriedItems = new ArrayList<DungeonItem>();
		if (xml.getChild("buried", xml.getNamespace()) != null)
			for (Element item : xml.getChild("buried", xml.getNamespace()).getChildren(DungeonItem.XML_ROOT, xml.getNamespace()))
			this.buriedItems.add(new DungeonItem(item));

		this.traps = new ArrayList<DungeonTrap>();
		if (xml.getChild("traps", xml.getNamespace()) == null)
		{
			this.traps.add(new DungeonTrap(new int[] { TrapRegistry.WONDER_TILE.id }, new int[] { 100 }, new FloorSet(1, this.floorCount)));
		} else
		{
			for (Element trap : xml.getChild("traps", xml.getNamespace()).getChildren(DungeonTrap.XML_ROOT, xml.getNamespace()))
				this.traps.add(new DungeonTrap(trap));
		}

		this.floorData = new ArrayList<FloorData>();
		for (Element data : xml.getChild("data", xml.getNamespace()).getChildren(FloorData.XML_ROOT, xml.getNamespace()))
		{
			FloorData d = null;
			if (this.floorData.isEmpty()) d = new FloorData(data);
			else
			{
				d = this.floorData.get(this.floorData.size() - 1).copy();
				d.load(data);
			}
			this.floorData.add(d);
		}

		this.weather = new HashMap<Integer, FloorSet>();
		if (xml.getChild("weather", xml.getNamespace()) != null)
			for (Element data : xml.getChild("weather", xml.getNamespace()).getChildren("w", xml.getNamespace()))
			this.weather.put(Integer.parseInt(data.getAttributeValue("id")), new FloorSet(data.getChild(FloorSet.XML_ROOT, xml.getNamespace())));
	}

	public Dungeon(int id, int floorCount, boolean direction, double monsterHouseChance, boolean recruits, int timeLimit, int stickyChance, int linkedTo,
			ArrayList<DungeonEncounter> pokemon, ArrayList<DungeonItem> items, ArrayList<DungeonItem> shopItems, ArrayList<DungeonItem> buriedItems,
			ArrayList<DungeonTrap> traps, ArrayList<FloorData> floorData, HashMap<Integer, FloorSet> weather, int mapx, int mapy)
	{
		this.id = id;
		this.floorCount = floorCount;
		this.direction = direction;
		this.recruitsAllowed = recruits;
		this.timeLimit = timeLimit;
		this.stickyChance = stickyChance;
		this.linkedTo = linkedTo;
		this.pokemon = pokemon;
		this.items = items;
		this.shopItems = shopItems;
		this.buriedItems = buriedItems;
		this.traps = traps;
		this.floorData = floorData;
		this.weather = weather;
		this.mapx = mapx;
		this.mapy = mapy;
	}

	/** @return The buried items found on the input floor. Keys are Item IDs, Values are Item weights. */
	public ArrayList<DungeonItem> buriedItems(int floor)
	{
		ArrayList<DungeonItem> items = new ArrayList<>();
		for (DungeonItem item : this.buriedItems)
			if (item.floors.contains(floor)) for (int i = 0; i < item.items.length; ++i)
				items.add(item);
		return items;
	}

	@Override
	public int compareTo(Dungeon o)
	{
		return Integer.compare(this.id, o.id);
	}

	/** @return The Data of the input floor. */
	public FloorData getData(int floor)
	{
		for (FloorData data : this.floorData)
			if (data.floors().contains(floor)) return data;
		return this.floorData.get(0);
	}

	/** @return The items found on the input floor. Keys are Item IDs, Values are Item weights. */
	public ArrayList<DungeonItem> items(int floor)
	{
		ArrayList<DungeonItem> items = new ArrayList<>();
		for (DungeonItem item : this.items)
			if (item.floors.contains(floor)) for (int i = 0; i < item.items.length; ++i)
				items.add(item);
		return items;
	}

	/** @return This Dungeon's name. */
	public Message name()
	{
		return new Message("dungeon." + this.id);
	}

	/** @return A new instance of this Dungeon. */
	public DungeonInstance newInstance(long seed)
	{
		return new DungeonInstance(this.id, seed);
	}

	public DungeonEncounter randomEncounter(Random random, int floor)
	{
		ArrayList<DungeonEncounter> candidates = new ArrayList<DungeonEncounter>(this.pokemon);
		candidates.removeIf((DungeonEncounter candidate) -> {
			return !candidate.floors.contains(floor);
		});
		return RandomUtil.random(candidates, random);
	}

	/** @return A random Item for the input floor. */
	public ItemStack randomItem(Random random, int floor)
	{
		ArrayList<DungeonItem> candidates = new ArrayList<DungeonItem>();
		candidates.addAll(this.items);
		candidates.removeIf(new Predicate<DungeonItem>() {
			@Override
			public boolean test(DungeonItem i)
			{
				return !i.floors.contains(floor);
			}
		});
		return candidates.get(random.nextInt(candidates.size())).generate(random);
	}

	@Override
	public String toString()
	{
		return this.id + "- " + this.name().toString();
	}

	public Element toXML()
	{
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("floors", Integer.toString(this.floorCount));
		root.setAttribute("mapx", Integer.toString(this.mapx));
		root.setAttribute("mapy", Integer.toString(this.mapy));
		XMLUtils.setAttribute(root, "down", this.direction, false);
		XMLUtils.setAttribute(root, "recruits", this.recruitsAllowed, true);
		XMLUtils.setAttribute(root, "limit", this.timeLimit, 2000);
		XMLUtils.setAttribute(root, "sticky", this.stickyChance, 0);
		XMLUtils.setAttribute(root, "linked", this.linkedTo, -1);

		Element pokemon = new Element("encounters");
		for (DungeonEncounter poke : this.pokemon)
			pokemon.addContent(poke.toXML());
		root.addContent(pokemon);

		Element items = new Element("items");
		for (DungeonItem item : this.items)
			items.addContent(item.toXML());
		root.addContent(items);

		if (!this.shopItems.isEmpty())
		{
			Element shops = new Element("shops");
			for (DungeonItem item : this.shopItems)
				shops.addContent(item.toXML());
			root.addContent(shops);
		}

		if (!this.buriedItems.isEmpty())
		{
			Element buried = new Element("buried");
			for (DungeonItem item : this.buriedItems)
				buried.addContent(item.toXML());
			root.addContent(buried);
		}

		if (!this.traps.isEmpty() && !(this.traps.size() == 1 && this.traps.get(0).ids.length == 1 && this.traps.get(0).ids[0] == TrapRegistry.WONDER_TILE.id))
		{
			Element traps = new Element("traps");
			for (DungeonTrap trap : this.traps)
				traps.addContent(trap.toXML());
			root.addContent(traps);
		}

		Element data = new Element("data");
		for (FloorData d : this.floorData)
			data.addContent(d.toXML(this.floorData.indexOf(d) == 0 ? null : this.floorData.get(this.floorData.indexOf(d) - 1)));
		root.addContent(data);

		if (!this.weather.isEmpty())
		{
			Element weather = new Element("weather");
			for (Integer id : this.weather.keySet())
				weather.addContent(new Element("w").setAttribute("id", Integer.toString(id)).addContent(this.weather.get(id).toXML()));
			root.addContent(weather);
		}

		return root;
	}

	/** @return The traps found on the input floor. First are Trap IDs, second are Trap weights. */
	public Pair<ArrayList<Integer>, ArrayList<Integer>> traps(int floor)
	{
		ArrayList<Integer> traps = new ArrayList<>(), weights = new ArrayList<>();
		for (DungeonTrap t : this.traps)
			if (t.floors.contains(floor)) for (int i = 0; i < t.ids.length; ++i)
			{
				traps.add(t.ids[i]);
				weights.add(t.chances[i]);
			}
		return new Pair<>(traps, weights);
	}

	/** @param floor - A Floor ID.
	 * @param random - A Random Number Generator.
	 * @return The prevailing Weather for the input Floor. */
	public Weather weather(int floor, Random random)
	{
		Weather w = Weather.CLEAR;

		for (Integer id : this.weather.keySet())
			if (this.weather.get(id).contains(floor))
			{
				w = Weather.find(id);
				if (w == null) w = Weather.CLEAR;
				else return w;
			}

		return w;
	}
}
