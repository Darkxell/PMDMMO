package com.darkxell.client.mechanics.animation;

import java.awt.Graphics2D;

import com.darkxell.client.resources.images.AnimationSpriteset;
import com.darkxell.common.pokemon.DungeonPokemon;

public class SpritesetAnimation extends PokemonAnimation
{

	public static enum BackSpriteUsage
	{
		/** The sprites should be drawn behind the Pokemon. */
		no,
		/** The sprites should be drawn above the Pokemon. */
		only,
		/** There are sprites behind and above the Pokemon. */
		yes;
	}

	public final BackSpriteUsage backSprites;
	/** Gravity values for this Animation. Defaults to half this Spriteset's size. */
	public final int gravityX, gravityY;
	/** The duration of each sprite. */
	public final int spriteDuration;
	/** The order of sprites. -1 for no sprite. */
	private final int[] sprites;
	/** The spriteset to use. */
	public final AnimationSpriteset spriteset;

	public SpritesetAnimation(DungeonPokemon target, AnimationSpriteset spriteset, int[] sprites, BackSpriteUsage backsprites, int spriteDuration, int gravityX,
			int gravityY, AnimationEndListener listener)
	{
		super(target, sprites.length * spriteDuration, listener);
		this.spriteset = spriteset;
		this.sprites = sprites;
		this.backSprites = backsprites;
		this.spriteDuration = spriteDuration;
		this.gravityX = gravityX;
		this.gravityY = gravityY;
	}

	private void draw(Graphics2D g, boolean back)
	{
		int index = this.index();
		if (back && this.backSprites == BackSpriteUsage.yes) index += this.spriteset.spriteCount() / 2;

		if (index != -1 && ((back && this.backSprites != BackSpriteUsage.no) || (!back && this.backSprites != BackSpriteUsage.only)))
			g.drawImage(this.spriteset.getSprite(index), (int) this.x - this.gravityX, (int) (this.y - this.gravityY), null);
	}

	public double getX()
	{
		return this.x;
	}

	public double getY()
	{
		return this.y;
	}

	public int index()
	{
		int i = this.tick() % this.duration / this.spriteDuration;
		if (i >= this.sprites.length) return -1;
		return this.sprites[i];
	}

	@Override
	public void postrender(Graphics2D g, int width, int height)
	{
		super.postrender(g, width, height);
		this.draw(g, false);
	}

	@Override
	public void prerender(Graphics2D g, int width, int height)
	{
		super.prerender(g, width, height);
		this.draw(g, true);
	}

	public void setXY(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

}
