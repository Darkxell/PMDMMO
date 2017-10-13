package com.darkxell.client.renderers.floor;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;

import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.DungeonRenderer;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Directions;

public class DungeonPokemonRenderer extends AbstractRenderer
{

	private static byte spriteDirection(short facing)
	{
		for (byte i = 0; i < Directions.directions().length; ++i)
			if (facing == Directions.directions()[i]) return i;
		return 0;
	}

	private HashMap<DungeonPokemon, PokemonSprite> sprites;

	public DungeonPokemonRenderer()
	{
		this.setZ(DungeonRenderer.LAYER_POKEMON);
		this.sprites = new HashMap<DungeonPokemon, PokemonSprite>();
	}

	public void draw(Graphics2D g, DungeonPokemon pokemon)
	{
		if (pokemon.tile != null) this.draw(g, pokemon, pokemon.tile.x, pokemon.tile.y);
	}

	public void draw(Graphics2D g, DungeonPokemon pokemon, double x, double y)
	{
		if (!this.sprites.containsKey(pokemon)) this.register(pokemon);

		PokemonSprite sprite = this.sprites.get(pokemon);
		if (pokemon.stateChanged)
		{
			sprite.setFacingDirection(spriteDirection(pokemon.facing()));
			pokemon.stateChanged = false;
		}

		if (sprite.getCurrentSprite() != null)
		{
			Point p = this.drawLocation(pokemon);
			int xPos = (int) (x * TILE_SIZE + p.x), yPos = (int) (y * TILE_SIZE + p.y);
			g.drawImage(sprite.getCurrentSprite(), xPos, yPos, null);

			int h = sprite.getHealthChange();
			if (h != 0)
			{
				String text = (h < 0 ? "" : "+") + Integer.toString(h);
				xPos = (int) (x * TILE_SIZE + TILE_SIZE / 2 - TextRenderer.instance.width(text) / 2);
				yPos = (int) (y * TILE_SIZE - sprite.getHealthPos() - TextRenderer.CHAR_HEIGHT / 2);
				TextRenderer.instance.render(g, text, xPos, yPos, true);
			}
		}
	}

	public Point drawLocation(DungeonPokemon pokemon)
	{
		PokemonSprite sprite = this.sprites.get(pokemon);
		return new Point(TILE_SIZE / 2 - sprite.pointer.gravityX, TILE_SIZE / 2 - sprite.pointer.gravityY);
	}

	/** @return The Sprite of the input Pokémon. */
	public PokemonSprite getSprite(DungeonPokemon pokemon)
	{
		return this.sprites.get(pokemon);
	}

	/** Creates a Sprite for the input Pokémon. */
	public PokemonSprite register(DungeonPokemon pokemon)
	{
		if (!this.sprites.containsKey(pokemon)) this.sprites.put(pokemon, new PokemonSprite(PokemonSpritesets.getSpriteset(pokemon.pokemon.species.id)));
		return this.getSprite(pokemon);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		int xStart = this.x() / TILE_SIZE, yStart = this.y() / TILE_SIZE;

		/* for (int x = xStart; x <= xStart + width / TILE_SIZE + 1; ++x) for (int y = yStart; y <= yStart + height / TILE_SIZE + 1; ++y) { Tile tile = this.floor.tileAt(x, y); if (tile != null) if (tile.getPokemon() != null) this.draw(g, tile.getPokemon(), x, y); } */

		for (DungeonPokemon pokemon : this.sprites.keySet())
			this.draw(g, pokemon);
	}

	/** Creates the Sprite of the input Pokémon. */
	public void unregister(DungeonPokemon pokemon)
	{
		this.sprites.remove(pokemon);
	}

	public void update()
	{
		for (PokemonSprite sprite : this.sprites.values())
			sprite.update();
	}

}
