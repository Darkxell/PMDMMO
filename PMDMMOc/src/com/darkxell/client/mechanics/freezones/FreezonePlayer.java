package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.resources.images.AbstractPokemonSpriteset;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.util.DoubleRectangle;

public class FreezonePlayer {

	public FreezoneMap map;
	/**
	 * The x position of the middle of the player hitbox. also corresponds to
	 * the gravity center of the sprite.
	 */
	public double x;
	/**
	 * The y position of the middle of the player hitbox. also corresponds to
	 * the gravity center of the sprite.
	 */
	public double y;
	public PokemonSprite playersprite;

	public FreezonePlayer(FreezoneMap map, PokemonSprite sprite, int x, int y) {
		this.playersprite = sprite;
		this.x = x;
		this.y = y;
		this.map = map;
	}

	public DoubleRectangle getHitboxAt(double x, double y) {
		return new DoubleRectangle(x - 0.8, y - 0.8, 1.6, 1.3);
	}

	/**
	 * Returns false if the player would collind in a solid tile/tileentity if
	 * it were at the wanted location, true otherwise.
	 */
	public boolean canBeAt(double x, double y) {
		DoubleRectangle hbx = getHitboxAt(x, y);
		if (this.map.getTileTypeAt(hbx.x, hbx.y) == FreezoneTile.TYPE_SOLID)
			return false;
		if (this.map.getTileTypeAt(hbx.x, hbx.y + hbx.height) == FreezoneTile.TYPE_SOLID)
			return false;
		if (this.map.getTileTypeAt(hbx.x + hbx.width, hbx.y) == FreezoneTile.TYPE_SOLID)
			return false;
		if (this.map.getTileTypeAt(hbx.x + hbx.width, hbx.y + hbx.height) == FreezoneTile.TYPE_SOLID)
			return false;
		return true;
	}

	public void update() {
		this.playersprite.update();

		if (ismovingUP && canBeAt(this.x, this.y - MOVESPEED))
			this.y -= MOVESPEED;
		if (ismovingRIGHT && canBeAt(this.x + MOVESPEED, this.y))
			this.x += MOVESPEED;
		if (ismovingDOWN && canBeAt(this.x, this.y + MOVESPEED))
			this.y += MOVESPEED;
		if (ismovingLEFT && canBeAt(this.x - MOVESPEED, this.y))
			this.x -= MOVESPEED;
	}

	public void pressKey(short key) {
		switch (key) {
		case Keys.KEY_UP:
			ismovingUP = true;
			ismovingDOWN = false;
			playersprite.setFacingDirection(getFacingFromMoveDirections());
			playersprite.setState(PokemonSprite.STATE_MOVE);
			break;
		case Keys.KEY_RIGHT:
			ismovingRIGHT = true;
			ismovingLEFT = false;
			playersprite.setFacingDirection(getFacingFromMoveDirections());
			playersprite.setState(PokemonSprite.STATE_MOVE);
			break;
		case Keys.KEY_DOWN:
			ismovingDOWN = true;
			ismovingUP = false;
			playersprite.setFacingDirection(getFacingFromMoveDirections());
			playersprite.setState(PokemonSprite.STATE_MOVE);
			break;
		case Keys.KEY_LEFT:
			ismovingLEFT = true;
			ismovingRIGHT = false;
			playersprite.setFacingDirection(getFacingFromMoveDirections());
			playersprite.setState(PokemonSprite.STATE_MOVE);
			break;
		}
	}

	public void releaseKey(short key) {
		switch (key) {
		case Keys.KEY_UP:
			ismovingUP = false;
			if (!ismoving())
				playersprite.setState(PokemonSprite.STATE_IDDLE);
			else
				playersprite.setFacingDirection(getFacingFromMoveDirections());
			break;
		case Keys.KEY_RIGHT:
			ismovingRIGHT = false;
			if (!ismoving())
				playersprite.setState(PokemonSprite.STATE_IDDLE);
			else
				playersprite.setFacingDirection(getFacingFromMoveDirections());
			break;
		case Keys.KEY_DOWN:
			ismovingDOWN = false;
			if (!ismoving())
				playersprite.setState(PokemonSprite.STATE_IDDLE);
			else
				playersprite.setFacingDirection(getFacingFromMoveDirections());
			break;
		case Keys.KEY_LEFT:
			ismovingLEFT = false;
			if (!ismoving())
				playersprite.setState(PokemonSprite.STATE_IDDLE);
			else
				playersprite.setFacingDirection(getFacingFromMoveDirections());
			break;
		}
	}

	public static final double MOVESPEED = 0.13;
	private boolean ismovingUP = false;
	private boolean ismovingRIGHT = false;
	private boolean ismovingDOWN = false;
	private boolean ismovingLEFT = false;

	private byte getFacingFromMoveDirections() {
		if (ismovingUP)
			if (ismovingRIGHT)
				return AbstractPokemonSpriteset.FACING_NE;
			else if (ismovingLEFT)
				return AbstractPokemonSpriteset.FACING_NW;
			else
				return AbstractPokemonSpriteset.FACING_N;
		else if (ismovingRIGHT)
			if (ismovingUP)
				return AbstractPokemonSpriteset.FACING_NE;
			else if (ismovingDOWN)
				return AbstractPokemonSpriteset.FACING_SE;
			else
				return AbstractPokemonSpriteset.FACING_E;
		else if (ismovingDOWN)
			if (ismovingRIGHT)
				return AbstractPokemonSpriteset.FACING_SE;
			else if (ismovingLEFT)
				return AbstractPokemonSpriteset.FACING_SW;
			else
				return AbstractPokemonSpriteset.FACING_S;
		else if (ismovingLEFT)
			if (ismovingUP)
				return AbstractPokemonSpriteset.FACING_NW;
			else if (ismovingDOWN)
				return AbstractPokemonSpriteset.FACING_SW;
			else
				return AbstractPokemonSpriteset.FACING_W;
		// NOT MOVING, CAN'T DETERMINE!
		System.err.println(
				"Could not determine facing direction from movements since the player is not moving. Returned north.");
		return AbstractPokemonSpriteset.FACING_N;
	}

	public boolean ismoving() {
		return ismovingUP || ismovingRIGHT || ismovingDOWN || ismovingLEFT;
	}

}
