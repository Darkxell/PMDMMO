package com.darkxell.client.state.dungeon;

import static com.darkxell.client.resources.images.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.resources.images.DungeonHudSpriteset;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.client.state.menu.dungeon.DungeonMenuState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.GameUtil;

public class ActionSelectionState extends DungeonSubState {

	private int rotationCounter = 0;

	public ActionSelectionState(DungeonState parent) {
		super(parent);
	}

	public short checkMovement() {
		short direction = -1;

		if (Keys.isPressed(Keys.KEY_UP) && Keys.isPressed(Keys.KEY_RIGHT) && !Keys.isPressed(Keys.KEY_DOWN)
				&& !Keys.isPressed(Keys.KEY_LEFT))
			direction = GameUtil.NORTHEAST;
		else if (Keys.isPressed(Keys.KEY_DOWN) && Keys.isPressed(Keys.KEY_RIGHT) && !Keys.isPressed(Keys.KEY_UP)
				&& !Keys.isPressed(Keys.KEY_LEFT))
			direction = GameUtil.SOUTHEAST;
		else if (Keys.isPressed(Keys.KEY_DOWN) && Keys.isPressed(Keys.KEY_LEFT) && !Keys.isPressed(Keys.KEY_UP)
				&& !Keys.isPressed(Keys.KEY_RIGHT))
			direction = GameUtil.SOUTHWEST;
		else if (Keys.isPressed(Keys.KEY_UP) && Keys.isPressed(Keys.KEY_LEFT) && !Keys.isPressed(Keys.KEY_DOWN)
				&& !Keys.isPressed(Keys.KEY_RIGHT))
			direction = GameUtil.NORTHWEST;

		else if (!this.parent.diagonal) {
			if (Keys.isPressed(Keys.KEY_UP) && !Keys.isPressed(Keys.KEY_LEFT) && !Keys.isPressed(Keys.KEY_DOWN)
					&& !Keys.isPressed(Keys.KEY_RIGHT))
				direction = GameUtil.NORTH;
			else if (Keys.isPressed(Keys.KEY_DOWN) && !Keys.isPressed(Keys.KEY_LEFT) && !Keys.isPressed(Keys.KEY_UP)
					&& !Keys.isPressed(Keys.KEY_RIGHT))
				direction = GameUtil.SOUTH;
			else if (Keys.isPressed(Keys.KEY_LEFT) && !Keys.isPressed(Keys.KEY_UP) && !Keys.isPressed(Keys.KEY_DOWN)
					&& !Keys.isPressed(Keys.KEY_RIGHT))
				direction = GameUtil.WEST;
			else if (Keys.isPressed(Keys.KEY_RIGHT) && !Keys.isPressed(Keys.KEY_LEFT) && !Keys.isPressed(Keys.KEY_DOWN)
					&& !Keys.isPressed(Keys.KEY_UP))
				direction = GameUtil.EAST;
		}

		if (direction != -1) {
			Persistance.player.getDungeonPokemon().setFacing(direction);
			if (!this.parent.rotating && Persistance.player.getDungeonPokemon().tryMoveTo(direction))
				return direction;
		}
		return -1;
	}

	private void drawArrow(Graphics2D g, short direction) {
		Point p = GameUtil.moveTo(0, 0, direction);
		BufferedImage img = DungeonHudSpriteset.instance.getArrow(direction);
		int x = this.parent.camera.x + (1 + p.x) * TILE_SIZE / 2 - img.getWidth() / 2 + this.rotationCounter / 3 * p.x;
		int y = this.parent.camera.y + (1 + p.y) * TILE_SIZE / 2 - img.getHeight() / 2 + this.rotationCounter / 3 * p.y;

		if (p.distance(0, 0) > 1) {
			x -= p.x * TILE_SIZE / 8;
			y -= p.y * TILE_SIZE / 8;
		}

		g.drawImage(img, x, y, null);
	}

	@Override
	public void onKeyPressed(short key) {
		if (key == Keys.KEY_MENU)
			Persistance.stateManager.setState(new DungeonMenuState(this.parent));
		if (key == Keys.KEY_ATTACK)
			ClientEventProcessor.processEvent(new MoveSelectionEvent(Persistance.floor,
					new LearnedMove(MoveRegistry.ATTACK.id), Persistance.player.getDungeonPokemon()));
	}

	@Override
	public void onKeyReleased(short key) {
	}

	@Override
	public void render(Graphics2D g, int width, int height) {
		if (this.isMain()) {
			if (this.parent.diagonal) {
				this.drawArrow(g, GameUtil.NORTHEAST);
				this.drawArrow(g, GameUtil.SOUTHEAST);
				this.drawArrow(g, GameUtil.SOUTHWEST);
				this.drawArrow(g, GameUtil.NORTHWEST);
			}
			if (this.parent.rotating)
				if (!this.parent.diagonal)
					this.drawArrow(g, Persistance.player.getDungeonPokemon().facing());
		}
	}

	@Override
	public void update() {
		if (this.isMain()) {
			short direction = this.checkMovement();
			if (direction != -1)
				ClientEventProcessor.actorTravels(direction, Keys.isPressed(Keys.KEY_RUN));
		}

		++this.rotationCounter;
		if (this.rotationCounter > TILE_SIZE * 2 / 3)
			this.rotationCounter = 0;
	}
}
