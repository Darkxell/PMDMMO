package com.darkxell.common.dungeon.floor;

import org.jdom2.Element;

import com.darkxell.common.dungeon.FloorSet;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.XMLUtils;

public class FloorData
{

	public static final byte NO_SHADOW = 0, NORMAL_SHADOW = 1, DENSE_SHADOW = 2;
	public static final String XML_ROOT = "d";

	/** The base Money of the Floor. Used to generate piles of Pok�. */
	private int baseMoney;
	/** The density of Buried Items. */
	private short buriedItemDensity;
	/** The type given to a Pok�mon using the move Camouflage. */
	private PokemonType camouflageType;
	/** The Floor's difficulty. */
	private int difficulty;
	/** Describes which Floors this Data applies to. */
	private FloorSet floors;
	/** True if this is a Boss Floor. */
	private boolean isBossFloor = false;
	/** The density of Items. */
	private short itemDensity;
	/** The Layout to use to generate the Floor. */
	private int layout;
	/** The chance of generating a Monster House in this Floor. */
	private short monsterHouseChance;
	/** The ID of the move chosen by Nature Power. */
	private int naturePower;
	/** The density of Pok�mon. */
	private short pokemonDensity;
	/** The Effect of the Secret Power move. (Strings, will later be replaced with IDs when implementing the move.) */
	private String secretPower;
	/** The type of shadows for this Floor. See {@link FloorData#NO_SHADOW} */
	private byte shadows;
	/** The chance of generating a Shop in this Floor. */
	private short shopChance;
	/** The soundtrack to play on this Floor. */
	private int soundtrack;
	/** The Spriteset to use for the terrain. */
	private int terrainSpriteset;
	/** The density of Traps. */
	private short trapDensity;

	public FloorData(Element xml)
	{
		this.load(xml);
	}

	public FloorData(FloorSet floors, int difficulty, int baseMoney, int layout, int terrainSpriteset, byte shadows, PokemonType camouflageType,
			int naturePower, String secretPower, int soundtrack, short shopChance, short monsterHouseChance, short itemDensity, short pokemonDensity,
			short trapDensity, short buriedItemDensity, boolean isBossFloor)
	{
		this.floors = floors;
		this.difficulty = difficulty;
		this.baseMoney = baseMoney;
		this.layout = layout;
		this.terrainSpriteset = terrainSpriteset;
		this.shadows = shadows;
		this.camouflageType = camouflageType;
		this.naturePower = naturePower;
		this.secretPower = secretPower;
		this.soundtrack = soundtrack;
		this.shopChance = shopChance;
		this.monsterHouseChance = monsterHouseChance;
		this.itemDensity = itemDensity;
		this.pokemonDensity = pokemonDensity;
		this.trapDensity = trapDensity;
		this.buriedItemDensity = buriedItemDensity;
		this.isBossFloor = isBossFloor;
	}

	public int baseMoney()
	{
		return this.baseMoney;
	}

	public short buriedItemDensity()
	{
		return this.buriedItemDensity;
	}

	public PokemonType camouflageType()
	{
		return this.camouflageType;
	}

	/** @return A copy of this Data. */
	public FloorData copy()
	{
		return new FloorData(this.floors.copy(), this.difficulty, this.baseMoney, this.layout, this.terrainSpriteset, this.shadows, this.camouflageType,
				this.naturePower, this.secretPower, this.soundtrack, this.shopChance, this.monsterHouseChance, this.itemDensity, this.pokemonDensity,
				this.trapDensity, this.buriedItemDensity, this.isBossFloor);
	}

	public int difficulty()
	{
		return this.difficulty;
	}

	public FloorSet floors()
	{
		return this.floors;
	}

	public boolean isBossFloor()
	{
		return this.isBossFloor;
	}

	public short itemDensity()
	{
		return this.itemDensity;
	}

	public Layout layout()
	{
		return Layout.find(this.layout);
	}

