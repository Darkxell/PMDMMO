package fr.darkxell.dataeditor.application.controller.cutscene;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.launchable.render.RenderProfile;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;
import com.darkxell.client.model.cutscene.end.CutsceneEndModel;

public class CloseTesterCutsceneEnd extends CutsceneEnd {

    public static class CloseTesterCutsceneEndModel extends CutsceneEndModel {

        public CloseTesterCutsceneEndModel() {
            super(null);
        }

        @Override
        public CutsceneEnd build(Cutscene cutscene) {
            return new CloseTesterCutsceneEnd(cutscene, this);
        }

        @Override
        protected CutsceneEndModel copyChild() {
            return new CloseTesterCutsceneEndModel();
        }

    }

    public CloseTesterCutsceneEnd(Cutscene c, CloseTesterCutsceneEndModel model) {
        super(c, model);
    }

    @Override
    public void onCutsceneEnd() {
        Launcher.setProcessingProfile(RenderProfile.PROFILE_UNDEFINED);
        Persistence.soundmanager.setBackgroundMusic(null);
        Persistence.frame.dispose();
    }

}
