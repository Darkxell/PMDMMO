package com.darkxell.common.model.floor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StaticFloorItemModel {
    
    @XmlAttribute
    private int id;
    
    @XmlAttribute
    private int x;
    
    @XmlAttribute
    private int y;
    
    @XmlAttribute
    private int quantity = 1;

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getQuantity() {
        return quantity;
    }

}
