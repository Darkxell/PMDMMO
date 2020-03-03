package com.darkxell.client.mechanics.cutscene.event;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.model.cutscene.event.AnimateCutsceneEventModel;
import com.darkxell.client.model.cutscene.event.AnimateCutsceneEventModel.AnimateCutsceneEventMode;
import com.darkxell.client.renderers.pokemon.CutscenePokemonRenderer;

public class AnimateCutsceneEvent extends CutsceneEvent implements AnimationEndListener {

    private PokemonAnimation animation;
    private boolean animationFinished = false;
    private boolean couldntLoad = false;
    private final AnimateCutsceneEventModel model;

    public AnimateCutsceneEvent(AnimateCutsceneEventModel model) {
        super(model);
        this.model = model;
    }

    public AnimateCutsceneEvent(AnimateCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
    }

    public AnimateCutsceneEventMode getMode() {
        return this.model.getMode();
    }

    public int getTarget() {
        return this.model.getTarget();
    }

    public int getAnimationID() {
        return this.model.getAnimationID();
    }

    @Override
    public boolean isOver() {
        if (this.couldntLoad || this.model.getMode() != AnimateCutsceneEventMode.PLAY)
            return true;
        if (this.animation == null)
            return false;
        return this.animationFinished;
    }

    @Override
    public void onAnimationEnd(AbstractAnimation animation) {
        this.animationFinished = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.animationFinished = false;
        CutsceneEntity entity = this.context.parent().player.getEntity(this.getTarget());
        if (entity instanceof CutscenePokemon) {
            this.animation = Animations.getCutsceneAnimation(this.getAnimationID(), (CutscenePokemon) entity,
                    this.getMode() == AnimateCutsceneEventMode.PLAY ? this : null);
            if (this.animation == null)
                this.couldntLoad = true;
            else {
                CutscenePokemonRenderer r = (CutscenePokemonRenderer) Persistence.currentmap.cutsceneEntityRenderers
                        .getRenderer(entity);
                if (r == null)
                    this.couldntLoad = true;
                else if (this.getMode() == AnimateCutsceneEventMode.STOP)
                    r.removeAnimation(this.animation.data);
                else {
                    if (this.getMode() == AnimateCutsceneEventMode.START) {
                        this.animation.source = this.animation.data;
                        this.animation.plays = -1;
                    }
                    this.animation.start();
                }
            }
        } else
            this.couldntLoad = true;
    }

}
