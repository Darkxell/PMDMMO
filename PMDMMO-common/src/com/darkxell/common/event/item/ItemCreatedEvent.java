package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.item.ItemContainer;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;

public class ItemCreatedEvent extends Event {

    public final ItemContainer container;
    public final ItemStack item;

    public ItemCreatedEvent(Floor floor, EventSource eventSource, ItemStack item, ItemContainer container) {
        super(floor, eventSource);
        this.item = item;
        this.container = container;
        if (this.container instanceof Pokemon)
            throw new IllegalArgumentException("Always use DungeonPokemon for ItemCreatedEvent.");
    }

    @Override
    public String loggerMessage() {
        return "Created item: " + this.item.name();
    }

    @Override
    public ArrayList<Event> processServer() {
        this.floor.dungeon.communication.itemIDs.register(this.item,
                this.container instanceof DungeonPokemon ? ((DungeonPokemon) this.container).usedPokemon : null);
        if (this.container.canAccept(this.item) != -1)
            this.container.addItem(this.item);
        else
            this.resultingEvents
                    .add(new ItemLandedEvent(this.floor, this, this.item, this.container.locationOnFloor()));
        return super.processServer();
    }

}
