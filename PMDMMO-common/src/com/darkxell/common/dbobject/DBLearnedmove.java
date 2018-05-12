package com.darkxell.common.dbobject;

public class DBLearnedmove {

	 public long id;
	    public int slot;
	    public int moveid;
	    public int ppmax;
	    public boolean islinked;
	    public int addedlevel;

	    public DBLearnedmove(long id, int slot, int moveid, int ppmax, boolean islinked, int addedlevel) {
	        this.id = id;
	        this.slot = slot;
	        this.moveid = moveid;
	        this.ppmax = ppmax;
	        this.islinked = islinked;
	        this.addedlevel = addedlevel;
	    }
	
}
