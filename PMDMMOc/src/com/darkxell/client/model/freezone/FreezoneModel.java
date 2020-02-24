package com.darkxell.client.model.freezone;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "freezone")
@XmlAccessorType(XmlAccessType.FIELD)
public class FreezoneModel {

    @XmlAttribute
    private int width;

    @XmlAttribute
    private int height;

    @XmlAttribute
    private String defaultTileset;

    @XmlElement(name = "tile")
    @XmlElementWrapper(name = "tiles")
    private ArrayList<FreezoneTileModel> tiles;

    public FreezoneModel() {
    }

    public FreezoneModel(int width, int height, String defaultTileset, ArrayList<FreezoneTileModel> tiles) {
        this.width = width;
        this.height = height;
        this.defaultTileset = defaultTileset;
        this.tiles = tiles;
    }

    public String getDefaultTileset() {
        return defaultTileset;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<FreezoneTileModel> getTiles() {
        return tiles;
    }

    public int getWidth() {
        return width;
    }

    public void setDefaultTileset(String defaultTileset) {
        this.defaultTileset = defaultTileset;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setTiles(ArrayList<FreezoneTileModel> tiles) {
        this.tiles = tiles;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public FreezoneModel copy() {
        ArrayList<FreezoneTileModel> tiles = new ArrayList<>();
        this.tiles.forEach(t -> tiles.add(t.copy()));
        return new FreezoneModel(width, height, defaultTileset, tiles);
    }

    public FreezoneTileModel findTile(int x, int y) {
        for (FreezoneTileModel tile : this.tiles)
            if (tile.getX() == x && tile.getY() == y)
                return tile;
        return null;
    }

}
