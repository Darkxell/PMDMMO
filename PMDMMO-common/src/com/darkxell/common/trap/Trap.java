package com.darkxell.common.trap;

import java.util.ArrayList;

import com.darkxell.common.Registrable;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.TrapSteppedOnEvent;
import com.darkxell.common.util.language.Message;

public abstract class Trap implements Registrable<Trap> {
    public final int id;

    public Trap(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    @Override
    public int compareTo(Trap o) {
        return Integer.compare(this.id, o.id);
    }

    public Message name() {
        return new Message("trap." + this.id);
    }

    public abstract void onPokemonStep(TrapSteppedOnEvent trapEvent, ArrayList<DungeonEvent> events);

    @Override
    public String toString() {
        return this.name().toString();
    }
}
