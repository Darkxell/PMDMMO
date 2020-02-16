package com.darkxell.common.pokemon;

import com.darkxell.common.dbobject.DBLearnedmove;
import com.darkxell.common.dungeon.TempIDRegistry.HasID;
import com.darkxell.common.move.Move;
import com.darkxell.common.registry.Registries;

public class LearnedMove implements HasID {
    private DBLearnedmove data;

    private Move move;

    /** This move's current Power Points. */
    private int pp;

    public LearnedMove(DBLearnedmove data) {
        this.setData(data);
    }

    public LearnedMove(int moveid) {
        this(new DBLearnedmove(0, 4, moveid, 1, true, false, 0));
        Move m = Registries.moves().find(moveid);
        if (m != null) {
            this.setMaxPP(m.getPP());
            this.setPP(m.getPP());
        }
    }

    public int getAddedLevel() {
        return this.data.addedlevel;
    }

    public DBLearnedmove getData() {
        return this.data;
    }

    @Override
    public long id() {
        return this.getData().id;
    }

    public boolean isEnabled() {
        return this.data.isenabled;
    }

    public boolean isLinked() {
        return this.data.islinked;
    }

    public int maxPP() {
        return this.data.ppmax;
    }

    public Move move() {
        return this.move;
    }

    public int moveId() {
        return this.data.moveid;
    }

    public int pp() {
        return this.pp;
    }

    public void setAddedLevel(int addedLevel) {
        this.data.addedlevel = addedLevel;
    }

    public void setData(DBLearnedmove data) {
        this.data = data;
        this.move = Registries.moves().find(this.data.moveid);
        this.setPP(data.ppmax);
    }

    public void setEnabled(boolean isEnabled) {
        this.data.isenabled = isEnabled;
    }

    @Override
    public void setId(long id) {
        this.getData().id = id;
    }

    public void setLinked(boolean isLinked) {
        this.data.islinked = isLinked;
    }

    public void setMaxPP(int maxPP) {
        this.data.ppmax = maxPP;
    }

    public void setPP(int pp) {
        this.pp = pp;
        if (this.pp < 0)
            this.pp = 0;
        if (this.pp > this.maxPP())
            this.pp = this.maxPP();
    }

    public void setSlot(int slot) {
        this.data.slot = slot;
    }

    public int slot() {
        return this.data.slot;
    }

}
