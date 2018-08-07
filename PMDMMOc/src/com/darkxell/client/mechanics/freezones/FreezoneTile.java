package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.resources.images.Sprite;

/**
 * A typical 8*8 tile found in freezones. Freezones are the areas where you can
 * move freely and don't have to fight.
 */
public class FreezoneTile {

	public byte type = 1;
	public static final byte TYPE_SOLID = 0;
	public static final byte TYPE_WALKABLE = 1;
	public Sprite sprite;

	public FreezoneTile(byte type, Sprite sprite) {
		this.type = type;
		this.sprite = sprite;
	}

}
