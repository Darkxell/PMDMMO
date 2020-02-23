package com.darkxell.client.mechanics.cutscene.end;

import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.mechanics.cutscene.CutsceneManager;
import com.darkxell.client.model.cutscene.end.PlayCutsceneCutsceneEndModel;

public class PlayCutsceneCutsceneEnd extends CutsceneEnd {

    private final PlayCutsceneCutsceneEndModel model;

    public PlayCutsceneCutsceneEnd(Cutscene cutscene, PlayCutsceneCutsceneEndModel model) {
        super(cutscene, model);
        this.model = model;
    }

    public String getCutsceneID() {
        return this.model.getCutsceneID();
    }

    @Override
    public void onCutsceneEnd() {
        super.onCutsceneEnd();
        if (this.getCutsceneID() != null)
            CutsceneManager.playCutscene(this.getCutsceneID(), this.fadesOut());
    }

}
