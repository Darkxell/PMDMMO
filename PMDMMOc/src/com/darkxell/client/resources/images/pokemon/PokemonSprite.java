package com.darkxell.client.resources.images.pokemon;

import java.awt.image.BufferedImage;

public class PokemonSprite {

	public final AbstractPokemonSpriteset pointer;

	public PokemonSprite(AbstractPokemonSpriteset pointer) {
		this.pointer = pointer;
	}

	/** Updates this sprite to the next frame. */
	public void update() {
		this.counter += this.tickSpeed;
		PokemonSpritesetState state = this.pointer.states[this.state];
		if (this.counter > state.duration(this.statecounter)) {
			this.counter = 0;
			if (this.statecounter + 1 < state.indexes.length)
				++this.statecounter;
			else {
				if (this.resetToIdleOnFinish && this.state != STATE_IDLE)
					this.setState(STATE_IDLE);
				else
					this.statecounter = 0;
			}
		}

		if (this.healthCounter > 0)
			--this.healthCounter;
		if (this.healthCounter == 0) {
			this.healthChange = 0;
		}
	}

	public BufferedImage getCurrentSprite() {
		return this.pointer.getSprite(this.state, this.facing, this.statecounter);
	}
	
	public void resetOnAnimationEnd() {
		this.resetToIdleOnFinish = true;
	}
	
	public void setState(byte state) {
		this.setState(state, false);
	}

	/**
	 * Changes the state of this Sprite to the wanted one. If the parsed state
	 * is already the state used by the pokemon sprite, this does nothing.
	 * @param playOnLoop - true if the state should play on loop until notified to stop. Defaults to false (i.e. only plays once).
	 */
	public void setState(byte state, boolean playOnLoop) {
		if (this.state != state) {
			this.state = state;
			this.counter = 0;
			this.statecounter = 0;
		}
		this.resetToIdleOnFinish = !playOnLoop;
	}

	private byte facing = 4;
	public static final byte FACING_N = 0;
	public static final byte FACING_NE = 1;
	public static final byte FACING_E = 2;
	public static final byte FACING_SE = 3;
	public static final byte FACING_S = 4;
	public static final byte FACING_SW = 5;
	public static final byte FACING_W = 6;
	public static final byte FACING_NW = 7;

	private byte state = 0;
	public static final byte STATE_IDLE = 0;
	public static final byte STATE_MOVE = 1;
	public static final byte STATE_ATTACK = 2;
	public static final byte STATE_SPECIAL1 = 3;
	public static final byte STATE_SPECIAL2 = 4;
	public static final byte STATE_SLEEP = 5;
	public static final byte STATE_HURT = 6;
	public static final byte STATE_REST = 7;
	public static final byte STATE_WAKING = 8;
	public static final byte STATE_VICTORYPOSE = 9;
	public static final byte STATE_EATING = 10;

	public static final int FRAMELENGTH = 10;
	public static final int HEALTHLENGTH = 60;

	public static final byte NEUTRAL_SHADOW = 0;
	public static final byte ALLY_SHADOW = 1;
	public static final byte PLAYER_SHADOW = 2;
	public static final byte ENEMY_SHADOW = 3;

	private int counter = 0;
	private int healthChange = 0;
	private int healthCounter = 0;
	/** When true, if in a repeatable state, will reset to idle state at the end of the current animation. Else, will keep on the same animation. */
	private boolean resetToIdleOnFinish = false;
	private int statecounter = 0;
	private int tickSpeed = 1;
	private byte shadowColor = NEUTRAL_SHADOW;

	public byte getShadowColor() {
		return this.shadowColor;
	}

	public void setShadowColor(byte shadowColor) {
		this.shadowColor = shadowColor;
	}

	public byte getState() {
		return this.state;
	}

	public byte getFacingDirection() {
		return this.facing;
	}

	public int getHealthChange() {
		return this.healthChange;
	}

	public int getHealthPos() {
		return (HEALTHLENGTH - this.healthCounter) / 4;
	}

	public void setFacingDirection(byte dir) {
		this.facing = dir;
	}

	public void setHealthChange(int healthChange) {
		this.healthChange = healthChange;
		this.healthCounter = HEALTHLENGTH;
	}

	public void setTickingSpeed(int tickSpeed) {
		this.tickSpeed = tickSpeed;
	}

}
