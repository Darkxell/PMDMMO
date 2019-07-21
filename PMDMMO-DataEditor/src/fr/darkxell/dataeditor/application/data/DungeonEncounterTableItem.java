package fr.darkxell.dataeditor.application.data;

import com.darkxell.common.Registries;
import com.darkxell.common.dungeon.data.DungeonEncounter;
import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.pokemon.PokemonSpecies;

public class DungeonEncounterTableItem implements Comparable<DungeonEncounterTableItem>
{

	public DungeonEncounter encounter;

	public DungeonEncounterTableItem(DungeonEncounter encounter)
	{
		this.encounter = encounter;
	}

	@Override
	public int compareTo(DungeonEncounterTableItem o)
	{
		int floors = this.getFloors().compareTo(o.getFloors());
		if (floors != 0) return floors;
		int ids = Integer.compare(this.encounter.id, o.encounter.id);
		if (ids != 0) return ids;
		int levels = Integer.compare(this.getLevel(), o.getLevel());
		if (levels != 0) return levels;
		return Integer.compare(this.getWeight(), o.getWeight());
	}

	public FloorSet getFloors()
	{
		return this.encounter.floors;
	}

	public int getLevel()
	{
		return this.encounter.level;
	}

	public PokemonSpecies getPokemon()
	{
		return Registries.species().find(this.encounter.id);
	}

	public int getWeight()
	{
		return this.encounter.weight;
	}

}
