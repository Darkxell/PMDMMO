package com.darkxell.common.dbobject;

import java.util.ArrayList;

import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class DBInventory implements Communicable {

    public ArrayList<DatabaseIdentifier> content = new ArrayList<>();
    public long id;
    public int maxsize;

    public DBInventory() {
    }

    public DBInventory(long id, int maxsize, ArrayList<DatabaseIdentifier> content) {
        this.id = id;
        this.maxsize = maxsize;
        if (content != null)
            this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DBInventory))
            return false;
        DBInventory o = (DBInventory) obj;
        if (!this.content.equals(o.content))
            return false;
        return this.id == o.id && this.maxsize == o.maxsize;
    }

    @Override
    public void read(JsonObject value) {
        this.id = value.getLong("id", this.id);
        this.maxsize = value.getInt("maxsize", this.maxsize);

        this.content = new ArrayList<>();
        JsonValue content = value.get("content");
        if (content != null && content.isArray())
            for (JsonValue id : content.asArray())
                if (id.isNumber())
                    this.content.add(new DatabaseIdentifier(id.asLong()));
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = Json.object();
        root.add("id", this.id);
        root.add("maxsize", this.maxsize);

        JsonArray content = new JsonArray();
        for (DatabaseIdentifier id : this.content)
            content.add(id.id);
        root.add("content", content);

        return root;
    }

}
