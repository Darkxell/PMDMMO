package com.darkxell.client.resources.images.layers;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.SpriteSet;

public class WetDreamSpriteSet extends SpriteSet {

	public WetDreamSpriteSet() {
		super("/graphicallayers/dream.png", 294, 192);
		createSprite("bigleft", 0, 0, 147, 192);
		createSprite("smallleft", 147, 0, 147, 192);
		createSprite("smallright", 294, 0, 147, 192);
		createSprite("bigright", 441, 0, 147, 192);
	}
	
	public BufferedImage getSmallLeft()
	{
		return this.getImg("smallleft");
	}
	public BufferedImage getSmallRight()
	{
		return this.getImg("smallright");
	}
	public BufferedImage getBigLeft()
	{
		return this.getImg("bigleft");
	}
	public BufferedImage getBigRight()
	{
		return this.getImg("bigright");
	}

}
