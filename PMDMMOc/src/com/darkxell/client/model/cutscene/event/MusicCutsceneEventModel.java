package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "music")
@XmlAccessorType(XmlAccessType.FIELD)
public class MusicCutsceneEventModel extends CutsceneEventModel {

    @XmlAttribute(name = "music")
    private String soundtrackID;

    public MusicCutsceneEventModel() {
        super(CutsceneEventType.music);
    }

    public MusicCutsceneEventModel(Integer id, String soundtrackID) {
        super(CutsceneEventType.music, id);
        this.soundtrackID = soundtrackID;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        MusicCutsceneEventModel clone = new MusicCutsceneEventModel();
        clone.setSoundtrackID(this.soundtrackID);
        return clone;
    }

    public String getSoundtrackID() {
        return soundtrackID;
    }

    public void setSoundtrackID(String soundtrackID) {
        this.soundtrackID = soundtrackID;
    }

    @Override
    public String toString() {
        return this.displayID() + "Music set to " + this.getSoundtrackID();
    }

}
