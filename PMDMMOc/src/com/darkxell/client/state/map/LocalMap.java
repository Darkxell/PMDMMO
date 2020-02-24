package com.darkxell.client.state.map;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.image.Sprites.MapSprites;
import com.darkxell.common.zones.LocalMapLocation;

public class LocalMap extends AbstractDisplayMap {

    public static final LocalMap instance = new LocalMap();

    public LocalMapLocation currentlocation = LocalMapLocation.BASE;

    @Override
    public void render(Graphics2D g, int width, int height) {
        Graphics2D g2 = this.canvas.createGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, width, height);
        if (!(currentlocation.x <= 1 && currentlocation.y <= 1)) {
            // displays the map if the location is possible
            int offsetx = this.canvas.getWidth() / 2 - currentlocation.x,
                    offsety = this.canvas.getHeight() / 2 - currentlocation.y;
            g2.drawImage(MapSprites.localMap.image(), offsetx, offsety, null);
            for (LocalMapLocation loc : LocalMapLocation.values())
                g2.drawImage(currentlocation == loc ? MapSprites.pins.red() : MapSprites.pins.yellow(), offsetx + loc.x,
                        offsety + loc.y, null);
        }
        // Displays the text position at the bottom
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
        if (Persistence.currentmap != null && Persistence.currentmap.info.maplocation != currentlocation)
            currentlocation = Persistence.currentmap.info.maplocation;
    }

}
