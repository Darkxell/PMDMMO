package com.darkxell.client.mechanics.freezones;

import com.darkxell.common.util.DoubleRectangle;

/**
 * Creates a new triggerzone. A triggerzone object has no intelligence, but describes
 * how a zone in a map can teleport you to an other position in a new map.
 */
public abstract class TriggerZone {

	public DoubleRectangle hitbox;

	/**
	 * Creates a new triggerzone. A triggerzone object has no intelligence, but
	 * describes how a zone in a map can teleport you to an other position in a
	 * new map.
	 * 
	 * @param x
	 *            the x position of the destination. If -1, uses the default X position in the destination.
	 * @param y
	 *            the y position of the destination. If -1, uses the default Y position in the destination.
	 * @param hitbox
	 *            the hitbox of the triggerzone in the current map.
	 */
	public TriggerZone(DoubleRectangle hitbox) {
		this.hitbox = hitbox;
	}

	/**
	 * Must be overriten when you create a new TriggerZone.
	 */
	public abstract void onEnter();

}
