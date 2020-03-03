package com.darkxell.client.mechanics.cutscene.end;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.model.cutscene.end.ResumeExplorationCutsceneEndModel;
import com.darkxell.client.state.dungeon.NextFloorState;

public class ResumeExplorationCutsceneEnd extends CutsceneEnd {

    public ResumeExplorationCutsceneEnd(Cutscene cutscene, ResumeExplorationCutsceneEndModel model) {
        super(cutscene, model);
    }

    @Override
    public void onCutsceneEnd() {
        super.onCutsceneEnd();
        NextFloorState.resumeExploration();
        Persistence.stateManager.setState(Persistence.dungeonState);
        Persistence.eventProcessor().processPending();
    }

}
