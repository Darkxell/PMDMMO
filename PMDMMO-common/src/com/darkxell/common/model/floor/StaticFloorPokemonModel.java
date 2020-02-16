package com.darkxell.common.model.floor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StaticFloorPokemonModel {

    @XmlAttribute
    private int id;

    @XmlAttribute
    private int x;

    @XmlAttribute
    private int y;

    @XmlAttribute
    private int level;

    @XmlElement
    private String ai;

    @XmlElement
    private boolean boss = false;

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLevel() {
        return level;
    }

    public String getAi() {
        return ai;
    }

    public boolean isBoss() {
        return boss;
    }

}
