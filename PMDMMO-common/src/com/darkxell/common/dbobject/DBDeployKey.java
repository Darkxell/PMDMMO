/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.common.dbobject;

import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/**
 *
 * @author Darkxell
 */
public class DBDeployKey implements Communicable {

    public String keyvalue = "";
    public long timestamp = 0;
    public boolean isused = false;
    public long playerid = 0;

    public DBDeployKey() {
    }

    public DBDeployKey(String keyvalue, long timestamp, boolean isused, long playerid) {
        this.keyvalue = keyvalue;
        this.timestamp = timestamp;
        this.isused = isused;
        this.playerid = playerid;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DBDeployKey)) {
            return false;
        }
        DBDeployKey o = (DBDeployKey) obj;
        return this.keyvalue.equals(o.keyvalue) && this.timestamp == o.timestamp && this.isused == o.isused && this.playerid == o.playerid;
    }

    @Override
    public void read(JsonObject value) {
        this.timestamp = value.getLong("timestamp", this.timestamp);
        this.keyvalue = value.getString("keyvalue", this.keyvalue);
        this.isused = value.getBoolean("isused", this.isused);
        this.playerid = value.getLong("playerid", playerid);
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = Json.object();
        root.add("timestamp", this.timestamp);
        root.add("keyvalue", this.keyvalue);
        root.add("isused", this.isused);
        root.add("playerid", this.playerid);
        return root;
    }

}
