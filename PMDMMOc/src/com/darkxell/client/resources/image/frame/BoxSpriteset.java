package com.darkxell.client.resources.image.frame;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.image.spritefactory.PMDSpriteset;
import com.darkxell.common.util.Direction;

public class BoxSpriteset extends PMDSpriteset {

    public BoxSpriteset() {
        super("/hud/boxcorners");
        this.createSprite(Direction.EAST.name(), 8, 3, 7, 1);
        this.createSprite(Direction.NORTH.name(), 7, 0, 1, 3);
        this.createSprite(Direction.NORTHEAST.name(), 8, 0, 7, 3);
        this.createSprite(Direction.NORTHWEST.name(), 0, 0, 7, 3);
        this.createSprite(Direction.SOUTH.name(), 7, 4, 1, 3);
        this.createSprite(Direction.SOUTHEAST.name(), 8, 4, 7, 3);
        this.createSprite(Direction.SOUTHWEST.name(), 0, 4, 7, 3);
        this.createSprite(Direction.WEST.name(), 0, 3, 7, 1);
    }

    public BufferedImage getSprite(Direction direction) {
        return this.getSprite(direction.name());
    }

}
