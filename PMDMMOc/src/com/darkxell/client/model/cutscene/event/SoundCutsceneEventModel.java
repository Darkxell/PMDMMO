package com.darkxell.client.model.cutscene.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.model.cutscene.common.CutsceneEventType;

@XmlRootElement(name = "sound")
@XmlAccessorType(XmlAccessType.FIELD)
public class SoundCutsceneEventModel extends CutsceneEventModel {

    @XmlAttribute(name = "sound")
    private String soundID;

    @XmlAttribute(name = "overmusic")
    private Boolean playOverMusic;

    public SoundCutsceneEventModel() {
        super(CutsceneEventType.sound);
    }

    public SoundCutsceneEventModel(Integer id, String soundID, Boolean playOverMusic) {
        super(CutsceneEventType.sound, id);
        this.soundID = soundID;
        this.playOverMusic = playOverMusic;
    }

    @Override
    protected CutsceneEventModel createCopy() {
        SoundCutsceneEventModel clone = new SoundCutsceneEventModel();
        clone.setSoundID(this.soundID);
        clone.setPlayOverMusic(this.playOverMusic);
        return clone;
    }

    public String getSoundID() {
        return soundID;
    }

    public void setSoundID(String soundID) {
        this.soundID = soundID;
    }

    public Boolean getPlayOverMusic() {
        return playOverMusic;
    }

    public void setPlayOverMusic(Boolean playOverMusic) {
        this.playOverMusic = playOverMusic;
    }

}
