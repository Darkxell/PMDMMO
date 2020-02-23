package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "setanimated")
@XmlAccessorType(XmlAccessType.FIELD)
public class SetAnimatedCutsceneEventModel extends CutsceneEventModel {

    @XmlAttribute
    private int target;

    @XmlAttribute
    private boolean animated;

    public SetAnimatedCutsceneEventModel() {
        super(CutsceneEventType.setanimated);
    }

    public SetAnimatedCutsceneEventModel(Integer id, int target, boolean animated) {
        super(CutsceneEventType.setanimated, id);
        this.target = target;
        this.animated = animated;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        SetAnimatedCutsceneEventModel clone = new SetAnimatedCutsceneEventModel();
        clone.setTarget(this.target);
        clone.setAnimated(this.animated);
        return clone;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

}
