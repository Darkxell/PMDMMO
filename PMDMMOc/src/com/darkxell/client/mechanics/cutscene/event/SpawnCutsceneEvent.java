package com.darkxell.client.mechanics.cutscene.event;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.model.cutscene.event.SpawnCutsceneEventModel;

public class SpawnCutsceneEvent extends CutsceneEvent {

    private final SpawnCutsceneEventModel model;

    public SpawnCutsceneEvent(SpawnCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model=model;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.context.parent().player.createEntity(this.model.getEntity());
    }

    @Override
    public String toString() {
        return this.displayID() + "Spawn " + this.model.getEntity();
    }

}
