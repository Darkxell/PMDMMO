package com.darkxell.common.model.dungeon;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.common.dungeon.data.Dungeon.DungeonDirection;
import com.darkxell.common.dungeon.data.DungeonItemGroup;
import com.darkxell.common.dungeon.data.DungeonTrapGroup;
import com.darkxell.common.dungeon.data.FloorData;

@XmlRootElement(name = "dungeon")
@XmlAccessorType(XmlAccessType.FIELD)
public class DungeonModel {

    /** Lists the buried Items found in this Dungeon. If empty, use the standard list. */
    @XmlElement(name = "buried")
    private ArrayList<DungeonItemGroup> buriedItems = new ArrayList<>();

    /** Whether this Dungeon goes up or down. See {@link DungeonDirection} */
    @XmlAttribute
    private DungeonDirection direction;

    /** Lists the Pokemon found in this Dungeon. */
    @XmlElement(name = "pokemon")
    @XmlElementWrapper(name = "encounters")
    private ArrayList<DungeonEncounterModel> encounters = new ArrayList<>();

    /** The number of Floors in this Dungeon. */
    @XmlAttribute(name = "floors")
    private int floorCount;

    /** Describes this Dungeon's Floors' data. */
    @XmlElement(name = "data")
    private ArrayList<FloorData> floorData = new ArrayList<>();

    /** This Dungeon's ID. */
    @XmlAttribute
    private int id;

    /** Lists the Items found in this Dungeon. */
    @XmlElement
    private ArrayList<DungeonItemGroup> items = new ArrayList<>();

    /** ID of the Dungeon this Dungeon leads to (e.g. Mt. Blaze to Mt. Blaze Peak). -1 if no leading Dungeon. */
    @XmlAttribute(name = "linked")
    private int linkedTo;

    /** The x position in pixels of the dungeon on the dungeons world map. */
    @XmlAttribute
    private int mapX;

    /** The y position in pixels of the dungeon on the dungeons world map. */
    @XmlAttribute
    private int mapY;

    /** True if Pokemon from this Dungeon can be recruited. */
    @XmlAttribute(name = "recruits")
    private boolean recruitsAllowed;

    /** Lists the Items found in this Dungeon's shops. */
    @XmlElement(name = "shops")
    private ArrayList<DungeonItemGroup> shopItems = new ArrayList<>();

    /** The chance of an Item being Sticky in this Dungeon. */
    @XmlAttribute(name = "sticky")
    private int stickyChance;

    /** The number of turns to spend on a single floor before being kicked. */
    @XmlAttribute(name = "limit")
    private int timeLimit;

    /** Lists the Traps found in this Dungeon. */
    @XmlElement
    private ArrayList<DungeonTrapGroup> traps = new ArrayList<>();

    /** The Weather in this Dungeon (Weather ID -> floors). */
    @XmlElement(name = "w")
    @XmlElementWrapper(name = "weather")
    private ArrayList<DungeonWeatherModel> weather = new ArrayList<>();

    public DungeonModel() {
    }

    public DungeonModel(int id, int floorCount, DungeonDirection direction, boolean recruits, int timeLimit,
            int stickyChance, int linkedTo, ArrayList<DungeonEncounterModel> encounters,
            ArrayList<DungeonItemGroup> items, ArrayList<DungeonItemGroup> shopItems,
            ArrayList<DungeonItemGroup> buriedItems, ArrayList<DungeonTrapGroup> traps, ArrayList<FloorData> floorData,
            ArrayList<DungeonWeatherModel> weather, int mapx, int mapy) {
        this.id = id;
        this.floorCount = floorCount;
        this.direction = direction;
        this.recruitsAllowed = recruits;
        this.timeLimit = timeLimit;
        this.stickyChance = stickyChance;
        this.linkedTo = linkedTo;
        this.encounters = encounters;
        this.items = items;
        this.shopItems = shopItems;
        this.buriedItems = buriedItems;
        this.traps = traps;
        this.floorData = floorData;
        this.weather = weather;
        this.mapX = mapx;
        this.mapY = mapy;
    }

    public ArrayList<DungeonItemGroup> getBuriedItems() {
        return buriedItems;
    }

    public DungeonDirection getDirection() {
        return direction;
    }

    public ArrayList<DungeonEncounterModel> getEncounters() {
        return encounters;
    }

    public int getFloorCount() {
        return floorCount;
    }

    public ArrayList<FloorData> getFloorData() {
        return floorData;
    }

    public int getId() {
        return id;
    }

    public ArrayList<DungeonItemGroup> getItems() {
        return items;
    }

    public int getLinkedTo() {
        return linkedTo;
    }

    public int getMapX() {
        return mapX;
    }

    public int getMapY() {
        return mapY;
    }

    public ArrayList<DungeonItemGroup> getShopItems() {
        return shopItems;
    }

    public int getStickyChance() {
        return stickyChance;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public ArrayList<DungeonTrapGroup> getTraps() {
        return traps;
    }

    public ArrayList<DungeonWeatherModel> getWeather() {
        return weather;
    }

    public boolean isRecruitsAllowed() {
        return recruitsAllowed;
    }

    public void setBuriedItems(ArrayList<DungeonItemGroup> buriedItems) {
        this.buriedItems = buriedItems;
    }

    public void setDirection(DungeonDirection direction) {
        this.direction = direction;
    }

    public void setEncounters(ArrayList<DungeonEncounterModel> encounters) {
        this.encounters = encounters;
    }

    public void setFloorCount(int floorCount) {
        this.floorCount = floorCount;
    }

    public void setFloorData(ArrayList<FloorData> floorData) {
        this.floorData = floorData;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItems(ArrayList<DungeonItemGroup> items) {
        this.items = items;
    }

    public void setLinkedTo(int linkedTo) {
        this.linkedTo = linkedTo;
    }

    public void setMapx(int mapx) {
        this.mapX = mapx;
    }

    public void setMapy(int mapy) {
        this.mapY = mapy;
    }

    public void setRecruitsAllowed(boolean recruitsAllowed) {
        this.recruitsAllowed = recruitsAllowed;
    }

    public void setShopItems(ArrayList<DungeonItemGroup> shopItems) {
        this.shopItems = shopItems;
    }

    public void setStickyChance(int stickyChance) {
        this.stickyChance = stickyChance;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void setTraps(ArrayList<DungeonTrapGroup> traps) {
        this.traps = traps;
    }

    public void setWeather(ArrayList<DungeonWeatherModel> weather) {
        this.weather = weather;
    }

}
