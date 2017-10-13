package com.darkxell.client.mechanics.animation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.tilesets.StatusSpritesets;
import com.darkxell.common.pokemon.DungeonPokemon;

public class StatChangeAnimation extends PokemonAnimation
{

	private BufferedImage[] frontSprites, backSprites;
	public final int stage;
	public final int stat;

	public StatChangeAnimation(AnimationEndListener listener, DungeonPokemon target, int stat, int stage)
	{
		super(target, 30, listener);
		this.stat = stat;
		this.stage = stage;

		this.frontSprites = StatusSpritesets.instance.getSprites(this.stat, true, this.stage >= 0);
		this.backSprites = StatusSpritesets.instance.getSprites(this.stat, false, this.stage >= 0);
	}

	private BufferedImage backSprite()
	{
		return this.backSprites[(int) (this.completion() * this.backSprites.length)];
	}

	private BufferedImage frontSprite()
	{
		return this.frontSprites[(int) (this.completion() * this.frontSprites.length)];
	}

	@Override
	public void postrender(Graphics2D g, int width, int height)
	{
		super.postrender(g, width, height);
		g.drawImage(this.frontSprite(), (int) this.x, (int) this.y, null);
	}

	@Override
	public void prerender(Graphics2D g, int width, int height)
	{
		super.prerender(g, width, height);
		g.drawImage(this.backSprite(), (int) this.x, (int) this.y, null);
	}

	@Override
	public void update()
	{
		super.update();
		this.x -= StatusSpritesets.SPRITE_SIZE / 2;
		this.y -= StatusSpritesets.SPRITE_SIZE / 2;
	}

}
