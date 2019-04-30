package com.darkxell.client.mechanics.freezones.entities;

import java.util.Random;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.state.menu.freezone.FriendActionSelectionState;
import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.MathUtil;

public class FriendPokemonEntity extends PokemonFreezoneEntity {

    public static final int IDLE_TIME_MIN = 50, IDLE_TIME_MAX = 100;
    public static final double MAX_DISTANCE = 3;
    public static final double SPEED = .2;

    /** AI variables. */
    private double destinationX = -1, destinationY = -1;
    private int idleTime = IDLE_TIME_MAX;
    public final Pokemon pokemon;
    private boolean frozen = false;
    private double startX, startY;

    public FriendPokemonEntity(Pokemon pokemon) {
        super(0, 0, new PokemonSprite(PokemonSpritesets.getSpriteset(pokemon.species().id)));
        this.pokemon = pokemon;
        this.pkmnsprite = new PokemonSprite(PokemonSpritesets.getSpriteset(this.pokemon));
        this.isSolid = false;
        this.canInteract = true;
    }

    @Override
    public AbstractRenderer createRenderer() {
        AbstractPokemonRenderer r = (AbstractPokemonRenderer) super.createRenderer();
        r.sprite().setFacingDirection(Direction.randomDirection(new Random()));
        return r;
    }

    private void freeze() {
        this.frozen = true;
        this.pkmnsprite.setFacingDirection(AIUtils.closestDirection(
                MathUtil.angle(this.posX, this.posY, Persistence.currentplayer.x, Persistence.currentplayer.y)));
        this.pkmnsprite.setState(PokemonSpriteState.MOVE);
        this.pkmnsprite.setState(PokemonSpriteState.IDLE);
        this.pkmnsprite.setAnimated(false);
    }

    @Override
    public void onInteract() {
        this.freeze();
        Persistence.stateManager
                .setState(new FriendActionSelectionState(Persistence.stateManager.getCurrentState(), this));
    }

    /** Call when this Pokemon isn't being talked to anymore. */
    public void release() {
        this.frozen = false;
        this.idleTime = IDLE_TIME_MIN;
        this.pkmnsprite.setAnimated(true);
    }

    public void spawnAt(double x, double y) {
        this.startX = x;
        this.startY = y;

        this.posX = this.startX + Math.random() * (MAX_DISTANCE * 2) - MAX_DISTANCE;
        this.posY = this.startY + Math.random() * (MAX_DISTANCE * 2) - MAX_DISTANCE;
    }

    @Override
    public void update() {
        super.update();

        if (this.frozen)
            return;
        if (this.idleTime > 0)
            --this.idleTime; // Pause
        else if (this.destinationX == -1 || this.destinationY == -1) { // Find new destination
            this.destinationX = Math.random() * MAX_DISTANCE * 2 - MAX_DISTANCE + this.startX;
            this.destinationY = Math.random() * MAX_DISTANCE * 2 - MAX_DISTANCE + this.startY;
            this.pkmnsprite.setFacingDirection(AIUtils
                    .closestDirection(MathUtil.angle(this.posX, this.posY, this.destinationX, this.destinationY)));
            this.pkmnsprite.setState(PokemonSpriteState.MOVE);
        } else { // Move
            this.posX += Math.signum(this.destinationX - this.posX)
                    * Math.min(SPEED, Math.abs(this.destinationX - this.posX));
            this.posY += Math.signum(this.destinationY - this.posY)
                    * Math.min(SPEED, Math.abs(this.destinationY - this.posY));

            if (this.destinationX == this.posX && this.destinationY == this.posY) { // Destination reached
                this.idleTime = (int) (IDLE_TIME_MIN + Math.random() * (IDLE_TIME_MAX - IDLE_TIME_MIN));
                this.destinationX = this.destinationY = -1;
                this.pkmnsprite.setState(PokemonSpriteState.IDLE);
            }
        }
    }
}
