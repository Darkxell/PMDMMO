package com.darkxell.client.mechanics.freezones;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.resources.images.pokemon.AbstractPokemonSpriteset;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.util.DoubleRectangle;

public class FreezonePlayer {

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

	public FreezonePlayer(PokemonSprite sprite, int x, int y) {
		this.playersprite = sprite;
		this.x = x;
		this.y = y;
	}

	public DoubleRectangle getHitboxAt(double x, double y) {
		return new DoubleRectangle(x - 0.8, y - 0.8, 1.6, 1.3);
	}

	/**
	 * Returns false if the player would collind in a solid tile/tileentity if
	 * it were at the wanted location, true otherwise.
	 */
	public boolean canBeAt(double x, double y) {
		if (Persistance.currentmap == null)
			return true;
		DoubleRectangle hbx = getHitboxAt(x, y);
		for (int i = 0; i < Persistance.currentmap.entities.size(); i++) {
			FreezoneEntity ety = Persistance.currentmap.entities.get(i);
			if (ety.isSolid && ety.getHitbox(ety.posX, ety.posY).intersects(this.getHitboxAt(x, y)))
				return false;
		}

		if (Persistance.currentmap.getTileTypeAt(hbx.x, hbx.y) == FreezoneTile.TYPE_SOLID)
			return false;
		if (Persistance.currentmap.getTileTypeAt(hbx.x, hbx.y + hbx.height) == FreezoneTile.TYPE_SOLID)
			return false;
		if (Persistance.currentmap.getTileTypeAt(hbx.x + hbx.width, hbx.y) == FreezoneTile.TYPE_SOLID)
			return false;
		if (Persistance.currentmap.getTileTypeAt(hbx.x + hbx.width,
				hbx.y + hbx.height) == FreezoneTile.TYPE_SOLID)
			return false;
		return true;
	}

	public void update() {
		this.playersprite.update();
		double truemovespeed = isSprinting ? MOVESPEED * 2 : MOVESPEED;
		if (ismovingUP && canBeAt(this.x, this.y - truemovespeed))
			this.y -= truemovespeed;
		if (ismovingRIGHT && canBeAt(this.x + truemovespeed, this.y))
			this.x += truemovespeed;
		if (ismovingDOWN && canBeAt(this.x, this.y + truemovespeed))
			this.y += truemovespeed;
		if (ismovingLEFT && canBeAt(this.x - truemovespeed, this.y))
			this.x -= truemovespeed;
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
		case Keys.KEY_ATTACK:
			if (canInteract())
				getInteractionTarget().onInteract();
			break;
		case Keys.KEY_RUN:
			this.isSprinting = true;
			break;
		}
	}

	public void releaseKey(short key) {
		switch (key) {
		case Keys.KEY_UP:
			ismovingUP = false;
			if (!ismoving())
				playersprite.setState(PokemonSprite.STATE_IDLE);
			else
				playersprite.setFacingDirection(getFacingFromMoveDirections());
			break;
		case Keys.KEY_RIGHT:
			ismovingRIGHT = false;
			if (!ismoving())
				playersprite.setState(PokemonSprite.STATE_IDLE);
			else
				playersprite.setFacingDirection(getFacingFromMoveDirections());
			break;
		case Keys.KEY_DOWN:
			ismovingDOWN = false;
			if (!ismoving())
				playersprite.setState(PokemonSprite.STATE_IDLE);
			else
				playersprite.setFacingDirection(getFacingFromMoveDirections());
			break;
		case Keys.KEY_LEFT:
			ismovingLEFT = false;
			if (!ismoving())
				playersprite.setState(PokemonSprite.STATE_IDLE);
			else
				playersprite.setFacingDirection(getFacingFromMoveDirections());
			break;
		case Keys.KEY_RUN:
			this.isSprinting = false;
			break;
		}
	}

	public void setState(byte state) {
		playersprite.setState(state);
	}

	/**
	 * Force the player from stopping it's movement. This will also prevent the
	 * polayer from sprinting util he presses the sprint key again.
	 */
	public void forceStop() {
		ismovingUP = false;
		ismovingRIGHT = false;
		ismovingDOWN = false;
		ismovingLEFT = false;
		isSprinting = false;
		playersprite.setState(PokemonSprite.STATE_IDLE);
	}

	public static final double MOVESPEED = 0.2;
	private boolean ismovingUP = false;
	private boolean ismovingRIGHT = false;
	private boolean ismovingDOWN = false;
	private boolean ismovingLEFT = false;
	private boolean isSprinting = false;

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

	/** Returns the hitbox of the interaction window in front of the player. */
	public DoubleRectangle getInteractionBox() {
		byte facing = playersprite.getFacingDirection();
		double tpx = this.x;
		if (facing == AbstractPokemonSpriteset.FACING_E || facing == AbstractPokemonSpriteset.FACING_NE
				|| facing == AbstractPokemonSpriteset.FACING_SE)
			tpx += 2.5;
		if (facing == AbstractPokemonSpriteset.FACING_W || facing == AbstractPokemonSpriteset.FACING_NW
				|| facing == AbstractPokemonSpriteset.FACING_SW)
			tpx -= 2.5;
		double tpy = this.y;
		if (facing == AbstractPokemonSpriteset.FACING_S || facing == AbstractPokemonSpriteset.FACING_SE
				|| facing == AbstractPokemonSpriteset.FACING_SW)
			tpy += 2.5;
		if (facing == AbstractPokemonSpriteset.FACING_N || facing == AbstractPokemonSpriteset.FACING_NE
				|| facing == AbstractPokemonSpriteset.FACING_NW)
			tpy -= 2.5;
		return new DoubleRectangle(tpx, tpy, 1.3, 1.3, true);
	}

	/**
	 * Returns true if the player can interact with something in it's current
	 * position.
	 */
	public boolean canInteract() {
		if (Persistance.currentmap == null)
			return false;
		for (int i = 0; i < Persistance.currentmap.entities.size(); ++i) {
			FreezoneEntity et = Persistance.currentmap.entities.get(i);
			if (et.canInteract && et.getHitbox(et.posX, et.posY).intersects(this.getInteractionBox()))
				return true;
		}
		return false;
	}

	/** Returns the first entity found the player can interact with. */
	public FreezoneEntity getInteractionTarget() {
		if (Persistance.currentmap == null)
			return null;
		for (int i = 0; i < Persistance.currentmap.entities.size(); i++) {
			FreezoneEntity et = Persistance.currentmap.entities.get(i);
			if (et.canInteract && et.getHitbox(et.posX, et.posY).intersects(this.getInteractionBox()))
				return et;
		}
		return null;
	}

}
