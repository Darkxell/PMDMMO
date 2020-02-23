package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class CutsceneEventModel {

    @XmlAttribute(name = "eventid")
    private Integer id;

    @XmlTransient
    public final CutsceneEventType type;

    public CutsceneEventModel() {
        this.type = null;
    }

    public CutsceneEventModel(CutsceneEventType type) {
        this.type = type;
    }

    public CutsceneEventModel(CutsceneEventType type, Integer id) {
        this.type = type;
        this.id = id;
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public CutsceneEventModel copy() {
        CutsceneEventModel clone = this.createCopy();
        clone.setID(this.id);
        return clone;
    }

    /** Create copy at lowest class level */
    protected abstract CutsceneEventModel createCopy();

}
