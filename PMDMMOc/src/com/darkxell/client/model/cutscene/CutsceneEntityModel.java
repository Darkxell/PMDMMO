package com.darkxell.client.model.cutscene;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "entity")
@XmlAccessorType(XmlAccessType.FIELD)
public class CutsceneEntityModel {

    @XmlAttribute
    private Integer cutsceneid;

    @XmlAttribute
    private Double xpos;

    @XmlAttribute
    private Double ypos;

    public CutsceneEntityModel() {
    }

    public CutsceneEntityModel(Integer cutsceneid, Double xpos, Double ypos) {
        super();
        this.cutsceneid = cutsceneid;
        this.xpos = xpos;
        this.ypos = ypos;
    }

    public Integer getCutsceneID() {
        return cutsceneid;
    }

    public Double getXPos() {
        return xpos;
    }

    public Double getYPos() {
        return ypos;
    }

    public void setCutsceneID(Integer cutsceneid) {
        this.cutsceneid = cutsceneid;
    }

    public void setXPos(Double xpos) {
        this.xpos = xpos;
    }

    public void setYPos(Double ypos) {
        this.ypos = ypos;
    }

    public CutsceneEntityModel copy() {
        return new CutsceneEntityModel(cutsceneid, xpos, ypos);
    }

    public static Class<?>[] getXmlClassesToBind() {
        return new Class<?>[] { CutsceneEntityModel.class, CutscenePokemonModel.class };
    }

}
