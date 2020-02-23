package com.darkxell.client.model.cutscene.event;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneDialogScreenModel;
import com.darkxell.client.model.cutscene.common.CutsceneEventType;
import com.darkxell.client.model.cutscene.common.MessageModel;

@XmlRootElement(name = "option")
public class OptionDialogCutsceneEventModel extends CutsceneEventModel {

    @XmlElement
    private CutsceneDialogScreenModel question;

    @XmlElement(name = "option")
    private ArrayList<MessageModel> options;

    public OptionDialogCutsceneEventModel() {
        super(CutsceneEventType.option);
    }

    public OptionDialogCutsceneEventModel(Integer id, CutsceneDialogScreenModel question,
            ArrayList<MessageModel> options) {
        super(CutsceneEventType.option, id);
        this.question = question;
        this.options = options;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        OptionDialogCutsceneEventModel clone = new OptionDialogCutsceneEventModel();
        clone.setQuestion(this.question.copy());
        clone.setOptions(new ArrayList<>());
        this.options.forEach(o -> clone.getOptions().add(o.copy()));
        return clone;
    }

    public CutsceneDialogScreenModel getQuestion() {
        return question;
    }

    public void setQuestion(CutsceneDialogScreenModel question) {
        this.question = question;
    }

    public ArrayList<MessageModel> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<MessageModel> options) {
        this.options = options;
    }

}
