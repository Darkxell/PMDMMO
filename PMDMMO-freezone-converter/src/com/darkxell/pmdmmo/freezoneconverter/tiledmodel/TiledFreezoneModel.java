package com.darkxell.pmdmmo.freezoneconverter.tiledmodel;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "room")
@XmlAccessorType(XmlAccessType.FIELD)
public class TiledFreezoneModel {

	@XmlElement
	private int width;

	@XmlElement
	private int height;

	@XmlElement(name = "tile")
	@XmlElementWrapper(name = "tiles")
	private ArrayList<TiledFreezoneTileModel> tiles;
	
	public TiledFreezoneModel() {}

	public int getHeight() {
		return height;
	}

	public ArrayList<TiledFreezoneTileModel> getTiles() {
		return tiles;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setTiles(ArrayList<TiledFreezoneTileModel> tiles) {
		this.tiles = tiles;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
