package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageSource;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.event.stats.ExperienceGeneratedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.item.effects.ThrowableItemEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;

public class ProjectileThrownEvent extends Event implements DamageSource {

    public final Tile destination;
    public final Direction direction;
    public final ExperienceGeneratedEvent experienceEvent;
    public final Item item;
    public final DungeonPokemon thrower;

    public ProjectileThrownEvent(Floor floor, EventSource eventSource, Item item, DungeonPokemon thrower,
            Tile destination) {
        super(floor, eventSource);
        this.item = item;
        this.thrower = thrower;
        this.destination = destination;
        this.experienceEvent = this.thrower.type == DungeonPokemonType.TEAM_MEMBER
                ? new ExperienceGeneratedEvent(this.floor, this, this.thrower.player())
                : null;
        this.direction = AIUtils.generalDirection(this.thrower.tile(), this.destination);
    }

    @Override
    public ExperienceGeneratedEvent getExperienceEvent() {
        return this.experienceEvent;
    }

    @Override
    public String loggerMessage() {
        return this.thrower + " threw " + this.item + " (destination= " + this.destination + ").";
    }

    @Override
    public ArrayList<Event> processServer() {
        if (this.item.effect() instanceof ThrowableItemEffect && this.destination.getPokemon() != null) {
            this.resultingEvents.add(new DamageDealtEvent(this.floor, this, this.destination.getPokemon(), this,
                    DamageType.ITEM, ((ThrowableItemEffect) this.item.effect()).damage));
            this.resultingEvents.add(this.experienceEvent);
        } else {
            Tile land = this.destination;
            boolean caught = false;
            if (land.getPokemon() != null) {
                ItemStack i = new ItemStack(this.item.id);
                DungeonPokemon catcher = land.getPokemon();
                if (this.item.effect().isUsableOnCatch()) {
                    this.resultingEvents.add(new ItemUseEvent(this.floor, this, this.item, thrower, catcher, true));
                    caught = true;
                } else if (catcher.canAccept(i) != -1) {
                    catcher.addItem(i);
                    this.messages.add(new Message("item.caught").addReplacement("<pokemon>", catcher.getNickname())
                            .addReplacement("<item>", this.item.name()));
                    caught = true;
                }
            }
            if (!caught) {
                while (land.isWall())
                    land = land.adjacentTile(this.direction.opposite());
                this.resultingEvents.add(new ItemLandedEvent(this.floor, this, new ItemStack(this.item.id, 1), land));
            }
        }
        return super.processServer();
    }

}
