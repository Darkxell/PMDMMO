package com.darkxell.common.dungeon.floor;

import org.jdom2.Element;

import com.darkxell.common.dungeon.FloorSet;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonType;

public class FloorData
{

	public static final byte NO_SHADOW = 0, NORMAL_SHADOW = 1, DENSE_SHADOW = 2;
	public static final String XML_ROOT = "d";

	/** The base Money of the Floor. Used to generate piles of Pok�. */
	public final int baseMoney;
	/** The type given to a Pok�mon using the move Camouflage. */
	public final PokemonType camouflageType;
	/** The Floor's difficulty. */
	public final int difficulty;
	/** Describes which Floors this Data applies to. */
	public final FloorSet floors;
	/** The Layout to use to generate the Floor. */
	public final int layout;
	/** The ID of the move chosen by Nature Power. */
	public final int naturePower;
	/** The Effect of the Secret Power move. (Strings, will later be replaced with IDs when implementing the move.) */
	public final String secretPower;
	/** The type of shadows for this Floor. See {@link FloorData#NO_SHADOW} */
	public final byte shadows;
	/** The Spriteset to use for the terrain. */
	public final int terrainSpriteset;

	public FloorData(Element xml)
	{
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT));
		this.difficulty = Integer.parseInt(xml.getAttributeValue("difficulty"));
		this.baseMoney = Integer.parseInt(xml.getAttributeValue("money"));
		this.layout = Integer.parseInt(xml.getAttributeValue("layout"));
		this.terrainSpriteset = Integer.parseInt(xml.getAttributeValue("terrain"));
		this.shadows = xml.getAttribute("shadows") == null ? 0 : Byte.parseByte(xml.getAttributeValue("shadows"));
		this.camouflageType = PokemonType.find(xml.getAttribute("camouflage") == null ? 0 : Integer.parseInt(xml.getAttributeValue("camouflage")));
		this.naturePower = Integer.parseInt(xml.getAttributeValue("nature"));
		this.secretPower = xml.getAttributeValue("secret");
	}

	public FloorData(FloorSet floors, int difficulty, int baseMoney, int layout, int terrainSpriteset, byte shadows, PokemonType camouflageType,
			int naturePower, String secretPower)
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
		xml.setAttribute("shadows", Byte.toString(this.shadows));
		xml.setAttribute("camouflage", Integer.toString(this.camouflageType.id));
		xml.setAttribute("nature", Integer.toString(this.naturePower));
		xml.setAttribute("secret", this.secretPower);
		return xml;
	}

}
