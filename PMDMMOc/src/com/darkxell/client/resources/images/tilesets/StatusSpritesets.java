package com.darkxell.client.resources.images.tilesets;

import java.awt.image.BufferedImage;

import com.darkxell.common.pokemon.PokemonStats;

public class StatusSpritesets extends AbstractTileset
{

	public static StatusSpritesets instance = new StatusSpritesets();

	public static final int SPRITE_SIZE = 50, SPRITE_COUNT = 13;

	public StatusSpritesets()
	{
		super("/status/stat_changes.png", SPRITE_SIZE, SPRITE_SIZE);
	}

	public BufferedImage[] getSprites(int stat, boolean front, boolean rising)
	{
		int y = stat * 2;
		if (stat > PokemonStats.HEALTH) y -= 2;
		if (!front) y += 1;

		BufferedImage[] sprites = new BufferedImage[SPRITE_COUNT];
		for (int x = 0; x < SPRITE_COUNT; ++x)
			sprites[x] = this.SPRITES[y * SPRITE_COUNT + (rising ? x : SPRITE_COUNT - 1 - x)];

		return sprites;
	}

}
