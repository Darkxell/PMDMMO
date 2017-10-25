package com.darkxell.client.resources.images.tilesets.freezones;

import com.darkxell.client.resources.images.tilesets.AbstractTileset;

public class BaseTileset  extends AbstractTileset {

	public static BaseTileset instance = new BaseTileset();

	public BaseTileset() {
		super("/tilesets/base.png", 8, 8);
	}

}
