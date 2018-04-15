package com.darkxell.common.player;

import java.util.ArrayList;

import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class Player implements Communicable
{

	/** The Pok�mon in the rescue team. */
	private ArrayList<Pokemon> allies;
	public int currentStoryline;
	private ArrayList<DungeonPokemon> dungeonAllies;
	/** If in a Dungeon, reference to the Dungeon Pok�mon. null else. */
	private DungeonPokemon dungeonPokemon;
	/** This Player's Inventory. */
	public final Inventory inventory;
	/** The Pok�mon this Player embodies. */
	private Pokemon leaderPokemon;
	/** The current amount of Money of this Player. */
	public long money;
	/** The current amount of Money in this Player's bank. */
	public long moneyInBank;
	public String name;

	public Player()
	{
		this("???", null);
	}

	public Player(String name, Pokemon pokemon)
	{
		this.name = name;
		this.setLeaderPokemon(pokemon);
		this.inventory = new Inventory(Inventory.MAX_SIZE);
		this.money = 0;
		this.moneyInBank = 0;
		this.allies = new ArrayList<Pokemon>();
		this.dungeonAllies = new ArrayList<DungeonPokemon>();
	}

	public void addAlly(Pokemon pokemon)
	{
		this.allies.add(pokemon);
		this.dungeonAllies.add(new DungeonPokemon(pokemon));
		pokemon.setPlayer(this);
	}

	public void clearAllies()
	{
		for (Pokemon pokemon : this.allies)
			pokemon.setPlayer(this);
		this.allies.clear();
		this.resetDungeonTeam();
	}

	public DungeonPokemon getDungeonLeader()
	{
		if (this.dungeonPokemon == null) this.dungeonPokemon = new DungeonPokemon(this.leaderPokemon);
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
		if (index == 0) return this.getDungeonLeader();
		else if (index < this.dungeonAllies.size() + 1) return this.dungeonAllies.get(index - 1);
		return null;
	}

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

	public boolean isAlly(DungeonPokemon pokemon)
	{
		return pokemon.player() == this;
	}

	public boolean isAlly(Pokemon pokemon)
	{
		return pokemon != null && pokemon.player() == this;
	}

	@Override
	public void read(JsonObject value)
	{
		this.name = value.getString("name", "???");
		this.money = value.getInt("money", 0);
		this.moneyInBank = value.getInt("moneyInBank", 0);
		this.currentStoryline = value.getInt("storyline", 0);

		Pokemon leader = new Pokemon();
		leader.read(value.get("leader").asObject());
		this.setLeaderPokemon(leader);

		this.allies.clear();
		for (JsonValue allyJson : value.get("allies").asArray())
		{
			Pokemon ally = new Pokemon();
			ally.read(allyJson.asObject());
			this.addAlly(ally);
		}

		this.inventory.read(value.get("inventory").asObject());
	}

	public void removeAlly(Pokemon pokemon)
	{
		this.dungeonAllies.remove(this.allies.indexOf(pokemon));
		this.allies.remove(pokemon);
		pokemon.setPlayer(null);
	}

	public void resetDungeonTeam()
	{
		this.dungeonAllies.clear();
		for (int i = 0; i < this.dungeonAllies.size(); ++i)
			this.dungeonAllies.add(i, new DungeonPokemon(this.allies.get(i)));
	}

	public void setDungeonPokemon(DungeonPokemon dungeonPokemon)
	{
		this.dungeonPokemon = dungeonPokemon;
	}

	public void setDungeonPokemon(Pokemon pokemon, DungeonPokemon dungeonPokemon)
	{
		if (pokemon == this.getTeamLeader()) this.setDungeonPokemon(dungeonPokemon);
		else if (this.allies.contains(pokemon)) this.dungeonAllies.set(this.allies.indexOf(pokemon), dungeonPokemon);
	}

	public void setLeaderPokemon(Pokemon pokemon)
	{
		if (this.leaderPokemon != null) this.leaderPokemon.setPlayer(null);
		this.leaderPokemon = pokemon;
		this.leaderPokemon.setPlayer(this);
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject root = Json.object();

		root.add("name", this.name);
		root.add("money", this.money);
		root.add("moneryInBank", this.moneyInBank);
		root.add("storyline", this.currentStoryline);

		root.add("leader", this.leaderPokemon.toJson());

		JsonArray alliesJson = new JsonArray();
		for (Pokemon ally : this.allies)
			alliesJson.add(ally.toJson());
		root.add("allies", alliesJson);

		root.add("inventory", this.inventory.toJson());

		return root;
	}

}
