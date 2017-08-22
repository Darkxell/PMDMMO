package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;

/**
 * Represents a tileset. A tileset contains all of the images used in a certain
 * dungeon.
 */
public abstract class AbstractTileset {

	public AbstractTileset(String path, int tilewidth, int tileheight) {
		this.source = Res.getBase(path);
		int rows, cols;
		rows = this.source.getWidth() / tilewidth;
		cols = this.source.getHeight() / tileheight;
		this.SPRITES = new BufferedImage[cols * rows];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				SPRITES[(cols * i) + j] = Res.createimage(this.source, 16 * j, 16 * i, 16, 16);
	}

	/** The source image */
	private final BufferedImage source;
	/**
	 * The tileset sprites. For specific IDs, refer to AbstractTileset.ID_[...]
	 */
	public final BufferedImage[] SPRITES;

}
