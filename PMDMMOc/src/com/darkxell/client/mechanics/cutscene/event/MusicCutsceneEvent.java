package com.darkxell.client.mechanics.cutscene.event;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.model.cutscene.event.MusicCutsceneEventModel;
import com.darkxell.client.resources.music.SoundsHolder;

public class MusicCutsceneEvent extends CutsceneEvent {

    private final MusicCutsceneEventModel model;

    public MusicCutsceneEvent(MusicCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
    }
    
    public String getSoundtrackID() {
        return this.model.getSoundtrackID();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.getSoundtrackID() != null) {
            if (this.getSoundtrackID().equals("null"))
                Persistence.soundmanager.setBackgroundMusic(null);
            else
                Persistence.soundmanager.setBackgroundMusic(SoundsHolder.getSong(this.getSoundtrackID()));
        }
    }

    @Override
    public String toString() {
        return this.displayID() + "Music set to " + this.getSoundtrackID();
    }

}
