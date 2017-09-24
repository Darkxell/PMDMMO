package com.darkxell.client.state.map;

import java.awt.Graphics2D;

import com.darkxell.client.persistance.FreezoneMapHolder;
import com.darkxell.client.resources.images.MapResources;

public class LocalMap extends AbstractDisplayMap {

	public LOCALMAPLOCATION currentlocation = LOCALMAPLOCATION.BASE;

	@Override
	public void render(Graphics2D g, int width, int height) {
		Graphics2D g2 = this.canvas.createGraphics();
		int offsetx = this.canvas.getWidth() / 2 - currentlocation.x,
				offsety = this.canvas.getHeight() / 2 - currentlocation.y;
		g2.drawImage(MapResources.LOCALMAP, offsetx, offsety, null);
		for (LOCALMAPLOCATION loc : LOCALMAPLOCATION.values())
			g2.drawImage(currentlocation == loc ? MapResources.PIN_RED : MapResources.PIN_YELLOW, offsetx + loc.x,
					offsety + loc.y, null);
		// finished the render
		g2.dispose();
		g.drawImage(this.canvas, 0, 0, width, height, null);
	}

	@Override
	public void update() {
		if (FreezoneMapHolder.currentmap != null && FreezoneMapHolder.currentmap.getMapLocation() != currentlocation)
			currentlocation = FreezoneMapHolder.currentmap.getMapLocation();
	}

	public enum LOCALMAPLOCATION {
		BASE(340, 160),
		SQUARE(355, 165),
		DOJO(353, 180),
		POND(350, 147),
		OFFICE(372, 160),
		ZONE_PLAINS(350, 119),
		ZONE_CAVERN(355, 70),
		ZONE_NORTHENLAKE(384, 31),
		ZONE_SWAMP(414, 69),
		ZONE_GLACIER(454, 21),
		ZONE_MOUNTAINS(455, 113),
		ZONE_ORIENTALFOREST(460, 162),
		ZONE_RIVER(400, 158),
		ZONE_EASTERNLAKESIDE(402, 206),
		ZONE_ORIENTALLAKE(452, 206),
		ZONE_DESERT(450, 309),
		ZONE_PLATEAU(403, 256),
		ZONE_SOUTHERNPLAINS(350, 310),
		ZONE_JUNGLE(343, 260),
		ZONE_RUINS(300, 260),
		ZONE_VOLCANO(267, 204),
		ZONE_OCCIDENTALFOREST(265, 154),
		ZONE_POND(308, 126),
		ZONE_REMAINS(320, 24),
		ZONE_NORTHENISLAND(263, 74),
		ZONE_NORTHENSEA(212, 25),
		ZONE_SKY(30, 25),
		ZONE_SEASIDE(145, 75),
		ZONE_WESTERNISLAND(49, 116),
		ZONE_SOUTHERNISLAND(91, 265),
		ZONE_SOUTHWESTISLAND(37, 207),
		ZONE_SOUTHERNSEA(147, 292);

		public final int x;
		public final int y;

		private LOCALMAPLOCATION(int x, int y) {
			this.x = x;
			this.y = y;
		}

	}

}
