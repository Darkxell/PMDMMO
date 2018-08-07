package com.darkxell.client.resources.images.others;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.SpriteSet;
import com.darkxell.common.util.Direction;

public class MenuHudSpriteset extends SpriteSet
{

	public static final Dimension cornerNameSize = new Dimension(14, 10);
	public static final Dimension cornerSize = new Dimension(16, 10);

	public MenuHudSpriteset()
	{
		super("/hud/menu_hud.png", 32, 64);

		this.createSprite("arrow_select", 0, 0, 10, 16);
		this.createSprite("arrow_selected", 12, 0, 10, 16);
		this.createSprite("tab_left", 24, 0, 8, 8);
		this.createSprite("tab_right", 24, 8, 8, 8);
		this.createSprite("corner_nw", 0, 16, cornerSize.width, cornerSize.height);
		this.createSprite("corner_ne", 16, 16, cornerSize.width, cornerSize.height);
		this.createSprite("corner_sw", 0, 26, cornerSize.width, cornerSize.height);
		this.createSprite("corner_se", 16, 26, cornerSize.width, cornerSize.height);
		this.createSprite("name_corner_nw", 0, 36, cornerNameSize.width, cornerNameSize.height);
		this.createSprite("name_corner_ne", 18, 36, cornerNameSize.width, cornerNameSize.height);
		this.createSprite("name_corner_sw", 0, 46, cornerNameSize.width, cornerNameSize.height);
		this.createSprite("name_corner_se", 18, 46, cornerNameSize.width, cornerNameSize.height);
		this.createSprite("next_window", 0, 56, 11, 7);
	}

	public BufferedImage nextWindowArrow()
	{
		return this.getImg("next_window");
	}

	public BufferedImage selectedArrow()
	{
		return this.getImg("arrow_selected");
	}

	public BufferedImage selectionArrow()
	{
		return this.getImg("arrow_select");
	}

	public BufferedImage tabLeft()
	{
		return this.getImg("tab_left");
	}

	public BufferedImage tabRight()
	{
		return this.getImg("tab_right");
	}

	public BufferedImage windowCorner(Direction direction)
	{
		switch (direction)
		{
			case NORTHEAST:
				return this.getImg("corner_ne");
			case SOUTHEAST:
				return this.getImg("corner_se");
			case SOUTHWEST:
				return this.getImg("corner_sw");
			default:
				return this.getImg("corner_nw");
		}
	}

	public BufferedImage windowNameCorner(Direction direction)
	{
		switch (direction)
		{
			case NORTHEAST:
				return this.getImg("name_corner_ne");
			case SOUTHEAST:
				return this.getImg("name_corner_se");
			case SOUTHWEST:
				return this.getImg("name_corner_sw");
			default:
				return this.getImg("name_corner_nw");
		}
	}

}
