package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

public class SubSprite extends Sprite {

	public final int x, y, width, height;

	public SubSprite(String path, int x, int y, int width, int height) {
		super(path, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public BufferedImage image() {
		return SpriteFactory.instance().getSubimage(this.path, this.x, this.y, this.width, this.height);
	}

}
