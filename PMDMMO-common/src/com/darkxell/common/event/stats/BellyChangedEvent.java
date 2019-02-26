package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class BellyChangedEvent extends Event implements DamageSource {

    public final DungeonPokemon pokemon;
    /** How much the Pokemon's belly was filled. */
    public final double quantity;

    public BellyChangedEvent(Floor floor, EventSource eventSource, DungeonPokemon pokemon, double quantity) {
        super(floor, eventSource);
        this.pokemon = pokemon;
        this.quantity = quantity;
        this.priority = PRIORITY_ACTION_END;
    }

    @Override
    public ExperienceGeneratedEvent getExperienceEvent() {
        return null;
    }

    @Override
    public String loggerMessage() {
        return this.pokemon + "'s Belly changed by " + this.quantity;
    }

    @Override
    public ArrayList<Event> processServer() {
        double previous = this.pokemon.getBelly();
        this.pokemon.increaseBelly(this.quantity);
        if (this.quantity > 10) {
            if (this.pokemon.isBellyFull()) this.messages
                    .add(new Message("belly.filled.full").addReplacement("<pokemon>", this.pokemon.getNickname()));
            else this.messages.add(new Message("belly.filled").addReplacement("<pokemon>", this.pokemon.getNickname()));
        } else if (this.pokemon.getBelly() == 0 && previous != 0)
            this.messages.add(new Message("belly.empty").addReplacement("<pokemon>", this.pokemon.getNickname()));

        double now = this.pokemon.getBelly();

        if (previous >= 15 && now < 15) this.messages.add(new Message("belly.hungry"));
        else if (previous >= 7 && now < 7) this.messages.add(new Message("belly.hungry.very"));
        else if (previous == 0 && now == 0 && this.quantity < 0)
            this.resultingEvents.add(new DamageDealtEvent(this.floor, this, this.pokemon, this, DamageType.HUNGER, 1));

        return super.processServer();
    }

}
