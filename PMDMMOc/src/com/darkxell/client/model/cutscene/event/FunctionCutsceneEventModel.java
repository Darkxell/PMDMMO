package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "function")
@XmlAccessorType(XmlAccessType.FIELD)
public class FunctionCutsceneEventModel extends CutsceneEventModel {

    @XmlAttribute(name = "function")
    private String functionID;

    public FunctionCutsceneEventModel() {
        super(CutsceneEventType.function);
    }

    public FunctionCutsceneEventModel(Integer id, String functionID) {
        super(CutsceneEventType.function, id);
        this.functionID = functionID;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        FunctionCutsceneEventModel clone = new FunctionCutsceneEventModel();
        clone.setFunctionID(this.functionID);
        return clone;
    }

    public String getFunctionID() {
        return functionID;
    }

    public void setFunctionID(String functionID) {
        this.functionID = functionID;
    }

}
