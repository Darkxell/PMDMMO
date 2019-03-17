package com.darkxell.common.status;

import java.util.ArrayList;
import java.util.Random;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;

public class FloorStatus extends Status implements AffectsPokemon {

    public FloorStatus(int id, int durationMin, int durationMax) {
        super(id, durationMin, durationMax);
        FloorStatuses._registry.put(this.id, this);
    }

    public ActiveFloorStatus create(Object source, Random random) {
        return new ActiveFloorStatus(this, source,
                RandomUtil.nextIntInBounds(this.durationMin, this.durationMax, random));
    }

    public Message name() {
        return new Message("status.floor." + this.id);
    }

    public void onEnd(Floor floor, ActiveFloorStatus instance, ArrayList<Event> events) {
    }

    public void onStart(Floor floor, ActiveFloorStatus instance, ArrayList<Event> events) {
    }

    public void tick(Floor floor, ActiveFloorStatus instance, ArrayList<Event> events) {
    }

}
