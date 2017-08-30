package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

public class PokemonSprite {

	public final AbstractPokemonSpriteset pointer;

	public PokemonSprite(AbstractPokemonSpriteset pointer) {
		this.pointer = pointer;
	}

	/** Updates this sprite to the next frame. */
	public void update() {
		++counter;
		switch (state) {
		case STATE_IDDLE:
			if (counter > pointer.iddleAnimation[statecounter])
				if (statecounter + 1 < pointer.iddleAnimation.length) {
					++statecounter;
					counter = 0;
				} else {
					counter = 0;
					statecounter = 0;
				}

			break;
		case STATE_MOVE:
			if (counter >= FRAMELENGTH) {
				counter = 0;
				if (statecounter == pointer.moveFrames - 1)
					statecounter = 0;
				else
					++statecounter;
			}
			break;
		case STATE_ATTACK:
			if (counter >= FRAMELENGTH) {
				counter = 0;
				if (statecounter == pointer.attackFrames - 1)
					setState(STATE_IDDLE);
				else
					++statecounter;
			}
			break;
		case STATE_SPECIAL1:
			if (counter >= FRAMELENGTH) {
				counter = 0;
				if (statecounter == pointer.specialFrames - 1)
					setState(STATE_IDDLE);
				else
					++statecounter;
			}
			break;
		case STATE_SPECIAL2:
			if (counter >= FRAMELENGTH) {
				counter = 0;
				if (statecounter == pointer.special2Frames - 1)
					setState(STATE_IDDLE);
				else
					++statecounter;
			}
			break;
		case STATE_SLEEP:
		case STATE_REST:
			if (counter >= FRAMELENGTH) {
				counter = 0;
				if (statecounter == 1)
					statecounter = 0;
				else
					++statecounter;
			}
			break;
		case STATE_HURT:
			if (counter >= FRAMELENGTH)
				setState(STATE_IDDLE);
			break;
		case STATE_WAKING:
			if (counter >= FRAMELENGTH) {
				counter = 0;
				if (statecounter == pointer.ambiantwakeFrames - 1)
					setState(STATE_IDDLE);
				else
					++statecounter;
			}
			break;
		case STATE_VICTORYPOSE:
			if (counter >= FRAMELENGTH) {
				counter = 0;
				if (statecounter == pointer.ambiantvictoryFrames - 1)
					setState(STATE_IDDLE);
				else
					++statecounter;
			}
			break;
		case STATE_EATING:
			if (counter >= FRAMELENGTH) {
				counter = 0;
				if (statecounter == 3)
					statecounter = 0;
				else
					++statecounter;
			}
			break;
		default:
			System.err.println("Pokemon " + this.toString() + "in not in a valid state! id: " + state);
			break;
		}
	}

	public BufferedImage getCurrentSprite() {
		switch (state) {
		case STATE_IDDLE:
			return this.pointer.getIddleSprite(this.facing, this.statecounter);
		case STATE_MOVE:
			return this.pointer.getMoveSprite(this.facing, this.statecounter);
		case STATE_ATTACK:
			return this.pointer.getAttackSprite(this.facing, this.statecounter);
		case STATE_SPECIAL1:
			return this.pointer.getSpecialSprite(this.facing, this.statecounter);
		case STATE_SPECIAL2:
			return this.pointer.getSpecial2Sprite(this.facing, this.statecounter);
		case STATE_SLEEP:
			return this.pointer.getSleepSprite(this.facing, this.statecounter);
		case STATE_REST:
			return this.pointer.getRestSprite(this.facing, this.statecounter);
		case STATE_HURT:
			return this.pointer.getHurtSprite(this.facing, this.statecounter);
		case STATE_WAKING:
			return this.pointer.getWakeSprite(this.facing, this.statecounter);
		case STATE_VICTORYPOSE:
			return this.pointer.getVictorySprite(this.facing, this.statecounter);
		case STATE_EATING:
			return this.pointer.getJewelsSprite(this.facing, this.statecounter);
		default:
			System.err.println(
					"Pokemon " + this.toString() + "in not in a valid state and cannot return a sprite! id: " + state);
			return null;
		}
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

	private static final int FRAMELENGTH = 10;

	private int counter = 0;
	private int statecounter = 0;

	public byte getFacingDirection() {
		return this.facing;
	}

	public void setFacingDirection(byte dir) {
		this.facing = dir;
	}

}
