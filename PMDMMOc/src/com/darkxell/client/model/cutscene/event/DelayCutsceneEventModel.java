package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "delay")
@XmlAccessorType(XmlAccessType.FIELD)
public class DelayCutsceneEventModel extends CutsceneEventModel {

    @XmlAttribute
    private int ticks;

    public DelayCutsceneEventModel() {
        super(CutsceneEventType.delay);
    }

    public DelayCutsceneEventModel(Integer id, int ticks) {
        super(CutsceneEventType.delay, id);
        this.ticks = ticks;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        DelayCutsceneEventModel clone = new DelayCutsceneEventModel();
        clone.setTicks(this.ticks);
        return clone;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

}
