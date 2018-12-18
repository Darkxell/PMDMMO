package com.darkxell.client.renderers.pokemon;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.common.pokemon.DungeonPokemon;

/** Renders a Pokemon. This Renderer's Coordinates' units are Tiles. */
public class DungeonPokemonRenderer extends AbstractPokemonRenderer
{
	public final DungeonPokemon pokemon;

	int tick = 0;

	public DungeonPokemonRenderer(DungeonPokemon pokemon)
	{
		super(new PokemonSprite(PokemonSpritesets.getSpriteset(pokemon.usedPokemon)));
		this.pokemon = pokemon;
		this.setX(pokemon.tile().x);
		this.setY(pokemon.tile().y);
	}

	@Override
	public double drawX()
	{
		return this.x() + TILE_SIZE / 2;
	}

	@Override
	public double drawY()
	{
		return this.y() + TILE_SIZE / 2;
	}

	@Override
	public void render(Graphics2D g, PokemonSprite sprite, int x, int y)
	{
		double percent = this.pokemon.getHpPercentage();
		if (ClientSettings.getBooleanSetting(ClientSettings.HP_BARS))
		{
			int colorPercent = (int) Math.round(percent * 2.55);
			Rectangle outline = new Rectangle(x - TILE_SIZE / 2, y + TILE_SIZE / 3, TILE_SIZE, 5);
			Rectangle insideFilled = new Rectangle(outline.x + 1, outline.y + 1, (int) ((outline.width - 1) * percent / 100), outline.height - 1);
			int emptyWidth = outline.width - 1 - insideFilled.width;
			Rectangle insideEmpty = new Rectangle(outline.x + 1 + insideFilled.width, outline.y + 1, emptyWidth, outline.height - 1);
			g.setColor(new Color(255 - colorPercent, colorPercent, Math.max(128 - colorPercent, 0), 220));
			g.draw(outline);
			g.setColor(new Color(255 - colorPercent, colorPercent, Math.max(128 - colorPercent, 0), 150));
			g.fill(insideFilled);
			g.setColor(new Color(200, 0, 50, 50));
			g.fill(insideEmpty);
		}

		super.render(g, sprite, x, y);
	}

	@Override
	public boolean shouldRender(int width, int height)
	{
		if (!Persistence.dungeonState.floorVisibility.isVisible(this.pokemon)) return false;
		return super.shouldRender(width, height);
	}

	@Override
	public void update()
	{
		super.update();
		if (!(Persistence.stateManager.getCurrentState() instanceof NextFloorState) && this.pokemon.facing() != this.sprite.getFacingDirection())
			this.sprite.setFacingDirection(this.pokemon.facing());
	}

	@Override
	public double x()
	{
		return super.x() * TILE_SIZE;
	}

	@Override
	public double y()
	{
		return super.y() * TILE_SIZE;
	}

}
