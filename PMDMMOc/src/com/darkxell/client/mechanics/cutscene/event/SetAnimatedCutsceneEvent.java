package com.darkxell.client.mechanics.cutscene.event;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.model.cutscene.event.SetAnimatedCutsceneEventModel;

public class SetAnimatedCutsceneEvent extends CutsceneEvent {

    private final SetAnimatedCutsceneEventModel model;

    public SetAnimatedCutsceneEvent(SetAnimatedCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
    }

    public int getTarget() {
        return this.model.getTarget();
    }

    public boolean isAnimated() {
        return this.model.isAnimated();
    }

    @Override
    public void onStart() {
        super.onStart();
        CutsceneEntity entity = this.context.parent().player.getEntity(this.getTarget());
        if (entity instanceof CutscenePokemon)
            ((CutscenePokemon) entity).animated = this.isAnimated();
    }

    @Override
    public String toString() {
        return this.displayID() + "(" + this.getTarget() + ") becomes " + (this.isAnimated() ? "" : "not ") + "animated";
    }

}
