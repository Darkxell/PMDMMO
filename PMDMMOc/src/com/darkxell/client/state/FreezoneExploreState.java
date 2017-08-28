package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.zones.PokemonSquareFreezone;
import com.darkxell.client.persistance.FreezoneMapHolder;

public class FreezoneExploreState extends AbstractState {

	@Override
	public void onKeyPressed(short key) {
		if (FreezoneMapHolder.currentmap != null)
			FreezoneMapHolder.currentmap.player.pressKey(key);
	}

	@Override
	public void onKeyReleased(short key) {
		if (FreezoneMapHolder.currentmap != null)
			FreezoneMapHolder.currentmap.player.releaseKey(key);
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		FreezoneMap map = FreezoneMapHolder.currentmap;
		if (map != null) {
			int translateX = (int) (-map.player.x * 8 + (width / 2));
			int translateY = (int) (-map.player.y * 8 + (height / 2));

			g.translate(translateX, translateY);
			// Draws the map
			for (int i = 0; i < map.mapHeight; i++) {
				for (int j = 0; j < map.mapWidth; j++) {
					int tileid = (i * map.mapWidth) + j;
					g.drawImage(map.tiles[tileid].sprite, 8 * j, 8 * i, null);
				}
			}
			// TODO : draw the entities/player in Y position order.
			// Draws the player
			g.drawImage(map.player.playersprite.getCurrentSprite(),
					(int) (map.player.x * 8 - map.player.playersprite.pointer.gravityX),
					(int) (map.player.y * 8 - map.player.playersprite.pointer.gravityY), null);
			// draws the entities
			for (int i = 0; i < map.entities.size(); i++)
				map.entities.get(i).print(g);

			g.translate(-translateX, -translateY);
			g.setColor(Color.BLACK);
			g.drawString("UPS: " + Launcher.getUps() + ", FPS: " + Launcher.getFps(), 1, g.getFont().getSize() * 2);
		}
	}

	@Override
	public void update() {
		if (FreezoneMapHolder.currentmap == null)
			FreezoneMapHolder.currentmap = new PokemonSquareFreezone();
		else
			FreezoneMapHolder.currentmap.update();
	}

}
