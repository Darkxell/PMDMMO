package com.darkxell.client.resources.image.pokemon.body;

import java.awt.image.BufferedImage;

import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;

public class PokemonSprite {

    public static final int FRAMELENGTH = 10;

    public static final byte NEUTRAL_SHADOW = 0;
    public static final byte ALLY_SHADOW = 1;
    public static final byte PLAYER_SHADOW = 2;
    public static final byte ENEMY_SHADOW = 3;

    public static final float QUICKER = 1.5f;
    public static final float REGULAR_SPEED = 1f;
    public static final float SLOWER = .5f;

    private boolean animated = true;
    private float counter = 0;
    private PokemonSpriteState defaultState = PokemonSpriteState.IDLE;
    private Direction facing = Direction.SOUTH;
    public final PokemonSpriteset pointer;
    /**
     * When true, if in a repeatable state, will reset to idle state at the end of the current animation. Else, will
     * keep on the same animation.
     */
    private boolean resetToDefaultOnFinish = false;
    private byte shadowColor = NEUTRAL_SHADOW;
    private PokemonSpriteState state = PokemonSpriteState.IDLE;
    private float tickSpeed = 1;
    private int xOffset = 0, yOffset = 0;

    public PokemonSprite(PokemonSpriteset pointer) {
        this.pointer = pointer;
        if (this.pointer == null)
            new Exception().printStackTrace();
    }

    public PSDFrame getCurrentFrame() {
        return this.pointer.getFrame(this.state, this.facing, (int) this.counter);
    }

    public BufferedImage getCurrentSprite() {
        return this.pointer.getSprite(this.getCurrentFrame().frameID);
    }

    public Direction getFacingDirection() {
        return this.facing;
    }

    public byte getShadowColor() {
        return this.shadowColor;
    }

    public PokemonSpriteState getState() {
        return this.state;
    }

    public void resetOnAnimationEnd() {
        this.resetToDefaultOnFinish = true;
    }

    public void setFacingDirection(Direction dir) {
        this.facing = dir;
    }

    public void setShadowColor(byte shadowColor) {
        this.shadowColor = shadowColor;
    }

    public void setState(PokemonSpriteState state) {
        this.setState(state, false);
    }

    public PokemonSpriteState defaultState() {
        return this.defaultState;
    }

    public void setDefaultState(PokemonSpriteState state, boolean apply) {
        this.defaultState = state;
        if (apply)
            this.setState(state);
    }

    /**
     * Changes the state of this Sprite to the wanted one. If the parsed state is already the state used by the pokemon
     * sprite, this does nothing.
     *
     * @param playOnLoop - true if the state should play on loop until notified to stop. Defaults to false (i.e. only
     *                   plays once).
     */
    public void setState(PokemonSpriteState state, boolean playOnLoop) {
        if (this.state != state) {
            this.state = state;
            this.counter = 0;
        }
        this.resetToDefaultOnFinish = !playOnLoop;
    }

    public void setTickingSpeed(float tickSpeed) {
        this.tickSpeed = tickSpeed;
    }

    /** Updates this sprite to the next frame. */
    public void update() {
        if (this.isAnimated())
            this.counter += this.tickSpeed;
        PSDSequence state = this.pointer.getSequence(this.state, this.facing);
        if (this.counter >= state.duration) {
            this.counter = 0;
            if (this.resetToDefaultOnFinish && this.state != PokemonSpriteState.IDLE)
                this.resetToDefaultState();
        }
    }

    public void updateTickingSpeed(DungeonPokemon pokemon) {
        int s = pokemon.stats.getStage(Stat.Speed);
        if (s == 0)
            this.setTickingSpeed(SLOWER);
        else if (s == 1)
            this.setTickingSpeed(REGULAR_SPEED);
        else
            this.setTickingSpeed(QUICKER);
    }

    public void copyState(PokemonSprite sprite) {
        this.facing = sprite.facing;
        this.resetToDefaultOnFinish = sprite.resetToDefaultOnFinish;
        this.shadowColor = sprite.shadowColor;
        this.tickSpeed = sprite.tickSpeed;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public boolean isAnimated() {
        return this.animated;
    }

    public void resetToDefaultState() {
        this.setState(this.defaultState);
    }

    public int xOffset() {
        return xOffset;
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int yOffset() {
        return yOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public void setXYOffset(int x, int y) {
        this.xOffset = x;
        this.yOffset = y;
    }

    public double dashOffset() {
        return this.pointer.getSequence(this.state, this.getFacingDirection()).dashOffset((int) this.counter);
    }

}
