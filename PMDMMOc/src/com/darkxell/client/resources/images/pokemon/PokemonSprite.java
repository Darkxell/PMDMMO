package com.darkxell.client.resources.images.pokemon;

import java.awt.image.BufferedImage;

import com.darkxell.common.util.Direction;

public class PokemonSprite
{

	public final AbstractPokemonSpriteset pointer;

	public PokemonSprite(AbstractPokemonSpriteset pointer)
	{
		this.pointer = pointer;
	}

	/** Updates this sprite to the next frame. */
	public void update()
	{
		this.counter += this.tickSpeed;
		PokemonSpriteSequence state = this.pointer.getSequence(this.state, this.facing);
		if (this.counter >= state.duration)
		{
			this.counter = 0;
			if (this.resetToIdleOnFinish && this.state != PokemonSpriteState.IDLE) this.setState(PokemonSpriteState.IDLE);
		}

		if (this.healthCounter > 0) --this.healthCounter;
		if (this.healthCounter == 0)
		{
			this.healthChange = 0;
		}
	}

	public PokemonSpriteFrame getCurrentFrame()
	{
		return this.pointer.getSprite(this.state, this.facing, this.counter);
	}

	public BufferedImage getCurrentSprite()
	{
		return this.getCurrentFrame().getSprite();
	}

	public void resetOnAnimationEnd()
	{
		this.resetToIdleOnFinish = true;
	}

	public void setState(PokemonSpriteState state)
	{
		this.setState(state, false);
	}

	/** Changes the state of this Sprite to the wanted one. If the parsed state is already the state used by the pokemon sprite, this does nothing.
	 * 
	 * @param playOnLoop - true if the state should play on loop until notified to stop. Defaults to false (i.e. only plays once). */
	public void setState(PokemonSpriteState state, boolean playOnLoop)
	{
		if (this.state != state)
		{
			this.state = state;
			this.counter = 0;
		}
		this.resetToIdleOnFinish = !playOnLoop;
	}

	private Direction facing = Direction.SOUTH;

	public static enum PokemonSpriteState
	{
		IDLE(0),
		MOVE(1),
		ATTACK(2, true),
		SPECIAL(3),
		SLEEP(4),
		HURT(5),
		CHARGE(6),
		SHOOT(7),
		STRIKE(8, true),
		CHOP(9, true),
		SLICE(10, true),
		MULTISCRATCH(11, true),
		MULTISTRIKE(12, true),
		UPPERCUT(13, true),
		RICOCHET(14, true),
		BITE(15, true),
		SHAKE(16),
		JAB(17, true),
		KICK(18, true),
		LICK(19, true),
		SLAM(20, true),
		STOMP(21, true),
		APPEAL(22),
		DANCE(23),
		TWIRL(24),
		TAILWHIP(25),
		SING(26),
		SOUND(27),
		RUMBLE(28),
		FLAPAROUND(29),
		GAS(30, true),
		SHOCK(31),
		EMIT(32),
		WITHDRAW(33),
		REARUP(34),
		SWELL(35),
		SWING(36),
		DOUBLE(37),
		ROTATE(38),
		SPIN(39),
		JUMP(40),
		HIGHJUMP(41);
		/* REST(6), WAKING(7), VICTORYPOSE(8), EATING(9); */

		/** True if the State's animation should dash forward. */
		public final boolean hasDash;
		public final int id;

		private PokemonSpriteState(int id)
		{
			this(id, false);
		}

		private PokemonSpriteState(int id, boolean hasDash)
		{
			this.id = id;
			this.hasDash = hasDash;
		}
	}

	private PokemonSpriteState state = PokemonSpriteState.IDLE;

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
	private int tickSpeed = 1;
	private byte shadowColor = NEUTRAL_SHADOW;

	public byte getShadowColor()
	{
		return this.shadowColor;
	}

	public void setShadowColor(byte shadowColor)
	{
		this.shadowColor = shadowColor;
	}

	public PokemonSpriteState getState()
	{
		return this.state;
	}

	public Direction getFacingDirection()
	{
		return this.facing;
	}

	public int getHealthChange()
	{
		return this.healthChange;
	}

	public int getHealthPos()
	{
		return (HEALTHLENGTH - this.healthCounter) / 4;
	}

	public void setFacingDirection(Direction dir)
	{
		this.facing = dir;
	}

	public void setHealthChange(int healthChange)
	{
		this.healthChange = healthChange;
		this.healthCounter = HEALTHLENGTH;
	}

	public void setTickingSpeed(int tickSpeed)
	{
		this.tickSpeed = tickSpeed;
	}

}
