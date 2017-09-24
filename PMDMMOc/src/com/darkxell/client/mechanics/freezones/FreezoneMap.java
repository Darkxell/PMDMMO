package com.darkxell.client.mechanics.freezones;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.input.SAXBuilder;

import com.darkxell.client.persistance.FreezoneMapHolder;
import com.darkxell.client.resources.images.AbstractTileset;
import com.darkxell.client.state.map.LocalMap.LOCALMAPLOCATION;

/**
 * A tiled map of a freezone. Freezones are the areas where you can move freely
 * and don't have to fight.
 */
public abstract class FreezoneMap {

	public FreezoneTile[] tiles;
	/** The width of the map, in tiles. */
	public int mapWidth;
	/** The height of the map, in tiles.s */
	public int mapHeight;

	public String freezonebgm = "";

	/**
	 * List the entities in this map. Note that the player isn't actually an
	 * entity.
	 */
	public ArrayList<FreezoneEntity> entities = new ArrayList<>();

	public ArrayList<WarpZone> warpzones = new ArrayList<>();

	public FreezoneMap(String xmlfilepath) {
		File file = new File(xmlfilepath);
		SAXBuilder builder = new SAXBuilder();
		org.jdom2.Element rootelement;
		try {
			rootelement = builder.build(file).getRootElement();
			this.mapWidth = Integer.parseInt(rootelement.getChild("width").getText()) / 8;
			this.mapHeight = Integer.parseInt(rootelement.getChild("height").getText()) / 8;
			List<org.jdom2.Element> tiles = rootelement.getChild("tiles").getChildren();
			this.tiles = new FreezoneTile[mapWidth * mapHeight];
			for (int i = 0; i < this.tiles.length; i++)
				this.tiles[i] = new FreezoneTile(FreezoneTile.TYPE_WALKABLE, null);
			for (org.jdom2.Element element : tiles) {
				int refferingTileID = (mapWidth * (Integer.parseInt(element.getAttributeValue("y")) / 8))
						+ (Integer.parseInt(element.getAttributeValue("x")) / 8);
				if (element.getAttributeValue("bgName").equals("terrain")) {
					this.tiles[refferingTileID].type = element.getAttributeValue("xo").equals("0")
							? FreezoneTile.TYPE_SOLID : FreezoneTile.TYPE_WALKABLE;
				} else {
					AbstractTileset t = AbstractTileset.getTileset(element.getAttributeValue("bgName"));
					this.tiles[refferingTileID].sprite = t.SPRITES[((t.getSource().getWidth() / 8)
							* (Integer.parseInt(element.getAttributeValue("yo")) / 8))
							+ (Integer.parseInt(element.getAttributeValue("xo")) / 8)];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
		FreezoneMapHolder.currentplayer.update();
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update();
	}

	public byte getTileTypeAt(double x, double y) {
		int calc = mapWidth * (int) y + (int) x;
		if (calc >= this.tiles.length || calc < 0)
			return FreezoneTile.TYPE_WALKABLE;
		return this.tiles[calc].type;
	}

	public abstract LOCALMAPLOCATION getMapLocation();

}
