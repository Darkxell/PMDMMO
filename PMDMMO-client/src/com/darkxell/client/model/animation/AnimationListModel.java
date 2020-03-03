package com.darkxell.client.model.animation;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "animations")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimationListModel {

    @XmlElement(name = "a")
    @XmlElementWrapper(name = "abilities")
    public ArrayList<AnimationModel> abilities = new ArrayList<>();

    @XmlElement(name = "c")
    @XmlElementWrapper(name = "custom")
    public ArrayList<AnimationModel> custom = new ArrayList<>();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "items")
    public ArrayList<AnimationModel> items = new ArrayList<>();

    @XmlElement(name = "move")
    @XmlElementWrapper(name = "moves")
    public ArrayList<AnimationModel> moves = new ArrayList<>();

    @XmlElement(name = "movetarget")
    @XmlElementWrapper(name = "movetargets")
    public ArrayList<AnimationModel> movetargets = new ArrayList<>();

    @XmlElement(name = "projectile")
    @XmlElementWrapper(name = "projectiles")
    public ArrayList<AnimationModel> projectiles = new ArrayList<>();

    @XmlElement(name = "status")
    @XmlElementWrapper(name = "statuses")
    public ArrayList<AnimationModel> statuses = new ArrayList<>();

    public AnimationListModel() {
    }

    public AnimationListModel copy() {
        AnimationListModel clone = new AnimationListModel();
        this.abilities.forEach(m -> clone.abilities.add(m.copy()));
        this.custom.forEach(m -> clone.custom.add(m.copy()));
        this.items.forEach(m -> clone.items.add(m.copy()));
        this.moves.forEach(m -> clone.moves.add(m.copy()));
        this.movetargets.forEach(m -> clone.movetargets.add(m.copy()));
        this.projectiles.forEach(m -> clone.projectiles.add(m.copy()));
        this.statuses.forEach(m -> clone.statuses.add(m.copy()));
        return clone;
    }

}
