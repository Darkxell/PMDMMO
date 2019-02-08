package com.darkxell.common.event.item;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.player.ItemContainer.ItemContainerType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.Direction;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/** Describes the events occurring before using an Item. */
public class ItemSelectionEvent extends DungeonEvent implements Communicable {

    /** Direction to face when using the Item. */
    protected Direction direction;
    /** The Item that was used. */
    protected Item item;
    /** The Container the Item was from. */
    protected ItemContainer source;
    /** The index of the Item in the source Container. */
    protected int sourceIndex;
    /** The Pokemon that the Item was used on. null if there was no target. */
    protected DungeonPokemon target;
    /** The Pokemon that used the Item. */
    protected DungeonPokemon user;

    public ItemSelectionEvent(Floor floor) {
        super(floor);
    }

    public ItemSelectionEvent(Floor floor, Item item, DungeonPokemon user, DungeonPokemon target, ItemContainer source,
            int sourceIndex) {
        this(floor, item, user, target, source, sourceIndex, user.facing(), true);
    }

    public ItemSelectionEvent(Floor floor, Item item, DungeonPokemon user, DungeonPokemon target, ItemContainer source,
            int sourceIndex, Direction direction, boolean consumesTurn) {
        super(floor, consumesTurn ? user : null);
        this.item = item;
        this.user = user;
        this.target = target;
        this.source = source;
        this.sourceIndex = sourceIndex;
        this.direction = direction;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemSelectionEvent))
            return false;
        ItemSelectionEvent o = (ItemSelectionEvent) obj;
        if (this.item != o.item)
            return false;
        if (this.direction != o.direction)
            return false;
        if (this.source.containerID() != o.source.containerID())
            return false;
        if (this.user.id() != o.user.id())
            return false;
        if (this.sourceIndex != o.sourceIndex)
            return false;
        if (this.target.id() != o.target.id())
            return false;
        return true;
    }

    public Item item() {
        return this.item;
    }

    @Override
    public String loggerMessage() {
        return this.user + " selected the " + this.item.name();
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        if (this.item.effect().isUsable()) {
            this.messages.add(this.item.effect().getUseEffectMessage(this));
            if (this.item.effect().isConsummable() && this.source != null) {
                ItemStack stack = this.source.getItem(this.sourceIndex);
                stack.setQuantity(stack.quantity() - 1);
                if (stack.quantity() <= 0)
                    this.source.deleteItem(this.sourceIndex);
            }

            this.resultingEvents.add(new ItemUseEvent(this.floor, this.item, this.user, this.target));
        }
        return super.processServer();
    }

    @Override
    public void read(JsonObject value) throws JsonReadingException {
        try {
            Pokemon p = this.floor.dungeon.communication.pokemonIDs.get(value.getLong("user", 0));
            if (p == null)
                throw new JsonReadingException("No pokemon with ID " + value.getLong("user", 0));
            this.user = this.actor = p.getDungeonPokemon();
        } catch (JsonReadingException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for Pokemon ID: " + value.get("user"));
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

        try {
            this.direction = Direction.valueOf(value.getString("direction", Direction.NORTH.name()));
        } catch (IllegalArgumentException e) {
            throw new JsonReadingException("No direction with name " + value.get("direction"));
        }

        if (this.item.effect().isUsedOnTeamMember()) {
            if (value.get("target") == null)
                throw new JsonReadingException("No value for target ID!");
            try {
                Pokemon p = this.floor.dungeon.communication.pokemonIDs.get(value.getLong("target", 0));
                if (p == null)
                    throw new JsonReadingException("No pokemon with ID " + value.getLong("target", 0));
                this.target = p.getDungeonPokemon();
            } catch (JsonReadingException e) {
                throw e;
            } catch (Exception e) {
                throw new JsonReadingException("Wrong value for target ID: " + value.get("target"));
            }
        }

    }

    public DungeonPokemon target() {
        return this.target;
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = Json.object();
        root.add("user", this.user.id());
        if (this.target != null)
            root.add("target", this.target.id());
        root.add("sourcetype", this.source.containerType().name());
        root.add("sourceid", this.source.containerID());
        root.add("sourceindex", this.sourceIndex);
        root.add("direction", this.direction.name());
        return root;
    }

    public DungeonPokemon user() {
        return this.user;
    }

}
