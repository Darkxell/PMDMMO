package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

/** Describes the events occurring while using an Item. */
public class ItemUseEvent extends DungeonEvent {

    /** The Item that was used. */
    public final Item item;
    /** The Pokemon that the Item was used on. null if there was no target. */
    public final DungeonPokemon target;
    /** <code>true</code> if the item is used because it was thrown to the target. */
    public final boolean thrown;
    /** The Pokemon that used the Item. */
    public final DungeonPokemon user;

    public ItemUseEvent(Floor floor, Item item, DungeonPokemon user, DungeonPokemon target) {
        this(floor, item, user, target, false);
    }

    public ItemUseEvent(Floor floor, Item item, DungeonPokemon user, DungeonPokemon target, boolean thrown) {
        super(floor, eventSource);
        this.item = item;
        this.user = user;
        this.target = target;
        this.thrown = thrown;
    }

    @Override
    public String loggerMessage() {
        return this.user + " used the " + this.item.name();
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        if (this.thrown)
            this.item.useThrown(this.floor, this.user, this.target, this.resultingEvents);
        else
            this.item.use(this.floor, this.user, this.target, this.resultingEvents);
        if (this.resultingEvents.size() == 0)
            this.resultingEvents.add(new MessageEvent(this.floor, eventSource, new Message("item.no_effect")));
        return super.processServer();
    }

}
