package com.darkxell.client.model.cutscene.event;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "optionresult")
@XmlAccessorType(XmlAccessType.FIELD)
public class OptionResultCutsceneEventModel extends CutsceneEventModel {

    @XmlAttribute
    private int option;

    @XmlAttribute
    private int dialog;

    @XmlElementRef
    private ArrayList<CutsceneEventModel> results;

    public OptionResultCutsceneEventModel() {
        super(CutsceneEventType.optionresult);
    }

    public OptionResultCutsceneEventModel(Integer id, int option, int dialog, ArrayList<CutsceneEventModel> results) {
        super(CutsceneEventType.optionresult, id);
        this.option = option;
        this.dialog = dialog;
        this.results = results;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getDialog() {
        return dialog;
    }

    public void setDialog(int dialog) {
        this.dialog = dialog;
    }

    public ArrayList<CutsceneEventModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<CutsceneEventModel> results) {
        this.results = results;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        OptionResultCutsceneEventModel clone = new OptionResultCutsceneEventModel();
        clone.setOption(this.option);
        clone.setDialog(this.dialog);
        clone.setResults(new ArrayList<>());
        this.results.forEach(r -> clone.getResults().add(r.copy()));
        return clone;
    }

    @Override
    public String toString() {
        return this.displayID() + "If choice for event (" + this.dialog + ") is " + this.getOption() + ": Create "
                + this.results.size() + " events";
    }

}
