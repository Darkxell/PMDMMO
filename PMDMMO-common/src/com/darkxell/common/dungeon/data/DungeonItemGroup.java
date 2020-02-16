package com.darkxell.common.dungeon.data;

import java.util.ArrayList;
import java.util.Random;

import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.model.dungeon.DungeonItemGroupModel;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.RandomUtil;

/** A group of Items that can appear in a Dungeon. */
public class DungeonItemGroup {

    /** @return The list of weights associated with the input Items. */
    public static ArrayList<Integer> weights(ArrayList<DungeonItemGroup> items) {
        ArrayList<Integer> weights = new ArrayList<>();
        for (DungeonItemGroup item : items)
            weights.add(item.getWeight());
        return weights;
    }

    private final DungeonItemGroupModel model;

    public DungeonItemGroup(DungeonItemGroupModel model) {
        this.model = model;
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

    public DungeonItemGroupModel getModel() {
        return this.model;
    }

}
