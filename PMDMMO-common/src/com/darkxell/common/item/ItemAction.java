package com.darkxell.common.item;

import java.util.ArrayList;
import java.util.Comparator;

import com.darkxell.common.util.language.Message;

/** Possible actions to be executed on an Item. */
public enum ItemAction {
    /** Item wasn't moved by a Pokemon. */
    AUTO(12, "item.autoaction"),
    /** Removing the Item as shortcut. */
    DESELECT(7, "item.deselect"),
    /** Moving the Item in the inventory. */
    GET(0, "item.get"),
    /** Giving the Item to an ally. */
    GIVE(2, "item.give"),
    /** Viewing the Item description. */
    INFO(11, "item.info"),
    /** Placing the Item on the ground. */
    PLACE(6, "item.place"),
    /** Setting the Item as shortcut. */
    SET(8, "item.set"),
    /** Item is taken by a Pokemon from another. */
    STEAL(12, "item.steal"),
    /** Swapping the Item for another Item in the Inventory. */
    SWAP(4, "item.swap"),
    /** Swapping the Item for the Item on the ground. */
    SWITCH(5, "item.switch"),
    /** Taking the Item from an ally. */
    TAKE(3, "item.take"),
    /** Throwing the Item. */
    THROW(9, "item.throw"),
    /** Deleting the Item (in freezones). */
    TRASH(10, "item.trash"),
    /** Using the Item. */
    USE(1, "item.use");

    public static void sort(ArrayList<ItemAction> actions) {
        actions.sort(Comparator.comparingInt(o -> o.order));
    }

    public final String name;
    public final int order;

    ItemAction(int order, String name) {
        this.order = order;
        this.name = name;
    }

    public Message getName(ItemStack item) {
        if (this == USE && item != null)
            return item.item().effect().getUseName();
        return new Message(this.name);
    }

}