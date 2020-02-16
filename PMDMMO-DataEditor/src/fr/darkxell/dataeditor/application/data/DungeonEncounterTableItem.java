package fr.darkxell.dataeditor.application.data;

import com.darkxell.common.ai.AI.CustomAI;
import com.darkxell.common.dungeon.data.DungeonEncounter;
import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.pokemon.PokemonSpecies;
import com.darkxell.common.registry.Registries;

public class DungeonEncounterTableItem implements Comparable<DungeonEncounterTableItem> {

	public DungeonEncounter encounter;

	public DungeonEncounterTableItem(DungeonEncounter encounter) {
		this.encounter = encounter;
	}

	@Override
	public int compareTo(DungeonEncounterTableItem o) {
		int floors = this.getFloors().compareTo(o.getFloors());
		if (floors != 0) return floors;
		int ids = Integer.compare(this.encounter.getID(), o.encounter.getID());
		if (ids != 0) return ids;
		int levels = Integer.compare(this.getLevel(), o.getLevel());
		if (levels != 0) return levels;
		return Integer.compare(this.getWeight(), o.getWeight());
	}

	public CustomAI getAiType() {
		return this.encounter.getAIType();
	}

	public FloorSet getFloors() {
		return this.encounter.getFloors();
	}

	public int getLevel() {
		return this.encounter.getLevel();
	}

	public PokemonSpecies getPokemon() {
		return Registries.species().find(this.encounter.getID());
	}

	public int getWeight() {
		return this.encounter.getWeight();
	}

}
