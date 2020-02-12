package com.darkxell.common.model.dungeon;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.util.XMLUtils.IntegerArrayAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DungeonItemGroupModel {

    /** The weight of each Item. */
    @XmlAttribute
    @XmlJavaTypeAdapter(IntegerArrayAdapter.class)
    private Integer[] chances = new Integer[0];

    /** The floors this Item can appear on. */
    @XmlElement
    private FloorSet floors = null;

    /** The Item ID. */
    @XmlAttribute
    @XmlJavaTypeAdapter(IntegerArrayAdapter.class)
    private Integer[] items = new Integer[0];

    /** This Item group's weight. */
    @XmlAttribute
    private int weight = 1;

    public DungeonItemGroupModel() {
    }

    public DungeonItemGroupModel(Integer[] items, Integer[] chances, int weight, FloorSet floors) {
        this.items = items;
        this.chances = chances;
        this.weight = weight;
        this.floors = floors;
    }

    public Integer[] getChances() {
        return chances;
    }

    public FloorSet getFloors() {
        return floors;
    }

    public Integer[] getItems() {
        return items;
    }

    public int getWeight() {
        return weight;
    }

    public void setChances(Integer[] chances) {
        this.chances = chances;
    }

    public void setFloors(FloorSet floors) {
        this.floors = floors;
    }

    public void setItems(Integer[] items) {
        this.items = items;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
