package com.darkxell.client.resources.images.tilesets;

import com.darkxell.client.resources.images.AbstractTileset;

public class SquareTileset extends AbstractTileset {

	public static SquareTileset instance = new SquareTileset();

	public SquareTileset() {
		super("/tilesets/square.png", 8, 8);
	}

}
