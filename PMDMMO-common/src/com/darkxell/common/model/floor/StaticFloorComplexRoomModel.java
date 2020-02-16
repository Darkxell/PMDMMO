package com.darkxell.common.model.floor;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "complex")
@XmlAccessorType(XmlAccessType.FIELD)
public class StaticFloorComplexRoomModel extends IStaticFloorRoom {

    @XmlElement(name = "room")
    private ArrayList<StaticFloorRoomModel> parts = new ArrayList<>();

    public ArrayList<StaticFloorRoomModel> getParts() {
        return new ArrayList<>(parts);
    }

}
