package com.darkxell.client.model.cutscene.end;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.EnterDungeonCutsceneEnd;

@XmlRootElement(name = "enterdungeon")
@XmlAccessorType(XmlAccessType.FIELD)
public class EnterDungeonCutsceneEndModel extends CutsceneEndModel {

    @XmlAttribute(name = "id")
    private int dungeonID;

    public EnterDungeonCutsceneEndModel() {
        super(CutsceneEndType.ENTER_DUNGEON);
    }

    public EnterDungeonCutsceneEndModel(int dungeonID, String function, boolean fading) {
        super(CutsceneEndType.ENTER_DUNGEON, function, fading);
        this.dungeonID = dungeonID;
    }

    @Override
    protected CutsceneEndModel copyChild() {
        EnterDungeonCutsceneEndModel clone = new EnterDungeonCutsceneEndModel();
        clone.setDungeonID(this.getDungeonID());
        return clone;
    }

    public int getDungeonID() {
        return dungeonID;
    }

    public void setDungeonID(int cutsceneID) {
        this.dungeonID = cutsceneID;
    }

    @Override
    public CutsceneEnd build(Cutscene cutscene) {
        return new EnterDungeonCutsceneEnd(cutscene, this);
    }

}
