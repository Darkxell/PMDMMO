package com.darkxell.client.mechanics.cutscene.event;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.model.cutscene.event.DespawnCutsceneEventModel;

public class DespawnCutsceneEvent extends CutsceneEvent {

    private final DespawnCutsceneEventModel model;

    public DespawnCutsceneEvent(DespawnCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
    }

    public int getTarget() {
        return this.model.getTarget();
    }

    @Override
    public void onStart() {
        super.onStart();
        CutsceneEntity entity = this.context.parent().player.getEntity(this.getTarget());
        if (entity != null)
            this.context.parent().player.removeEntity(entity);
    }

}