	public void load(Element xml)
	{
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT, xml.getNamespace()));
		this.difficulty = XMLUtils.getAttribute(xml, "difficulty", this.difficulty);
		this.baseMoney = XMLUtils.getAttribute(xml, "money", this.baseMoney);
		this.layout = XMLUtils.getAttribute(xml, "layout", this.layout);
		this.terrainSpriteset = XMLUtils.getAttribute(xml, "terrain", this.terrainSpriteset);
		this.shadows = XMLUtils.getAttribute(xml, "shadows", this.shadows);
		this.camouflageType = PokemonType.find(XMLUtils.getAttribute(xml, "camouflage", this.camouflageType == null ? 0 : this.camouflageType.id));
		this.naturePower = XMLUtils.getAttribute(xml, "nature", this.naturePower);
		this.secretPower = xml.getAttributeValue("secret");
		this.soundtrack = XMLUtils.getAttribute(xml, "soundtrack", this.soundtrack);
		this.shopChance = XMLUtils.getAttribute(xml, "shop", this.shopChance);
		this.monsterHouseChance = XMLUtils.getAttribute(xml, "mhouse", this.monsterHouseChance);
		this.itemDensity = XMLUtils.getAttribute(xml, "items", this.itemDensity);
		this.pokemonDensity = XMLUtils.getAttribute(xml, "pokemon", this.pokemonDensity);
		this.trapDensity = XMLUtils.getAttribute(xml, "traps", this.trapDensity);
		this.buriedItemDensity = XMLUtils.getAttribute(xml, "buried", this.buriedItemDensity);
		this.isBossFloor = XMLUtils.getAttribute(xml, "bossfloor", this.isBossFloor);
	}

	public short monsterHouseChance()
	{
		return this.monsterHouseChance;
	}

	public Move naturePower()
	{
		return MoveRegistry.find(this.naturePower);
	}

	public short pokemonDensity()
	{
		return this.pokemonDensity;
	}

	public String secretPower()
	{
		return this.secretPower;
	}

	public byte shadows()
	{
		return this.shadows;
	}

	public short shopChance()
	{
		return this.shopChance;
	}

	public int soundtrack()
	{
		return this.soundtrack;
	}

	public int terrainSpriteset()
	{
		return this.terrainSpriteset;
	}

	public Element toXML(FloorData previous)
	{
		Element xml = new Element(XML_ROOT);
		xml.addContent(this.floors.toXML());
		if (previous != null || this.difficulty != 0) XMLUtils.setAttribute(xml, "difficulty", this.difficulty, previous != null ? previous.difficulty : -1);
		if (previous != null || this.baseMoney != 0) XMLUtils.setAttribute(xml, "money", this.baseMoney, previous != null ? previous.baseMoney : -1);
		if (previous != null || this.layout != 0) XMLUtils.setAttribute(xml, "layout", this.layout, previous != null ? previous.layout : -1);
		if (previous != null || this.terrainSpriteset != 0)
			XMLUtils.setAttribute(xml, "terrain", this.terrainSpriteset, previous != null ? previous.terrainSpriteset : -1);
		if (previous != null || this.shadows != 0) XMLUtils.setAttribute(xml, "shadows", this.shadows, previous != null ? previous.shadows : -1);
		if (previous != null || this.camouflageType.id != 0)
			XMLUtils.setAttribute(xml, "camouflage", this.camouflageType.id, previous != null ? previous.camouflageType.id : -1);
		if (previous != null || this.naturePower != 0) XMLUtils.setAttribute(xml, "nature", this.naturePower, previous != null ? previous.naturePower : -1);
		if (previous != null || this.secretPower != null)
			XMLUtils.setAttribute(xml, "secret", this.secretPower, previous != null ? previous.secretPower : null);
		if (previous != null || this.soundtrack != 0) XMLUtils.setAttribute(xml, "soundtrack", this.soundtrack, previous != null ? previous.soundtrack : null);
		if (previous != null || this.shopChance != 0) XMLUtils.setAttribute(xml, "shop", this.shopChance, previous != null ? previous.shopChance : -1);
		if (previous != null || this.monsterHouseChance != 0)
			XMLUtils.setAttribute(xml, "mhouse", this.monsterHouseChance, previous != null ? previous.monsterHouseChance : -1);
		if (previous != null || this.itemDensity != 0) XMLUtils.setAttribute(xml, "items", this.itemDensity, previous != null ? previous.itemDensity : -1);
		if (previous != null || this.pokemonDensity != 0)
			XMLUtils.setAttribute(xml, "pokemon", this.pokemonDensity, previous != null ? previous.pokemonDensity : -1);
		if (previous != null || this.trapDensity != 0) XMLUtils.setAttribute(xml, "traps", this.trapDensity, previous != null ? previous.trapDensity : -1);
		if (previous != null || this.buriedItemDensity != 0)
			XMLUtils.setAttribute(xml, "buried", this.buriedItemDensity, previous != null ? previous.buriedItemDensity : -1);
		if (previous != null || !this.isBossFloor) XMLUtils.setAttribute(xml, "bossfloor", this.isBossFloor, previous != null ? previous.isBossFloor : false);
		return xml;
	}

	public short trapDensity()
	{
		return this.trapDensity;
	}

}
