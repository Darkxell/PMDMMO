package com.darkxell.common.dungeon.data;

import java.util.ArrayList;
import java.util.Random;

import org.jdom2.Element;

import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.model.dungeon.DungeonItemGroupModel;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.XMLUtils;

/** A group of Items that can appear in a Dungeon. */
public class DungeonItemGroup {
    public static final String XML_ROOT = "group";

    /** @return The list of weights associated with the input Items. */
    public static ArrayList<Integer> weights(ArrayList<DungeonItemGroup> items) {
        ArrayList<Integer> weights = new ArrayList<>();
        for (DungeonItemGroup item : items)
            weights.add(item.getWeight());
        return weights;
    }

    private final DungeonItemGroupModel model;

    public DungeonItemGroup(Element xml) {
        this.model = new DungeonItemGroupModel();
        this.model.setWeight(XMLUtils.getAttribute(xml, "weight", 1));
        this.model.setFloors(new FloorSet(xml.getChild(FloorSet.XML_ROOT, xml.getNamespace())));
        this.model.setItems(XMLUtils.readIntegerArray(xml.getChild("ids", xml.getNamespace())));
        if (xml.getChild("chances", xml.getNamespace()) == null) {
            this.model.setChances(new Integer[this.model.getItems().length]);
            for (int i = 0; i < this.model.getChances().length; ++i)
                this.model.getChances()[i] = 1;
        } else
            this.model.setChances(XMLUtils.readIntegerArray(xml.getChild("chances", xml.getNamespace())));
    }

    public DungeonItemGroup(FloorSet floors, int weight, Integer[] items, Integer[] chances) {
        this.model = new DungeonItemGroupModel(items, chances, weight, floors);
    }

    ItemStack generate(Random random, boolean allowMoney) {
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<Integer> weights = new ArrayList<>();
        Integer[] items = this.getItemIDs();
        for (int i = 0; i < items.length; ++i)
            if (allowMoney || items[i] != Item.POKEDOLLARS) {
                ids.add(items[i]);
                weights.add(this.model.getChances()[i]);
            }
        return new ItemStack(RandomUtil.weightedRandom(ids, weights, random));
    }

    public Integer[] getChances() {
        return this.model.getChances();
    }

    public FloorSet getFloors() {
        return this.model.getFloors();
    }

    public Integer[] getItemIDs() {
        return this.model.getItems().clone();
    }

    public Integer getWeight() {
        return this.model.getWeight();
    }

    public Item[] items() {
        Item[] it = new Item[this.model.getItems().length];
        for (int i = 0; i < it.length; ++i)
            it[i] = Registries.items().find(this.model.getItems()[i]);
        return it;
    }

    public Element toXML() {
        Element root = new Element(XML_ROOT);
        XMLUtils.setAttribute(root, "weight", this.getWeight(), 1);
        root.addContent(this.getFloors().toXML());
        root.addContent(XMLUtils.toXML("ids", this.getItemIDs()));

        boolean chances = false;
        for (int c : this.getChances())
            if (c != 1) {
                chances = true;
                break;
            }
        if (chances)
            root.addContent(XMLUtils.toXML("chances", this.getChances()));
        return root;
    }

    public DungeonItemGroupModel getModel() {
        return this.model;
    }

}
