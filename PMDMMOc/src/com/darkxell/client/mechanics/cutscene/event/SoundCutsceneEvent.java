package com.darkxell.client.mechanics.cutscene.event;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.model.cutscene.event.SoundCutsceneEventModel;
import com.darkxell.client.resources.music.SoundManager;

public class SoundCutsceneEvent extends CutsceneEvent {

    private SoundCutsceneEventModel model;

    public SoundCutsceneEvent(SoundCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
    }

    public String getSoundID() {
        return this.model.getSoundID();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.getSoundID() != null)
            if (this.playOverMusic())
                SoundManager.playSoundOverMusic(this.getSoundID());
            else
                SoundManager.playSound(this.getSoundID());
    }

    public boolean playOverMusic() {
        return this.model.getPlayOverMusic();
    }

    @Override
    public String toString() {
        return this.displayID() + "Play " + this.getSoundID() + (this.playOverMusic() ? " over music" : "");
    }

}
