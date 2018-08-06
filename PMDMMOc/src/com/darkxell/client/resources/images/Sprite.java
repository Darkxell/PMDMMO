package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

/** Holds an Image. Allows proper loading using the SpriteFactory. */
public class Sprite
{

	private BufferedImage image;

	public Sprite(String path)
	{
		this(path, -1, -1);
	}

	public Sprite(String path, int width, int height)
	{
		this.image = SpriteFactory.instance().load(this, path, width, height);
	}

	/** @return The Image held in this Sprite. */
	public BufferedImage image()
	{
		return this.image;
	}

	/** This method is called when the SpriteFactory loads this Sprite. If overriding this method, the parent should always be called to set the base image.<br>
	 * This method is called by the SpriteFactory's Thread and should be thread-safe.
	 * 
	 * @param img - The Image after loading. */
	protected void loaded(BufferedImage img)
	{
		this.image = img;
	}

}
