package com.darkxell.client.mechanics.freezones;

import java.util.ArrayList;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.player.Player;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.util.Logger;

public class FreezonePlayer
{

	public static final double MOVESPEED = 0.2;
	private boolean ismovingDOWN = false;
	private boolean ismovingLEFT = false;
	private boolean ismovingRIGHT = false;
	private boolean ismovingUP = false;

	private boolean isSprinting = false;

	private PokemonSprite playersprite;

	private AbstractPokemonRenderer renderer;

	/** The x position of the middle of the player hitbox. also corresponds to the gravity center of the sprite. */
	public double x;

	/** The y position of the middle of the player hitbox. also corresponds to the gravity center of the sprite. */
	public double y;

	public FreezonePlayer(PokemonSprite sprite, int x, int y)
	{
		this.playersprite = sprite;
		this.playersprite.setShadowColor(PokemonSprite.ALLY_SHADOW);
		this.renderer = new AbstractPokemonRenderer(this.playersprite);
		this.x = x;
		this.y = y;
	}

	/** Returns false if the player would collide in a solid tile/tileentity if it were at the wanted location, true otherwise. */
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

	public DoubleRectangle getHitboxAt(double x, double y)
	{
		return new DoubleRectangle(x - 0.8, y - 0.8, 1.6, 1.3);
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

	public boolean ismoving()
	{
		return ismovingUP || ismovingRIGHT || ismovingDOWN || ismovingLEFT;
	}

	public void pressKey(Key key)
	{
		switch (key)
		{
			case UP:
				ismovingUP = true;
				ismovingDOWN = false;
				playersprite.setFacingDirection(getFacingFromMoveDirections());
				playersprite.setState(PokemonSpriteState.MOVE, true);
				break;
			case RIGHT:
				ismovingRIGHT = true;
				ismovingLEFT = false;
				playersprite.setFacingDirection(getFacingFromMoveDirections());
				playersprite.setState(PokemonSpriteState.MOVE, true);
				break;
			case DOWN:
				ismovingDOWN = true;
				ismovingUP = false;
				playersprite.setFacingDirection(getFacingFromMoveDirections());
				playersprite.setState(PokemonSpriteState.MOVE, true);
				break;
			case LEFT:
				ismovingLEFT = true;
				ismovingRIGHT = false;
				playersprite.setFacingDirection(getFacingFromMoveDirections());
				playersprite.setState(PokemonSpriteState.MOVE, true);
				break;
			case ATTACK:
				if (canInteract()) getInteractionTarget().onInteract();
				break;
			case RUN:
				this.isSprinting = true;
				break;

			default:
				break;
		}
	}

	public void releaseKey(Key key)
	{
		switch (key)
		{
			case UP:
				ismovingUP = false;
				if (!ismoving()) playersprite.setState(PokemonSpriteState.IDLE);
				else playersprite.setFacingDirection(getFacingFromMoveDirections());
				break;
			case RIGHT:
				ismovingRIGHT = false;
				if (!ismoving()) playersprite.setState(PokemonSpriteState.IDLE);
				else playersprite.setFacingDirection(getFacingFromMoveDirections());
				break;
			case DOWN:
				ismovingDOWN = false;
				if (!ismoving()) playersprite.setState(PokemonSpriteState.IDLE);
				else playersprite.setFacingDirection(getFacingFromMoveDirections());
				break;
			case LEFT:
				ismovingLEFT = false;
				if (!ismoving()) playersprite.setState(PokemonSpriteState.IDLE);
				else playersprite.setFacingDirection(getFacingFromMoveDirections());
				break;
			case RUN:
				this.isSprinting = false;
				break;

			default:
				break;
		}
	}

	public AbstractPokemonRenderer renderer()
	{
		return this.renderer;
	}

	public void setPlayer(Player player)
	{
		Persistance.player = player;
		if (player != null) this.updateSprite();
	}

	public void setState(PokemonSpriteState state)
	{
		playersprite.setState(state);
	}

	public void update()
	{
		if (Persistance.player != null && Persistance.player.getTeamLeader().species().id != this.playersprite.pointer.pokemonID) this.updateSprite();
		this.renderer.update();
		this.renderer.setXY(this.x * 8, this.y * 8);
		double truemovespeed = isSprinting ? MOVESPEED * 2 : MOVESPEED;
		if (ismovingUP && canBeAt(this.x, this.y - truemovespeed)) this.y -= truemovespeed;
		if (ismovingRIGHT && canBeAt(this.x + truemovespeed, this.y)) this.x += truemovespeed;
		if (ismovingDOWN && canBeAt(this.x, this.y + truemovespeed)) this.y += truemovespeed;
		if (ismovingLEFT && canBeAt(this.x - truemovespeed, this.y)) this.x -= truemovespeed;
	}

	private void updateSprite()
	{
		PokemonSprite sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(Persistance.player.getTeamLeader()));
		sprite.copyState(this.playersprite);
		this.playersprite = sprite;
		this.renderer = new AbstractPokemonRenderer(this.playersprite);
	}

}
