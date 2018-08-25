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
	public String passhash = "";
	public ArrayList<DatabaseIdentifier> pokemonsinparty;
	public ArrayList<DatabaseIdentifier> pokemonsinzones;
	public DatabaseIdentifier storageinventory;
	public int storyposition;
	public DatabaseIdentifier toolboxinventory;
        public ArrayList<String> missionsids;

	public DBPlayer()
	{}

	public DBPlayer(long id, String name, String passhash, long moneyinbank, long moneyinbag, int storyposition, ArrayList<DatabaseIdentifier> pokemonsinzones,
			ArrayList<DatabaseIdentifier> pokemonsinparty, DatabaseIdentifier mainpokemon, DatabaseIdentifier toolboxinventory,
			DatabaseIdentifier storageinventory,ArrayList<String> missionsids)
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
                this.missionsids = missionsids;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof DBPlayer)) return false;
		DBPlayer o = (DBPlayer) obj;
		if (this.id != o.id) return false;
		if ((this.name == null) != (o.name == null) || (this.name != null && !this.name.equals(o.name))) return false;
		if (this.moneyinbag != o.moneyinbag) return false;
		if (this.moneyinbank != o.moneyinbank) return false;
		if (this.storyposition != o.storyposition) return false;
		if ((this.pokemonsinzones == null) != (o.pokemonsinzones == null) || (this.pokemonsinzones != null && !this.pokemonsinzones.equals(o.pokemonsinzones)))
			return false;
		if ((this.pokemonsinparty == null) != (o.pokemonsinparty == null) || (this.pokemonsinparty != null && !this.pokemonsinparty.equals(o.pokemonsinparty)))
			return false;
		if ((this.mainpokemon == null) != (o.mainpokemon == null) || (this.mainpokemon != null && !this.mainpokemon.equals(o.mainpokemon))) return false;
		if ((this.toolboxinventory == null) != (o.toolboxinventory == null)
				|| (this.toolboxinventory != null && !this.toolboxinventory.equals(o.toolboxinventory)))
			return false;
		if ((this.storageinventory == null) != (o.storageinventory == null)
				|| (this.storageinventory != null && !this.storageinventory.equals(o.storageinventory)))
			return false;
                if ((this.missionsids == null) != (o.missionsids == null)
				|| (this.missionsids != null && !this.missionsids.equals(o.missionsids)))
			return false;
		return true;
	}

	@Override
	public void read(JsonObject value)
	{
		this.id = value.getLong("id", this.id);
		this.name = value.getString("name", this.name);
		this.passhash = value.getString("passhash", "");
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
		if (zonePokes != null && zonePokes.isArray())
		{
			for (JsonValue id : zonePokes.asArray())
				if (id.isNumber()) this.pokemonsinzones.add(new DatabaseIdentifier(id.asLong()));
		}

		this.pokemonsinparty = new ArrayList<>();
		JsonValue partyPokes = value.get("pokemonsinparty");
		if (partyPokes != null && partyPokes.isArray())
		{
			for (JsonValue id : partyPokes.asArray())
				if (id.isNumber()) this.pokemonsinparty.add(new DatabaseIdentifier(id.asLong()));
		}
                
                this.missionsids = new ArrayList<>();
		JsonValue missionsval = value.get("missionsids");
		if (missionsval != null && missionsval.isArray())
		{
			for (JsonValue id : missionsval.asArray())
				if (id.isNumber()) this.missionsids.add(id.asString());
		}
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject root = Json.object();
		root.add("id", this.id);
		root.add("name", this.name);
		root.add("moneyinbag", this.moneyinbag);
		root.add("moneyinbank", this.moneyinbank);
		root.add("storyposition", this.storyposition);
		if (this.mainpokemon != null) root.add("mainpokemon", this.mainpokemon.id);
		if (this.toolboxinventory != null) root.add("toolboxinventory", this.toolboxinventory.id);
		if (this.storageinventory != null) root.add("storageinventory", this.storageinventory.id);
		
		if (this.pokemonsinzones != null)
		{
			JsonArray zonePokes = new JsonArray();
			for (DatabaseIdentifier id : this.pokemonsinzones)
				zonePokes.add(id.id);
			root.add("pokemonsinzones", zonePokes);
		}

		if (this.pokemonsinparty != null)
		{
			JsonArray partyPokes = new JsonArray();
			for (DatabaseIdentifier id : this.pokemonsinparty)
				partyPokes.add(id.id);
			root.add("pokemonsinparty", partyPokes);
		}
                
                if (this.missionsids != null)
		{
			JsonArray missionsarray = new JsonArray();
			for (String id : this.missionsids)
				missionsarray.add(id);
			root.add("missionsids", missionsarray);
		}

		return root;
	}

}
