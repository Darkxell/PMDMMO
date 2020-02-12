package com.darkxell.common.model.dungeon;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.common.ai.AI.CustomAI;
import com.darkxell.common.dungeon.data.FloorSet;

@XmlRootElement(name = "encounter")
@XmlAccessorType(XmlAccessType.FIELD)
public class DungeonEncounterModel {

    /** If not null, a custom AI to give the Pokemon. */
    @XmlAttribute(name = "ai")
    private CustomAI aiType = CustomAI.NONE;

    /** The floors this Pokemon can appear on. */
    @XmlElement
    private FloorSet floors = null;

    /** The Pokemon ID. */
    @XmlAttribute
    private int id = 1;

    /** The Level of the Pokemon. */
    @XmlAttribute
    private int level = 1;

    /** The weight of the encounter. */
    @XmlAttribute
    private int weight = 1;

    public DungeonEncounterModel() {
    }

    public DungeonEncounterModel(int id, int level, int weight, CustomAI aiType, FloorSet floors) {
        this.id = id;
        this.level = level;
        this.weight = weight;
        this.aiType = aiType;
        this.floors = floors;
    }

    public CustomAI getAiType() {
        return aiType;
    }

    public FloorSet getFloors() {
        return floors;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public int getWeight() {
        return weight;
    }

    public void setAiType(CustomAI aiType) {
        this.aiType = aiType;
    }

    public void setFloors(FloorSet floors) {
        this.floors = floors;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
