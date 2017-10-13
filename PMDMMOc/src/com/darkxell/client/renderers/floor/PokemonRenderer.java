package com.darkxell.client.renderers.floor;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.Point;

import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Directions;

public class PokemonRenderer extends AbstractRenderer
{

	private static byte spriteDirection(short facing)
	{
		for (byte i = 0; i < Directions.directions().length; ++i)
			if (facing == Directions.directions()[i]) return i;
		return 0;
	}

	public final DungeonPokemon pokemon;
	public final PokemonSprite sprite;

	public PokemonRenderer(DungeonPokemon pokemon)
	{
		this.pokemon = pokemon;
		this.sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(this.pokemon.pokemon.species.id));
	}

	public Point drawLocation()
	{
		return new Point(TILE_SIZE / 2 - this.sprite.pointer.gravityX, TILE_SIZE / 2 - this.sprite.pointer.gravityY);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.pokemon.stateChanged)
		{
			this.sprite.setFacingDirection(spriteDirection(this.pokemon.facing()));
			pokemon.stateChanged = false;
		}

		if (this.sprite.getCurrentSprite() != null)
		{
			Point p = this.drawLocation();
			int xPos = (int) (this.x() * TILE_SIZE + p.x), yPos = (int) (this.y() * TILE_SIZE + p.y);
			g.drawImage(this.sprite.getCurrentSprite(), xPos, yPos, null);

			int h = this.sprite.getHealthChange();
			if (h != 0)
			{
				String text = (h < 0 ? "" : "+") + Integer.toString(h);
				xPos = (int) (this.x() * TILE_SIZE + TILE_SIZE / 2 - TextRenderer.instance.width(text) / 2);
				yPos = (int) (this.y() * TILE_SIZE - this.sprite.getHealthPos() - TextRenderer.CHAR_HEIGHT / 2);
				TextRenderer.instance.render(g, text, xPos, yPos, true);
			}
		}
	}

}
