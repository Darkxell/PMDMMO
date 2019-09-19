package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.player.ItemContainer.ItemContainerType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class ItemThrownEvent extends Event implements Communicable {

    private Item item;
    private ItemContainer source;
    private int sourceIndex;
    private DungeonPokemon thrower;
    // FIXME: Add direction to thrown events

    public ItemThrownEvent(Floor floor, EventSource eventSource) {
        super(floor, eventSource);
    }

    public ItemThrownEvent(Floor floor, EventSource eventSource, DungeonPokemon thrower, ItemContainer source,
            int sourceIndex) {
        super(floor, eventSource, thrower);
        this.thrower = thrower;
        this.source = source;
        this.sourceIndex = sourceIndex;
        this.item = this.source.getItem(this.sourceIndex).item();
    }

    @Override
    public boolean isValid() {
        return this.item.effect().isThrowable();
    }

    public Item item() {
        return this.item;
    }

    @Override
    public String loggerMessage() {
        return null;
    }

    @Override
    public ArrayList<Event> processServer() {
        this.messages.add(new Message("item.thrown").addReplacement("<pokemon>", this.thrower.getNickname())
                .addReplacement("<item>", this.item.name()));

        ItemStack stack = this.source.getItem(this.sourceIndex);
        stack.setQuantity(stack.quantity() - 1);
        if (stack.quantity() <= 0)
            this.source.deleteItem(this.sourceIndex);

        this.resultingEvents.add(new ProjectileThrownEvent(this.floor, this, this.item, this.thrower,
                this.item.effect().findDestinationStraight(floor, this.thrower, item, true)));
        return super.processServer();
    }

    @Override
    public void read(JsonObject value) throws JsonReadingException {
        try {
            Pokemon p = this.floor.dungeon.communication.pokemonIDs.get(value.getLong("thrower", 0));
            if (p == null)
                throw new JsonReadingException("No pokemon with ID " + value.getLong("thrower", 0));
            this.thrower = this.actor = p.getDungeonPokemon();
        } catch (JsonReadingException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for Pokemon ID: " + value.get("thrower"));
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

        this.item = this.source.getItem(this.sourceIndex).item();

        try {
            this.sourceIndex = value.getInt("sourceindex", 0);
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for source index: " + value.get("sourceindex"));
        }
    }

    public ItemContainer source() {
        return this.source;
    }

    public int sourceIndex() {
        return this.sourceIndex;
    }

    public DungeonPokemon thrower() {
        return this.thrower;
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = Json.object();
        root.add("thrower", this.thrower.id());
        root.add("sourcetype", this.source.containerType().name());
        root.add("sourceid", this.source.containerID());
        root.add("sourceindex", this.sourceIndex);
        return root;
    }

}
