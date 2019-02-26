package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class StatChangedEvent extends Event {
    public static final String[] MESSAGES = new String[] { "stat.decrease.3", "stat.decrease.2", "stat.decrease.1",
            null, "stat.increase.1", "stat.increase.2", "stat.increase.3", };

    private int effectiveChange = 0;
    public final int stage;
    public final Stat stat;
    public final DungeonPokemon target;

    public StatChangedEvent(Floor floor, DungeonEventSource eventSource, DungeonPokemon target, Stat stat, int stage) {
        super(floor, eventSource);
        this.target = target;
        this.stat = stat;
        this.stage = stage;
    }

    public int effectiveChange() {
        return this.effectiveChange;
    }

    @Override
    public boolean isValid() {
        return !this.target.isFainted();
    }

    @Override
    public String loggerMessage() {
        return this.target + " has its " + this.stat + " changed by " + this.stage;
    }

    @Override
    public ArrayList<Event> processServer() {
        this.effectiveChange = this.target.stats.effectiveChange(this.stat, this.stage);

        if (this.effectiveChange != 0) {
            this.target.stats.addStage(this.stat, this.effectiveChange);
            if (this.stat == Stat.Speed)
                this.resultingEvents.add(new SpeedChangedEvent(this.floor, this, this.target));
        }

        String messageID = MESSAGES[this.effectiveChange + 3];
        if (this.effectiveChange == 0)
            if (this.stage > 0)
                messageID = "stat.increase.fail";
            else
                messageID = "stat.decrease.fail";
        if (this.stat != Stat.Speed || this.effectiveChange == 0)
            this.messages.add(new Message(messageID).addReplacement("<pokemon>", this.target.getNickname())
                    .addReplacement("<stat>", new Message("stat." + this.stat)));

        return super.processServer();
    }
}
