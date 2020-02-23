package com.darkxell.client.model.cutscene;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.darkxell.common.util.XMLUtils.FreezoneInfoAdapter;
import com.darkxell.common.zones.FreezoneInfo;

@XmlRootElement(name = "creation")
@XmlAccessorType(XmlAccessType.FIELD)
public class CutsceneCreationModel {

    @XmlAttribute
    @XmlJavaTypeAdapter(FreezoneInfoAdapter.class)
    private FreezoneInfo freezone;

    @XmlAttribute(name = "camerax")
    private Double cameraX;

    @XmlAttribute(name = "cameray")
    private Double cameraY;

    @XmlAttribute
    private Boolean fading;

    @XmlAttribute
    private Boolean drawmap;

    @XmlElementRef
    private ArrayList<CutsceneEntityModel> entities;

    public CutsceneCreationModel() {
    }

    public CutsceneCreationModel(FreezoneInfo freezone, Double cameraX, Double cameraY, Boolean fading, Boolean drawmap,
            ArrayList<CutsceneEntityModel> entities) {
        this.freezone = freezone;
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.fading = fading;
        this.drawmap = drawmap;
        this.entities = entities;
    }

    public Double getCameraX() {
        return cameraX;
    }

    public Double getCameraY() {
        return cameraY;
    }

    public FreezoneInfo getFreezone() {
        return freezone;
    }

    public ArrayList<CutsceneEntityModel> getEntities() {
        return entities;
    }

    public Boolean isDrawmap() {
        return drawmap;
    }

    public Boolean isFading() {
        return fading;
    }

    public void setCameraX(Double cameraX) {
        this.cameraX = cameraX;
    }

    public void setCameraY(Double cameraY) {
        this.cameraY = cameraY;
    }

    public void setDrawmap(Boolean drawmap) {
        this.drawmap = drawmap;
    }

    public void setEntities(ArrayList<CutsceneEntityModel> entities) {
        this.entities = entities;
    }

    public void setFreezone(FreezoneInfo freezone) {
        this.freezone = freezone;
    }

    public void setFading(Boolean fading) {
        this.fading = fading;
    }

    public CutsceneCreationModel copy() {
        ArrayList<CutsceneEntityModel> entities = new ArrayList<>();
        this.entities.forEach(e -> entities.add(e.copy()));
        return new CutsceneCreationModel(this.freezone, this.cameraX, this.cameraY, fading, drawmap, entities);
    }

}
