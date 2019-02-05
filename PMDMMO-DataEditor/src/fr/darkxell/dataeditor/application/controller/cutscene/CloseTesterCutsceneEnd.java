package fr.darkxell.dataeditor.application.controller.cutscene;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.launchable.render.RenderProfile;
import com.darkxell.client.mechanics.cutscene.Cutscene;
import com.darkxell.client.mechanics.cutscene.Cutscene.CutsceneEnd;

public class CloseTesterCutsceneEnd extends CutsceneEnd {

    public CloseTesterCutsceneEnd(Cutscene c) {
        super(c, (String) null, false);
    }

    @Override
    public void onCutsceneEnd() {
        Launcher.setProcessingProfile(RenderProfile.PROFILE_UNDEFINED);
        Persistence.soundmanager.setBackgroundMusic(null);
        Persistence.frame.dispose();
    }

    @Override
    protected String xmlName() {
        return "closetester";
    }

}
