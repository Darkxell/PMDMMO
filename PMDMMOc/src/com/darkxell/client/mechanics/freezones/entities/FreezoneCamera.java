package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezonePlayer;

/**
 * Pseudo-entity that corresponds to the camera position.
 *
 * This entity will attempt to track the player, but will avoid showing positions that are out-of-bounds. This entity
 * is persistent across all freezone maps.
 */
public class FreezoneCamera {

	private static final int SHAKE_TICK_FRAMES = 5;
	private static final int TILESIZE = 8;
	public int renderHeight = Integer.MAX_VALUE;
	public int renderWidth  = Integer.MAX_VALUE;
	private int shakeTimer = 0;
	private int shakeX = 0, shakeY = 0;
	private int shaking = 0;
	private FreezonePlayer target;
	public double x = 0;
	public double y = 0;

	/** Creates a new Freezonecamera following the wanted player. (Don't go and create multiple players just for the sake of adding multiple cameras tho...) */
    public FreezoneCamera(FreezonePlayer targetPlayer) {
        this.target = targetPlayer;
        if (targetPlayer != null) {
            this.x = targetPlayer.x;
            this.y = targetPlayer.y;
        }
    }

    public double finalX() {
        return this.x + this.shakeX;
    }


	public double finalY() {
		return this.y + this.shakeY;
	}

	private boolean isXposOOB(double x) {
		return (x < (renderWidth  / 2) / TILESIZE + 0.3)
				|| (x > Persistence.currentmap.mapWidth - ((renderWidth  / 2) / TILESIZE) - 0.3);
	}

	private boolean isYposOOB(double y) {
		return (y < (renderHeight / 2) / TILESIZE + 0.3)
				|| (y > Persistence.currentmap.mapHeight - ((renderHeight / 2) / TILESIZE) - 0.3);
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
			if (this.shakeTimer >= SHAKE_TICK_FRAMES) {
				this.shakeTimer = 0;
				this.onShakeTick();
			}
		}

		if (target == null) return;
		boolean isXFarFromPlayer = x > target.x + 4 || x < target.x - 4;
		boolean isYFarFromPlayer = y > target.y + 4 || y < target.y - 4;
		double cameraspeed = isXFarFromPlayer ? 0.4d : 0.2d;
		// X POSITIONING
		if (Persistence.currentmap.mapWidth * TILESIZE <= renderWidth ) {
			this.x = ((double) Persistence.currentmap.mapWidth) / 2;
		} else {
			double newx = (x > target.x + 1) ? x - cameraspeed : (x < target.x - 1) ? x + cameraspeed : x;
			if (isXposOOB(newx)) {
				if (isXposOOB(x)) {
					if (x <= (renderWidth  / 2) / TILESIZE + 0.3) x = (renderWidth  / 2) / TILESIZE + 0.3;
					else x = Persistence.currentmap.mapWidth - ((renderWidth  / 2) / TILESIZE) - 0.3;
				}
			} else x = newx;
		}
		cameraspeed = isYFarFromPlayer ? 0.4d : 0.2d;
		// Y POSITIONING
		if (Persistence.currentmap.mapHeight * TILESIZE <= renderHeight) {
			this.y = ((double) Persistence.currentmap.mapHeight) / 2;
		} else {
			double newy = (y > target.y + 1) ? y - cameraspeed : (y < target.y - 1) ? y + cameraspeed : y;
			if (isYposOOB(newy)) {
				if (isYposOOB(y)) {
					if (y <= (renderHeight / 2) / TILESIZE + 0.3) y = (renderHeight / 2) / TILESIZE + 0.3;
					else y = Persistence.currentmap.mapHeight - ((renderHeight / 2) / TILESIZE) - 0.3;
				}
			} else y = newy;
		}
	}
}
