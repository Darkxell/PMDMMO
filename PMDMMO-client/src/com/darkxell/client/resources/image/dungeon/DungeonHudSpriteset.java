package com.darkxell.client.resources.image.dungeon;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDSpriteset;
import com.darkxell.common.util.Direction;

public class DungeonHudSpriteset extends PMDSpriteset {

    public DungeonHudSpriteset() {
        super("/hud/dungeon_hud.png");
        this.createSprite(Direction.NORTH.name(), 13, 1, 8, 5);
        this.createSprite(Direction.NORTHEAST.name(), 6, 0, 6, 6);
        this.createSprite(Direction.EAST.name(), 27, 2, 5, 8);
        this.createSprite(Direction.SOUTHEAST.name(), 6, 6, 6, 6);
        this.createSprite(Direction.SOUTH.name(), 13, 6, 8, 5);
        this.createSprite(Direction.SOUTHWEST.name(), 0, 6, 6, 6);
        this.createSprite(Direction.WEST.name(), 22, 2, 5, 8);
        this.createSprite(Direction.NORTHWEST.name(), 0, 0, 6, 6);
    }

    /** @return The arrow pointing in the input direction. */
    public BufferedImage getArrow(Direction direction) {
        return this.getSprite(direction.name());
    }

}
