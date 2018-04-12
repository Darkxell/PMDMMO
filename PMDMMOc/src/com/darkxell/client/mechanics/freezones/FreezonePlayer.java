package com.darkxell.client.mechanics.freezones;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.player.Player;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.Logger;

public class FreezonePlayer
{

	/** The x position of the middle of the player hitbox. also corresponds to the gravity center of the sprite. */
	public double x;
	/** The y position of the middle of the player hitbox. also corresponds to the gravity center of the sprite. */
	public double y;
	/** Reference to the player object if loaded. For now, only used if it's the main Player. */
	private Player player;
	public PokemonSprite playersprite;

	public FreezonePlayer(PokemonSprite sprite, int x, int y)
	{
		this(null, sprite, x, y);
	}

	public FreezonePlayer(Player player, int x, int y)
	{
		this(player, new PokemonSprite(PokemonSpritesets.getSpriteset(player.getTeamLeader())), x, y);
	}

	public FreezonePlayer(Player player, PokemonSprite sprite, int x, int y)
	{
		this.player = player;
		this.playersprite = sprite;
		this.x = x;
		this.y = y;
	}

	public DoubleRectangle getHitboxAt(double x, double y)
	{
		return new DoubleRectangle(x - 0.8, y - 0.8, 1.6, 1.3);
	}

	/** Returns false if the player would collind in a solid tile/tileentity if it were at the wanted location, true otherwise. */
	public boolean canBeAt(double x, double y)
	{
		if (Persistance.currentmap == null) return true;
		DoubleRectangle hbx = getHitboxAt(x, y);
		ArrayList<FreezoneEntity> entities = Persistance.currentmap.entities();
		for (int i = 0; i < entities.size(); i++)
		{
			FreezoneEntity ety = entities.get(i);
			if (ety.isSolid && ety.getHitbox(ety.posX, ety.posY).intersects(this.getHitboxAt(x, y))) return false;
		}

		if (Persistance.currentmap.getTileTypeAt(hbx.x, hbx.y) == FreezoneTile.TYPE_SOLID) return false;
		if (Persistance.currentmap.getTileTypeAt(hbx.x, hbx.y + hbx.height) == FreezoneTile.TYPE_SOLID) return false;
		if (Persistance.currentmap.getTileTypeAt(hbx.x + hbx.width, hbx.y) == FreezoneTile.TYPE_SOLID) return false;
		if (Persistance.currentmap.getTileTypeAt(hbx.x + hbx.width, hbx.y + hbx.height) == FreezoneTile.TYPE_SOLID) return false;
		return true;
	}

	public void update()
	{
		if (this.player != null && this.player.getTeamLeader().species().compoundID() != this.playersprite.pointer.pokemonID) this.updateSprite();
		this.playersprite.update();
		double truemovespeed = isSprinting ? MOVESPEED * 2 : MOVESPEED;
		if (ismovingUP && canBeAt(this.x, this.y - truemovespeed)) this.y -= truemovespeed;
		if (ismovingRIGHT && canBeAt(this.x + truemovespeed, this.y)) this.x += truemovespeed;
		if (ismovingDOWN && canBeAt(this.x, this.y + truemovespeed)) this.y += truemovespeed;
		if (ismovingLEFT && canBeAt(this.x - truemovespeed, this.y)) this.x -= truemovespeed;
	}

	public void pressKey(short key)
	{
		switch (key)
		{
			case Keys.KEY_UP:
				ismovingUP = true;
				ismovingDOWN = false;
				playersprite.setFacingDirection(getFacingFromMoveDirections());
				playersprite.setState(PokemonSpriteState.MOVE, true);
				break;
			case Keys.KEY_RIGHT:
				ismovingRIGHT = true;
				ismovingLEFT = false;
				playersprite.setFacingDirection(getFacingFromMoveDirections());
				playersprite.setState(PokemonSpriteState.MOVE, true);
				break;
			case Keys.KEY_DOWN:
				ismovingDOWN = true;
				ismovingUP = false;
				playersprite.setFacingDirection(getFacingFromMoveDirections());
				playersprite.setState(PokemonSpriteState.MOVE, true);
				break;
			case Keys.KEY_LEFT:
				ismovingLEFT = true;
				ismovingRIGHT = false;
				playersprite.setFacingDirection(getFacingFromMoveDirections());
				playersprite.setState(PokemonSpriteState.MOVE, true);
				break;
			case Keys.KEY_ATTACK:
				if (canInteract()) getInteractionTarget().onInteract();
				break;
			case Keys.KEY_RUN:
				this.isSprinting = true;
				break;
		}
	}

