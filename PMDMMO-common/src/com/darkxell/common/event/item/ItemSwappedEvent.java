package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class ItemSwappedEvent extends ItemMovedEvent {
    public ItemSwappedEvent(Floor floor) {
        super(floor);
    }

    public ItemSwappedEvent(Floor floor, ItemAction action, DungeonPokemon mover, ItemContainer source, int sourceIndex,
            ItemContainer destination, int destinationIndex) {
        super(floor, action, mover, source, sourceIndex, destination, destinationIndex);
    }

    public ItemContainer destination() {
        return this.destination;
    }

    @Override
    public String loggerMessage() {
        return this.mover + " moved an item";
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        this.messages.clear();
        String message = "inventory.swap";
        if (this.source instanceof Tile || this.destination instanceof Tile)
            message = "ground.swap";
        this.messages.add(new Message(message).addReplacement("<pokemon>", mover.getNickname())
                .addReplacement("<item-given>", this.source.getItem(this.sourceIndex).name())
                .addReplacement("<item-gotten>", this.destination.getItem(this.destinationIndex).name()));

        ItemStack i = this.source.getItem(this.sourceIndex);
        this.source.setItem(this.sourceIndex, this.destination.getItem(this.destinationIndex));
        this.destination.setItem(this.destinationIndex, i);
        return this.resultingEvents;
    }

    public ItemContainer source() {
        return this.source;
    }
}
