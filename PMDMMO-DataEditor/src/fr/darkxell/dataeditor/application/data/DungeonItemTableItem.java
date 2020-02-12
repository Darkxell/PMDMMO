package fr.darkxell.dataeditor.application.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.darkxell.common.dungeon.data.DungeonItemGroup;
import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.item.Item;

public class DungeonItemTableItem implements Comparable<DungeonItemTableItem> {

    public DungeonItemGroup itemGroup;

    public DungeonItemTableItem(DungeonItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }

    @Override
    public int compareTo(DungeonItemTableItem o) {
        return this.getFloors().compareTo(o.getFloors());
    }

    public FloorSet getFloors() {
        return this.itemGroup.getFloors();
    }

    public List<Item> getItems() {
        return Arrays.asList(this.itemGroup.items());
    }

    public Integer getWeight() {
        return this.itemGroup.getWeight();
    }

    public List<Integer> getWeights() {
        ArrayList<Integer> weights = new ArrayList<>();
        for (int w : this.itemGroup.getChances())
            weights.add(w);
        return weights;
    }

}
