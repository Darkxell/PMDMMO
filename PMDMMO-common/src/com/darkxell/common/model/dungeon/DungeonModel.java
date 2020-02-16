package com.darkxell.common.model.dungeon;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.darkxell.common.dungeon.data.Dungeon.DungeonDirection;

@XmlRootElement(name = "dungeon")
@XmlAccessorType(XmlAccessType.FIELD)
public class DungeonModel {

    /** This Dungeon's ID. */
    @XmlAttribute
    private int id;

    /** The number of Floors in this Dungeon. */
    @XmlAttribute(name = "floors")
    private int floorCount;

    /** Whether this Dungeon goes up or down. See {@link DungeonDirection} */
    @XmlAttribute
    private DungeonDirection direction;

    /** True if Pokemon from this Dungeon can be recruited. */
    @XmlAttribute(name = "recruits")
    private boolean recruitsAllowed;

    /** The chance of an Item being Sticky in this Dungeon. */
    @XmlAttribute(name = "sticky")
    private int stickyChance;

    /** The number of turns to spend on a single floor before being kicked. */
    @XmlAttribute(name = "limit")
    private int timeLimit;

    /** ID of the Dungeon this Dungeon leads to (e.g. Mt. Blaze to Mt. Blaze Peak). -1 if no leading Dungeon. */
    @XmlAttribute(name = "linked")
    private int linkedTo;

    /** The x position in pixels of the dungeon on the dungeons world map. */
    @XmlAttribute(name = "mapx")
    private int mapX;

    /** The y position in pixels of the dungeon on the dungeons world map. */
    @XmlAttribute(name = "mapy")
    private int mapY;

    /** Lists the Pokemon found in this Dungeon. */
    @XmlElement(name = "encounter")
    @XmlElementWrapper(name = "encounters")
    private ArrayList<DungeonEncounterModel> encounters = new ArrayList<>();

    /** Lists the Items found in this Dungeon. */
    @XmlElement(name = "group")
    @XmlElementWrapper(name = "items")
    private ArrayList<DungeonItemGroupModel> items = new ArrayList<>();

    /** Lists the Items found in this Dungeon's shops. */
    @XmlElement(name = "group")
    @XmlElementWrapper(name = "shops")
    private ArrayList<DungeonItemGroupModel> shopItems = new ArrayList<>();

    /** Lists the buried Items found in this Dungeon. If empty, use the standard list. */
    @XmlElement(name = "group")
    @XmlElementWrapper(name = "buried")
    private ArrayList<DungeonItemGroupModel> buriedItems = new ArrayList<>();

    /** Describes this Dungeon's Floors' data. */
    @XmlElement(name = "d")
    @XmlElementWrapper(name = "data")
    private ArrayList<FloorDataModel> floorData = new ArrayList<>();

    /** Lists the Traps found in this Dungeon. */
    @XmlElement(name = "trap")
    @XmlElementWrapper(name = "traps")
    private ArrayList<DungeonTrapGroupModel> traps = new ArrayList<>();

    /** The Weather in this Dungeon (Weather ID -> floors). */
    @XmlElement(name = "w")
    @XmlElementWrapper(name = "weather")
    private ArrayList<DungeonWeatherModel> weather = new ArrayList<>();

    public DungeonModel() {
    }

