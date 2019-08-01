package com.darkxell.common.dungeon;

import java.util.HashMap;

import com.darkxell.common.dbobject.DatabaseIdentifier;
import com.darkxell.common.dungeon.TempIDRegistry.HasID;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;

/**
 * Class that holds temporary IDs while exploring a Dungeon.<br>
 * As each object (Item, Pokemon...) needs an ID for Client <-> server communication, temporary IDs need to be set while
 * exploring a Dungeon. <br>
 * To avoid having a lot of client <-> server in-dungeon payloads, and because many Pokemon or Item will not continue to
 * exist when exiting the Dungeon, this class creates temporary IDs for this exploration. <br>
 * Objects that exit this Dungeon will have an ID created for them by server.
 */
public abstract class TempIDRegistry<T extends HasID> {
    /**
     * Any Object that needs to be registered in a TempIDRegistry should implement this <code>HasID</code> interface.
     */
    public static interface HasID {
        /** @return This Object's ID. */
        public long id();

        /** @param id - The new ID of this Object. */
        public void setId(long id);
    }

    public static class ItemsTempIDRegistry extends TempIDRegistry<ItemStack> {
        public long register(ItemStack item, Pokemon pokemon) {
            long id = this.register(item);
            if (pokemon != null)
                pokemon.getData().holdeditem = new DatabaseIdentifier(id);

            return id;
        }
    }

    public static class MovesTempIDRegistry extends TempIDRegistry<LearnedMove> {
        public long register(LearnedMove move, Pokemon pokemon) {
            long id = this.register(move);
            if (pokemon != null)
                for (int i = 0; i < pokemon.moveCount(); ++i)
                    if (pokemon.move(i) == move) {
                        if (pokemon.getData().learnedmoves.size() == i)
                            pokemon.getData().learnedmoves.add(new DatabaseIdentifier(id));
                        else
                            pokemon.getData().learnedmoves.set(i, new DatabaseIdentifier(id));
                        break;
                    }

            return id;
        }
    }

    public static class PokemonTempIDRegistry extends TempIDRegistry<Pokemon> {
        public long register(Pokemon pokemon, ItemsTempIDRegistry items, MovesTempIDRegistry moves) {
            long id = this.register(pokemon);
            if (pokemon.hasItem())
                items.register(pokemon.getItem(), pokemon);
            for (int i = 0; i < pokemon.moveCount(); ++i)
                moves.register(pokemon.move(i), pokemon);

            return id;
        }

        /** Unregisters the input Pokemon and the Item and Moves it may have. */
        public void unregister(Pokemon pokemon, ItemsTempIDRegistry items, TempIDRegistry<LearnedMove> moves) {
            this.unregister(pokemon.getData().id);
            if (pokemon.hasItem())
                items.unregister(pokemon.getItem().getData().id);
            for (int i = 0; i < pokemon.moveCount(); ++i)
                moves.unregister(pokemon.move(i).getData().id);
        }
    }

    private HashMap<Long, T> registry = new HashMap<>();

    public void clear() {
        this.registry.clear();
    }

    public T get(long id) {
        return this.registry.get(id);
    }

    private long newID() {
        long next = -2;
        while (this.registry.containsKey(next))
            --next;
        return next;
    }

    protected long register(long id, T object) {
        if (id != 0 && this.registry.containsKey(id))
            return id;
        if (id == 0)
            id = this.newID();
        this.registry.put(id, object);
        object.setId(id);
        return id;
    }

    protected long register(T object) {
        if (this.registry.containsValue(object))
            return object.id();
        return this.register(object.id(), object);
    }

    protected void unregister(long id) {
        this.registry.remove(id);
    }
}
