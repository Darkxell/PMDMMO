package com.darkxell.client.resources.images.tilesets;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.AbstractTileset;

public class DungeonMapTileset extends AbstractTileset
{

	public static final DungeonMapTileset INSTANCE = new DungeonMapTileset();

	public DungeonMapTileset()
	{
		super("resources/tilesets/dungeon/map.png", 4, 4);
	}

	public BufferedImage ally()
	{
		return this.tileAt(2, 0);
	}

	public BufferedImage enemy()
	{
		return this.tileAt(0, 1);
	}

	public BufferedImage ground()
	{
		return this.tileAt(0, 2);
	}

	public BufferedImage item()
	{
		return this.tileAt(4, 0);
	}

	public BufferedImage other()
	{
		return this.tileAt(3, 0);
	}

	public BufferedImage outlaw()
	{
		return this.tileAt(1, 1);
	}

	public BufferedImage player(byte variant)
	{
		return this.tileAt(variant, 0);
	}

	public BufferedImage stairs()
	{
		return this.tileAt(2, 2);
	}

	private BufferedImage tileAt(int x, int y)
	{
		return this.SPRITES[x + y * 5];
	}

	public BufferedImage trap()
	{
		return this.tileAt(3, 2);
	}

	public BufferedImage warpzone()
	{
		return this.tileAt(4, 2);
	}

	public BufferedImage wonder()
	{
		return this.tileAt(1, 2);
	}

}
