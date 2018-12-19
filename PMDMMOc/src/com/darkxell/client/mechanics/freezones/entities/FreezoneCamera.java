package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezonePlayer;

/** The camera "entity". This behaves pretty much exactly like an entity, but is not rendered and doesn't update as the others do. This entity will follow the player as much as it can, but will avoid showing out of bounds areas if the player is too close to walls. Also this entity, like tha player,
 * is not dependennt to a map and will follow the player through warpzone by being persistant. */
public class FreezoneCamera {

	private static final int SHAKETIME = 5;
	private static final int TILESIZE = 8;
    public int renderheight = -1;
    public int renderwidth = -1;
	private int shakeTimer = 0;
	private int shakeX = 0, shakeY = 0;
	private int shaking = 0;
	private FreezonePlayer target;
	public double x = 0;
	public double y = 0;

	/** Creates a new Freezonecamera following the wanted player. (Don't go and create multiple players just for the sake of adding multiple cameras tho...) */
	public FreezoneCamera(FreezonePlayer tofollow) {
		this.target = tofollow;
		if (tofollow != null) {
			this.x = tofollow.x;
			this.y = tofollow.y;
		}
	}

	public double finalX() {
		return this.x + this.shakeX;
	}

	public double finalY() {
		return this.y + this.shakeY;
	}

	private boolean isXposOOB(double x) {
		return (x < (renderwidth / 2) / TILESIZE + 0.3)
				|| (x > Persistence.currentmap.mapWidth - ((renderwidth / 2) / TILESIZE) - 0.3);
	}

	private boolean isYposOOB(double y) {
		return (y < (renderheight / 2) / TILESIZE + 0.3)
				|| (y > Persistence.currentmap.mapHeight - ((renderheight / 2) / TILESIZE) - 0.3);
	}

	private void onShakeTick() {
		if (this.shakeX == 0 && this.shakeY == 0) {
			this.shakeX = this.shakeY = this.shaking;
		} else if (this.shakeX > 0 && this.shakeY > 0) {
			this.shakeX = -this.shaking;
			this.shakeY = this.shaking;
		} else if (this.shakeX < 0 && this.shakeY > 0) {
			this.shakeX = -this.shaking;
			this.shakeY = -this.shaking;
		} else if (this.shakeX < 0 && this.shakeY < 0) {
			this.shakeX = this.shaking;
			this.shakeY = -this.shaking;
		} else if (this.shakeX > 0 && this.shakeY < 0) {
			this.shakeX = this.shakeY = 0;
		}
	}

	public void setShaking(int strength) {
		this.shaking = strength;
		if (this.shaking == 0) this.shakeX = this.shakeY = this.shakeTimer = 0;
	}

	public void update() {
		// Shake
		if (this.shaking != 0) {
			++this.shakeTimer;
			if (this.shakeTimer >= SHAKETIME) {
				this.shakeTimer = 0;
				this.onShakeTick();
			}
		}

		if (target == null) return;
		boolean isXFarFromPlayer = x > target.x + 4 || x < target.x - 4;
		boolean isYFarFromPlayer = y > target.y + 4 || y < target.y - 4;
		double cameraspeed = isXFarFromPlayer ? 0.4d : 0.2d;
		// X POSITIONING
		if (Persistence.currentmap.mapWidth * TILESIZE <= renderwidth) {
			this.x = ((double) Persistence.currentmap.mapWidth) / 2;
		} else {
			double newx = (x > target.x + 1) ? x - cameraspeed : (x < target.x - 1) ? x + cameraspeed : x;
			if (isXposOOB(newx)) {
				if (isXposOOB(x)) {
					if (x <= (renderwidth / 2) / TILESIZE + 0.3) x = (renderwidth / 2) / TILESIZE + 0.3;
					else x = Persistence.currentmap.mapWidth - ((renderwidth / 2) / TILESIZE) - 0.3;
				}
			} else x = newx;
		}
		cameraspeed = isYFarFromPlayer ? 0.4d : 0.2d;
		// Y POSITIONING
		if (Persistence.currentmap.mapHeight * TILESIZE <= renderheight) {
			this.y = ((double) Persistence.currentmap.mapHeight) / 2;
		} else {
			double newy = (y > target.y + 1) ? y - cameraspeed : (y < target.y - 1) ? y + cameraspeed : y;
			if (isYposOOB(newy)) {
				if (isYposOOB(y)) {
					if (y <= (renderheight / 2) / TILESIZE + 0.3) y = (renderheight / 2) / TILESIZE + 0.3;
					else y = Persistence.currentmap.mapHeight - ((renderheight / 2) / TILESIZE) - 0.3;
				}
			} else y = newy;
		}
	}
}
