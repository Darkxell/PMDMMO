package com.darkxell.common.model.floor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.common.util.Direction;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StaticFloorSpawnModel {
    
    @XmlAttribute
    private int x;
    
    @XmlAttribute
    private int y;
    
    @XmlAttribute
    private Direction facing = Direction.SOUTH;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getFacing() {
        return facing;
    }

}
