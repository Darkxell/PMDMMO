package com.darkxell.client.resources.images.tilesets;

import com.darkxell.client.resources.images.AbstractTileset;

public class PondTileset extends AbstractTileset {

	public static PondTileset instance = new PondTileset();

	public PondTileset() {
		super("/tilesets/pond.png", 8, 8);
	}

}