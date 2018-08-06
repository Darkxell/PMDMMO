package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/** Represents a set of Sprites derived from a single Sprite. */
public class SpriteSet extends Sprite
{

	private HashMap<String, Sprite> sprites = new HashMap<>();

	public SpriteSet(String path)
	{
		super(path);
	}

	public SpriteSet(String path, int width, int height)
	{
		super(path, width, height);
	}

	/** Registers a Sprite in this Spriteset.
	 * 
	 * @param id - The ID of the Sprite.
	 * @param x <b>y width height</b> - The part of this Spriteset's base Image.
	 * @return The created Sprite. */
	public Sprite createSprite(String id, int x, int y, int width, int height)
	{
		this.sprites.put(id, SpriteFactory.instance().subSprite(this, x, y, width, height));
		return this.sprites.get(id);
	}

	/** @param id - The ID of one of this Spriteset's Sprites.
	 * @return The Sprite with the input ID. */
	public Sprite get(String id)
	{
		return this.sprites.get(id);
	}

	/** @param id - The ID of one of this Spriteset's Sprites.
	 * @return The Image of the Sprite with the input ID. */
	public BufferedImage getImg(String id)
	{
		Sprite s = this.get(id);
		if (s == null) return null;
		return s.image();
	}

}
