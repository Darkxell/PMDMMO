package com.darkxell.client.resources.images.tilesets;

import com.darkxell.client.resources.images.AbstractTileset;

public class BaseTileset  extends AbstractTileset {

	public static BaseTileset instance = new BaseTileset();

	public BaseTileset() {
		super("/tilesets/base.png", 8, 8);
	}

}
