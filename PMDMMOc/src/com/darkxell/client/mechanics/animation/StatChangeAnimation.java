package com.darkxell.client.mechanics.animation;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.resources.images.tilesets.StatusSpritesets;
import com.darkxell.common.pokemon.DungeonPokemon;

public class StatChangeAnimation extends AbstractAnimation
{

	private BufferedImage[] frontSprites, backSprites;
	public final int stage;
	public final int stat;
	public final DungeonPokemon target;

	public StatChangeAnimation(AnimationEndListener listener, DungeonPokemon target, int stat, int stage)
	{
		super(30, listener);
		this.target = target;
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
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		int x = this.target.tile.x * TILE_SIZE + TILE_SIZE / 2 - StatusSpritesets.SPRITE_SIZE / 2;
		int y = this.target.tile.y * TILE_SIZE + TILE_SIZE / 2 - StatusSpritesets.SPRITE_SIZE / 2;

		g.drawImage(this.backSprite(), x, y, null);
		DungeonPokemonRenderer.instance.draw(g, this.target);
		g.drawImage(this.frontSprite(), x, y, null);
	}
}
