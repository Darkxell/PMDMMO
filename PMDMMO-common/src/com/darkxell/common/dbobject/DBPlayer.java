package com.darkxell.common.dbobject;

import java.util.ArrayList;

import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class DBPlayer implements Communicable
{

	public long id;
	public DatabaseIdentifier mainpokemon;
	public long moneyinbag;
	public long moneyinbank;
	public String name;
	public String passhash;
	public ArrayList<DatabaseIdentifier> pokemonsinparty;
	public ArrayList<DatabaseIdentifier> pokemonsinzones;
	public DatabaseIdentifier storageinventory;
	public int storyposition;
	public DatabaseIdentifier toolboxinventory;

	public DBPlayer(long id, String name, String passhash, long moneyinbank, long moneyinbag, int storyposition, ArrayList<DatabaseIdentifier> pokemonsinzones,
			ArrayList<DatabaseIdentifier> pokemonsinparty, DatabaseIdentifier mainpokemon, DatabaseIdentifier toolboxinventory,
			DatabaseIdentifier storageinventory)
	{
		this.id = id;
		this.name = name;
		this.passhash = passhash;
		this.moneyinbag = moneyinbag;
		this.moneyinbank = moneyinbank;
		this.storyposition = storyposition;
		this.pokemonsinparty = pokemonsinparty;
		this.pokemonsinzones = pokemonsinzones;
		this.mainpokemon = mainpokemon;
		this.toolboxinventory = toolboxinventory;
		this.storageinventory = storageinventory;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof DBPlayer)) return false;
		DBPlayer o = (DBPlayer) obj;
		return this.id == o.id && this.name.equals(o.name) && this.passhash.equals(o.passhash) && this.moneyinbag == o.moneyinbag
				&& this.moneyinbank == o.moneyinbank && this.storyposition == o.storyposition && this.pokemonsinzones.equals(o.pokemonsinzones)
				&& this.pokemonsinparty.equals(o.pokemonsinparty) && this.mainpokemon.equals(o.mainpokemon) && this.toolboxinventory.equals(o.toolboxinventory)
				&& this.storageinventory.equals(o.storageinventory);
	}

	@Override
	public void read(JsonObject value)
	{
		this.id = value.getLong("id", this.id);
		this.name = value.getString("name", this.name);
		this.passhash = value.getString("passhash", this.passhash);
		this.moneyinbag = value.getLong("moneyinbag", this.moneyinbag);
		this.moneyinbank = value.getLong("moneyinbank", this.moneyinbank);
		this.storyposition = value.getInt("storyposition", this.storyposition);

		JsonValue leader = value.get("mainpokemon");
		this.mainpokemon = leader != null && leader.isNumber() ? new DatabaseIdentifier(leader.asLong()) : null;
		JsonValue toolbox = value.get("toolboxinventory");
		this.toolboxinventory = toolbox != null && toolbox.isNumber() ? new DatabaseIdentifier(toolbox.asLong()) : null;
		JsonValue storage = value.get("storageinventory");
		this.storageinventory = storage != null && storage.isNumber() ? new DatabaseIdentifier(storage.asLong()) : null;

		this.pokemonsinzones = new ArrayList<>();
		JsonValue zonePokes = value.get("pokemonsinzones");
		if (zonePokes != null && zonePokes.isArray()) for (JsonValue id : zonePokes.asArray())
			if (id.isNumber()) this.pokemonsinzones.add(new DatabaseIdentifier(id.asLong()));

		this.pokemonsinparty = new ArrayList<>();
		JsonValue partyPokes = value.get("pokemonsinparty");
		if (partyPokes != null && partyPokes.isArray()) for (JsonValue id : partyPokes.asArray())
			if (id.isNumber()) this.pokemonsinparty.add(new DatabaseIdentifier(id.asLong()));
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject root = Json.object();
		root.add("id", this.id);
		root.add("name", this.name);
		root.add("passhash", this.passhash);
		root.add("moneyinbag", this.moneyinbag);
		root.add("moneyinbank", this.moneyinbank);
		root.add("storyposition", this.storyposition);
		if (this.mainpokemon != null) root.add("mainpokemon", this.mainpokemon.id);
		if (this.toolboxinventory != null) root.add("toolboxinventory", this.toolboxinventory.id);
		if (this.storageinventory != null) root.add("storageinventory", this.storageinventory.id);

		JsonArray zonePokes = new JsonArray();
		for (DatabaseIdentifier id : this.pokemonsinzones)
			zonePokes.add(id.id);
		root.add("pokemonsinzones", zonePokes);

		JsonArray partyPokes = new JsonArray();
		for (DatabaseIdentifier id : this.pokemonsinparty)
			partyPokes.add(id.id);
		root.add("pokemonsinparty", partyPokes);

		return root;
	}

}
