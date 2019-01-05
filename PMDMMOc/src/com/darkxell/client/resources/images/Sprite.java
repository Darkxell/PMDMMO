package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

/** Holds an Image. Allows proper loading using the SpriteFactory. */
public class Sprite {

	public final String path;

	public Sprite(String path) {
		this(path, -1, -1);
	}

	public Sprite(String path, int width, int height) {
		this(path, width, height, true);
	}

	Sprite(String path, int width, int height, boolean doLoad) {
		this.path = path;
		if (doLoad) SpriteFactory.instance().load(this.path, width, height);
	}

	public void dispose() {
		SpriteFactory.instance().dispose(this.path);
	}

	/** @return The Image held in this Sprite. */
	public BufferedImage image() {
		return SpriteFactory.instance().get(this.path);
	}

	/** @return <code>true</code> if this Sprite is loaded. */
	public boolean isLoaded() {
		return SpriteFactory.instance().isResourceLoaded(this.path);
	}

}
