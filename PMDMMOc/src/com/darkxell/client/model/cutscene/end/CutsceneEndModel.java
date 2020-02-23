package com.darkxell.client.model.cutscene.end;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;

@XmlRootElement(name = "end")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class CutsceneEndModel {

    @XmlAttribute(name = "fadesout")
    private Boolean fadesOut;

    @XmlAttribute(name = "function")
    private String functionID;

    @XmlTransient
    public final CutsceneEndType type;

    public CutsceneEndModel() {
        this.type = null;
    }

    public CutsceneEndModel(CutsceneEndType type) {
        this.type = type;
    }

    public CutsceneEndModel(CutsceneEndType type, String functionID, Boolean fadesOut) {
        this.type = type;
        this.functionID = functionID;
        this.fadesOut = fadesOut;
    }

    public CutsceneEndModel copy() {
        CutsceneEndModel clone = this.copyChild();
        clone.setFadesOut(this.fadesOut);
        clone.setFunctionID(this.functionID);
        return clone;
    }

    /** Start the copy at the lowest class */
    protected abstract CutsceneEndModel copyChild();

    public Boolean isFadesOut() {
        return fadesOut;
    }

    public String getFunctionID() {
        return functionID;
    }

    public void setFadesOut(Boolean fadesOut) {
        this.fadesOut = fadesOut;
    }

    public void setFunctionID(String functionID) {
        this.functionID = functionID;
    }

    public abstract CutsceneEnd build(Cutscene cutscene);

    public static Class<?>[] getXmlClassesToBind() {
        return new Class<?>[] { EnterDungeonCutsceneEndModel.class, LoadFreezoneCutsceneEndModel.class,
                PlayCutsceneCutsceneEndModel.class, ResumeExplorationCutsceneEndModel.class };
    }

}
