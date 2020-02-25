package com.darkxell.client.model.cutscene.end;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.mechanics.cutscene.end.ResumeExplorationCutsceneEnd;

@XmlRootElement(name = "resumeexploration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResumeExplorationCutsceneEndModel extends CutsceneEndModel {

    public ResumeExplorationCutsceneEndModel() {
        super(CutsceneEndType.RESUME_EXPLORATION);
    }

    public ResumeExplorationCutsceneEndModel(String function, boolean fading) {
        super(CutsceneEndType.RESUME_EXPLORATION, function, fading);
    }

    @Override
    protected CutsceneEndModel copyChild() {
        return new ResumeExplorationCutsceneEndModel();
    }

    @Override
    public CutsceneEnd build(Cutscene cutscene) {
        return new ResumeExplorationCutsceneEnd(cutscene, this);
    }

}