    public DungeonModel(int id, int floorCount, DungeonDirection direction, boolean recruits, int timeLimit,
            int stickyChance, int linkedTo, ArrayList<DungeonEncounterModel> encounters,
            ArrayList<DungeonItemGroupModel> items, ArrayList<DungeonItemGroupModel> shopItems,
            ArrayList<DungeonItemGroupModel> buriedItems, ArrayList<DungeonTrapGroupModel> traps,
            ArrayList<FloorDataModel> floorData, ArrayList<DungeonWeatherModel> weather, int mapx, int mapy) {
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

    public DungeonModel copy() {
        ArrayList<DungeonEncounterModel> encounters = new ArrayList<>();
        ArrayList<FloorDataModel> floorData = new ArrayList<>();
        ArrayList<DungeonItemGroupModel> items = new ArrayList<>();
        ArrayList<DungeonItemGroupModel> buriedItems = new ArrayList<>();
        ArrayList<DungeonItemGroupModel> shopItems = new ArrayList<>();
        ArrayList<DungeonTrapGroupModel> traps = new ArrayList<>();
        ArrayList<DungeonWeatherModel> weather = new ArrayList<>();
        this.encounters.forEach(e -> encounters.add(e.copy()));
        this.floorData.forEach(f -> floorData.add(f.copy()));
        this.items.forEach(i -> items.add(i.copy()));
        this.buriedItems.forEach(i -> buriedItems.add(i.copy()));
        this.shopItems.forEach(i -> shopItems.add(i.copy()));
        this.traps.forEach(t -> traps.add(t.copy()));
        this.weather.forEach(w -> weather.add(w.copy()));
        return new DungeonModel(id, floorCount, direction, this.recruitsAllowed, timeLimit, stickyChance, linkedTo,
                encounters, items, shopItems, buriedItems, traps, floorData, weather, this.mapX, this.mapY);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DungeonModel))
            return false;

        DungeonModel o = (DungeonModel) obj;
        if (this.id != o.id || this.direction != o.direction || this.floorCount != o.floorCount
                || this.linkedTo != o.linkedTo || this.mapX != o.mapX || this.mapY != o.mapY
                || this.recruitsAllowed != o.recruitsAllowed || this.stickyChance != o.stickyChance
                || this.timeLimit != o.timeLimit || this.buriedItems.size() != o.buriedItems.size()
                || this.encounters.size() != o.encounters.size() || this.floorData.size() != o.floorData.size()
                || this.items.size() != o.items.size() || this.shopItems.size() != o.shopItems.size()
                || this.traps.size() != o.traps.size() || this.weather.size() != o.weather.size())

            return false;

        for (int i = 0; i < this.buriedItems.size(); ++i) {
            if (!this.buriedItems.get(i).equals(o.buriedItems.get(i)))
                return false;
        }

        for (int i = 0; i < this.encounters.size(); ++i) {
            if (!this.encounters.get(i).equals(o.encounters.get(i)))
                return false;
        }

        for (int i = 0; i < this.items.size(); ++i) {
            if (!this.items.get(i).equals(o.items.get(i)))
                return false;
        }

        for (int i = 0; i < this.shopItems.size(); ++i) {
            if (!this.shopItems.get(i).equals(o.shopItems.get(i)))
                return false;
        }

        for (int i = 0; i < this.floorData.size(); ++i) {
            if (!this.floorData.get(i).equals(o.floorData.get(i)))
                return false;
        }

        for (int i = 0; i < this.weather.size(); ++i) {
            if (!this.weather.get(i).equals(o.weather.get(i)))
                return false;
        }

        return true;
    }

    public ArrayList<DungeonItemGroupModel> getBuriedItems() {
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

    public ArrayList<FloorDataModel> getFloorData() {
        return floorData;
    }

    public int getId() {
        return id;
    }

    public ArrayList<DungeonItemGroupModel> getItems() {
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

    public ArrayList<DungeonItemGroupModel> getShopItems() {
        return shopItems;
    }

    public int getStickyChance() {
        return stickyChance;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public ArrayList<DungeonTrapGroupModel> getTraps() {
        return traps;
    }

    public ArrayList<DungeonWeatherModel> getWeather() {
        return weather;
    }

    public boolean isRecruitsAllowed() {
        return recruitsAllowed;
    }

    public void setBuriedItems(ArrayList<DungeonItemGroupModel> buriedItems) {
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

    public void setFloorData(ArrayList<FloorDataModel> floorData) {
        this.floorData = floorData;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItems(ArrayList<DungeonItemGroupModel> items) {
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

    public void setShopItems(ArrayList<DungeonItemGroupModel> shopItems) {
        this.shopItems = shopItems;
    }

    public void setStickyChance(int stickyChance) {
        this.stickyChance = stickyChance;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void setTraps(ArrayList<DungeonTrapGroupModel> traps) {
        this.traps = traps;
    }

    public void setWeather(ArrayList<DungeonWeatherModel> weather) {
        this.weather = weather;
    }

}
