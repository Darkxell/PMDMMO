package fr.darkxell.dataeditor.application.data;

import com.darkxell.common.item.Item;

public class SingleItemTableItem implements Comparable<SingleItemTableItem> {

    public Item item;
    public Integer weight;

    public SingleItemTableItem(Item item, Integer weight) {
        this.item = item;
        this.weight = weight;
    }

    @Override
    public int compareTo(SingleItemTableItem o) {
        return Integer.compare(this.item.getID(), o.item.getID());
    }

    public Item getItem() {
        return this.item;
    }

    public String getWeight() {
        return String.valueOf(this.weight);
    }

}
