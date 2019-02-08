package com.darkxell.common.dbobject;

import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class DBLearnedmove implements Communicable {

    public int addedlevel;
    /** ID of the Learned Move. */
    public long id;
    public boolean isenabled;
    public boolean islinked;
    /** ID of the Move. */
    public int moveid;
    public int ppmax;
    public int slot;

    public DBLearnedmove() {
    }

    public DBLearnedmove(long id, int slot, int moveid, int ppmax, boolean isenabled, boolean islinked,
            int addedlevel) {
        this.id = id;
        this.slot = slot;
        this.moveid = moveid;
        this.ppmax = ppmax;
        this.isenabled = isenabled;
        this.islinked = islinked;
        this.addedlevel = addedlevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DBLearnedmove))
            return false;
        DBLearnedmove o = (DBLearnedmove) obj;
        return this.id == o.id && this.slot == o.slot && this.moveid == o.moveid && this.ppmax == o.ppmax
                && this.isenabled == o.isenabled && this.islinked == o.islinked && this.addedlevel == o.addedlevel;
    }

    @Override
    public void read(JsonObject value) {
        this.id = value.getLong("id", this.id);
        this.slot = value.getInt("slot", this.slot);
        this.moveid = value.getInt("moveid", this.moveid);
        this.ppmax = value.getInt("ppmax", this.ppmax);
        this.isenabled = value.getBoolean("isenabled", this.isenabled);
        this.islinked = value.getBoolean("islinked", this.islinked);
        this.addedlevel = value.getInt("addedlevel", this.addedlevel);
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = Json.object();
        root.add("id", this.id);
        root.add("slot", this.slot);
        root.add("moveid", this.moveid);
        root.add("ppmax", this.ppmax);
        root.add("isenabled", this.isenabled);
        root.add("islinked", this.islinked);
        root.add("addedlevel", this.addedlevel);
        return root;
    }

}
