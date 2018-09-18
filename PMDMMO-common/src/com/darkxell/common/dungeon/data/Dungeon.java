package com.darkxell.common.dungeon.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Predicate;

import org.jdom2.Element;

import com.darkxell.common.dungeon.DungeonExploration;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemEffects;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;
import com.darkxell.common.weather.Weather;

/** Describes a Dungeon: floors, Pokemon, items... */
public class Dungeon implements Comparable<Dungeon>
{

	public static enum DungeonDirection
	{
		DOWN,
		UP;

		public static DungeonDirection find(boolean value)
		{
			if (value) return DOWN;
			return UP;
		}

		public boolean value()
		{
			return this == DOWN;
		}
	}

	public static final String XML_ROOT = "dungeon";

	/** Lists the buried Items found in this Dungeon. If empty, use the standard list. */
	private final ArrayList<DungeonItemGroup> buriedItems;
	/** Whether this Dungeon goes up or down. See {@link DungeonDirection} */
	public final DungeonDirection direction;
	/** The number of Floors in this Dungeon. */
	public final int floorCount;
	/** Describes this Dungeon's Floors' data. */
	private final ArrayList<FloorData> floorData;
	/** This Dungeon's ID. */
	public final int id;
	/** Lists the Items found in this Dungeon. */
	private final ArrayList<DungeonItemGroup> items;
	/** ID of the Dungeon this Dungeon leads to (e.g. Mt. Blaze to Mt. Blaze Peak). -1 if no leading Dungeon. */
	public final int linkedTo;
	/** The x position in pixels of the dungeon on the dungeons world map. */
	public final int mapx;
	/** The y position in pixels of the dungeon on the dungeons world map. */
	public final int mapy;
	/** Lists the Pokemon found in this Dungeon. */
	private final ArrayList<DungeonEncounter> pokemon;
	/** True if Pokemon from this Dungeon can be recruited. */
	public final boolean recruitsAllowed;
	/** Lists the Items found in this Dungeon's shops. */
	private final ArrayList<DungeonItemGroup> shopItems;
	/** The chance of an Item being Sticky in this Dungeon. */
	public final int stickyChance;
	/** The number of turns to spend on a single floor before being kicked. */
	public final int timeLimit;
	/** Lists the Traps found in this Dungeon. */
	private final ArrayList<DungeonTrapGroup> traps;
	/** The Weather in this Dungeon (Weather ID -> floors). */
	private final HashMap<Integer, FloorSet> weather;

