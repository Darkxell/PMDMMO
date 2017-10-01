package com.darkxell.common.dungeon.floor;

import java.util.ArrayList;

import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

public enum TileType
{

	AIR(5, 'a'),
	GROUND(0, ' '),
	LAVA(4, 'l'),
	STAIR(6, 'S'),
	WALL(1, 'M'),
	WALL_END(2, 'm'),
	WARP_ZONE(7, 'x'),
	WATER(3, 'w');

	@SuppressWarnings("unchecked")
	private static ArrayList<TileType>[] tileGroups = new ArrayList[]
	{ new ArrayList<TileType>(), new ArrayList<TileType>() };
	static
	{
		tileGroups[0].add(WALL);
		tileGroups[0].add(WALL_END);

		tileGroups[1].add(GROUND);
		tileGroups[1].add(STAIR);
		tileGroups[1].add(WARP_ZONE);
	}

	/** @return The Tile type with the input character. */
	public static TileType find(char c)
	{
		for (TileType type : values())
			if (type.c == c) return type;
		return null;
	}

	/** @return The Tile type with the input id. */
	public static TileType find(int id)
	{
		for (TileType type : values())
			if (type.id == id) return type;
		return null;
	}

	/** Character for debug purpuses. */
	public final char c;
	public final int id;

	private TileType(int id, char c)
	{
		this.id = id;
		this.c = c;
	}

	public boolean canWalkOn(DungeonPokemon pokemon)
	{
		if (pokemon.pokemon.species.isType(PokemonType.GHOST)) return this != WALL_END;
		if (pokemon.pokemon.species.isType(PokemonType.WATER) && this == WATER) return true;
		if (pokemon.pokemon.species.isType(PokemonType.FIRE) && this == LAVA) return true;
		if (pokemon.pokemon.species.isType(PokemonType.FLYING) && this == AIR) return true;
		return this == GROUND || this == STAIR;
	}

	/** @return True if this Tile connects to the input Tile. */
	public boolean connectsTo(TileType type)
	{
		for (ArrayList<TileType> group : tileGroups)
			if (group.contains(this) && group.contains(type)) return true;
		return false;
	}

}
