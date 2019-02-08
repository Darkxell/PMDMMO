package com.darkxell.client.state.freezone;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;

import com.darkxell.client.graphics.AbstractGraphicsLayer;
import com.darkxell.client.graphics.AbstractRenderer;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezone.FreezoneMap;
import com.darkxell.client.mechanics.freezone.FreezoneTerrain;
import com.darkxell.client.mechanics.freezone.FreezoneTile;
import com.darkxell.client.mechanics.freezone.Freezones;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.DoubleRectangle;
import com.darkxell.common.zones.FreezoneInfo;

public class AbstractFreezoneState extends AbstractState {

    public boolean musicset = false;

    @Override
    public void onKeyPressed(Key key) {
    }

    @Override
    public void onKeyReleased(Key key) {
    }

    @Override
    public void render(Graphics2D g, int width, int height) {
        FreezoneMap map = Persistence.currentmap;
        Persistence.freezoneCamera.renderHeight = height;
        Persistence.freezoneCamera.renderWidth = width;
        // Draws the sea background if needed.
        AbstractGraphicsLayer background = Persistence.currentmap.getBackground();
        if (background != null)
            background.render(g, width, height);
        // Draws the surroundings.
        int translateX = (int) (-Persistence.freezoneCamera.finalX() * 8 + (width / 2));
        int translateY = (int) (-Persistence.freezoneCamera.finalY() * 8 + (height / 2));

        g.translate(translateX, translateY);

        // Draws the map
        FreezoneTerrain terrain = map.getTerrain();
        for (int i = 0; i < terrain.getHeight(); i++)
            for (int j = 0; j < terrain.getWidth(); j++) {
                FreezoneTile tile = terrain.get(j, i);
                if (tile.sprite != null)
                    g.drawImage(tile.sprite.image(), 8 * j, 8 * i, null);
                if (Persistence.debugdisplaymode && tile.type == FreezoneTile.TYPE_SOLID) {
                    g.setColor(new Color(150, 20, 20, 100));
                    g.fillRect(8 * j, 8 * i, 8, 8);
                }
            }

        // Draw the entities & player in X & Y position order.
        ArrayList<AbstractRenderer> entities = map.entityRenderers.listRenderers();
        if (this instanceof CutsceneState)
            // System.out.println(map.cutsceneEntityRenderers.listRenderers());
            entities.addAll(map.cutsceneEntityRenderers.listRenderers());
        else
            entities.add(Persistence.currentplayer.renderer());
        entities.sort(Comparator.naturalOrder());
        for (AbstractRenderer entity : entities)
            entity.render(g, width, height);

        if (Persistence.debugdisplaymode) {
            g.setColor(new Color(20, 20, 200, 160));
            DoubleRectangle dbrct = Persistence.currentplayer.getHitboxAt(Persistence.currentplayer.x,
                    Persistence.currentplayer.y);
            g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));

            g.setColor(new Color(150, 20, 130, 120));
            dbrct = Persistence.currentplayer.getInteractionBox();
            g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));

            // draws the warpzones and camera position if debugmode
            for (int i = 0; i < map.triggerzones.size(); i++) {
                g.setColor(new Color(255, 255, 255, 130));
                dbrct = map.triggerzones.get(i).getHitbox();
                g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));
            }
            g.setColor(new Color(240, 55, 54, 150));
            g.fillRect((int) (Persistence.freezoneCamera.getX() * 8), (int) (Persistence.freezoneCamera.getY() * 8), 4,
                    4);
        }

        g.translate(-translateX, -translateY);
    }

    @Override
    public void update() {
        // Updates the freezoneBackground if needeed
        AbstractGraphicsLayer background = Persistence.currentmap.getBackground();
        if (background != null)
            background.update();
        // CREATES AND UPDATES THE MAP
        if (Persistence.currentmap == null)
            Persistence.currentmap = Freezones.loadMap(FreezoneInfo.BASE);
        else
            Persistence.currentmap.update();
        // UPDATES THE CAMERA
        if (Persistence.freezoneCamera != null)
            Persistence.freezoneCamera.update();
        if (!musicset) {
            musicset = true;
            Persistence.soundmanager.setBackgroundMusic(SoundsHolder.getSong(Persistence.currentmap.freezonebgm));
        }

        Persistence.currentmap.entityRenderers.update();

    }

}
