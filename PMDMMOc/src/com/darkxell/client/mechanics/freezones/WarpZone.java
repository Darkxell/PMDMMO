package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.state.StateManager;
import com.darkxell.common.util.DoubleRectangle;

/**
 * Creates a new warpzone. A warpzone object has no intelligence, but describes
 * how a zone in a map can teleport you to an other position in a new map.
 */
public class WarpZone extends TriggerZone {

	public int toX;
	public int toY;
	
	public FreezoneInfo destination;

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
	public WarpZone(int x, int y, FreezoneInfo destination, DoubleRectangle hitbox) {
		super(hitbox);
		toX = x;
		toY = y;
		this.destination = destination;
	}
	
	@Override
	public void onEnter()
	{
		FreezoneMap destination = this.destination.getMap();
		if (destination != null) StateManager.setExploreState(destination, this.toX, this.toY);
	}

}
