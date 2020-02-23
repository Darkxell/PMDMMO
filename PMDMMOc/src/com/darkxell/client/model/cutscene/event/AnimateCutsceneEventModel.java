package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "animate")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimateCutsceneEventModel extends CutsceneEventModel {
    public enum AnimateCutsceneEventMode {
        PLAY,
        START,
        STOP
    }

    @XmlAttribute(name = "animation")
    private int animationID;

    @XmlAttribute
    private int target;

    @XmlAttribute
    private AnimateCutsceneEventMode mode;

    public AnimateCutsceneEventModel() {
        super(CutsceneEventType.animate);
    }

    public AnimateCutsceneEventModel(int id, int animationID, AnimateCutsceneEventMode mode, int target) {
        super(CutsceneEventType.animate, id);
        this.animationID = animationID;
        this.mode = mode;
        this.target = target;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        AnimateCutsceneEventModel clone = new AnimateCutsceneEventModel();
        clone.setTarget(this.target);
        clone.setAnimationID(this.animationID);
        clone.setMode(this.mode);
        return clone;
    }

    public int getAnimationID() {
        return animationID;
    }

    public AnimateCutsceneEventMode getMode() {
        return mode;
    }

    public int getTarget() {
        return target;
    }

    public void setAnimationID(int animationID) {
        this.animationID = animationID;
    }

    public void setMode(AnimateCutsceneEventMode mode) {
        this.mode = mode;
    }

    public void setTarget(int target) {
        this.target = target;
    }

}
