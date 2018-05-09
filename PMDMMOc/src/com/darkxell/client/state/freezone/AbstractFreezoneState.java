package com.darkxell.client.state.freezone;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.FreezoneTile;
import com.darkxell.client.mechanics.freezones.zones.BaseFreezone;
import com.darkxell.client.mechanics.freezones.zones.OfficeFreezone;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.resources.music.SoundsHolder;
import com.darkxell.client.state.AbstractState;
import com.darkxell.common.util.DoubleRectangle;

public class AbstractFreezoneState extends AbstractState
{

	@Override
	public void onKeyPressed(short key)
	{}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		FreezoneMap map = Persistance.currentmap;
		Persistance.freezoneCamera.renderheight = height;
		Persistance.freezoneCamera.renderwidth = width;
		// Draws the sea background if needed.
		if (map instanceof OfficeFreezone) ((OfficeFreezone) map).background.render(g, width, height);
		// Draws the surroundings.
		if (map != null)
		{
			int translateX = (int) (-Persistance.freezoneCamera.x * 8 + (width / 2));
			int translateY = (int) (-Persistance.freezoneCamera.y * 8 + (height / 2));

			g.translate(translateX, translateY);
			// Draws the map
			for (int i = 0; i < map.mapHeight; i++)
			{
				for (int j = 0; j < map.mapWidth; j++)
				{
					int tileid = (i * map.mapWidth) + j;
					g.drawImage(map.tiles[tileid].sprite, 8 * j, 8 * i, null);
					if (Persistance.debugdisplaymode) if (map.tiles[tileid].type == FreezoneTile.TYPE_SOLID)
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
			if (this instanceof CutsceneState) {
				//System.out.println(map.cutsceneEntityRenderers.listRenderers());
				entities.addAll(map.cutsceneEntityRenderers.listRenderers());
			}
			else entities.add(Persistance.currentplayer.renderer());
			entities.sort(Comparator.naturalOrder());
			for (AbstractRenderer entity : entities)
				entity.render(g, width, height);

			if (Persistance.debugdisplaymode)
			{
				g.setColor(new Color(20, 20, 200, 160));
				DoubleRectangle dbrct = Persistance.currentplayer.getHitboxAt(Persistance.currentplayer.x, Persistance.currentplayer.y);
				g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));

				g.setColor(new Color(150, 20, 130, 120));
				dbrct = Persistance.currentplayer.getInteractionBox();
				g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));
			}

			// draws the warpzones and camera position if debugmode
			if (Persistance.debugdisplaymode)
			{
				for (int i = 0; i < map.warpzones.size(); i++)
				{
					g.setColor(new Color(255, 255, 255, 130));
					DoubleRectangle dbrct = map.warpzones.get(i).hitbox;
					g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));
				}
				g.setColor(new Color(240, 55, 54, 150));
				g.fillRect((int) (Persistance.freezoneCamera.x * 8), (int) (Persistance.freezoneCamera.y * 8), 4, 4);
			}

			g.translate(-translateX, -translateY);
		}
	}

	protected boolean musicset = false;

	@Override
	public void update()
	{
		// Updates the freezoneBackground if needeed
		if (Persistance.currentmap instanceof OfficeFreezone) ((OfficeFreezone) Persistance.currentmap).background.update();
		// CREATES AND UPDATES THE MAP
		if (Persistance.currentmap == null) Persistance.currentmap = new BaseFreezone();
		else Persistance.currentmap.update();
		// UPDATES THE CAMERA
		if (Persistance.freezoneCamera != null) Persistance.freezoneCamera.update();
		if (!musicset)
		{
			musicset = true;
			Persistance.soundmanager.setBackgroundMusic(SoundsHolder.getSong(Persistance.currentmap.freezonebgm));
		}

		for (AbstractRenderer renderer : Persistance.currentmap.entityRenderers.listRenderers())
			renderer.update();
		for (AbstractRenderer renderer : Persistance.currentmap.cutsceneEntityRenderers.listRenderers())
			renderer.update();

	}

}
