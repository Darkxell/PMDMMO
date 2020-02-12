package com.darkxell.common.model.dungeon;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.common.dungeon.data.FloorSet;

@XmlRootElement(name = "w")
@XmlAccessorType(XmlAccessType.FIELD)
public class DungeonWeatherModel {

    @XmlElement
    private FloorSet floors;

    @XmlAttribute
    private int id;

    public DungeonWeatherModel() {
    }

    public DungeonWeatherModel(int id, FloorSet floors) {
        this.id = id;
        this.floors = floors;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DungeonWeatherModel))
            return false;
        DungeonWeatherModel o = (DungeonWeatherModel) obj;
        return this.id == o.id && this.floors.equals(o.floors);
    }

    public FloorSet getFloors() {
        return floors;
    }

    public int getID() {
        return id;
    }

    public void setFloors(FloorSet floors) {
        this.floors = floors;
    }

    public void setID(int id) {
        this.id = id;
    }

}
