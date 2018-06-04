package com.darkxell.common.player;

import java.util.ArrayList;

import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.common.dbobject.DatabaseIdentifier;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;

public class Player
{

	public ArrayList<Pokemon> allies;

	private DBPlayer data;

	public ArrayList<DungeonPokemon> dungeonAllies;

	/** This Player's Inventory. */
	private Inventory inventory;

	public Pokemon leaderPokemon;

	public Player(DBPlayer data)
	{
		this.setData(data);
	}

	public Player(String name, Pokemon pokemon)
	{
		this(new DBPlayer(0, name, null, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), pokemon == null ? null : new DatabaseIdentifier(pokemon.id()), null,
				null));
		this.setLeaderPokemon(pokemon);
	}

	public void addAlly(Pokemon pokemon)
	{
		this.data.pokemonsinparty.add(new DatabaseIdentifier(pokemon.id()));
		this.allies.add(pokemon);
		this.dungeonAllies.add(new DungeonPokemon(pokemon));
		pokemon.setPlayer(this);
	}

	public void clearAllies()
	{
		for (Pokemon pokemon : this.allies)
			pokemon.setPlayer(this);
		this.data.pokemonsinparty.clear();
		this.allies.clear();
		this.resetDungeonTeam();
	}

	public DBPlayer getData()
	{
		return this.data;
	}

	public DungeonPokemon getDungeonLeader()
	{
		if (this.leaderPokemon.getDungeonPokemon() == null) this.leaderPokemon.createDungeonPokemon();
		return this.leaderPokemon.getDungeonPokemon();
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
		if (index == 0) return this.getDungeonLeader();
		else if (index < this.dungeonAllies.size() + 1) return this.dungeonAllies.get(index - 1);
		return null;
	}

	/** Returns the full player's team. The team leader is at position 0 in the array. */
	public Pokemon[] getTeam()
	{
		Pokemon[] team = new Pokemon[this.allies.size() + 1];
		team[0] = this.getTeamLeader();
		for (int i = 1; i < team.length; ++i)
			team[i] = this.allies.get(i - 1);
		return team;
	}

	public Pokemon getTeamLeader()
	{
		return this.leaderPokemon;
	}

	public Inventory inventory()
	{
		return this.inventory;
	}

	public boolean isAlly(DungeonPokemon pokemon)
	{
		return pokemon != null && pokemon.player() == this;
	}

	public boolean isAlly(Pokemon pokemon)
	{
		return pokemon != null && pokemon.player() == this;
	}

	public long moneyInBag()
	{
		return this.data.moneyinbag;
	}

	public String name()
	{
		return this.data.name;
	}

	public void removeAlly(Pokemon pokemon)
	{
		if (!this.allies.contains(pokemon)) return;
		this.data.pokemonsinparty.remove(this.allies.indexOf(pokemon));
		this.dungeonAllies.remove(this.allies.indexOf(pokemon));
		this.allies.remove(pokemon);
		pokemon.setPlayer(null);
	}

	public void resetDungeonTeam()
	{
		this.dungeonAllies.clear();
		for (int i = 0; i < this.allies.size(); ++i)
			this.dungeonAllies.add(i, new DungeonPokemon(this.allies.get(i)));
	}

	public void setData(DBPlayer data)
	{
		this.data = data;
		this.allies = new ArrayList<>();
		this.dungeonAllies = new ArrayList<>();
		this.inventory = new Inventory();
	}

	public void setInventory(Inventory inventory)
	{
		this.inventory = inventory;
	}

	public void setLeaderPokemon(Pokemon pokemon)
	{
		if (this.leaderPokemon != null) this.leaderPokemon.setPlayer(null);
		this.leaderPokemon = pokemon;
		if (this.leaderPokemon != null)
		{
			this.leaderPokemon.createDungeonPokemon();
			this.leaderPokemon.setPlayer(this);
			this.data.mainpokemon = new DatabaseIdentifier(this.leaderPokemon.id());
		} else this.data.mainpokemon = null;
	}

	public void setMoneyInBag(long moneyInBag)
	{
		this.data.moneyinbag = moneyInBag;
	}

	public void setStoryPosition(int storyPosition)
	{
		this.data.storyposition = storyPosition;
	}

	public int storyPosition()
	{
		return this.data.storyposition;
	}

	@Override
	public String toString()
	{
		return this.name();
	}

}
