package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "drawmap")
@XmlAccessorType(XmlAccessType.FIELD)
public class DrawMapCutsceneEventModel extends CutsceneEventModel {

    @XmlAttribute(name = "draw")
    private Boolean drawMap;

    public DrawMapCutsceneEventModel() {
        super(CutsceneEventType.drawmap);
    }

    public DrawMapCutsceneEventModel(Integer id, Boolean drawMap) {
        super(CutsceneEventType.drawmap, id);
        this.drawMap = drawMap;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        DrawMapCutsceneEventModel clone = new DrawMapCutsceneEventModel();
        clone.setDrawMap(this.drawMap);
        return clone;
    }

    public Boolean getDrawMap() {
        return drawMap;
    }

    public void setDrawMap(Boolean drawMap) {
        this.drawMap = drawMap;
    }

    @Override
    public String toString() {
        if (this.getDrawMap())
            return "Start drawing the map";
        return "Stop drawing the map";
    }

}
