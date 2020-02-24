package com.darkxell.client.mechanics.cutscene;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.Freezones;
import com.darkxell.client.mechanics.freezones.entities.FreezoneCamera;
import com.darkxell.client.model.cutscene.CutsceneCreationModel;
import com.darkxell.client.model.cutscene.CutsceneEntityModel;
import com.darkxell.common.zones.FreezoneInfo;

public class CutsceneCreation {

    Cutscene cutscene;
    private final CutsceneCreationModel model;

    public CutsceneCreation(Cutscene cutscene, CutsceneCreationModel model) {
        this.cutscene = cutscene;
        this.model = model;
    }

    public void create() {
        Persistence.currentmap = Freezones.loadMap(this.getFreezone());
        Persistence.freezoneCamera = new FreezoneCamera(null);
        if (this.getCameraX() != null)
            Persistence.freezoneCamera.x = this.getCameraX();
        if (this.getCameraY() != null)
            Persistence.freezoneCamera.y = this.getCameraY();
        this.cutscene.player.mapAlpha = this.getDrawMap() ? 1 : 0;
        for (CutsceneEntityModel entity : this.getEntities())
            this.cutscene.player.createEntity(entity);
    }

    public Double getCameraX() {
        return this.model.getCameraX();
    }

    public Double getCameraY() {
        return this.model.getCameraY();
    }

    public boolean getDrawMap() {
        return this.model.isDrawmap();
    }

    public ArrayList<CutsceneEntityModel> getEntities() {
        ArrayList<CutsceneEntityModel> entities = new ArrayList<>();
        this.model.getEntities().forEach(e -> entities.add(e.copy()));
        return entities;
    }

    public boolean getFading() {
        return this.model.isFading();
    }

    public FreezoneInfo getFreezone() {
        return this.model.getFreezone();
    }

    public CutsceneCreationModel getModel() {
        return this.model;
    }

}
