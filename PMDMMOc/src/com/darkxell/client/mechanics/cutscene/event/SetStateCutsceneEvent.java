package com.darkxell.client.mechanics.cutscene.event;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.model.cutscene.event.SetStateCutsceneEventModel;
import com.darkxell.client.resources.image.pokemon.body.PokemonSpriteState;

public class SetStateCutsceneEvent extends CutsceneEvent {

    private final SetStateCutsceneEventModel model;

    public SetStateCutsceneEvent(SetStateCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
    }

    public PokemonSpriteState getState() {
        return this.model.getState();
    }

    public int getTarget() {
        return this.model.getTarget();
    }

    @Override
    public void onStart() {
        super.onStart();
        CutsceneEntity entity = this.context.parent().player.getEntity(this.getTarget());
        if (entity instanceof CutscenePokemon)
            ((CutscenePokemon) entity).currentState = this.getState();
    }

    @Override
    public String toString() {
        return this.displayID() + "(" + this.getTarget() + ") gains state " + this.getState();
    }

}
