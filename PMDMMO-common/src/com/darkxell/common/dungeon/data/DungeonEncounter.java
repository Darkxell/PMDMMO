package com.darkxell.common.dungeon.data;

import org.jdom2.Element;

import com.darkxell.common.ai.AI;
import com.darkxell.common.ai.AI.CustomAI;
import com.darkxell.common.ai.StationaryWildAI;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.XMLUtils;

/** Describes how a Pokemon appears in a Dungeon. */
public class DungeonEncounter {

	/** Utility class created when spawning a Pokemon. */
	public static class CreatedEncounter {

		/** May be null. If so, AI will be created automatically when calling {@link Floor#summonPokemon}. */
		public AI ai;
		public DungeonPokemon pokemon;
		public Tile tile;

		public CreatedEncounter(DungeonPokemon pokemon, Tile tile, AI ai) {
			this.pokemon = pokemon;
			this.tile = tile;
			this.ai = ai;
		}

	}

	public static final String XML_ROOT = "pokemon";

	/** If not null, a custom AI to give the Pokemon. */
	public final CustomAI aiType;
	/** The floors this Pokemon can appear on. */
	public final FloorSet floors;
	/** The Pokemon ID. */
	public final int id;
	/** The Level of the Pokemon. */
	public final int level;
	/** The weight of the encounter. */
	public final int weight;

	public DungeonEncounter(Element xml) {
		this.id = Integer.parseInt(xml.getAttributeValue("id"));
		this.level = Integer.parseInt(xml.getAttributeValue("level"));
		this.weight = XMLUtils.getAttribute(xml, "weight", 1);
		this.floors = new FloorSet(xml.getChild(FloorSet.XML_ROOT, xml.getNamespace()));
		this.aiType = CustomAI.valueOf(XMLUtils.getAttribute(xml, "ai", CustomAI.NONE.name()));
	}

	public DungeonEncounter(int id, int level, int weight, FloorSet floors, CustomAI aiType) {
		this.id = id;
		this.level = level;
		this.floors = floors;
		this.weight = weight;
		this.aiType = aiType;
	}

	public CreatedEncounter generate(Floor floor) {
		DungeonPokemon pokemon = new DungeonPokemon(this.pokemon().generate(floor.random, this.level));
		Tile tile = floor.randomEmptyTile(true, true, floor.random);
		AI ai;
		switch (this.aiType) {
			case STATIONARY:
				ai = new StationaryWildAI(floor, pokemon);
				break;

			default:
				ai = null;
				break;
		}
		return new CreatedEncounter(pokemon, tile, ai);
	}

	public PokemonSpecies pokemon() {
		return Registries.species().find(this.id);
	}

	public Element toXML() {
		Element root = new Element(XML_ROOT);
		root.setAttribute("id", Integer.toString(this.id));
		root.setAttribute("level", Integer.toString(this.level));
		if (this.weight != 1) root.setAttribute("weight", Integer.toString(this.weight));
		if (this.aiType != CustomAI.NONE) root.setAttribute("ai", this.aiType.name());
		root.addContent(this.floors.toXML());
		return root;
	}

}
