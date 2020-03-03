package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "rotate")
@XmlAccessorType(XmlAccessType.FIELD)
public class RotateCutsceneEventModel extends CutsceneEventModel {

    @XmlAttribute
    private int target;

    @XmlAttribute
    private int distance;

    @XmlAttribute
    private Integer speed;

    public RotateCutsceneEventModel() {
        super(CutsceneEventType.rotate);
    }

    public RotateCutsceneEventModel(Integer id, int target, int distance, Integer speed) {
        super(CutsceneEventType.rotate, id);
        this.target = target;
        this.distance = distance;
        this.speed = speed;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        RotateCutsceneEventModel clone = new RotateCutsceneEventModel();
        clone.setTarget(this.target);
        clone.setDistance(this.distance);
        clone.setSpeed(this.speed);
        return clone;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return this.displayID() + "(" + this.getTarget() + ") rotates clockwise " + this.getDistance() + " times";
    }

}
