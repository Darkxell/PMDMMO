package com.darkxell.common.dbobject;

import java.util.ArrayList;

import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class DBPokemon implements Communicable
{

	public int abilityid;
	public long experience;
	public int formid;
	public int gender;
	public DatabaseIdentifier holdeditem;
	public long id;
	public int iq;
	public boolean isshiny;
	public ArrayList<DatabaseIdentifier> learnedmoves = new ArrayList<>();
	public int level;
	public String nickname;
	public int specieid;
	public int stat_atk;
	public int stat_def;
	public int stat_hp;
	public int stat_speatk;
	public int stat_spedef;

	public DBPokemon()
	{}

	public DBPokemon(long id, int specieid, int formid, int abilityid, int gender, String nickname, int level, long experience, int iq, boolean isshiny,
			int stat_atk, int stat_def, int stat_speatk, int stat_spedef, int stat_hp, DatabaseIdentifier holdeditem,
			ArrayList<DatabaseIdentifier> learnedmoves)
	{
		this.id = id;
		this.specieid = specieid;
		this.formid = formid;
		this.abilityid = abilityid;
		this.gender = gender;
		this.nickname = nickname;
		this.level = level;
		this.experience = experience;
		this.iq = iq;
		this.isshiny = isshiny;
		this.stat_atk = stat_atk;
		this.stat_def = stat_def;
		this.stat_speatk = stat_speatk;
		this.stat_spedef = stat_spedef;
		this.stat_hp = stat_hp;
		this.holdeditem = holdeditem;
		this.learnedmoves = learnedmoves;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof DBPokemon)) return false;
		DBPokemon o = (DBPokemon) obj;
		if (this.id != o.id) return false;
		if (this.specieid != o.specieid) return false;
		if (this.formid != o.formid) return false;
		if (this.abilityid != o.abilityid) return false;
		if (this.gender != o.gender) return false;
		if (this.nickname != o.nickname) return false;
		if (this.level != o.level) return false;
		if (this.experience != o.experience) return false;
		if (this.iq != o.iq) return false;
		if (this.isshiny != o.isshiny) return false;
		if (this.stat_atk != o.stat_atk) return false;
		if (this.stat_def != o.stat_def) return false;
		if (this.stat_speatk != o.stat_speatk) return false;
		if (this.stat_spedef != o.stat_spedef) return false;
		if (this.stat_hp != o.stat_hp) return false;
		if ((this.holdeditem == null) != (o.holdeditem == null) || (this.holdeditem != null && !this.holdeditem.equals(o.holdeditem))) return false;
		if ((this.learnedmoves == null) != (o.learnedmoves == null) || (this.learnedmoves != null && !this.learnedmoves.equals(o.learnedmoves))) return false;
		return true;
	}

	@Override
	public void read(JsonObject value)
	{
		this.id = value.getLong("id", this.id);
		this.specieid = value.getInt("specieid", this.specieid);
		this.formid = value.getInt("formid", this.formid);
		this.abilityid = value.getInt("abilityid", this.abilityid);
		this.gender = value.getInt("gender", this.gender);
		this.nickname = value.getString("nickname", this.nickname);
		this.level = value.getInt("level", this.level);
		this.experience = value.getLong("experience", this.experience);
		this.iq = value.getInt("iq", this.iq);
		this.isshiny = value.getBoolean("isshiny", this.isshiny);
		this.stat_atk = value.getInt("stat_atk", this.stat_atk);
		this.stat_def = value.getInt("stat_def", this.stat_def);
		this.stat_speatk = value.getInt("stat_speatk", this.stat_speatk);
		this.stat_spedef = value.getInt("stat_spedef", this.stat_spedef);
		this.stat_hp = value.getInt("stat_hp", this.stat_hp);

		JsonValue held = value.get("holdeditem");
		this.holdeditem = held != null && held.isNumber() ? new DatabaseIdentifier(held.asLong()) : null;

		this.learnedmoves = new ArrayList<>();
		JsonValue moves = value.get("learnedmoves");
		if (moves != null && moves.isArray())
		{
			for (JsonValue id : moves.asArray())
				if (id.isNumber()) this.learnedmoves.add(new DatabaseIdentifier(id.asLong()));
		}
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject root = Json.object();
		root.add("id", this.id);
		root.add("specieid", this.specieid);
		root.add("formid", this.formid);
		root.add("abilityid", this.abilityid);
		root.add("gender", this.gender);
		if (this.nickname != null) root.add("nickname", this.nickname);
		root.add("level", this.level);
		root.add("experience", this.experience);
		root.add("iq", this.iq);
		root.add("isshiny", this.isshiny);
		root.add("stat_atk", this.stat_atk);
		root.add("stat_def", this.stat_def);
		root.add("stat_speatk", this.stat_speatk);
		root.add("stat_spedef", this.stat_spedef);
		root.add("stat_hp", this.stat_hp);
		if (this.holdeditem != null) root.add("holdeditem", this.holdeditem.id);

		if (this.learnedmoves != null)
		{
			JsonArray moves = new JsonArray();
			for (DatabaseIdentifier id : this.learnedmoves)
				moves.add(id.id);
			root.add("learnedmoves", moves);
		}

		return root;
	}

}
