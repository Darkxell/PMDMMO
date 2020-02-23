package com.darkxell.client.model.cutscene.event;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "wait")
@XmlAccessorType(XmlAccessType.FIELD)
public class WaitCutsceneEventModel extends CutsceneEventModel {

    @XmlElement(name = "event")
    private ArrayList<Integer> events;

    public WaitCutsceneEventModel() {
        super(CutsceneEventType.wait);
    }

    public WaitCutsceneEventModel(Integer id, ArrayList<Integer> events) {
        super(CutsceneEventType.wait, id);
        this.events = events;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        WaitCutsceneEventModel clone = new WaitCutsceneEventModel();
        clone.setEvents(new ArrayList<>(this.events));
        return clone;
    }

    public ArrayList<Integer> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Integer> events) {
        this.events = events;
    }

}
