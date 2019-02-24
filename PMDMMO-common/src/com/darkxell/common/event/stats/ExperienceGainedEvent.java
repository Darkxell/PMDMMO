package com.darkxell.common.event.stats;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.language.Message;

public class ExperienceGainedEvent extends DungeonEvent {

    public int experience;
    /** The amount of levels that were passed with this experience gain. */
    private int levelsup;
    /** The Pokemon which will gain experience. */
    public final Pokemon pokemon;

    public ExperienceGainedEvent(Floor floor, DungeonEventSource eventSource, Pokemon pokemon, int experience) {
        super(floor, eventSource);
        this.pokemon = pokemon;
        this.experience = experience;
    }

    @Override
    public boolean isValid() {
        return !this.pokemon.getDungeonPokemon().isFainted();
    }

    public int levelsup() {
        return this.levelsup;
    }

    @Override
    public String loggerMessage() {
        return this.messages.get(0).toString();
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        this.messages.add(new Message("xp.gain").addReplacement("<pokemon>", this.pokemon.getNickname())
                .addReplacement("<xp>", Integer.toString(this.experience)));

        this.levelsup = this.pokemon.level();
        this.pokemon.gainExperience(this, this.resultingEvents);
        this.levelsup = this.pokemon.level() - this.levelsup;

        return super.processServer();
    }

}
