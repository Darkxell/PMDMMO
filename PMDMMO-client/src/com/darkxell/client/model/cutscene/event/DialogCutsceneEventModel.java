package com.darkxell.client.model.cutscene.event;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneDialogScreenModel;
import com.darkxell.client.model.cutscene.common.CutsceneEventType;
import com.darkxell.common.util.language.Message;

@XmlRootElement(name = "dialog")
@XmlAccessorType(XmlAccessType.FIELD)
public class DialogCutsceneEventModel extends CutsceneEventModel {

    @XmlAttribute(name = "isnarrator")
    private Boolean isNarratorDialog;

    @XmlElement(name = "dialogscreen")
    private ArrayList<CutsceneDialogScreenModel> screens;

    public DialogCutsceneEventModel() {
        super(CutsceneEventType.dialog);
    }

    public DialogCutsceneEventModel(Integer id, Boolean isNarratorDialog,
            ArrayList<CutsceneDialogScreenModel> screens) {
        super(CutsceneEventType.dialog, id);
        this.isNarratorDialog = isNarratorDialog;
        this.screens = screens;
    }

    public ArrayList<CutsceneDialogScreenModel> getScreens() {
        return screens;
    }

    public void setScreens(ArrayList<CutsceneDialogScreenModel> screens) {
        this.screens = screens;
    }

    public Boolean getIsNarratorDialog() {
        return isNarratorDialog;
    }

    public void setIsNarratorDialog(Boolean isNarratorDialog) {
        this.isNarratorDialog = isNarratorDialog;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        DialogCutsceneEventModel clone = new DialogCutsceneEventModel();
        clone.setIsNarratorDialog(this.isNarratorDialog);
        clone.setScreens(new ArrayList<>());
        this.screens.forEach(s -> clone.getScreens().add(s.copy()));
        return clone;
    }

    @Override
    public String toString() {
        Message m = null;
        if (this.screens.size() > 0) {
            m = new Message(this.screens.get(0).getText(), this.screens.get(0).getTranslate());
        }
        return this.displayID() + "Dialog: " + (m == null ? "[nothing]" : m + "...");
    }

}
