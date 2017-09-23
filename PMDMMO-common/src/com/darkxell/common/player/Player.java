package com.darkxell.common.player;

import java.util.ArrayList;

import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;

public class Player
{

	/** The Pokémon in the rescue team. */
	private ArrayList<Pokemon> allies;
	private ArrayList<DungeonPokemon> dungeonAllies;
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
		this.dungeonAllies = new ArrayList<DungeonPokemon>();
	}

	public void addAlly(Pokemon pokemon)
	{
		this.allies.add(pokemon);
		this.dungeonAllies.add(new DungeonPokemon(pokemon));
		pokemon.player = this;
	}

	public void clearAllies()
	{
		for (Pokemon pokemon : this.allies)
			pokemon.player = null;
		this.allies.clear();
		this.resetDungeonTeam();
	}

	public DungeonPokemon getDungeonPokemon()
	{
		if (this.dungeonPokemon == null) this.dungeonPokemon = new DungeonPokemon(this.mainPokemon);
		return this.dungeonPokemon;
	}

	public DungeonPokemon[] getDungeonTeam()
	{
		DungeonPokemon[] team = new DungeonPokemon[this.allies.size() + 1];
		for (int i = 0; i < team.length; ++i)
			team[i] = this.getMember(i);
		return team;
	}

	public DungeonPokemon getMember(int index)
	{
		if (index == 0) return this.getDungeonPokemon();
		else if (index < this.dungeonAllies.size() + 1) return this.dungeonAllies.get(index - 1);
		return null;
	}

	public Pokemon getPokemon()
	{
		return this.mainPokemon;
	}

	public Pokemon[] getTeam()
	{
		Pokemon[] team = new Pokemon[this.allies.size() + 1];
		team[0] = this.getPokemon();
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
		this.dungeonAllies.remove(this.allies.indexOf(pokemon));
		this.allies.remove(pokemon);
		pokemon.player = null;
	}

	public void resetDungeonTeam()
	{
		for (int i = 0; i < this.dungeonAllies.size(); ++i)
			this.dungeonAllies.set(i, new DungeonPokemon(this.allies.get(i)));
	}

	public void setDungeonPokemon(DungeonPokemon dungeonPokemon)
	{
		this.dungeonPokemon = dungeonPokemon;
	}

	public void setDungeonPokemon(Pokemon pokemon, DungeonPokemon dungeonPokemon)
	{
		if (pokemon == this.getPokemon()) this.setDungeonPokemon(dungeonPokemon);
		else if (this.allies.contains(pokemon)) this.dungeonAllies.set(this.allies.indexOf(pokemon), dungeonPokemon);
	}

	public void setMainPokemon(Pokemon pokemon)
	{
		if (this.mainPokemon != null) this.mainPokemon.player = null;
		this.mainPokemon = pokemon;
		this.mainPokemon.player = this;
	}

}
