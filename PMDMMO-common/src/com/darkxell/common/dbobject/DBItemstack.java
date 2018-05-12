package com.darkxell.common.dbobject;

import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class DBItemstack implements Communicable
{

	public long id;
	public int itemid;
	public long quantity;

	public DBItemstack(long id, int itemid, long quantity)
	{
		this.id = id;
		this.itemid = itemid;
		this.quantity = quantity;

	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof DBItemstack)) return false;
		DBItemstack o = (DBItemstack) obj;
		return this.id == o.id && this.itemid == o.itemid && this.quantity == o.quantity;
	}

	@Override
	public void read(JsonObject value)
	{
		this.id = value.getLong("id", this.id);
		this.itemid = value.getInt("itemid", this.itemid);
		this.quantity = value.getLong("quantity", this.quantity);
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject root = Json.object();
		root.add("id", this.id);
		root.add("itemid", this.itemid);
		root.add("quantity", this.quantity);
		return root;
	}

}
