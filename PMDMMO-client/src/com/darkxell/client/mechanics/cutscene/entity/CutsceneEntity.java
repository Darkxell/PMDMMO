package com.darkxell.client.mechanics.cutscene.entity;

import com.darkxell.client.model.cutscene.CutsceneEntityModel;
import com.darkxell.client.renderers.AbstractRenderer;

public class CutsceneEntity {

    private final CutsceneEntityModel model;
    public double xPos, yPos;

    public CutsceneEntity(CutsceneEntityModel model) {
        this.model = model;
        this.xPos = model.getXPos() == null ? 0 : model.getXPos();
        this.yPos = model.getYPos() == null ? 0 : model.getYPos();
    }

    public AbstractRenderer createRenderer() {
        return null;
    }

    public Integer getID() {
        return this.model.getCutsceneID();
    }

    @Override
    public String toString() {
        return this.model.getCutsceneID() + " @ X=" + this.xPos + ", Y=" + this.yPos;
    }

}
