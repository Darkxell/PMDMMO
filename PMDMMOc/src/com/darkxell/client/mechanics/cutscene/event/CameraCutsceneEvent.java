package com.darkxell.client.mechanics.cutscene.event;

import java.awt.Point;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;
import com.darkxell.client.mechanics.cutscene.CutsceneContext;
import com.darkxell.client.mechanics.cutscene.CutsceneEvent;
import com.darkxell.client.model.cutscene.event.CameraCutsceneEventModel;

public class CameraCutsceneEvent extends CutsceneEvent {

    private final CameraCutsceneEventModel model;
    protected int tick = 0, duration = 0;
    protected TravelAnimation travel;

    public CameraCutsceneEvent(CameraCutsceneEventModel model) {
        super(model);
        this.model = model;
    }

    public CameraCutsceneEvent(CameraCutsceneEventModel model, CutsceneContext context) {
        super(model, context);
        this.model = model;
    }

    public double getSpeed() {
        return this.model.getSpeed();
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
        if (Persistence.freezoneCamera != null) {
            Persistence.freezoneCamera.x = this.travel.destination.getX();
            Persistence.freezoneCamera.y = this.travel.destination.getY();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Persistence.freezoneCamera != null) {
            double startX = Persistence.freezoneCamera.x, startY = Persistence.freezoneCamera.y;
            double destX = this.getXPos() == null ? startX : this.getXPos(),
                    destY = this.getYPos() == null ? startY : this.getYPos();
            this.travel = new TravelAnimation(new Point.Double(startX, startY), new Point.Double(destX, destY));
            this.duration = (int) Math.floor(this.travel.distance.distance(new Point(0, 0)) / this.getSpeed());
        } else
            this.tick = this.duration;
    }

    @Override
    public String toString() {
        return this.displayID() + "Camera moves to X=" + (this.getXPos() == null ? "[UNCHANGED]" : this.getXPos())
                + ", Y=" + (this.getYPos() == null ? "[UNCHANGED]" : this.getYPos());
    }

    @Override
    public void update() {
        super.update();
        if (!this.isOver()) {
            ++this.tick;
            this.travel.update(this.tick * 1d / this.duration);
        }
        if (Persistence.freezoneCamera != null) {
            Persistence.freezoneCamera.x = this.travel.current().getX();
            Persistence.freezoneCamera.y = this.travel.current().getY();
        }
    }
}
