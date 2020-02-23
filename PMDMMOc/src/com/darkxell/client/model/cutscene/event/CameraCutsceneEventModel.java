package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "camera")
@XmlAccessorType(XmlAccessType.FIELD)
public class CameraCutsceneEventModel extends CutsceneEventModel {

    @XmlAttribute(name = "xpos")
    private Double x;

    @XmlAttribute(name = "ypos")
    private Double y;

    @XmlAttribute
    private Double speed;

    public CameraCutsceneEventModel() {
        super(CutsceneEventType.camera);
    }

    public CameraCutsceneEventModel(Integer id, Double x, Double y, Double speed) {
        super(CutsceneEventType.camera, id);
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        CameraCutsceneEventModel clone = new CameraCutsceneEventModel();
        clone.setX(this.x);
        clone.setY(this.y);
        clone.setSpeed(this.speed);
        return clone;
    }

    public Double getSpeed() {
        return speed;
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

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

}
