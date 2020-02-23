package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.CutsceneEntityModel;
import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "spawn")
@XmlAccessorType(XmlAccessType.FIELD)
public class SpawnCutsceneEventModel extends CutsceneEventModel {

    @XmlElementRef
    private CutsceneEntityModel entity;

    public SpawnCutsceneEventModel() {
        super(CutsceneEventType.spawn);
    }

    public SpawnCutsceneEventModel(Integer id, CutsceneEntityModel entity) {
        super(CutsceneEventType.spawn, id);
        this.entity = entity;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        SpawnCutsceneEventModel clone = new SpawnCutsceneEventModel();
        clone.setEntity(this.entity.copy());
        return clone;
    }

    public CutsceneEntityModel getEntity() {
        return entity;
    }

    public void setEntity(CutsceneEntityModel entity) {
        this.entity = entity;
    }

}
