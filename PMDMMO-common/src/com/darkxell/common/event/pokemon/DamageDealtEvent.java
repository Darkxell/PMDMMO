package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.stats.ExperienceGeneratedEvent;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class DamageDealtEvent extends Event {
    public static interface DamageSource {
        /** @return The ExperienceGainedEvent to add experience to. */
        public ExperienceGeneratedEvent getExperienceEvent();
    }

    public enum DamageType {
        COLLISION,
        CONDITION,
        HUNGER,
        ITEM,
        MOVE,
        RECOIL,
        WEATHER
    }

    public static class DefaultDamageSource implements DamageSource {
        public final ExperienceGeneratedEvent experienceEvent;

        public DefaultDamageSource(Floor floor, Player player, DungeonEventSource eventSource) {
            this.experienceEvent = player == null ? null : new ExperienceGeneratedEvent(floor, eventSource, player);
        }

        @Override
        public ExperienceGeneratedEvent getExperienceEvent() {
            return this.experienceEvent;
        }
    }

    public final int damage;
    public final DamageType damageType;
    /** The Source that dealt damage. */
    public final DamageSource source;
    public final DungeonPokemon target;

    public DamageDealtEvent(Floor floor, DungeonEventSource eventSource, DungeonPokemon target, DamageSource source,
            DamageType type, int damage) {
        super(floor, eventSource);
        this.target = target;
        this.source = source;
        this.damageType = type;
        this.damage = damage;
    }

    @Override
    public String loggerMessage() {
        return this.target + " took " + this.damage + " from " + this.source;
    }

    @Override
    public ArrayList<Event> processServer() {
        String damageID = "move.damage_dealt";
        if (this.damageType == DamageType.WEATHER)
            damageID = "weather.damage_dealt";
        else if (this.damageType == DamageType.RECOIL)
            damageID = "move.recoil";
        else if (this.damageType == DamageType.HUNGER)
            damageID = null;

        if (damageID != null)
            this.messages.add(new Message(damageID).addReplacement("<pokemon>", target.getNickname())
                    .addReplacement("<amount>", Integer.toString(damage)));

        this.target.setHP(this.target.getHp() - this.damage);
        if (this.target.getHp() == 0)
            this.resultingEvents.add(new FaintedPokemonEvent(this.floor, this, this.target, this.source));
        return super.processServer();
    }

}
