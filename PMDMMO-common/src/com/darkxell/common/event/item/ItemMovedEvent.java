package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Inventory;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.player.ItemContainer.ItemContainerType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class ItemMovedEvent extends DungeonEvent implements Communicable {

    protected ItemAction action;
    protected DungeonPokemon mover;
    protected ItemContainer source, destination;
    protected int sourceIndex, destinationIndex;

    public ItemMovedEvent(Floor floor, DungeonEventSource eventSource) {
        super(floor, eventSource);
    }

    public ItemMovedEvent(Floor floor, DungeonEventSource eventSource, ItemAction action, DungeonPokemon mover, ItemContainer source,
            int sourceIndex, ItemContainer destination, int destinationIndex) {
        this(floor, eventSource, action, mover, source, sourceIndex, destination, destinationIndex, true);
    }

    public ItemMovedEvent(Floor floor, DungeonEventSource eventSource, ItemAction action, DungeonPokemon mover, ItemContainer source,
            int sourceIndex, ItemContainer destination, int destinationIndex, boolean isTurnAction) {
        super(floor, eventSource, isTurnAction ? mover : null);
        this.mover = mover;
        this.action = action;
        this.source = source;
        this.sourceIndex = sourceIndex;
        this.destination = destination;
        this.destinationIndex = destinationIndex;
    }

    public ItemContainer destination() {
        return this.destination;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemMovedEvent))
            return false;
        ItemMovedEvent o = (ItemMovedEvent) obj;
        if (this.action != o.action)
            return false;
        if (this.mover.id() != o.mover.id())
            return false;
        if (this.source.containerID() != o.source.containerID())
            return false;
        if (this.destination.containerID() != o.destination.containerID())
            return false;
        if (this.sourceIndex != o.sourceIndex)
            return false;
        if (this.destinationIndex != o.destinationIndex)
            return false;
        return true;
    }

    @Override
    public boolean isValid() {
        if (this.mover != null && this.mover.type == DungeonPokemonType.RESCUEABLE)
            return false;
        if (this.source instanceof DungeonPokemon && ((DungeonPokemon) this.source).isFainted())
            return false;
        if (this.destination instanceof DungeonPokemon && ((DungeonPokemon) this.destination).isFainted())
            return false;
        return super.isValid();
    }

    @Override
    public String loggerMessage() {
        return this.mover + " moved an item.";
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        String message = null;
        if (this.action == ItemAction.GIVE)
            message = "inventory.give";
        else if (this.action == ItemAction.GET) {
            if (destination instanceof Inventory)
                message = "ground.inventory";
            else
                message = "ground.pickup";
        } else if (this.action == ItemAction.PLACE)
            message = "ground.place";
        else if (this.action == ItemAction.TAKE)
            message = "inventory.taken";
        else if (this.action == ItemAction.AUTO)
            message = "ground.place.auto";
        else if (this.action == ItemAction.STEAL)
            message = "inventory.steal";
        if (message != null)
            this.messages.add(new Message(message)
                    .addReplacement("<pokemon>", this.mover == null ? "null" : this.mover.getNickname().toString())
                    .addReplacement("<item>", this.source.getItem(this.sourceIndex).name()));

        ItemStack i = this.source.getItem(this.sourceIndex);
        this.source.deleteItem(this.sourceIndex);
        if (this.destinationIndex >= this.destination.size() || this.destinationIndex == -1)
            this.destination.addItem(i);
        else
            this.destination.setItem(this.destinationIndex, i);
        return super.processServer();
    }

    @Override
    public void read(JsonObject value) throws JsonReadingException {
        try {
            Pokemon p = this.floor.dungeon.communication.pokemonIDs.get(value.getLong("mover", 0));
            if (p == null)
                throw new JsonReadingException("No pokemon with ID " + value.getLong("mover", 0));
            this.mover = this.actor = p.getDungeonPokemon();
        } catch (JsonReadingException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for Pokemon ID: " + value.get("mover"));
        }

        try {
            ItemContainerType type = ItemContainerType.valueOf(value.getString("sourcetype", "null"));
            long id = value.getLong("sourceid", 0);
            this.source = this.floor.dungeon.communication.identifyContainer(this.floor, type, id);
        } catch (JsonReadingException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonReadingException("Wrong values for source container: type=" + value.get("sourcetype")
                    + ", id=" + value.get("sourceid"));
        }

        try {
            this.sourceIndex = value.getInt("sourceindex", 0);
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for source index: " + value.get("sourceindex"));
        }

        try {
            ItemContainerType type = ItemContainerType.valueOf(value.getString("destinationtype", "null"));
            long id = value.getLong("destinationid", 0);
            this.destination = this.floor.dungeon.communication.identifyContainer(this.floor, type, id);
        } catch (JsonReadingException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonReadingException("Wrong values for destination container: type="
                    + value.get("destinationtype") + ", id=" + value.get("destinationid"));
        }

        try {
            this.destinationIndex = value.getInt("destinationindex", 0);
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for destination index: " + value.get("destinationindex"));
        }

        try {
            this.action = ItemAction.valueOf(value.getString("action", ItemAction.INFO.name()));
            if (this.action == ItemAction.INFO)
                throw new JsonReadingException("");
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for item action: " + value.get("action"));
        }
    }

    public ItemContainer source() {
        return this.source;
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = Json.object();
        root.add("mover", this.mover.id());
        root.add("sourcetype", this.source.containerType().name());
        root.add("sourceid", this.source.containerID());
        root.add("sourceindex", this.sourceIndex);
        root.add("destinationtype", this.destination.containerType().name());
        root.add("destinationid", this.destination.containerID());
        root.add("destinationindex", this.destinationIndex);
        root.add("action", this.action.name());
        return root;
    }

}
