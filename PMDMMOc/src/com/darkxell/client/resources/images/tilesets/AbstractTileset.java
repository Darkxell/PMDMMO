package com.darkxell.client.resources.images.tilesets;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;
import com.darkxell.client.resources.images.tilesets.freezones.BaseTileset;
import com.darkxell.client.resources.images.tilesets.freezones.DojoTileset;
import com.darkxell.client.resources.images.tilesets.freezones.ForestTileset;
import com.darkxell.client.resources.images.tilesets.freezones.LcaveTileset;
import com.darkxell.client.resources.images.tilesets.freezones.OfficeTileset;
import com.darkxell.client.resources.images.tilesets.freezones.PondTileset;
import com.darkxell.client.resources.images.tilesets.freezones.SquareTileset;

/**
 * Represents a tileset. A tileset contains all of the images used in a certain
 * dungeon.
 */
public abstract class AbstractTileset {

	public AbstractTileset(String path, int tilewidth, int tileheight) {
		this.source = Res.getBase(path);
		int rows, cols;
		cols = this.source.getWidth() / tilewidth;
		rows = this.source.getHeight() / tileheight;
		this.SPRITES = new BufferedImage[cols * rows];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				SPRITES[(cols * i) + j] = Res.createimage(this.source, tilewidth * j, tileheight * i, tilewidth,
						tileheight);
	}

	/** The source image */
	private final BufferedImage source;
	/**
	 * The tileset sprites. For specific IDs, refer to AbstractTileset.ID_[...]
	 */
	public final BufferedImage[] SPRITES;

	/**
	 * Gets The wanted existing instance of a tileset using a string ID. THis
	 * string ID is usually used in the maps .xml files.
	 */
	public static AbstractTileset getTileset(String code) {
		switch (code) {
		case "square":
			return SquareTileset.instance;
		case "base":
			return BaseTileset.instance;
		case "dojo":
			return DojoTileset.instance;
		case "forest":
			return ForestTileset.instance;
		case "pond":
			return PondTileset.instance;
		case "lcave":
		case "lumiouscave":
			return LcaveTileset.instance;
		case "pelipper office":
		case "office":
			return OfficeTileset.instance;
		default:
			System.err.println("Could not find the desired tileset: " + code);
			return null;
		}
	}

	/** Returns the source bufferedImage of this tileset. */
	public BufferedImage getSource() {
		return this.source;
	}

}
