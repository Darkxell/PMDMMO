package com.darkxell.client.resources.images.hud;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.SpriteSet;
import com.darkxell.common.util.Direction;

public class DungeonHudSpriteset extends SpriteSet {

    public DungeonHudSpriteset() {
        super("/hud/dungeon_hud.png", 32, 12);
        this.createSprite("NORTH", 13, 1, 8, 5);
        this.createSprite("NORTHEAST", 6, 0, 6, 6);
        this.createSprite("EAST", 27, 2, 5, 8);
        this.createSprite("SOUTHEAST", 6, 6, 6, 6);
        this.createSprite("SOUTH", 13, 6, 8, 5);
        this.createSprite("SOUTHWEST", 0, 6, 6, 6);
        this.createSprite("WEST", 22, 2, 5, 8);
        this.createSprite("NORTHWEST", 0, 0, 6, 6);
    }

    /** @return The arrow pointing in the input direction. */
    public BufferedImage getArrow(Direction direction) {
        return this.getImg(direction.name());
    }

}
