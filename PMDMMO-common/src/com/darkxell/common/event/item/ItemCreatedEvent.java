package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Inventory;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;

public class ItemCreatedEvent extends DungeonEvent {

    public final ItemContainer container;
    public final ItemStack item;

    public ItemCreatedEvent(Floor floor, DungeonEventSource eventSource, ItemStack item, ItemContainer container) {
        super(floor, eventSource);
        this.item = item;
        this.container = container;
    }

    @Override
    public String loggerMessage() {
        return "Created item: " + this.item.name();
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        this.floor.dungeon.communication.itemIDs.register(this.item, this.container instanceof Pokemon
                ? ((Pokemon) this.container)
                : this.container instanceof DungeonPokemon ? ((DungeonPokemon) this.container).usedPokemon : null);
        if (this.container.canAccept(this.item) != -1) this.container.addItem(this.item);
        else {
            Tile landing = null;
            if (this.container instanceof Tile) landing = (Tile) this.container;
            else if (this.container instanceof DungeonPokemon) landing = ((DungeonPokemon) this.container).tile();
            else if (this.container instanceof Inventory)
                landing = ((Inventory) this.container).owner.getDungeonLeader().tile();
            this.resultingEvents.add(new ItemLandedEvent(this.floor, this, this.item, landing));
        }
        return super.processServer();
    }

}
