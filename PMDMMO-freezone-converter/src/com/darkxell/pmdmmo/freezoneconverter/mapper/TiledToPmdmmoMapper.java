package com.darkxell.pmdmmo.freezoneconverter.mapper;

import static com.darkxell.client.resources.image.tileset.freezone.AbstractFreezoneTileset.TILE_SIZE;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import com.darkxell.client.model.freezone.FreezoneModel;
import com.darkxell.client.model.freezone.FreezoneTileModel;
import com.darkxell.pmdmmo.freezoneconverter.tiledmodel.TiledFreezoneModel;
import com.darkxell.pmdmmo.freezoneconverter.tiledmodel.TiledFreezoneTileModel;

public class TiledToPmdmmoMapper {

	private static class TilesetCounter implements Comparable<TilesetCounter> {
		public int count;
		public String name;

		public TilesetCounter(String bgName) {
			this.name = bgName;
			this.count = 0;
		}

		@Override
		public int compareTo(TilesetCounter o) {
			return Integer.compare(count, o.count);
		}
	}

	private static String findDefaultTileset(ArrayList<TiledFreezoneTileModel> tiles) {
		HashMap<String, TilesetCounter> tilesets = new HashMap<>();
		for (TiledFreezoneTileModel tile : tiles) {
			if (tile.getBgName().equals("terrain"))
				continue;
			if (!tilesets.containsKey(tile.getBgName()))
				tilesets.put(tile.getBgName(), new TilesetCounter(tile.getBgName()));
			++tilesets.get(tile.getBgName()).count;
		}

		ArrayList<TilesetCounter> count = new ArrayList<>(tilesets.values());
		count.sort(Comparator.naturalOrder());
		return count.get(count.size() - 1).name;
	}

	public static FreezoneModel map(TiledFreezoneModel tmodel) {
		FreezoneModel model = new FreezoneModel();
		model.setWidth(tmodel.getWidth() / TILE_SIZE);
		model.setHeight(tmodel.getHeight() / TILE_SIZE);
		model.setDefaultTileset(findDefaultTileset(tmodel.getTiles()));
		mapTiles(tmodel, model);
		return model;
	}

	private static void mapTerrainTiles(ArrayList<TiledFreezoneTileModel> terrainTiles, FreezoneModel model) {
		for (TiledFreezoneTileModel ttile : terrainTiles) {
			FreezoneTileModel tile = model.findTile(ttile.getX() / TILE_SIZE, ttile.getY() / TILE_SIZE);
			if (tile == null)
				System.err.println("Tile (" + ttile.getX() / TILE_SIZE + ", " + ttile.getY() / TILE_SIZE
						+ ") present in terrain but not in tileset data!");
			else
				tile.setSolid(ttile.getXo() == 0);
		}
	}

	private static void mapTiles(TiledFreezoneModel tmodel, FreezoneModel model) {
		ArrayList<TiledFreezoneTileModel> terrainTiles = new ArrayList<>();
		ArrayList<TiledFreezoneTileModel> tilesetTiles = new ArrayList<>();

		for (TiledFreezoneTileModel tile : tmodel.getTiles()) {
			if (tile.getBgName().equals("terrain"))
				terrainTiles.add(tile);
			else
				tilesetTiles.add(tile);
		}

		model.setTiles(new ArrayList<>());
		mapTilesetTiles(tilesetTiles, model);
		mapTerrainTiles(terrainTiles, model);
	}

	private static void mapTilesetTiles(ArrayList<TiledFreezoneTileModel> tilesetTiles, FreezoneModel model) {
		for (TiledFreezoneTileModel ttile : tilesetTiles) {
			FreezoneTileModel tile = new FreezoneTileModel();
			tile.setX(ttile.getX() / TILE_SIZE);
			tile.setY(ttile.getY() / TILE_SIZE);
			tile.setXo(ttile.getXo() / TILE_SIZE);
			tile.setYo(ttile.getYo() / TILE_SIZE);
			tile.setTileset(ttile.getBgName());
			tile.setSolid(false);
			model.getTiles().add(tile);
		}
	}

}
