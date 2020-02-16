package com.darkxell.common.item;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.model.item.ItemCategory;
import com.darkxell.common.model.item.ItemModel;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.registry.Registrable;
import com.darkxell.common.util.XMLUtils;
import com.darkxell.common.util.language.Message;

/** Represents an Item type. */
public class Item implements AffectsPokemon, Registrable<Item> {

    public static final int POKEDOLLARS = 0;
    public static final String XML_ROOT = "item";

    private final ItemModel model;

    public Item(Element xml) {
        this.model = new ItemModel();
        this.model.setID(Integer.parseInt(xml.getAttributeValue("id")));
        this.model.setCategory(
                ItemCategory.valueOf(XMLUtils.getAttribute(xml, "category", ItemCategory.OTHERS.name()).toUpperCase()));
        this.model.setPrice(Integer.parseInt(xml.getAttributeValue("price")));
        this.model.setSell(Integer.parseInt(xml.getAttributeValue("sell")));
        this.model.setEffectID(XMLUtils.getAttribute(xml, "effect", -1));
        this.model.setSpriteID(XMLUtils.getAttribute(xml, "sprite", 255));
        this.model.setStackable(XMLUtils.getAttribute(xml, "stackable", false));
        this.model.setRare(XMLUtils.getAttribute(xml, "rare", false));
        this.model.setExtra(XMLUtils.getAttribute(xml, "extra", (String) null));
    }

    public Item(int id, ItemCategory category, int price, int sell, int effectID, int spriteID, boolean stackable,
            boolean rare, String extra) {
        this.model = new ItemModel(id, category, price, sell, effectID, spriteID, stackable, rare, extra);
    }

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

    public Element toXML() {
        Element root = new Element(XML_ROOT);
        root.setAttribute("id", Integer.toString(this.getID()));
        XMLUtils.setAttribute(root, "category", this.getCategory().name(), ItemCategory.OTHERS.name());
        root.setAttribute("price", Integer.toString(this.getPrice()));
        root.setAttribute("sell", Integer.toString(this.getSell()));
        XMLUtils.setAttribute(root, "effect", this.getEffectID(), -1);
        XMLUtils.setAttribute(root, "sprite", this.getSpriteID(), 255);
        XMLUtils.setAttribute(root, "stackable", this.isStackable(), false);
        XMLUtils.setAttribute(root, "rare", this.isRare(), false);
        return root;
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
