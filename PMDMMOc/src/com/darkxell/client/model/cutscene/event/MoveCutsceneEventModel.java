package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "move")
@XmlAccessorType(XmlAccessType.FIELD)
public class MoveCutsceneEventModel extends CutsceneEventModel {

    @XmlAttribute(name = "xpos")
    private Double x;

    @XmlAttribute(name = "ypos")
    private Double y;

    @XmlAttribute
    private Double speed;

    @XmlAttribute
    private Integer target;

    public MoveCutsceneEventModel() {
        super(CutsceneEventType.move);
    }

    public MoveCutsceneEventModel(Integer id, Double x, Double y, Double speed, Integer target) {
        super(CutsceneEventType.move, id);
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.target = target;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        MoveCutsceneEventModel clone = new MoveCutsceneEventModel();
        clone.setX(this.x);
        clone.setY(this.y);
        clone.setSpeed(this.speed);
        clone.setTarget(this.target);
        return clone;
    }

    public Double getSpeed() {
        return speed;
    }

    public Integer getTarget() {
        return target;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

}
