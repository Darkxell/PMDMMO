package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.FreezoneTile;
import com.darkxell.client.mechanics.freezones.zones.PokemonSquareFreezone;
import com.darkxell.client.persistance.FreezoneMapHolder;

public class FreezoneExploreState extends AbstractState {

	@Override
	public void onKeyPressed(short key) {
	}

	@Override
	public void onKeyReleased(short key) {

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

			for (int i = 0; i < map.mapHeight; i++) {
				for (int j = 0; j < map.mapWidth; j++) {
					int tileid = (i * map.mapWidth) + j;
					g.drawImage(map.tiles[tileid].sprite, translateX + (8 * j), translateY + (8 * i), null);
					if (map.tiles[tileid].type == FreezoneTile.TYPE_WALKABLE)
						g.setColor(new Color(0, 200, 0, 40));
					else
						g.setColor(new Color(200, 0, 0, 70));
					g.fillRect(translateX + (8 * j), translateY + (8 * i), 8, 8);
				}
			}

			// TODO : draw the player
			g.translate(-translateX, -translateY);

		}
	}

	@Override
	public void update() {
		if (FreezoneMapHolder.currentmap == null)
			FreezoneMapHolder.currentmap = new PokemonSquareFreezone();
	}

}
