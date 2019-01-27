package com.darkxell.client.resources.images.hud;

import java.awt.image.BufferedImage;

import com.darkxell.client.graphics.TextRenderer.PMDChar;
import com.darkxell.client.resources.images.Sprite;
import com.darkxell.client.resources.images.SpriteSet;

public class FontSpriteSet extends SpriteSet
{
	public static final int CHAR_HEIGHT = 11;
	public static final int COLUMNS = 20, ROWS = 20;
	public static final int GRID_WIDTH = CHAR_HEIGHT, GRID_HEIGHT = CHAR_HEIGHT;

	public FontSpriteSet()
	{
		super("/hud/font.png", COLUMNS * GRID_WIDTH, ROWS * GRID_HEIGHT);

		for (PMDChar c : PMDChar.values())
			if (c.isChar()) this.createSprite(c.value, c.xPos * GRID_WIDTH, c.yPos * GRID_HEIGHT, c.width, CHAR_HEIGHT);
	}

	Sprite get(PMDChar c)
	{
		return this.get(c.value);
	}

	public BufferedImage getImg(PMDChar c)
	{
		return this.get(c).image();
	}

}
