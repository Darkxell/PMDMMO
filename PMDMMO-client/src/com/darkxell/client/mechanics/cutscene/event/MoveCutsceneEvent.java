package com.darkxell.client.mechanics.cutscene.event;

import java.awt.Point;

import com.darkxell.client.mechanics.animation.travel.TravelAnimation;
import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.mechanics.cutscene.entity.CutsceneEntity;
import com.darkxell.client.model.cutscene.event.MoveCutsceneEventModel;

public class MoveCutsceneEvent extends CutsceneEvent {

    private CutsceneEntity entity;
    private final MoveCutsceneEventModel model;
    protected int tick = 0, duration = 0;
    protected TravelAnimation travel;

    public MoveCutsceneEvent(MoveCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
    }

    public double getSpeed() {
        return this.model.getSpeed();
    }

    public Integer getTarget() {
        return this.model.getTarget();
    }

    public Double getXPos() {
        return this.model.getX();
    }

    public Double getYPos() {
        return this.model.getY();
    }

    @Override
    public boolean isOver() {
        return this.tick == this.duration;
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (this.entity != null) {
            this.entity.xPos = this.travel.destination.getX();
            this.entity.yPos = this.travel.destination.getY();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.entity = this.context.parent().player.getEntity(this.getTarget());
        if (this.entity != null) {
            double startX = this.entity.xPos, startY = this.entity.yPos;
            double destX = this.getXPos() == null ? startX : this.getXPos(),
                    destY = this.getYPos() == null ? startY : this.getYPos();
            this.travel = new TravelAnimation(new Point.Double(startX, startY), new Point.Double(destX, destY));
            this.duration = (int) Math.floor(this.travel.distance.distance(new Point(0, 0)) / this.getSpeed());
        } else
            this.tick = this.duration;
    }

    @Override
    public void update() {
        super.update();
        if (!this.isOver()) {
            ++this.tick;
            this.travel.update(this.tick * 1d / this.duration);
        }
        if (this.entity != null) {
            this.entity.xPos = this.travel.current().getX();
            this.entity.yPos = this.travel.current().getY();
        }
    }

}
