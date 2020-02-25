package com.darkxell.pmdmmo.freezoneconverter.tiledmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tile")
@XmlAccessorType(XmlAccessType.FIELD)
public class TiledFreezoneTileModel {

	@XmlAttribute
	private String bgName;

	@XmlAttribute
	private int x;

	@XmlAttribute
	private int y;

	@XmlAttribute
	private int xo;

	@XmlAttribute
	private int yo;

	public TiledFreezoneTileModel() {
	}

	public String getBgName() {
		return bgName;
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

	public void setBgName(String bgName) {
		this.bgName = bgName;
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

}
