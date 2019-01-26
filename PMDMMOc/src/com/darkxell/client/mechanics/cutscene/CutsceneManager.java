package com.darkxell.client.mechanics.cutscene;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.resources.Res;
import com.darkxell.client.state.TransitionState;
import com.darkxell.client.state.freezone.CutsceneState;
import com.darkxell.common.util.XMLUtils;

import java.io.InputStream;

public class CutsceneManager {
    public static Cutscene loadCutscene(String id) {
        InputStream is = Res.get("/cutscenes/" + id + ".xml");
        if (is == null) {
            return null;
        }
        return new Cutscene(id, XMLUtils.read(is));
    }

    public static void playCutscene(String id) {
        playCutscene(id, true);
    }

    public static void playCutscene(String id, boolean fading) {
        Cutscene c = loadCutscene(id);
        if (c == null) {
            return;
        }
        Persistence.cutsceneState = new CutsceneState(c);

        if (fading) {
            Persistence.stateManager.setState(
                    new TransitionState(Persistence.stateManager.getCurrentState(), Persistence.cutsceneState) {
                        @Override
                        public void onTransitionHalf() {
                            super.onTransitionHalf();
                            c.creation.create();
                        }
                    });
        } else {
            c.creation.create();
            Persistence.stateManager.setState(Persistence.cutsceneState);
        }
    }
}
