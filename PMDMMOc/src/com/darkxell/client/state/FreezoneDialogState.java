package com.darkxell.client.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.launchable.Launcher;
import com.darkxell.client.mechanics.freezones.FreezoneMap;
import com.darkxell.client.mechanics.freezones.FreezoneTile;
import com.darkxell.client.mechanics.freezones.zones.PokemonSquareFreezone;
import com.darkxell.client.persistance.FreezoneMapHolder;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.Hud;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.util.DoubleRectangle;

public class FreezoneDialogState extends AbstractState {

	private String dialog;
	private int dialogposition = 0;
	private ArrayList<String> showableDialog = null;
	private int appearance = 0;

	public FreezoneDialogState(String dialog) {
		FreezoneMapHolder.currentplayer.forceStop();
		FreezoneMapHolder.currentplayer.setState(PokemonSprite.STATE_IDDLE);
		this.dialog = dialog;
	}

	@Override
	public void onKeyPressed(short key) {
		switch (key) {
		case Keys.KEY_ATTACK:
			dialogposition += 2;
			appearance = 0;
			if (showableDialog != null && dialogposition >= showableDialog.size())
				Launcher.stateManager.setState(new FreezoneExploreState());
			break;
		}
	}

	@Override
	public void onKeyReleased(short key) {
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		FreezoneMap map = FreezoneMapHolder.currentmap;
		if (map != null) {
			int translateX = (int) (-FreezoneMapHolder.currentplayer.x * 8 + (width / 2));
			int translateY = (int) (-FreezoneMapHolder.currentplayer.y * 8 + (height / 2));

			g.translate(translateX, translateY);
			// Draws the map
			for (int i = 0; i < map.mapHeight; i++) {
				for (int j = 0; j < map.mapWidth; j++) {
					int tileid = (i * map.mapWidth) + j;
					g.drawImage(map.tiles[tileid].sprite, 8 * j, 8 * i, null);
					if (FreezoneExploreState.debugdisplaymode)
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
			g.drawImage(FreezoneMapHolder.currentplayer.playersprite.getCurrentSprite(),
					(int) (FreezoneMapHolder.currentplayer.x * 8
							- FreezoneMapHolder.currentplayer.playersprite.pointer.gravityX),
					(int) (FreezoneMapHolder.currentplayer.y * 8
							- FreezoneMapHolder.currentplayer.playersprite.pointer.gravityY),
					null);
			if (FreezoneExploreState.debugdisplaymode) {
				g.setColor(new Color(20, 20, 200, 160));
				DoubleRectangle dbrct = FreezoneMapHolder.currentplayer.getHitboxAt(FreezoneMapHolder.currentplayer.x,
						FreezoneMapHolder.currentplayer.y);
				g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));

				g.setColor(new Color(150, 20, 130, 120));
				dbrct = FreezoneMapHolder.currentplayer.getInteractionBox();
				g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8), (int) (dbrct.height * 8));
			}
			// draws the entities
			for (int i = 0; i < map.entities.size(); i++) {
				map.entities.get(i).print(g);
				if (FreezoneExploreState.debugdisplaymode) {
					g.setColor(new Color(20, 20, 200, 160));
					DoubleRectangle dbrct = map.entities.get(i).getHitbox(map.entities.get(i).posX,
							map.entities.get(i).posY);
					g.fillRect((int) (dbrct.x * 8), (int) (dbrct.y * 8), (int) (dbrct.width * 8),
							(int) (dbrct.height * 8));
				}
			}

			// Translates back
			g.translate(-translateX, -translateY);

			g.drawImage(Hud.button, width - 70, 5, null);
			TextRenderer.instance.render(g, "Next", 10, 10);
			// TODO : change the "NEXT" here to a language dependent
			// string..
			int temp_tw_width = width - 40;
			int temp_tw_height = temp_tw_width * Hud.textwindow.getHeight() / Hud.textwindow.getWidth();
			g.drawImage(Hud.textwindow, 20, height - temp_tw_height - 20, temp_tw_width, temp_tw_height, null);

			if (this.showableDialog == null)
				this.showableDialog = TextRenderer.instance.splitLines(this.dialog, width - 80);
			if (showableDialog.size() > dialogposition) {
				String sss = showableDialog.get(dialogposition);
				TextRenderer.instance.render(g, sss.substring(0, appearance >= sss.length() ? sss.length() - 1 : appearance), 40, height - temp_tw_height - 20
						+ (temp_tw_height / 3));
			}
			if (showableDialog.size() > dialogposition + 1) {
				String sss = showableDialog.get(dialogposition);
				String sst = showableDialog.get(dialogposition + 1);
				TextRenderer.instance.render(
						g,
						sst.substring(0,
								appearance - sss.length() < 0 ? 0 : appearance - sss.length() >= sst.length() ? sst.length() - 1 : appearance - sss.length()),
						40, height - temp_tw_height - 20 + (temp_tw_height / 3 * 2));
			}
			if (FreezoneExploreState.debugdisplaymode) {
				g.setColor(Color.BLACK);
				TextRenderer.instance.render(g, "UPS: " + Launcher.getUps() + ", FPS: " + Launcher.getFps(), 1, 1);
			}
		}
	}

	@Override
	public void update() {
		++appearance;
		if (FreezoneMapHolder.currentmap == null)
			FreezoneMapHolder.currentmap = new PokemonSquareFreezone();
		else
			FreezoneMapHolder.currentmap.update();
	}

}
