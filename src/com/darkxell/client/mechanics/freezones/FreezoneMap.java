package com.darkxell.client.mechanics.freezones;

import java.io.File;
import java.util.List;

import org.jdom2.input.SAXBuilder;

import com.darkxell.client.resources.images.AbstractTileset;

/**
 * a tiled map of a freezone. Freezones are the areas where you can move freely
 * and don't have to fight.
 */
public class FreezoneMap {

	public FreezoneTile[] tiles;
	public int mapWidth;
	public int mapHeight;

	// TODO : entities and tileentities.

	public FreezoneMap(String xmlfilepath) {
		File file = new File(xmlfilepath);
		SAXBuilder builder = new SAXBuilder();
		org.jdom2.Element rootelement;
		try {
			rootelement = builder.build(file).getRootElement();
			this.mapWidth = Integer.parseInt(rootelement.getChild("width").getText());
			this.mapHeight = Integer.parseInt(rootelement.getChild("height").getText());
			List<org.jdom2.Element> tiles = rootelement.getChild("tiles").getChildren();
			this.tiles = new FreezoneTile[mapWidth * mapHeight];
			for (int i = 0; i < this.tiles.length; i++)
				this.tiles[i] = new FreezoneTile(FreezoneTile.TYPE_WALKABLE, null);
			for (org.jdom2.Element element : tiles) {
				int refferingTileID = ((Integer.parseInt(element.getAttributeValue("x")) / 8)
						* (Integer.parseInt(element.getAttributeValue("y")) / 8))
						+ (Integer.parseInt(element.getAttributeValue("x")) / 8);
				if (element.getAttributeValue("bgName").equals("terrain")) {
					this.tiles[refferingTileID].type = element.getAttributeValue("xo").equals("0")
							? FreezoneTile.TYPE_SOLID : FreezoneTile.TYPE_WALKABLE;
				} else {
					AbstractTileset t = AbstractTileset.getTileset(element.getAttributeValue("bgName"));
					this.tiles[refferingTileID].sprite = t.SPRITES[((Integer.parseInt(element.getAttributeValue("xo"))
							/ 8) * (Integer.parseInt(element.getAttributeValue("yo")) / 8))
							+ (Integer.parseInt(element.getAttributeValue("xo")) / 8)];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
