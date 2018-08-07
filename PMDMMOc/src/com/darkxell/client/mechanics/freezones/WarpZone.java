package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.state.StateManager;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

/**
 * Creates a new warpzone. A warpzone object has no intelligence, but describes
 * how a zone in a map can teleport you to an other position in a new map.
 */
public class WarpZone extends TriggerZone {

	public int toX;
	public int toY;
	
	public FreezoneInfo destination;
	public Direction direction;

	public WarpZone(int x, int y, FreezoneInfo destination, DoubleRectangle hitbox) {
		this(x, y, destination, null, hitbox);
	}

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
	 * @param direction
	 *            the new direction to face upon entering the new Freezone. null to keep current direction.
	 */
	public WarpZone(int x, int y, FreezoneInfo destination, Direction direction, DoubleRectangle hitbox) {
		super(hitbox);
		toX = x;
		toY = y;
		this.destination = destination;
		this.direction = direction;
	}
	
	@Override
	public void onEnter()
	{
		StateManager.setExploreState(this.destination, this.direction, this.toX, this.toY);
	}

}
