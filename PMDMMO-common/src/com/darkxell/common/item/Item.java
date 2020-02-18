package com.darkxell.common.item;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.model.item.ItemCategory;
import com.darkxell.common.model.item.ItemModel;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.registry.Registrable;
import com.darkxell.common.util.language.Message;

/** Represents an Item type. */
public class Item implements AffectsPokemon, Registrable<Item> {

    public static final int POKEDOLLARS = 0;

    private final ItemModel model;

    public Item(ItemModel model) {
        this.model = model;
    }

    @Override
    public int compareTo(Item o) {
        return Integer.compare(this.getID(), o.getID());
    }

    public Message description() {
        return this.effect().description(this);
    }

    public ItemEffect effect() {
        return ItemEffects.find(this.getEffectID());
    }

    public ItemCategory getCategory() {
        return this.model.getCategory();
    }

    public int getEffectID() {
        return this.model.getEffectID();
    }

    public String getExtra() {
        return this.model.getExtra();
    }

    public int getID() {
        return this.model.getID();
    }

    public ArrayList<ItemAction> getLegalActions(boolean inDungeon) {
        ArrayList<ItemAction> actions = new ArrayList<>();
        if (inDungeon) {
            if (this.effect().isUsable())
                actions.add(ItemAction.USE);
            if (!this.isRare() && this.effect().isThrowable())
                actions.add(ItemAction.THROW);
        }
        actions.add(ItemAction.INFO);
        return actions;
    }

    public int getPrice() {
        return this.model.getPrice();
    }

    public int getSell() {
        return this.model.getSell();
    }

    public int getSpriteID() {
        return this.model.getSpriteID();
    }

    public boolean isRare() {
        return this.model.isRare();
    }

    public boolean isStackable() {
        return this.model.isStackable();
    }

    public Message name() {
        return this.effect().name(this);
    }

    @Override
    public String toString() {
        return this.name().toString();
    }

    /**
     * Called when this Item is used.
     *
     * @param itemEvent - The current Floor.
     */
    public void use(ItemUseEvent itemEvent, ArrayList<Event> events) {
        this.effect().use(itemEvent, events);
    }

    /**
     * Called when this Item is used when caught.
     */
    public void useThrown(ItemUseEvent itemEvent, ArrayList<Event> events) {
        this.effect().useThrown(itemEvent, events);
    }

    public ItemModel getModel() {
        return this.model;
    }

}
