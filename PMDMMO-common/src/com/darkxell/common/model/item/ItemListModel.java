package com.darkxell.common.model.item;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemListModel {

    @XmlElement(name = "item")
    public final ArrayList<ItemModel> items = new ArrayList<>();

    public ItemListModel copy() {
        ItemListModel clone = new ItemListModel();
        for (ItemModel item : this.items)
            clone.items.add(item.copy());
        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemListModel))
            return false;
        ItemListModel o = (ItemListModel) obj;
        if (this.items.size() != o.items.size())
            return false;
        for (int i = 0; i < items.size(); ++i) {
            if (!this.items.get(i).equals(o.items.get(i)))
                return false;
        }
        return true;
    }

}
