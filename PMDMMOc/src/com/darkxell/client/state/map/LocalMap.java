package com.darkxell.client.state.map;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.images.Sprites.Res_Map;
import com.darkxell.common.zones.LocalMapLocation;

public class LocalMap extends AbstractDisplayMap {

	public static final LocalMap instance = new LocalMap();

	public LocalMapLocation currentlocation = LocalMapLocation.BASE;

	@Override
	public void render(Graphics2D g, int width, int height) {
		Graphics2D g2 = this.canvas.createGraphics();
		int offsetx = this.canvas.getWidth() / 2 - currentlocation.x,
				offsety = this.canvas.getHeight() / 2 - currentlocation.y;
		g2.drawImage(Res_Map.LOCALMAP.image(), offsetx, offsety, null);
		for (LocalMapLocation loc : LocalMapLocation.values())
			g2.drawImage(currentlocation == loc ? Res_Map.PIN_RED.image() : Res_Map.PIN_YELLOW.image(), offsetx + loc.x,
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
		if (Persistance.currentmap != null && Persistance.currentmap.getInfo().maplocation != currentlocation)
			currentlocation = Persistance.currentmap.getInfo().maplocation;
	}

}
