package com.darkxell.client.model.cutscene;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.darkxell.client.model.cutscene.end.CutsceneEndModel;
import com.darkxell.client.model.cutscene.event.CutsceneEventModel;

@XmlRootElement(name = "cutscene")
@XmlAccessorType(XmlAccessType.FIELD)
public class CutsceneModel implements Comparable<CutsceneModel> {

    @XmlElement
    private CutsceneCreationModel creation;

    @XmlElementRef
    @XmlElementWrapper(name = "events")
    private ArrayList<CutsceneEventModel> events;

    @XmlElementRef
    @XmlElementWrapper(name = "onfinish")
    private ArrayList<CutsceneEndModel> end;

    @XmlTransient
    public String name;

    public CutsceneModel() {
    }

    public CutsceneModel(CutsceneCreationModel creation, ArrayList<CutsceneEventModel> events, CutsceneEndModel end) {
        this.creation = creation;
        this.events = events;
        this.end = new ArrayList<>();
        this.end.add(end);
    }

    public CutsceneCreationModel getCreation() {
        return creation;
    }

    public CutsceneEndModel getEnd() {
        return this.end.get(0);
    }

    public ArrayList<CutsceneEventModel> getEvents() {
        return events;
    }

    public void setCreation(CutsceneCreationModel creation) {
        this.creation = creation;
    }

    public void setEnd(CutsceneEndModel end) {
        this.end.clear();
        this.end.add(end);
    }

    public void setEvents(ArrayList<CutsceneEventModel> events) {
        this.events = events;
    }

    public CutsceneModel copy() {
        ArrayList<CutsceneEventModel> event = new ArrayList<>();
        this.events.forEach(e -> event.add(e.copy()));
        return new CutsceneModel(this.creation.copy(), event, this.getEnd().copy());
    }

    @Override
    public int compareTo(CutsceneModel o) {
        return this.name.compareTo(o.name);
    }

}
