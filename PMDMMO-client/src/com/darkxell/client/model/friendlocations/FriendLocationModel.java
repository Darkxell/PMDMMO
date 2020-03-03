package com.darkxell.client.model.friendlocations;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "friend")
@XmlAccessorType(XmlAccessType.FIELD)
public class FriendLocationModel {

    @XmlAttribute
    private int species;

    @XmlAttribute
    private int x;

    @XmlAttribute
    private int y;

    @XmlAttribute
    private Boolean shiny;

    public FriendLocationModel() {
    }

    public FriendLocationModel(int species, int x, int y, Boolean shiny) {
        this.species = species;
        this.x = x;
        this.y = y;
        this.shiny = shiny;
    }

    public Boolean getShiny() {
        return shiny;
    }

    public int getSpecies() {
        return species;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setShiny(Boolean shiny) {
        this.shiny = shiny;
    }

    public void setSpecies(int species) {
        this.species = species;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public FriendLocationModel copy() {
        return new FriendLocationModel(species, x, y, shiny);
    }

}
