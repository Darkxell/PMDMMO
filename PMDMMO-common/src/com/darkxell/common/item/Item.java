package com.darkxell.common.item;

import java.util.ArrayList;
import java.util.Comparator;

import org.jdom2.Element;

import com.darkxell.common.Registrable;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

/** Represents an Item type. */
public class Item implements AffectsPokemon, Registrable<Item> {
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

    public enum ItemCategory {
        BERRIES(3),
        DRINKS(4),
        EQUIPABLE(0),
        EVOLUTIONARY(11),
        FOOD(2),
        GUMMIS(5),
        HMS(10),
        ORBS(8),
        OTHER_USABLES(7),
        OTHERS(12),
        SEEDS(6),
        THROWABLE(1),
        TMS(9);

        public final int order;

        ItemCategory(int order) {
            this.order = order;
        }
    }

    public static final int POKEDOLLARS = 0;
    public static final String XML_ROOT = "item";

    /** This Item's Category. */
    public final ItemCategory category;
    /** This Item's effect ID. */
    public final int effectID;
    /** This Item's ID. */
    public final int id;
    /** True if this Item is Rare, and thus can't be trashed, sold or thrown. */
    public final boolean isRare;
    /** True if this Item can be stacked. */
    public final boolean isStackable;
    /** This Item's price to buy. */
    public final int price;
    /** This Item's price to sell. */
    public final int sell;
    /** The ID of the Item's sprite. */
    public final int spriteID;

    public Item(Element xml) {
        this.id = Integer.parseInt(xml.getAttributeValue("id"));
        this.category = ItemCategory
                .valueOf(XMLUtils.getAttribute(xml, "category", ItemCategory.OTHERS.name()).toUpperCase());
        this.price = Integer.parseInt(xml.getAttributeValue("price"));
        this.sell = Integer.parseInt(xml.getAttributeValue("sell"));
        this.effectID = XMLUtils.getAttribute(xml, "effect", -1);
        this.spriteID = XMLUtils.getAttribute(xml, "sprite", 255);
        this.isStackable = XMLUtils.getAttribute(xml, "stackable", false);
        this.isRare = XMLUtils.getAttribute(xml, "rare", false);
    }

    public Item(int id, ItemCategory category, int price, int sell, int effectID, int spriteID, boolean stackable,
            boolean rare) {
        this.id = id;
        this.category = category;
        this.price = price;
        this.sell = sell;
        this.spriteID = spriteID;
        this.effectID = effectID;
        this.isStackable = stackable;
        this.isRare = rare;
    }

    public int getID() {
        return this.id;
    }

    @Override
    public int compareTo(Item o) {
        return Integer.compare(this.id, o.id);
    }

    public Message description() {
        return this.effect().description(this);
    }

    public ItemEffect effect() {
        return ItemEffects.find(this.effectID);
    }

    public ArrayList<ItemAction> getLegalActions(boolean inDungeon) {
        ArrayList<ItemAction> actions = new ArrayList<>();
        if (inDungeon) {
            if (this.effect().isUsable())
                actions.add(ItemAction.USE);
            if (!this.isRare && this.effect().isThrowable())
                actions.add(ItemAction.THROW);
        }
        actions.add(ItemAction.INFO);
        return actions;
    }

    public Message name() {
        return this.effect().name(this);
    }

    @Override
    public String toString() {
        return this.name().toString();
    }

    public Element toXML() {
        Element root = new Element(XML_ROOT);
        root.setAttribute("id", Integer.toString(this.id));
        XMLUtils.setAttribute(root, "category", this.category.name(), ItemCategory.OTHERS.name());
        root.setAttribute("price", Integer.toString(this.price));
        root.setAttribute("sell", Integer.toString(this.sell));
        XMLUtils.setAttribute(root, "effect", this.effectID, -1);
        XMLUtils.setAttribute(root, "sprite", this.spriteID, 255);
        XMLUtils.setAttribute(root, "stackable", this.isStackable, false);
        XMLUtils.setAttribute(root, "rare", this.isRare, false);
        return root;
    }

    /**
     * Called when this Item is used.
     *
     * @param floor   - The current Floor.
     * @param pokemon - The Pokemon using the Item.
     * @param target  - The Pokemon the Item is being used on. May be null if there is no target.
     */
    public void use(Floor floor, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events) {
        this.effect().use(floor, this, pokemon, target, events);
    }

    /**
     * Called when this Item is used when caught.
     *
     * @param floor   - The current Floor.
     * @param pokemon - The Pokemon using the Item.
     * @param target  - The Pokemon the Item is being used on. May be null if there is no target.
     */
    public void useThrown(Floor floor, DungeonPokemon pokemon, DungeonPokemon target, ArrayList<DungeonEvent> events) {
        this.effect().useThrown(floor, this, pokemon, target, events);
    }

}
