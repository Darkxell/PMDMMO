package com.darkxell.client.mechanics.cutscene;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.model.cutscene.CutsceneModel;
import com.darkxell.client.model.io.ClientModelIOHandlers;
import com.darkxell.client.state.TransitionState;
import com.darkxell.client.state.freezone.CutsceneState;

public class CutsceneManager {
    public static Cutscene loadCutscene(String id) {
        CutsceneModel model = ClientModelIOHandlers.cutscene.read(CutsceneManager.class.getResource("/cutscenes/" + id + ".xml"));
        return new Cutscene(id, model);
    }

    public static void playCutscene(String id, boolean fading) {
        Cutscene c = loadCutscene(id);
        if (c == null)
            return;
        Persistence.cutsceneState = new CutsceneState(c);

        if (fading)
            Persistence.stateManager.setState(
                    new TransitionState(Persistence.stateManager.getCurrentState(), Persistence.cutsceneState) {
                        @Override
                        public void onTransitionHalf() {
                            super.onTransitionHalf();
                            c.creation.create();
                        }
                    });
        else {
            c.creation.create();
            Persistence.stateManager.setState(Persistence.cutsceneState);
        }
    }
}
