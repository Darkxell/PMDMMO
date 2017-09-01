package com.darkxell.client.resources.images.tilesets;

import com.darkxell.client.resources.images.AbstractTileset;

public class DojoTileset extends AbstractTileset {

	public static DojoTileset instance = new DojoTileset();

	public DojoTileset() {
		super("/tilesets/dojo.png", 8, 8);
	}

}