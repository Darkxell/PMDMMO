package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.FreezoneTile;
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
			int translateX = 0;// (int) (-ViewCamera.cameraX *16 +
								// GameCanvas.ScreenWidth / 2);
			int translateY = 0;// (int) (-ViewCamera.cameraY *16 +
								// GameCanvas.ScreenHeight / 2);

			g.translate(translateX, translateY);
			// Draws the map
			for (int i = 0; i < map.mapHeight/2; i++) {
				for (int j = 0; j < map.mapWidth/2; j++) {
					int tileid = (i * map.mapWidth) + j;
					g.drawImage(map.tiles[tileid].sprite, translateX + (8 * j), translateY + (8 * i), null);
					if (map.tiles[tileid].type == FreezoneTile.TYPE_WALKABLE)
						g.setColor(new Color(0, 200, 0, 20));
					else
						g.setColor(new Color(200, 0, 0, 25));
					g.fillRect(translateX + (8 * j), translateY + (8 * i), 8, 8);
				}
			}
			// Draws the player
			g.drawImage(map.player.playersprite.getCurrentSprite(),
					(int) (map.player.x * 8 - map.player.playersprite.pointer.gravityX),
					(int) (map.player.y * 8 - map.player.playersprite.pointer.gravityY), null);
			// TODO: draws the entities and tileentities
			g.translate(-translateX, -translateY);

			g.setColor(Color.BLACK);
			g.drawString("UPS: " + Launcher.updater.currentUPS() + ", FPS: " + Launcher.renderer.currentFPS(), 1,
					g.getFont().getSize() * 2);
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
