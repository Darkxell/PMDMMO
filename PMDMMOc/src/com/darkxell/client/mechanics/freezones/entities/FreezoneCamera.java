package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.mechanics.freezones.FreezonePlayer;
import com.darkxell.client.persistance.FreezoneMapHolder;

/**
 * The camera "entity". This behaves pretty much exactly like an entity, but is
 * not rendered and doesn't update as the others do. This entity will follow the
 * player as much as it can, but will avoid showing out of bounds areas if the
 * player is too close to walls. Also this entity, like tha player, is not
 * dependennt to a map and will follow the player through warpzone by being
 * persistant.
 */
public class FreezoneCamera {

	public int renderwidth = Integer.MAX_VALUE;
	public int renderheight = Integer.MAX_VALUE;
	public double x = 0;
	public double y = 0;
	private FreezonePlayer target;
	private static final int TILESIZE = 8;

	/**
	 * Creates a new Freezonecamera following the wanted player. (Don't go and
	 * create multiple players just for the sake of adding multiple cameras
	 * tho...)
	 */
	public FreezoneCamera(FreezonePlayer tofollow) {
		this.target = tofollow;
		if (tofollow != null) {
			this.x = tofollow.x;
			this.y = tofollow.y;
		}
	}

	public void update() {
		boolean isFarFromPlayer = x > target.x + 4 || x < target.x - 4 || y > target.y + 4 || y < target.y - 4;
		double cameraspeed = isFarFromPlayer ? 0.4d : 0.2d;
		// X POSITIONING
		double newx = (x > target.x + 1) ? x - cameraspeed : (x < target.x - 1) ? x + cameraspeed : x;
		if (isXposOOB(newx)) {
			if (isXposOOB(x)) {
				if (x < (renderwidth / 2) / TILESIZE)
					x += cameraspeed * 3;
				if (x > FreezoneMapHolder.currentmap.mapWidth - ((renderwidth / 2) / TILESIZE))
					x -= cameraspeed * 3;
			}
		} else
			x = newx;
		// Y POSITIONING
		double newy = (y > target.y + 1) ? y - cameraspeed : (y < target.y - 1) ? y + cameraspeed : y;
		if (isYposOOB(newy)) {
			if (isYposOOB(y)) {
				if (y < (renderheight / 2) / TILESIZE)
					y += cameraspeed * 3;
				if (y > FreezoneMapHolder.currentmap.mapHeight - ((renderheight / 2) / TILESIZE))
					y -= cameraspeed * 3;
			}
		} else
			y = newy;
	}

	private boolean isYposOOB(double y) {
		return (y < (renderheight / 2) / TILESIZE + 1)
				|| (y > FreezoneMapHolder.currentmap.mapHeight - ((renderheight / 2) / TILESIZE) - 1);
	}

	private boolean isXposOOB(double x) {
		return (x < (renderwidth / 2) / TILESIZE + 1)
				|| (x > FreezoneMapHolder.currentmap.mapWidth - ((renderwidth / 2) / TILESIZE) - 1);
	}
}
