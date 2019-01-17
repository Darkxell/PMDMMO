package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezonePlayer;
import com.darkxell.client.mechanics.freezones.FreezoneTerrain;

/**
 * Pseudo-entity that corresponds to the camera position.
 * <p>
 * This entity will attempt to track the player, but will avoid showing positions that are out-of-bounds. This entity
 * is persistent across all freezone maps.
 */
public class FreezoneCamera {
    private static final int SHAKE_TICK_FRAMES = 5;
    private static final int TILESIZE = 8;

    public int renderHeight = -1;
    public int renderWidth = -1;

    private int shakeTimer = 0;
    private int shakeStep = 0;
    private int shakeX = 0;
    private int shakeY = 0;
    private int shakeIntensity = 0;

    private FreezonePlayer target;
    private double x = 0;
    private double y = 0;

    public FreezoneCamera(FreezonePlayer targetPlayer) {
        this.target = targetPlayer;
        if (targetPlayer != null) {
            this.x = targetPlayer.x;
            this.y = targetPlayer.y;
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double finalX() {
        return this.x + this.shakeX;
    }

    public double finalY() {
        return this.y + this.shakeY;
    }

    public void setShakeIntensity(int strength) {
        this.shakeIntensity = strength;
        if (this.shakeIntensity == 0) {
            this.shakeX = this.shakeY = this.shakeTimer = 0;
        }
    }

    private void advanceShakeAnimation() {
        switch (this.shakeStep) {
            case 0:
                this.shakeX = this.shakeY = this.shakeIntensity;
                break;
            case 1:
                this.shakeX = -this.shakeIntensity;
                this.shakeY = this.shakeIntensity;
                break;
            case 2:
                this.shakeX = -this.shakeIntensity;
                this.shakeY = -this.shakeIntensity;
                break;
            case 3:
                this.shakeX = this.shakeIntensity;
                this.shakeY = -this.shakeIntensity;
            case 4:
                this.shakeX = this.shakeY = 0;
            default:
                this.shakeStep = 0;
                return;
        }

        this.shakeStep++;
    }

    private void updateShake() {
        if (this.shakeIntensity == 0) {
            return;
        }

        this.shakeTimer++;

        if (this.shakeTimer % SHAKE_TICK_FRAMES == 0) {
            this.advanceShakeAnimation();
        }
    }

    private boolean isValidTile(double tile, double renderTiles, double mapTiles) {
        double least = (renderTiles / 2) + 0.3;
        double most = mapTiles - (renderTiles / 2) - 0.3;
        return tile >= least && tile <= most;
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

        if (isValidTile(newVal, maxTiles, mapTiles)) {
            return newVal;
        }

        if (isValidTile(val, maxTiles, mapTiles)) {
            return val;
        }

        // as a last resort, clamp to map boundary (with a slight buffer)
        double minPos = maxTiles / 2 + 0.3;
        double maxPos = mapTiles - maxTiles / 2 - 0.3;
        return Math.max(minPos, Math.min(maxPos, val));
    }

    public void update() {
        updateShake();

        if (target == null) {
            return;
        }

        FreezoneTerrain terrain = Persistence.currentmap.getTerrain();
        this.x = this.calculateAxisPos(this.x, this.target.x, terrain.getWidth(),
                (double) this.renderWidth / TILESIZE);
        this.y = this.calculateAxisPos(this.y, this.target.y, terrain.getHeight(),
                (double) this.renderHeight / TILESIZE);
    }
}
