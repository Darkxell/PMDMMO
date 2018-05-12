package com.darkxell.common.dbobject;

import java.util.ArrayList;

public class DBInventory {

	public long id;
    public int maxsize;
    public ArrayList<DatabaseIdentifier> content;

    public DBInventory(long id, int maxsize, ArrayList<DatabaseIdentifier> content) {
        this.id = id;
        this.maxsize = maxsize;
        this.content = content;
    }
	
}
