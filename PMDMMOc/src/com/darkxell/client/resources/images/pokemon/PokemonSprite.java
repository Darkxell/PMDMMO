package com.darkxell.client.resources.images.pokemon;

import java.awt.image.BufferedImage;

public class PokemonSprite {

	public final AbstractPokemonSpriteset pointer;

	public PokemonSprite(AbstractPokemonSpriteset pointer) {
		this.pointer = pointer;
	}

	/** Updates this sprite to the next frame. */
	public void update() {
		++this.counter;
		PokemonSpritesetState state = this.pointer.states[this.state];
		if (this.counter > state.duration(this.statecounter))
		{
			this.counter = 0;
			if (this.statecounter + 1 < state.indexes.length) ++this.statecounter;
			else
			{
				if (this.state == STATE_ATTACK || this.state == STATE_HURT || this.state == STATE_SPECIAL1 || this.state == STATE_SPECIAL2 || this.state == STATE_WAKING
						|| this.state == STATE_VICTORYPOSE) this.setState(STATE_IDDLE);
				else this.statecounter = 0;
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

	public void setState(byte state) {
		this.state = state;
		this.counter = 0;
		this.statecounter = 0;
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
	public static final byte STATE_IDDLE = 0;
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

	private int counter = 0;
	private int healthChange = 0;
	private int healthCounter = 0;
	private int statecounter = 0;

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

}
