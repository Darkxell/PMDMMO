package com.darkxell.common.player;

import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;

public class Player
{

	/** If in a Dungeon, reference to the Dungeon Pokémon. null else. */
	private DungeonPokemon dungeonPokemon;
	/** This Player's ID. */
	public final int id;
	/** This Player's Inventory. */
	public final Inventory inventory;
	/** The current amount of Money of this Player. */
	public int money;
	/** The Pokémon this Player embodies. */
	private Pokemon pokemon;

	public Player(int id, Pokemon pokemon)
	{
		this.id = id;
		this.pokemon = pokemon;
		this.inventory = new Inventory(Inventory.MAX_SIZE);
		this.money = 0;
	}

	public DungeonPokemon getDungeonPokemon()
	{
		if (this.dungeonPokemon == null) this.dungeonPokemon = new DungeonPokemon(this.pokemon, this);
		return this.dungeonPokemon;
	}

	public Pokemon getPokemon()
	{
		return this.pokemon;
	}

	public void setDungeonPokemon(DungeonPokemon dungeonPokemon)
	{
		this.dungeonPokemon = dungeonPokemon;
	}

	public void setPokemon(Pokemon pokemon)
	{
		this.pokemon = pokemon;
	}

}
