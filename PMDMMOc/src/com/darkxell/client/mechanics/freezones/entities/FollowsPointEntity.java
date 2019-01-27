package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.mechanics.freezones.FreezonePlayer;
import com.darkxell.client.graphics.AbstractRenderer;
import com.darkxell.client.graphics.renderer.FreezonePokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.util.Direction;

/**
 * A Pokemon entity that follows a point.
 */
public class FollowsPointEntity extends FreezoneEntity {
    protected double destinationX;
    protected double destinationY;
    protected double moveDistance = 0.5, sprintDistance = 5;
    protected PokemonSprite sprite;

    {
        this.interactive = true;
    }

    public FollowsPointEntity(double x, double y, PokemonSprite sprite) {
        this.posX = x;
        this.posY = y;

        this.sprite = sprite;
        this.destinationX = x;
        this.destinationY = y;
    }

    @Override
    public AbstractRenderer createRenderer() {
        return new FreezonePokemonRenderer(this, this.sprite);
    }

    public PokemonSprite sprite() {
        return this.sprite;
    }

    @Override
    public void update() {
        // Calculates the movespeed of the entity
        double movespeed = FreezonePlayer.MOVESPEED;
        boolean up = false, right = false, down = false, left = false;
        if (destinationX > posX + this.sprintDistance ||
                destinationX < posX - this.sprintDistance ||
                destinationY > posY + this.sprintDistance ||
                destinationY < posY - this.sprintDistance) {
            movespeed *= 2;
        }
        // Moves the pokemon accordingly
        if (destinationX > posX + this.moveDistance) {
            posX += movespeed;
            right = true;
        } else if (destinationX < posX - this.moveDistance) {
            posX -= movespeed;
            left = true;
        }
        if (destinationY > posY + this.moveDistance) {
            posY += movespeed;
            down = true;
        } else if (destinationY < posY - this.moveDistance) {
            posY -= movespeed;
            up = true;
        }
        // Sets the rotation of the pokemonSprite used
        if (up && right) {
            this.sprite.setFacingDirection(Direction.NORTHEAST);
        } else if (right && down) {
            this.sprite.setFacingDirection(Direction.SOUTHEAST);
        } else if (down && left) {
            this.sprite.setFacingDirection(Direction.SOUTHWEST);
        } else if (left && up) {
            this.sprite.setFacingDirection(Direction.NORTHWEST);
        } else if (up) {
            this.sprite.setFacingDirection(Direction.NORTH);
        } else if (right) {
            this.sprite.setFacingDirection(Direction.EAST);
        } else if (down) {
            this.sprite.setFacingDirection(Direction.SOUTH);
        } else if (left) {
            this.sprite.setFacingDirection(Direction.WEST);
        }
        // Sets the running/idle state
        if (up || right || down || left) {
            this.sprite.setState(PokemonSpriteState.MOVE);
        } else {
            this.sprite.setState(PokemonSpriteState.IDLE);
        }
    }
}
