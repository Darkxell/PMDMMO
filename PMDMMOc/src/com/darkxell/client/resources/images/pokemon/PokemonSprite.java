package com.darkxell.client.resources.images.pokemon;

import java.awt.image.BufferedImage;

import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;

public class PokemonSprite
{

	public static enum PokemonSpriteState
	{
		APPEAL(22),
		ATTACK(2, true),
		BITE(15, true),
		CHARGE(6),
		CHOP(9, true),
		DANCE(23),
		DOUBLE(37),
		EMIT(32),
		FLAPAROUND(29),
		GAS(30, true),
		HIGHJUMP(41),
		HURT(5),
		IDLE(0),
		JAB(17, true),
		JUMP(40),
		KICK(18, true),
		LICK(19, true),
		MOVE(1),
		MULTISCRATCH(11, true),
		MULTISTRIKE(12, true),
		REARUP(34),
		RICOCHET(14, true),
		ROTATE(38),
		RUMBLE(28),
		SHAKE(16),
		SHOCK(31),
		SHOOT(7),
		SING(26),
		SLAM(20, true),
		SLEEP(4),
		SLICE(10, true),
		SOUND(27),
		SPECIAL(3),
		SPIN(39),
		STOMP(21, true),
		STRIKE(8, true),
		SWELL(35),
		SWING(36),
		TAILWHIP(25),
		TWIRL(24),
		UPPERCUT(13, true),
		WITHDRAW(33);
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
	public final AbstractPokemonSpriteset pointer;
	/** When true, if in a repeatable state, will reset to idle state at the end of the current animation. Else, will keep on the same animation. */
	private boolean resetToDefaultOnFinish = false;
	private byte shadowColor = NEUTRAL_SHADOW;
	private PokemonSpriteState state = PokemonSpriteState.IDLE;
	private float tickSpeed = 1;

	public PokemonSprite(AbstractPokemonSpriteset pointer)
	{
		this.pointer = pointer;
	}

	public PokemonSpriteFrame getCurrentFrame()
	{
		return this.pointer.getSprite(this.state, this.facing, (int) this.counter);
	}

	public BufferedImage getCurrentSprite()
	{
		return this.getCurrentFrame().getSprite();
	}

	public Direction getFacingDirection()
	{
		return this.facing;
	}

	public byte getShadowColor()
	{
		return this.shadowColor;
	}

	public PokemonSpriteState getState()
	{
		return this.state;
	}

	public void resetOnAnimationEnd()
	{
		this.resetToDefaultOnFinish = true;
	}

	public void setFacingDirection(Direction dir)
	{
		this.facing = dir;
	}

	public void setShadowColor(byte shadowColor)
	{
		this.shadowColor = shadowColor;
	}

	public void setState(PokemonSpriteState state)
	{
		this.setState(state, false);
	}

	public PokemonSpriteState defaultState()
	{
		return this.defaultState;
	}

	public void setDefaultState(PokemonSpriteState state, boolean apply)
	{
		this.defaultState = state;
		if (apply) this.setState(state);
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
		this.resetToDefaultOnFinish = !playOnLoop;
	}

	public void setTickingSpeed(float tickSpeed)
	{
		this.tickSpeed = tickSpeed;
	}

	/** Updates this sprite to the next frame. */
	public void update()
	{
		if (this.isAnimated()) this.counter += this.tickSpeed;
		PokemonSpriteSequence state = this.pointer.getSequence(this.state, this.facing);
		if (this.counter >= state.duration)
		{
			this.counter = 0;
			if (this.resetToDefaultOnFinish && this.state != PokemonSpriteState.IDLE) this.setState(this.defaultState);
		}
	}

	public void updateTickingSpeed(DungeonPokemon pokemon)
	{
		int s = pokemon.stats.getStage(Stat.Speed);
		if (s == 0) this.setTickingSpeed(SLOWER);
		else if (s == 1) this.setTickingSpeed(REGULAR_SPEED);
		else this.setTickingSpeed(QUICKER);
	}

	public void copyState(PokemonSprite sprite)
	{
		this.facing = sprite.facing;
		this.resetToDefaultOnFinish = sprite.resetToDefaultOnFinish;
		this.shadowColor = sprite.shadowColor;
		this.tickSpeed = sprite.tickSpeed;
	}

	public void setAnimated(boolean animated)
	{
		this.animated = animated;
	}

	public boolean isAnimated()
	{
		return this.animated;
	}

}
