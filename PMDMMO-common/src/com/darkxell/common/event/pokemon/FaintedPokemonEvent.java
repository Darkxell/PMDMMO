package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.BossDefeatedEvent;
import com.darkxell.common.event.dungeon.PlayerLosesEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.util.language.Message;

public class FaintedPokemonEvent extends DungeonEvent {

    /**
     * The source that damaged the fainted Pokemon. Can be null if the fainting damage didn't result from a Pokemon's
     * move.
     */
    public final DamageSource damage;
    public final DungeonPokemon pokemon;

    public FaintedPokemonEvent(Floor floor, DungeonPokemon pokemon, DamageSource damage) {
        super(floor, eventSource);
        this.pokemon = pokemon;
        this.damage = damage;
    }

    @Override
    public String loggerMessage() {
        return this.messages.get(0).toString();
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        this.messages.add(new Message("pokemon.fainted").addReplacement("<pokemon>", pokemon.getNickname()));

        if (this.pokemon.hasItem())
            this.pokemon.tile().setItem(this.pokemon.getItem());
        if (this.damage.getExperienceEvent() != null)
            this.damage.getExperienceEvent().experience += this.pokemon.experienceGained();
        this.floor.unsummonPokemon(this.pokemon);
        if (this.pokemon.type == DungeonPokemonType.TEAM_MEMBER) {
            int moveID = -1;
            if (this.damage != null && this.damage instanceof MoveUse)
                moveID = ((MoveUse) this.damage).move.moveId();
            this.resultingEvents.add(new PlayerLosesEvent(this.floor, eventSource, this.pokemon.originalPokemon.player(), moveID));
        }

        if (this.pokemon.type == DungeonPokemonType.BOSS) {
            boolean wasLastBoss = true;
            for (DungeonPokemon p : this.floor.listPokemon())
                if (p.type == DungeonPokemonType.BOSS) {
                    wasLastBoss = false;
                    break;
                }

            if (wasLastBoss)
                this.resultingEvents.add(new BossDefeatedEvent(this.floor, eventSource));
        }

        return super.processServer();
    }
}
