package com.darkxell.client.mechanics.animation;

import java.awt.Graphics2D;
import java.awt.Point;

import com.darkxell.client.resources.images.hud.AnimationSpriteset;

public class StaticAnimation extends AbstractAnimation
{

	private static int computeDuration(int[][] sprites)
	{
		int d = 0;
		for (int[] s : sprites)
			d += s[1];
		return d;
	}

	private static int[][] makeSprites(int spriteCount, int spriteDuration)
	{
		int[][] sprites = new int[spriteCount][2];
		for (int i = 0; i < spriteCount; ++i)
		{
			sprites[i][0] = i;
			sprites[i][1] = spriteDuration;
		}
		return sprites;
	}

	protected final Point location;
	private final int[][] sprites;
	public final AnimationSpriteset spriteset;

	public StaticAnimation(AnimationEndListener listener, AnimationSpriteset spriteset, Point location, int spriteDuration)
	{
		this(listener, spriteset, location, makeSprites(spriteset.spriteCount(), spriteDuration));
	}

	/** @param sprites - Describes which sprites to display and for how long. */
	public StaticAnimation(AnimationEndListener listener, AnimationSpriteset spriteset, Point location, int[][] sprites)
	{
		super(computeDuration(sprites), listener);
		this.spriteset = spriteset;
		this.location = location;
		this.sprites = sprites;
	}

	public int currentSprite()
	{
		int s = this.tick();
		for (int[] sprite : this.sprites)
		{
			s -= sprite[1];
			if (s < 0) return sprite[0];
		}
		return 0;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		g.drawImage(this.spriteset.getSprite(this.currentSprite()), this.location.x, this.location.y, null);
	}

}
