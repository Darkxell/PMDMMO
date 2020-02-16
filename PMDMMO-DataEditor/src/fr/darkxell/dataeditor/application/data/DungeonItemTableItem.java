package fr.darkxell.dataeditor.application.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.model.dungeon.DungeonItemGroupModel;

public class DungeonItemTableItem implements Comparable<DungeonItemTableItem> {

    public DungeonItemGroupModel itemGroup;

    public DungeonItemTableItem(DungeonItemGroupModel itemGroup) {
        this.itemGroup = itemGroup;
    }

    @Override
    public int compareTo(DungeonItemTableItem o) {
        return this.getFloors().compareTo(o.getFloors());
    }

    public FloorSet getFloors() {
        return this.itemGroup.getFloors();
    }

    public List<Integer> getItems() {
        return Arrays.asList(this.itemGroup.getItems());
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
