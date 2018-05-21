package com.darkxell.client.state.map;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.images.others.MapResources;
import com.darkxell.common.util.language.Message;

public class LocalMap extends AbstractDisplayMap {

	public static final LocalMap instance = new LocalMap();
	
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
		g2.setColor(Palette.TRANSPARENT_GRAY);
		g2.fillRect(0, this.canvas.getHeight() - 20, this.canvas.getWidth(), 20);
		TextRenderer.render(g2, currentlocation.displayname.toString(),
				this.canvas.getWidth() / 2 - TextRenderer.width(currentlocation.displayname) / 2,
				this.canvas.getHeight() - 15);
		// finished the render
		g2.dispose();
		g.drawImage(this.canvas, 0, 0, width, height, null);
	}

	@Override
	public void update() {
		if (Persistance.currentmap != null && Persistance.currentmap.getMapLocation() != currentlocation)
			currentlocation = Persistance.currentmap.getMapLocation();
	}

	public enum LOCALMAPLOCATION {
		BASE(340, 160, new Message("zone.base"),"base"),
		SQUARE(355, 165, new Message("zone.square"),"square"),
		DOJO(353, 180, new Message("zone.dojo"),"dojo"),
		POND(350, 147, new Message("zone.wpond"),"wpond"),
		OFFICE(372, 160, new Message("zone.office"),"office"),
		ZONE_PLAINS(350, 119, new Message("zone.plains"),""),
		ZONE_CAVERN(355, 70, new Message("zone.cavern"),""),
		ZONE_NORTHENLAKE(384, 31, new Message("zone.northenlake"),""),
		ZONE_SWAMP(414, 69, new Message("zone.swamp"),""),
		ZONE_GLACIER(454, 21, new Message("zone.glacier"),""),
		ZONE_MOUNTAINS(455, 113, new Message("zone.mountains"),""),
		ZONE_ORIENTALFOREST(460, 162, new Message("zone.orientalforest"),""),
		ZONE_RIVER(400, 158, new Message("zone.river"),""),
		ZONE_EASTERNLAKESIDE(402, 206, new Message("zone.easternlakeside"),""),
		ZONE_ORIENTALLAKE(452, 206, new Message("zone.orientallake"),""),
		ZONE_DESERT(450, 309, new Message("zone.desert"),""),
		ZONE_PLATEAU(403, 256, new Message("zone.plateau"),""),
		ZONE_SOUTHERNPLAINS(350, 310, new Message("zone.southernplains"),""),
		ZONE_JUNGLE(343, 260, new Message("zone.jungle"),""),
		ZONE_RUINS(300, 260, new Message("zone.ruins"),""),
		ZONE_VOLCANO(267, 204, new Message("zone.volcano"),""),
		ZONE_OCCIDENTALFOREST(265, 154, new Message("zone.occidentalforest"),""),
		ZONE_POND(308, 126, new Message("zone.pond"),""),
		ZONE_REMAINS(320, 24, new Message("zone.remains"),""),
		ZONE_NORTHENISLAND(263, 74, new Message("zone.northenisland"),""),
		ZONE_NORTHENSEA(212, 25, new Message("zone.northensea"),""),
		ZONE_SKY(30, 25, new Message("zone.sky"),""),
		ZONE_SEASIDE(145, 75, new Message("zone.seaside"),""),
		ZONE_WESTERNISLAND(49, 116, new Message("zone.westernisland"),""),
		ZONE_SOUTHERNISLAND(91, 265, new Message("zone.southernisland"),""),
		ZONE_SOUTHWESTISLAND(37, 207, new Message("zone.southwestisland"),""),
		ZONE_SOUTHERNSEA(147, 292, new Message("zone.southernsea"),""),
		STRATFOREST(320, 150, new Message("zone.startforest"),"forest"),
		LUMIOUSCAVE(355, 137, new Message("zone.lumiouscave"),"lcave");

		public final int x;
		public final int y;
		public final Message displayname;
		public final String id;
		
		private LOCALMAPLOCATION(int x, int y, Message name,String id) {
			this.x = x;
			this.y = y;
			this.displayname = name;
			this.id = id;
		}

	}

}
