package com.darkxell.client.renderers;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.resources.images.pokemons.PokemonSpriteset1;
import com.darkxell.client.resources.images.pokemons.PokemonSpriteset265;
import com.darkxell.client.resources.images.tilesets.CommonDungeonTileset;
import com.darkxell.client.resources.images.tilesets.FloorDungeonTileset;
import com.darkxell.client.resources.images.tilesets.ItemsSpriteset;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.pokemon.PokemonD;
import com.darkxell.common.util.GameUtil;

public class FloorRenderer
{
	private static final int ITEM_POS = (AbstractDungeonTileset.TILE_SIZE - ItemsSpriteset.ITEM_SIZE) / 2;
	private static final PokemonSprite P = new PokemonSprite(PokemonSpriteset265.instance);
	private static final PokemonSprite PS = new PokemonSprite(PokemonSpriteset1.instance);

	private static byte spriteDirection(short facing)
	{
		for (byte i = 0; i < GameUtil.directions().length; ++i)
			if (facing == GameUtil.directions()[i]) return i;
		return 0;
	}

	public final Floor floor;
	public final FloorDungeonTileset tileset;

	public FloorRenderer(Floor floor)
	{
		this.floor = floor;
		this.tileset = new FloorDungeonTileset("resources/tilesets/dungeon-" + floor.dungeon.id + ".png");
	}

	/** Renders the Floor.
	 * 
	 * @param xPos, yPos - Translate values
	 * @param width, height - State dimension */
	public void drawFloor(Graphics2D g, int xPos, int yPos, int width, int height)
	{
		int xStart = xPos / TILE_SIZE, yStart = yPos / TILE_SIZE;
		g.translate(-xPos, -yPos);

		for (int x = xStart; x < Floor.ALL_WIDTH && x <= xStart + width / TILE_SIZE + 1; ++x)
			for (int y = yStart; y < Floor.ALL_HEIGHT && y <= yStart + height / TILE_SIZE + 1; ++y)
				this.drawTile(g, this.floor.tileAt(x, y));

		for (int x = xStart; x < Floor.ALL_WIDTH && x <= xStart + width / TILE_SIZE + 1; ++x)
			for (int y = yStart; y < Floor.ALL_HEIGHT && y <= yStart + height / TILE_SIZE + 1; ++y)
			{
				Tile t = this.floor.tileAt(x, y);
				if (t.getPokemon() != null) this.drawPokemon(g, t.getPokemon(), x, y);
			}

		g.translate(xPos, yPos);
	}

	/** Renders a Pokémon. */
	private void drawPokemon(Graphics2D g, PokemonD pokemon, int x, int y)
	{
		PokemonSprite s = pokemon.pokemon.species.id == 1 ? PS : P;
		if (pokemon.stateChanged)
		{
			s.setFacingDirection(spriteDirection(pokemon.facing()));
			pokemon.stateChanged = false;
		}
		g.drawImage(s.getCurrentSprite(), x * TILE_SIZE + TILE_SIZE / 2 - s.pointer.gravityX, y * TILE_SIZE + TILE_SIZE / 2 - s.pointer.gravityY, null);
	}

	/** Renders a Tile. */
	public void drawTile(Graphics2D g, Tile tile)
	{
		if (tile == null) return;

		BufferedImage sprite = null;
		if (tile.type() == TileType.STAIR) sprite = CommonDungeonTileset.INSTANCE.stairs(this.floor.dungeon.direction);
		else if (tile.type() == TileType.WONDER_TILE) sprite = CommonDungeonTileset.INSTANCE.wonderTile();
		else if (tile.type() == TileType.WARP_ZONE) sprite = CommonDungeonTileset.INSTANCE.warp();
		else sprite = this.tileset.tile(tile);

		g.drawImage(sprite, tile.x * TILE_SIZE, tile.y * TILE_SIZE, null);
		if (tile.getItem() != null && tile.type() == TileType.GROUND) g.drawImage(ItemsSpriteset.instance.SPRITES[tile.getItem().item().spriteID], tile.x
				* TILE_SIZE + ITEM_POS, tile.y * TILE_SIZE + ITEM_POS, null);

		g.drawRect(tile.x * TILE_SIZE, tile.y * TILE_SIZE, (tile.x + 1) * TILE_SIZE, (tile.y + 1) * TILE_SIZE);
	}

	public void update()
	{
		P.update();
		PS.update();
	}

}
