package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "target")
@XmlAccessorType(XmlAccessType.FIELD)
public class DespawnCutsceneEventModel extends CutsceneEventModel {

    @XmlAttribute
    private int target;

    public DespawnCutsceneEventModel() {
        super(CutsceneEventType.despawn);
    }

    public DespawnCutsceneEventModel(Integer id, int target) {
        super(CutsceneEventType.despawn, id);
        this.target = target;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        DespawnCutsceneEventModel clone = new DespawnCutsceneEventModel();
        clone.setTarget(this.target);
        return clone;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return this.displayID() + "(" + this.getTarget() + ") despawns";
    }

}
