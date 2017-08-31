package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.FreezoneTile;
import com.darkxell.client.mechanics.freezones.zones.PokemonSquareFreezone;
import com.darkxell.client.persistance.FreezoneMapHolder;
import com.darkxell.client.resources.images.Hud;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.util.DoubleRectangle;

public class FreezoneExploreState extends AbstractState {

	public FreezoneExploreState() {
		FreezoneMap map = FreezoneMapHolder.currentmap;
		if (Keys.isPressed(Keys.KEY_UP))
			map.player.pressKey(Keys.KEY_UP);
		if (Keys.isPressed(Keys.KEY_RIGHT))
			map.player.pressKey(Keys.KEY_RIGHT);
		if (Keys.isPressed(Keys.KEY_DOWN))
			map.player.pressKey(Keys.KEY_DOWN);
		if (Keys.isPressed(Keys.KEY_LEFT))
			map.player.pressKey(Keys.KEY_LEFT);
	}

	/**
	 * Displays the debug information. Careful, this is not optimized and will
	 * have a hight CPU drain. It also makes the game really ugly, it's a debug
	 * mode...
	 */
	static boolean debugdisplaymode = false;

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
					if (debugdisplaymode)
						if (map.tiles[tileid].type == FreezoneTile.TYPE_SOLID) {
							g.setColor(new Color(150, 20, 20, 100));
							g.fillRect(8 * j, 8 * i, 8, 8);
						} else if (map.tiles[tileid].type == FreezoneTile.TYPE_SOLID) {
							g.setColor(new Color(20, 150, 20, 100));
							g.fillRect(8 * j, 8 * i, 8, 8);
						}
				}
			}
			// TODO : draw the entities/player in Y position order.
			// Draws the player
			g.drawImage(map.player.playersprite.getCurrentSprite(),
					(int) (map.player.x * 8 - map.player.playersprite.pointer.gravityX),
					(int) (map.player.y * 8 - map.player.playersprite.pointer.gravityY), null);
			if (debugdisplaymode) {
				g.setColor(new Color(20, 20, 200, 160));
				DoubleRectangle dbrct = map.player.getHitboxAt(map.player.x, map.player.y);
				g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));

				g.setColor(new Color(150, 20, 130, 120));
				dbrct = map.player.getInteractionBox();
				g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));
			}
			// draws the entities
			for (int i = 0; i < map.entities.size(); i++) {
				map.entities.get(i).print(g);
				if (debugdisplaymode) {
					g.setColor(new Color(20, 20, 200, 160));
					DoubleRectangle dbrct = map.entities.get(i).getHitbox(map.entities.get(i).posX,
							map.entities.get(i).posY);
					g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8),
							(int) (dbrct.height * 8));
				}
			}

			g.translate(-translateX, -translateY);

			if (map.player.canInteract()) {
				g.drawImage(Hud.button, width - 70, 5, null);
				g.setColor(Color.BLACK);
				g.drawString("Interact", width - 50, 20);
				g.setColor(Color.WHITE);
				g.drawString("Interact", width - 50, 18);
				// TODO : change the "INTERACT" here to a language dependent
				// string, and change it to match the action.
			}

			if (debugdisplaymode) {
				g.setColor(Color.BLACK);
				g.drawString("UPS: " + Launcher.getUps() + ", FPS: " + Launcher.getFps(), 1, g.getFont().getSize() * 2);
			}
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
