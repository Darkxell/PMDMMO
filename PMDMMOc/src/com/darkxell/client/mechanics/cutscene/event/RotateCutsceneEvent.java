package com.darkxell.client.mechanics.cutscene.event;

import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.model.cutscene.event.RotateCutsceneEventModel;

public class RotateCutsceneEvent extends CutsceneEvent {

    public static final int DEFAULT_SPEED = 5;

    private int currentDistance;
    private final RotateCutsceneEventModel model;
    private CutscenePokemon pokemon;
    private int tick;

    public RotateCutsceneEvent(RotateCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
    }

    public int getDistance() {
        return this.model.getDistance();
    }

    public int getSpeed() {
        return this.model.getSpeed();
    }

    public int getTarget() {
        return this.model.getTarget();
    }

    @Override
    public boolean isOver() {
        return this.currentDistance == Math.abs(this.getDistance());
    }

    @Override
    public void onStart() {
        super.onStart();
        CutsceneEntity entity = this.context.parent().player.getEntity(this.getTarget());
        if (entity instanceof CutscenePokemon) {
            this.pokemon = (CutscenePokemon) entity;
            this.currentDistance = this.tick = 0;

            if (this.rotatesInstantly()) {
                this.currentDistance = this.getDistance();
                for (int i = 0; i < Math.abs(this.getDistance()); ++i)
                    if (this.getDistance() > 0)
                        this.pokemon.facing = this.pokemon.facing.rotateClockwise();
                    else
                        this.pokemon.facing = this.pokemon.facing.rotateCounterClockwise();
            }
        } else
            this.currentDistance = this.getDistance();
    }

    private boolean rotatesInstantly() {
        return this.getSpeed() == 0;
    }

    @Override
    public String toString() {
        return this.displayID() + "(" + this.getTarget() + ") rotates clockwise " + this.getDistance() + " times";
    }

    @Override
    public void update() {
        super.update();
        if (!this.isOver()) {
            ++this.tick;
            if (this.tick >= this.getSpeed()) {
                if (this.getDistance() > 0)
                    this.pokemon.facing = this.pokemon.facing.rotateClockwise();
                else
                    this.pokemon.facing = this.pokemon.facing.rotateCounterClockwise();
                ++this.currentDistance;
                this.tick = 0;
            }
        }
    }

}
