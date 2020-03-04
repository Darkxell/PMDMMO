package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class StatResetEvent extends Event {

    public final Stat stat;
    public final DungeonPokemon target;

    public StatResetEvent(Floor floor, EventSource eventSource, DungeonPokemon target, Stat stat) {
        super(floor, eventSource);
        this.target = target;
        this.stat = stat;
    }

    @Override
    public boolean isValid() {
        return !this.target.isFainted();
    }

    @Override
    public String loggerMessage() {
        return this.target + " has its " + this.stat.getName() + " stat reset";
    }

    @Override
    public ArrayList<Event> processServer() {

        if (this.stat == Stat.Speed) {
            this.target.stats.resetSpeed();
            this.messages.add(new Message("stat.speed.0").addReplacement("<pokemon>", this.target.getNickname()));
        } else if (this.stat != Stat.Health) {
            this.target.stats.setStage(this.stat, Stat.DEFAULT_STAGE);
        }

        return super.processServer();
    }
}
