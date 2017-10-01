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
	public final int baseMoney;
	/** The density of Buried Items. */
	public final short buriedItemDensity;
	/** The type given to a Pokémon using the move Camouflage. */
	public final PokemonType camouflageType;
	/** The Floor's difficulty. */
	public final int difficulty;
	/** Describes which Floors this Data applies to. */
	public final FloorSet floors;
	/** The density of Items. */
	public final short itemDensity;
	/** The Layout to use to generate the Floor. */
	public final int layout;
	/** The chance of generating a Monster House in this Floor. */
	public final short monsterHouseChance;
	/** The ID of the move chosen by Nature Power. */
	public final int naturePower;
	/** The density of Pokémon. */
	public final short pokemonDensity;
	/** The Effect of the Secret Power move. (Strings, will later be replaced with IDs when implementing the move.) */
	public final String secretPower;
	/** The type of shadows for this Floor. See {@link FloorData#NO_SHADOW} */
	public final byte shadows;
	/** The chance of generating a Shop in this Floor. */
	public final short shopChance;
	/** The Spriteset to use for the terrain. */
	public final int terrainSpriteset;
	/** The density of Traps. */
	public final short trapDensity;
	/** The weather in this Floor. */
	public final byte weather;

	public FloorData(Element xml)
	{
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT));
		this.difficulty = Integer.parseInt(xml.getAttributeValue("difficulty"));
		this.baseMoney = Integer.parseInt(xml.getAttributeValue("money"));
		this.layout = Integer.parseInt(xml.getAttributeValue("layout"));
		this.terrainSpriteset = Integer.parseInt(xml.getAttributeValue("terrain"));
		this.shadows = XMLUtils.getAttribute(xml, "shadows", (byte) 0);
		this.camouflageType = PokemonType.find(XMLUtils.getAttribute(xml, "camouflage", 0));
		this.naturePower = Integer.parseInt(xml.getAttributeValue("nature"));
		this.secretPower = xml.getAttributeValue("secret");
		this.shopChance = XMLUtils.getAttribute(xml, "shop", (byte) 0);
		this.monsterHouseChance = XMLUtils.getAttribute(xml, "mhouse", (byte) 0);
		this.itemDensity = XMLUtils.getAttribute(xml, "items", (short) 0);
		this.pokemonDensity = XMLUtils.getAttribute(xml, "pokemon", (short) 0);
		this.trapDensity = XMLUtils.getAttribute(xml, "trap", (short) 0);
		this.buriedItemDensity = XMLUtils.getAttribute(xml, "buried", (short) 0);
		this.weather = XMLUtils.getAttribute(xml, "weather", (byte) 0);
	}

	public FloorData(FloorSet floors, int difficulty, int baseMoney, int layout, int terrainSpriteset, byte shadows, PokemonType camouflageType,
			int naturePower, String secretPower, short shopChance, short monsterHouseChance, short itemDensity, short pokemonDensity, short trapDensity,
			short buriedItemDensity, byte weather)
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
		this.weather = weather;
	}

	public Layout layout()
	{
		return Layout.find(this.layout);
	}

	public Move naturePower()
	{
		return MoveRegistry.find(this.naturePower);
	}

	public Element toXML()
	{
		Element xml = new Element(XML_ROOT);
		xml.addContent(this.floors.toXML());
		xml.setAttribute("difficulty", Integer.toString(this.difficulty));
		xml.setAttribute("money", Integer.toString(this.baseMoney));
		xml.setAttribute("layout", Integer.toString(this.layout));
		xml.setAttribute("terrain", Integer.toString(this.terrainSpriteset));
		XMLUtils.setAttribute(xml, "shadows", this.shadows, 0);
		xml.setAttribute("camouflage", Integer.toString(this.camouflageType.id));
		xml.setAttribute("nature", Integer.toString(this.naturePower));
		xml.setAttribute("secret", this.secretPower);
		XMLUtils.setAttribute(xml, "shop", this.shopChance, 0);
		XMLUtils.setAttribute(xml, "mhouse", this.monsterHouseChance, 0);
		XMLUtils.setAttribute(xml, "items", this.itemDensity, 0);
		XMLUtils.setAttribute(xml, "pokemon", this.pokemonDensity, 0);
		XMLUtils.setAttribute(xml, "trap", this.trapDensity, 0);
		XMLUtils.setAttribute(xml, "buried", this.buriedItemDensity, 0);
		XMLUtils.setAttribute(xml, "weather", this.weather, 0);
		return xml;
	}

}
