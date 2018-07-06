package com.darkxell.client.resources.images.tilesets;

import java.util.HashMap;

public class FreezoneTilesetFactory {

	/** Returns a tileset made from the parsed tileset image file path. */
	public static AbstractTileset getTileset(String path) {
		if (tilesets.containsKey(path))
			return tilesets.get(path);
		AbstractTileset tileset = new AbstractTileset(path, 8, 8);
		tilesets.put(path, tileset);
		return tileset;
	}

	private static HashMap<String, AbstractTileset> tilesets = new HashMap<>(50);

}
