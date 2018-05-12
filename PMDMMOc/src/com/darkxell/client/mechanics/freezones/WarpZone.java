package com.darkxell.client.mechanics.freezones;

import com.darkxell.common.util.DoubleRectangle;

/**
 * Creates a new warpzone. A warpzone object has no intelligence, but describes
 * how a zone in a map can teleport you to an other position in a new map.
 */
public abstract class WarpZone {

	public int toX;
	public int toY;

	public DoubleRectangle hitbox;

	/**
	 * Creates a new warpzone. A warpzone object has no intelligence, but
	 * describes how a zone in a map can teleport you to an other position in a
	 * new map.
	 * 
	 * @param x
	 *            the x position of the destination. If -1, uses the default X position in the destination.
	 * @param y
	 *            the y position of the destination. If -1, uses the default Y position in the destination.
	 * @param hitbox
	 *            the hitbox of the warpzone in the current map.
	 */
	public WarpZone(int x, int y, DoubleRectangle hitbox) {
		toX = x;
		toY = y;
		this.hitbox = hitbox;
	}

	/**
	 * Must be overriten when you create a new WarpZone. This method should
	 * build and return a pointer to the destination map.<br/>
	 * <br/>
	 * <strong>Exemple:</strong><br/>
	 * return new BaseFreezone();
	 */
	public abstract FreezoneMap getDestination();

}