	public Dungeon(Element xml)
	{
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.floorCount = Integer.parseInt(xml.getAttributeValue("floors"));
		this.direction = DungeonDirection.find(XMLUtils.getAttribute(xml, "down", DungeonDirection.UP.value()));
		this.recruitsAllowed = XMLUtils.getAttribute(xml, "recruits", true);
		this.timeLimit = XMLUtils.getAttribute(xml, "limit", 2000);
		this.linkedTo = XMLUtils.getAttribute(xml, "linked", -1);
		this.stickyChance = XMLUtils.getAttribute(xml, "sticky", 0);
		this.mapx = XMLUtils.getAttribute(xml, "mapx", -260) + 260;
		this.mapy = XMLUtils.getAttribute(xml, "mapy", -140) + 140;

		this.pokemon = new ArrayList<DungeonEncounter>();
		for (Element pokemon : xml.getChild("encounters", xml.getNamespace()).getChildren(DungeonEncounter.XML_ROOT, xml.getNamespace()))
			this.pokemon.add(new DungeonEncounter(pokemon));

		this.items = new ArrayList<DungeonItemGroup>();
		for (Element item : xml.getChild("items", xml.getNamespace()).getChildren(DungeonItemGroup.XML_ROOT, xml.getNamespace()))
			this.items.add(new DungeonItemGroup(item));

		this.shopItems = new ArrayList<DungeonItemGroup>();
		if (xml.getChild("shops", xml.getNamespace()) != null)
			for (Element item : xml.getChild("shops", xml.getNamespace()).getChildren(DungeonItemGroup.XML_ROOT, xml.getNamespace()))
			this.shopItems.add(new DungeonItemGroup(item));

		this.buriedItems = new ArrayList<DungeonItemGroup>();
		if (xml.getChild("buried", xml.getNamespace()) != null)
			for (Element item : xml.getChild("buried", xml.getNamespace()).getChildren(DungeonItemGroup.XML_ROOT, xml.getNamespace()))
			this.buriedItems.add(new DungeonItemGroup(item));

		this.traps = new ArrayList<DungeonTrapGroup>();
		if (xml.getChild("traps", xml.getNamespace()) == null)
		{
			this.traps.add(new DungeonTrapGroup(new int[] { TrapRegistry.WONDER_TILE.id }, new int[] { 100 }, new FloorSet(1, this.floorCount)));
		} else
		{
			for (Element trap : xml.getChild("traps", xml.getNamespace()).getChildren(DungeonTrapGroup.XML_ROOT, xml.getNamespace()))
				this.traps.add(new DungeonTrapGroup(trap));
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

	public Dungeon(int id, int floorCount, DungeonDirection direction, boolean recruits, int timeLimit, int stickyChance, int linkedTo,
			ArrayList<DungeonEncounter> pokemon, ArrayList<DungeonItemGroup> items, ArrayList<DungeonItemGroup> shopItems,
			ArrayList<DungeonItemGroup> buriedItems, ArrayList<DungeonTrapGroup> traps, ArrayList<FloorData> floorData, HashMap<Integer, FloorSet> weather,
			int mapx, int mapy)
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
	public ArrayList<DungeonItemGroup> buriedItems(int floor)
	{
		ArrayList<DungeonItemGroup> items = new ArrayList<>();
		for (DungeonItemGroup item : this.buriedItems)
			if (item.floors.contains(floor)) for (int i = 0; i < item.items.length; ++i)
				items.add(item);
		return items;
	}

	public ArrayList<DungeonItemGroup> buriedItemsData()
	{
		return new ArrayList<>(this.buriedItems);
	}

	@Override
	public int compareTo(Dungeon o)
	{
		return Integer.compare(this.id, o.id);
	}

	public ArrayList<DungeonEncounter> encountersData()
	{
		return new ArrayList<>(this.pokemon);
	}

	/** @return The Data of the input floor. */
	public FloorData getData(int floor)
	{
		for (FloorData data : this.floorData)
			if (data.floors().contains(floor)) return data;
		return this.floorData.get(0);
	}

	/** @return A random quantity of Pokedollars for a single stack. */
	public int getMoneyQuantity(Random random, int floor)
	{
		final int[] moneyTable = new int[] { 4, 6, 10, 14, 22, 26, 37, 38, 46, 58, 62, 74, 82, 86, 94, 106, 118, 122, 134, 142, 146, 158, 166, 178, 194, 202,
				206, 214, 218, 226, 254, 262, 274, 278, 298, 302, 314, 326, 334, 346, 358, 362, 382, 386, 394, 398, 422, 454, 458, 466, 478, 482, 502, 514, 526,
				538, 542, 554, 562, 566, 586, 614, 622, 626, 634, 662, 674, 694, 698, 706, 718, 734, 746, 758, 768, 778, 794, 802, 818, 838, 842, 862, 866, 878,
				886, 898, 914, 922, 926, 934, 958, 974, 982, 998, 1000, 1100, 1300, 1500, 20000 };

		int r = random.nextInt(99) + 1;
		int value = 0;
		int max = this.getData(floor).baseMoney() * 40;

		for (int i = 0; i < 200; ++i)
		{
			value = moneyTable[r - 1];
			if (value <= max) return value;
			else r /= 2;
		}
		return moneyTable[0];
	}

	/** @return The items found on the input floor. Keys are Item IDs, Values are Item weights. */
	public ArrayList<DungeonItemGroup> items(int floor)
	{
		ArrayList<DungeonItemGroup> items = new ArrayList<>();
		for (DungeonItemGroup item : this.items)
			if (item.floors.contains(floor)) for (int i = 0; i < item.items.length; ++i)
				items.add(item);
		return items;
	}

	public ArrayList<DungeonItemGroup> itemsData()
	{
		return new ArrayList<>(this.items);
	}

	/** @return This Dungeon's name. */
	public Message name()
	{
		return new Message("dungeon." + this.id);
	}

	/** @return A new instance of this Dungeon. */
	public DungeonExploration newInstance(long seed)
	{
		return new DungeonExploration(this.id, seed);
	}

	public ArrayList<DungeonEncounter> pokemon(int floor)
	{
		ArrayList<DungeonEncounter> encounters = new ArrayList<DungeonEncounter>(this.pokemon);
		encounters.removeIf((DungeonEncounter candidate) -> {
			return !candidate.floors.contains(floor);
		});
		return encounters;
	}

	public DungeonEncounter randomEncounter(Random random, int floor)
	{
		ArrayList<DungeonEncounter> candidates = this.pokemon(floor);
		return RandomUtil.random(candidates, random);
	}

	/** @return A random Item for the input floor. May return <code>null</code> if there are no items on the floor, or if they don't meet requirements. */
	public ItemStack randomItem(Random random, int floor, boolean allowMoney)
	{
		ArrayList<DungeonItemGroup> candidates = new ArrayList<DungeonItemGroup>();
		candidates.addAll(this.items);
		candidates.removeIf(new Predicate<DungeonItemGroup>() {
			@Override
			public boolean test(DungeonItemGroup i)
			{
				if (!allowMoney && i.items.length == 1 && i.items[0] == Item.POKEDOLLARS) return true;
				return !i.floors.contains(floor);
			}
		});
		if (candidates.size() == 0) return null;
		ItemStack stack = candidates.get(random.nextInt(candidates.size())).generate(random, allowMoney);

		int quantity = 0;
		if (stack.item().effect() == ItemEffects.Pokedollars) quantity = this.getMoneyQuantity(random, floor);
		else if (stack.item().isStackable) quantity = RandomUtil.nextGaussian(10, 7, random);
		if (quantity <= 0) quantity = 1;

		return stack;
	}

	public ArrayList<DungeonItemGroup> shopItemsData()
	{
		return new ArrayList<>(this.shopItems);
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
		XMLUtils.setAttribute(root, "down", this.direction.value(), DungeonDirection.UP.value());
		XMLUtils.setAttribute(root, "recruits", this.recruitsAllowed, true);
		XMLUtils.setAttribute(root, "limit", this.timeLimit, 2000);
		XMLUtils.setAttribute(root, "sticky", this.stickyChance, 0);
		XMLUtils.setAttribute(root, "linked", this.linkedTo, -1);

		Element pokemon = new Element("encounters");
		for (DungeonEncounter poke : this.pokemon)
			pokemon.addContent(poke.toXML());
		root.addContent(pokemon);

		Element items = new Element("items");
		for (DungeonItemGroup item : this.items)
			items.addContent(item.toXML());
		root.addContent(items);

		if (!this.shopItems.isEmpty())
		{
			Element shops = new Element("shops");
			for (DungeonItemGroup item : this.shopItems)
				shops.addContent(item.toXML());
			root.addContent(shops);
		}

		if (!this.buriedItems.isEmpty())
		{
			Element buried = new Element("buried");
			for (DungeonItemGroup item : this.buriedItems)
				buried.addContent(item.toXML());
			root.addContent(buried);
		}

		if (!this.traps.isEmpty() && !(this.traps.size() == 1 && this.traps.get(0).ids.length == 1 && this.traps.get(0).ids[0] == TrapRegistry.WONDER_TILE.id))
		{
			Element traps = new Element("traps");
			for (DungeonTrapGroup trap : this.traps)
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
		for (DungeonTrapGroup t : this.traps)
			if (t.floors.contains(floor)) for (int i = 0; i < t.ids.length; ++i)
			{
				traps.add(t.ids[i]);
				weights.add(t.chances[i]);
			}
		return new Pair<>(traps, weights);
	}

	public ArrayList<DungeonTrapGroup> trapsData()
	{
		return new ArrayList<>(this.traps);
	}

	/** @param floor - A Floor ID.
	 * @param random - A Random Number Generator.
	 * @return The prevailing Weather for the input Floor. */
	public Weather weather(int floor, Random random)
	{
		Weather w = Weather.CLEAR;

		ArrayList<Weather> candidates = new ArrayList<>();

		for (Integer id : this.weather.keySet())
			if (id == Weather.random) candidates.add(Weather.random(random));
			else if (this.weather.get(id).contains(floor)) candidates.add(Weather.find(id));

		if (candidates.isEmpty()) return w;
		return RandomUtil.random(candidates, random);
	}

	@SuppressWarnings("unchecked")
	public HashMap<Integer, FloorSet> weatherData()
	{
		return (HashMap<Integer, FloorSet>) this.weather.clone();
	}
}