	public void releaseKey(short key)
	{
		switch (key)
		{
			case Keys.KEY_UP:
				ismovingUP = false;
				if (!ismoving()) playersprite.setState(PokemonSpriteState.IDLE);
				else playersprite.setFacingDirection(getFacingFromMoveDirections());
				break;
			case Keys.KEY_RIGHT:
				ismovingRIGHT = false;
				if (!ismoving()) playersprite.setState(PokemonSpriteState.IDLE);
				else playersprite.setFacingDirection(getFacingFromMoveDirections());
				break;
			case Keys.KEY_DOWN:
				ismovingDOWN = false;
				if (!ismoving()) playersprite.setState(PokemonSpriteState.IDLE);
				else playersprite.setFacingDirection(getFacingFromMoveDirections());
				break;
			case Keys.KEY_LEFT:
				ismovingLEFT = false;
				if (!ismoving()) playersprite.setState(PokemonSpriteState.IDLE);
				else playersprite.setFacingDirection(getFacingFromMoveDirections());
				break;
			case Keys.KEY_RUN:
				this.isSprinting = false;
				break;
		}
	}

	public void setState(PokemonSpriteState state)
	{
		playersprite.setState(state);
	}

	/** Force the player from stopping it's movement. This will also prevent the polayer from sprinting util he presses the sprint key again. */
	public void forceStop()
	{
		ismovingUP = false;
		ismovingRIGHT = false;
		ismovingDOWN = false;
		ismovingLEFT = false;
		isSprinting = false;
		playersprite.setState(PokemonSpriteState.IDLE);
	}

	public static final double MOVESPEED = 0.2;
	private boolean ismovingUP = false;
	private boolean ismovingRIGHT = false;
	private boolean ismovingDOWN = false;
	private boolean ismovingLEFT = false;
	private boolean isSprinting = false;

	private Direction getFacingFromMoveDirections()
	{
		if (ismovingUP)
		{
			if (ismovingRIGHT) return Direction.NORTHEAST;
			else if (ismovingLEFT) return Direction.NORTHWEST;
			else return Direction.NORTH;
		} else if (ismovingDOWN)
		{
			if (ismovingRIGHT) return Direction.SOUTHEAST;
			else if (ismovingLEFT) return Direction.SOUTHWEST;
			else return Direction.SOUTH;
		} else if (ismovingLEFT) return Direction.WEST;
		else if (ismovingRIGHT) return Direction.EAST;

		// NOT MOVING, CAN'T DETERMINE!
		Logger.e("Could not determine facing direction from movements since the player is not moving. Returned north.");
		return Direction.NORTH;
	}

	public boolean ismoving()
	{
		return ismovingUP || ismovingRIGHT || ismovingDOWN || ismovingLEFT;
	}

	/** Returns the hitbox of the interaction window in front of the player. */
	public DoubleRectangle getInteractionBox()
	{
		Direction facing = playersprite.getFacingDirection();

		double tpx = this.x;
		if (facing.contains(Direction.EAST)) tpx += 2.5;
		if (facing.contains(Direction.WEST)) tpx -= 2.5;

		double tpy = this.y;
		if (facing.contains(Direction.SOUTH)) tpy += 2.5;
		if (facing.contains(Direction.NORTH)) tpy -= 2.5;

		return new DoubleRectangle(tpx, tpy, 1.3, 1.3, true);
	}

	/** Returns true if the player can interact with something in it's current position. */
	public boolean canInteract()
	{
		if (Persistance.currentmap == null) return false;
		ArrayList<FreezoneEntity> entities = Persistance.currentmap.entities();
		for (int i = 0; i < entities.size(); ++i)
		{
			FreezoneEntity et = entities.get(i);
			if (et.canInteract && et.getHitbox(et.posX, et.posY).intersects(this.getInteractionBox())) return true;
		}
		return false;
	}

	/** Returns the first entity found the player can interact with. */
	public FreezoneEntity getInteractionTarget()
	{
		if (Persistance.currentmap == null) return null;
		ArrayList<FreezoneEntity> entities = Persistance.currentmap.entities();
		for (int i = 0; i < entities.size(); i++)
		{
			FreezoneEntity et = entities.get(i);
			if (et.canInteract && et.getHitbox(et.posX, et.posY).intersects(this.getInteractionBox())) return et;
		}
		return null;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
		if (player != null) this.updateSprite();
	}

	private void updateSprite()
	{
		PokemonSprite sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(this.player.getTeamLeader()));
		sprite.copyState(this.playersprite);
		this.playersprite = sprite;
	}

}
