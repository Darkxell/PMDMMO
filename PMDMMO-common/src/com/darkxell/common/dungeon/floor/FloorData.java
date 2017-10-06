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

	/** The base Money of the Floor. Used to generate piles of Poké. */
	private int baseMoney;
	/** The density of Buried Items. */
	private short buriedItemDensity;
	/** The type given to a Pokémon using the move Camouflage. */
	private PokemonType camouflageType;
	/** The Floor's difficulty. */
	private int difficulty;
	/** Describes which Floors this Data applies to. */
	private FloorSet floors;
	/** The density of Items. */
	private short itemDensity;
	/** The Layout to use to generate the Floor. */
	private int layout;
	/** The chance of generating a Monster House in this Floor. */
	private short monsterHouseChance;
	/** The ID of the move chosen by Nature Power. */
	private int naturePower;
	/** The density of Pokémon. */
	private short pokemonDensity;
	/** The Effect of the Secret Power move. (Strings, will later be replaced with IDs when implementing the move.) */
	private String secretPower;
	/** The type of shadows for this Floor. See {@link FloorData#NO_SHADOW} */
	private byte shadows;
	/** The chance of generating a Shop in this Floor. */
	private short shopChance;
	/** The Spriteset to use for the terrain. */
	private int terrainSpriteset;
	/** The density of Traps. */
	private short trapDensity;

	public FloorData(Element xml)
	{
		this.load(xml);
	}

	public FloorData(FloorSet floors, int difficulty, int baseMoney, int layout, int terrainSpriteset, byte shadows, PokemonType camouflageType,
			int naturePower, String secretPower, short shopChance, short monsterHouseChance, short itemDensity, short pokemonDensity, short trapDensity,
			short buriedItemDensity)
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
		this.shopChance = shopChance;
		this.monsterHouseChance = monsterHouseChance;
		this.itemDensity = itemDensity;
		this.pokemonDensity = pokemonDensity;
		this.trapDensity = trapDensity;
		this.buriedItemDensity = buriedItemDensity;
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

	/** @return this.A copy of this Data. */
	public FloorData copy()
	{
		return new FloorData(this.floors.copy(), this.difficulty, this.baseMoney, this.layout, this.terrainSpriteset, this.shadows, this.camouflageType,
				this.naturePower, this.secretPower, this.shopChance, this.monsterHouseChance, this.itemDensity, this.pokemonDensity, this.trapDensity,
				this.buriedItemDensity);
	}

	public int difficulty()
	{
		return this.difficulty;
	}

	public FloorSet floors()
	{
		return this.floors;
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
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT));
		this.difficulty = XMLUtils.getAttribute(xml, "difficulty", 0);
		this.baseMoney = XMLUtils.getAttribute(xml, "money", 0);
		this.layout = XMLUtils.getAttribute(xml, "layout", 0);
		this.terrainSpriteset = XMLUtils.getAttribute(xml, "terrain", 0);
		this.shadows = XMLUtils.getAttribute(xml, "shadows", (byte) 0);
		this.camouflageType = PokemonType.find(XMLUtils.getAttribute(xml, "camouflage", 0));
		this.naturePower = XMLUtils.getAttribute(xml, "nature", 0);
		this.secretPower = xml.getAttributeValue("secret");
		this.shopChance = XMLUtils.getAttribute(xml, "shop", (byte) 0);
		this.monsterHouseChance = XMLUtils.getAttribute(xml, "mhouse", (byte) 0);
		this.itemDensity = XMLUtils.getAttribute(xml, "items", (short) 0);
		this.pokemonDensity = XMLUtils.getAttribute(xml, "pokemon", (short) 0);
		this.trapDensity = XMLUtils.getAttribute(xml, "traps", (short) 0);
		this.buriedItemDensity = XMLUtils.getAttribute(xml, "buried", (short) 0);
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

	public int terrainSpriteset()
	{
		return this.terrainSpriteset;
	}

	public Element toXML(FloorData previous)
	{
		Element xml = new Element(XML_ROOT);
		xml.addContent(this.floors.toXML());
		if (this.difficulty != 0) XMLUtils.setAttribute(xml, "difficulty", this.difficulty, previous != null ? previous.difficulty : 0);
		if (this.baseMoney != 0) XMLUtils.setAttribute(xml, "money", this.baseMoney, previous != null ? previous.baseMoney : 0);
		if (this.layout != 0) XMLUtils.setAttribute(xml, "layout", this.layout, previous != null ? previous.layout : 0);
		if (this.terrainSpriteset != 0) XMLUtils.setAttribute(xml, "terrain", this.terrainSpriteset, previous != null ? previous.terrainSpriteset : 0);
		if (this.shadows != 0) XMLUtils.setAttribute(xml, "shadows", this.shadows, previous != null ? previous.shadows : 0);
		XMLUtils.setAttribute(xml, "camouflage", this.camouflageType.id, previous != null ? previous.camouflageType.id : 0);
		if (this.naturePower != 0) XMLUtils.setAttribute(xml, "nature", this.naturePower, previous != null ? previous.naturePower : 0);
		if (this.secretPower != null) XMLUtils.setAttribute(xml, "secret", this.secretPower, previous != null ? previous.secretPower : null);
		if (this.shopChance != 0) XMLUtils.setAttribute(xml, "shop", this.shopChance, previous != null ? previous.shopChance : 0);
		if (this.monsterHouseChance != 0) XMLUtils.setAttribute(xml, "mhouse", this.monsterHouseChance, previous != null ? previous.monsterHouseChance : 0);
		if (this.itemDensity != 0) XMLUtils.setAttribute(xml, "items", this.itemDensity, previous != null ? previous.itemDensity : 0);
		if (this.pokemonDensity != 0) XMLUtils.setAttribute(xml, "pokemon", this.pokemonDensity, previous != null ? previous.pokemonDensity : 0);
		if (this.trapDensity != 0) XMLUtils.setAttribute(xml, "traps", this.trapDensity, previous != null ? previous.trapDensity : 0);
		if (this.buriedItemDensity != 0) XMLUtils.setAttribute(xml, "buried", this.buriedItemDensity, previous != null ? previous.buriedItemDensity : 0);
		return xml;
	}

	public short trapDensity()
	{
		return this.trapDensity;
	}

}
