package com.darkxell.client.model.freezone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tile")
@XmlAccessorType(XmlAccessType.FIELD)
public class FreezoneTileModel {

    @XmlAttribute
    private int x;

    @XmlAttribute
    private int y;

    @XmlAttribute
    private int xo;

    @XmlAttribute
    private int yo;

    @XmlAttribute
    private Boolean solid;

    @XmlAttribute
    private String tileset;

    public FreezoneTileModel() {
    }

    public FreezoneTileModel(int x, int y, int xo, int yo, Boolean solid, String tileset) {
        this.x = x;
        this.y = y;
        this.xo = xo;
        this.yo = yo;
        this.solid = solid;
        this.tileset = tileset;
    }

    public Boolean getSolid() {
        return solid;
    }

    public String getTileset() {
        return tileset;
    }

    public int getX() {
        return x;
    }

    public int getXo() {
        return xo;
    }

    public int getY() {
        return y;
    }

    public int getYo() {
        return yo;
    }

    public void setSolid(Boolean solid) {
        this.solid = solid;
    }

    public void setTileset(String tileset) {
        this.tileset = tileset;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setXo(int xo) {
        this.xo = xo;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setYo(int yo) {
        this.yo = yo;
    }

    public FreezoneTileModel copy() {
        return new FreezoneTileModel(x, y, xo, yo, solid, tileset);
    }

}
