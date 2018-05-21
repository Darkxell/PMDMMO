package com.darkxell.client.resources.images.tilesets.freezones;

import com.darkxell.client.resources.images.tilesets.AbstractTileset;

public class LcaveTileset extends AbstractTileset{

	public static LcaveTileset instance = new LcaveTileset();

	public LcaveTileset() {
		super("/tilesets/lcave.png", 8, 8);
	}
}
