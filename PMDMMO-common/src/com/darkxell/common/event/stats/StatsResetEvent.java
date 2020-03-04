package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class StatsResetEvent extends Event {

    public final DungeonPokemon target;

    public StatsResetEvent(Floor floor, EventSource eventSource, DungeonPokemon target) {
        super(floor, eventSource);
        this.target = target;
    }

    @Override
    public boolean isValid() {
        return !this.target.isFainted();
    }

    @Override
    public String loggerMessage() {
        return this.target + " has its stats reset.";
    }

    @Override
    public ArrayList<Event> processServer() {
        this.messages.add(new Message("stat.reset").addReplacement("<pokemon>", this.target.getNickname()));

        if (this.target.stats.getStage(Stat.Speed) != 1)
            this.resultingEvents.add(new StatResetEvent(this.floor, this, this.target, Stat.Speed));

        for (Stat s : Stat.values())
            if (s != Stat.Speed && s != Stat.Health) {
                int stage = this.target.stats.getStage(s);
                if (stage != Stat.DEFAULT_STAGE)
                    this.resultingEvents.add(new StatResetEvent(this.floor, this, this.target, s));
            }

        return super.processServer();
    }

}
