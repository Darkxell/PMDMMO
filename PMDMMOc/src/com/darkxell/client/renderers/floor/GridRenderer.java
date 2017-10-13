package com.darkxell.client.renderers.floor;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.util.HashSet;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.DungeonRenderer;
import com.darkxell.client.resources.images.tilesets.CommonDungeonTileset;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Logger;

public class GridRenderer extends AbstractRenderer
{

	public final Floor floor;

	public GridRenderer()
	{
		this.floor = Persistance.floor;
		this.setZ(DungeonRenderer.LAYER_GRID);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		int xStart = (int) (this.x() / TILE_SIZE), yStart = (int) (this.y() / TILE_SIZE);

		DungeonPokemon player = Persistance.player.getDungeonPokemon();

		HashSet<Tile> faced = new HashSet<Tile>();
		Tile tile = player.tile;
		do
		{
			faced.add(tile);
			tile = tile.adjacentTile(player.facing());
		} while (tile != null);

		for (int x = xStart; x < this.floor.getWidth() && x <= xStart + width / TILE_SIZE + 1; ++x)
			for (int y = yStart; y < this.floor.getHeight() && y <= yStart + height / TILE_SIZE + 1; ++y)
			{
				tile = this.floor.tileAt(x, y);
				if (tile == null) Logger.e("null tile at " + x + ", " + y);
				if (tile.type() != TileType.WALL && tile.type() != TileType.WALL_END) g.drawImage(CommonDungeonTileset.INSTANCE.grid(faced.contains(tile)),
						tile.x * TILE_SIZE, tile.y * TILE_SIZE, null);
			}
	}

}
