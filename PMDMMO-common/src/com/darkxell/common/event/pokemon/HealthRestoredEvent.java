package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class HealthRestoredEvent extends Event {

    private int effectiveHeal = -1;
    public final int health;
    public final DungeonPokemon target;

    public HealthRestoredEvent(Floor floor, EventSource eventSource, DungeonPokemon target, int health) {
        super(floor, eventSource);
        this.target = target;
        this.health = health;
    }

    public int effectiveHeal() {
        return this.effectiveHeal;
    }

    @Override
    public String loggerMessage() {
        return this.target + " recovered " + health + " HP.";
    }

    @Override
    public ArrayList<Event> processServer() {
        this.effectiveHeal = Math.min(this.target.getMaxHP() - this.target.getHp(), this.health);
        if (this.effectiveHeal == 0)
            this.messages.add(new Message("move.heal.zero").addReplacement("<pokemon>", this.target.getNickname()));
        else
            this.messages.add(new Message("move.heal").addReplacement("<pokemon>", this.target.getNickname())
                    .addReplacement("<amount>", String.valueOf(this.effectiveHeal)));
        this.target.setHP(this.target.getHp() + this.health);
        return super.processServer();
    }

}
