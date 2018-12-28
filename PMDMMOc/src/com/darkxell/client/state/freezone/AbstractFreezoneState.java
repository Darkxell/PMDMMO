package com.darkxell.client.state.freezone;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.FreezoneTile;
import com.darkxell.client.mechanics.freezones.zones.BaseFreezone;
import com.darkxell.client.mechanics.freezones.zones.DreamFreezone;
import com.darkxell.client.mechanics.freezones.zones.OfficeFreezone;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.util.DoubleRectangle;

public class AbstractFreezoneState extends AbstractState
{

	public boolean musicset = false;

	@Override
	public void onKeyPressed(Key key)
	{}

	@Override
	public void onKeyReleased(Key key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		FreezoneMap map = Persistence.currentmap;
		Persistence.freezoneCamera.renderHeight = height;
		Persistence.freezoneCamera.renderWidth = width;
		// Draws the sea background if needed.
		if (map instanceof OfficeFreezone) ((OfficeFreezone) map).background.render(g, width, height);
		if (map instanceof DreamFreezone) ((DreamFreezone) map).background.render(g, width, height);
		// Draws the surroundings.
		if (map != null)
		{
			int translateX = (int) (-Persistence.freezoneCamera.finalX() * 8 + (width / 2));
			int translateY = (int) (-Persistence.freezoneCamera.finalY() * 8 + (height / 2));

			g.translate(translateX, translateY);
			// Draws the map
			for (int i = 0; i < map.mapHeight; i++)
			{
				for (int j = 0; j < map.mapWidth; j++)
				{
					int tileid = (i * map.mapWidth) + j;
					g.drawImage(map.tiles[tileid].sprite.image(), 8 * j, 8 * i, null);
					if (Persistence.debugdisplaymode) if (map.tiles[tileid].type == FreezoneTile.TYPE_SOLID)
					{
						g.setColor(new Color(150, 20, 20, 100));
						g.fillRect(8 * j, 8 * i, 8, 8);
					} else if (map.tiles[tileid].type == FreezoneTile.TYPE_SOLID)
					{
						g.setColor(new Color(20, 150, 20, 100));
						g.fillRect(8 * j, 8 * i, 8, 8);
					}
				}
			}

			// Draw the entities & player in X & Y position order.
			ArrayList<AbstractRenderer> entities = map.entityRenderers.listRenderers();
			if (this instanceof CutsceneState)
			{
				// System.out.println(map.cutsceneEntityRenderers.listRenderers());
				entities.addAll(map.cutsceneEntityRenderers.listRenderers());
			} else entities.add(Persistence.currentplayer.renderer());
			entities.sort(Comparator.naturalOrder());
			for (AbstractRenderer entity : entities)
				entity.render(g, width, height);

			if (Persistence.debugdisplaymode)
			{
				g.setColor(new Color(20, 20, 200, 160));
				DoubleRectangle dbrct = Persistence.currentplayer.getHitboxAt(Persistence.currentplayer.x, Persistence.currentplayer.y);
				g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));

				g.setColor(new Color(150, 20, 130, 120));
				dbrct = Persistence.currentplayer.getInteractionBox();
				g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));

				// draws the warpzones and camera position if debugmode
				for (int i = 0; i < map.triggerzones.size(); i++)
				{
					g.setColor(new Color(255, 255, 255, 130));
					dbrct = map.triggerzones.get(i).hitbox;
					g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));
				}
				g.setColor(new Color(240, 55, 54, 150));
				g.fillRect((int) (Persistence.freezoneCamera.x * 8), (int) (Persistence.freezoneCamera.y * 8), 4, 4);
			}

			g.translate(-translateX, -translateY);
		}
	}

	@Override
	public void update()
	{
		// Updates the freezoneBackground if needeed
		if (Persistence.currentmap instanceof OfficeFreezone) ((OfficeFreezone) Persistence.currentmap).background.update();
		if (Persistence.currentmap instanceof DreamFreezone) ((DreamFreezone) Persistence.currentmap).background.update();
		// CREATES AND UPDATES THE MAP
		if (Persistence.currentmap == null) Persistence.currentmap = new BaseFreezone();
		else Persistence.currentmap.update();
		// UPDATES THE CAMERA
		if (Persistence.freezoneCamera != null) Persistence.freezoneCamera.update();
		if (!musicset)
		{
			musicset = true;
			Persistence.soundmanager.setBackgroundMusic(SoundsHolder.getSong(Persistence.currentmap.freezonebgm));
		}

		Persistence.currentmap.entityRenderers.update();

	}

}
