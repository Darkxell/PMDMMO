package com.darkxell.client.model.cutscene.end;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.PlayCutsceneCutsceneEnd;

@XmlRootElement(name = "playcutscene")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayCutsceneCutsceneEndModel extends CutsceneEndModel {

    @XmlAttribute(name = "id")
    private String cutsceneID;

    public PlayCutsceneCutsceneEndModel() {
        super(CutsceneEndType.PLAY_CUTSCENE);
    }

    public PlayCutsceneCutsceneEndModel(String cutsceneID, String function, boolean fading) {
        super(CutsceneEndType.PLAY_CUTSCENE, function, fading);
        this.cutsceneID = cutsceneID;
    }

    public String getCutsceneID() {
        return cutsceneID;
    }

    public void setCutsceneID(String cutsceneID) {
        this.cutsceneID = cutsceneID;
    }

    @Override
    protected CutsceneEndModel copyChild() {
        PlayCutsceneCutsceneEndModel clone = new PlayCutsceneCutsceneEndModel();
        clone.setCutsceneID(this.getCutsceneID());
        return clone;
    }

    @Override
    public CutsceneEnd build(Cutscene cutscene) {
        return new PlayCutsceneCutsceneEnd(cutscene, this);
    }

}
