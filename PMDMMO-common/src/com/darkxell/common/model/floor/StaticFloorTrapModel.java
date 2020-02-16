package com.darkxell.common.model.floor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "trap")
@XmlAccessorType(XmlAccessType.FIELD)
public class StaticFloorTrapModel {

    @XmlAttribute
    private int id;

    @XmlAttribute
    private int x;

    @XmlAttribute
    private int y;

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
