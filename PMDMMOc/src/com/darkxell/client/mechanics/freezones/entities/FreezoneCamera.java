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

    private boolean isTileOOB(double tile, double renderTiles, double mapTiles) {
        boolean below = tile < (renderTiles / 2) + 0.3;
        boolean above = tile > mapTiles - (renderTiles / 2) - 0.3;
        return below || above;
    }

    private double calculateAxisPos(double val, double targetVal, double mapTiles, double maxTiles) {
        // if the map can be fit entirely on the screen, just place the map in the center
        if (mapTiles <= maxTiles) {
            return mapTiles / 2;
        }

        boolean isFar = Math.abs(val - targetVal) > 4;
        double deltaVal = isFar ? 0.4d : 0.2d;

        double newVal = val;
        if (val > targetVal + 1) {
            newVal -= deltaVal;
        } else if (val < targetVal - 1) {
            newVal += deltaVal;
        }

        if (!isTileOOB(newVal, maxTiles, mapTiles)) {
            return newVal;
        }

        if (!isTileOOB(val, maxTiles, mapTiles)) {
            return val;
        }

        // as a last resort, clamp to map boundary (with a slight buffer)

        double minPos = maxTiles / 2 + 0.3;
        double maxPos = mapTiles - maxTiles / 2 - 0.3;
        return Math.max(minPos, Math.min(maxPos, val));
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

        if (target == null) {
            return;
        }

        this.x = this.calculateAxisPos(this.x, this.target.x, Persistence.currentmap.mapWidth,
                this.renderwidth / TILESIZE);
        this.y = this.calculateAxisPos(this.y, this.target.y, Persistence.currentmap.mapHeight,
                this.renderheight / TILESIZE);
    }
}
