package com.darkxell.common.player;

import java.util.ArrayList;

import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;

public class Player
{

	/** The Pokémon in the rescue team. */
	private ArrayList<Pokemon> allies;
	/** If in a Dungeon, reference to the Dungeon Pokémon. null else. */
	private DungeonPokemon dungeonPokemon;
	/** This Player's ID. */
	public final int id;
	/** This Player's Inventory. */
	public final Inventory inventory;
	/** The Pokémon this Player embodies. */
	private Pokemon mainPokemon;
	/** The current amount of Money of this Player. */
	public int money;

	public Player(int id, Pokemon pokemon)
	{
		this.id = id;
		this.setMainPokemon(pokemon);
		this.inventory = new Inventory(Inventory.MAX_SIZE);
		this.money = 0;
		this.allies = new ArrayList<Pokemon>();
	}

	public void addAlly(Pokemon pokemon)
	{
		this.allies.add(pokemon);
		pokemon.player = this;
	}

	public void clearAllies()
	{
		for (Pokemon pokemon : this.allies)
			pokemon.player = null;
		this.allies.clear();
	}

	public DungeonPokemon getDungeonPokemon()
	{
		if (this.dungeonPokemon == null) this.dungeonPokemon = new DungeonPokemon(this.mainPokemon);
		return this.dungeonPokemon;
	}

	public Pokemon getPokemon()
	{
		return this.mainPokemon;
	}

	public Pokemon[] getTeam()
	{
		Pokemon[] team = new Pokemon[this.allies.size() + 1];
		team[0] = this.mainPokemon;
		for (int i = 1; i < team.length; ++i)
			team[i] = this.allies.get(i - 1);
		return team;
	}

	public boolean isAlly(Pokemon pokemon)
	{
		return this.mainPokemon == pokemon || this.allies.contains(pokemon);
	}

	public void removeAlly(Pokemon pokemon)
	{
		this.allies.remove(pokemon);
		pokemon.player = null;
	}

	public void setDungeonPokemon(DungeonPokemon dungeonPokemon)
	{
		this.dungeonPokemon = dungeonPokemon;
	}

	public void setMainPokemon(Pokemon pokemon)
	{
		if (this.mainPokemon != null) this.mainPokemon.player = null;
		this.mainPokemon = pokemon;
		this.mainPokemon.player = this;
	}

}
