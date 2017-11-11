package com.darkxell.client.state.dungeon;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.resources.images.others.DungeonHudSpriteset;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.client.state.menu.dungeon.DungeonMenuState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.event.TurnSkippedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.Directions;

public class ActionSelectionState extends DungeonSubState
{

	private int rotationCounter = 0;

	public ActionSelectionState(DungeonState parent)
	{
		super(parent);
	}

	public short checkMovement()
	{
		short direction = -1;
		boolean canRun = !Persistance.player.getDungeonLeader().isFamished();

		if (Keys.directionPressed(canRun, Keys.KEY_UP, Keys.KEY_RIGHT)) direction = Directions.NORTHEAST;
		else if (Keys.directionPressed(canRun, Keys.KEY_DOWN, Keys.KEY_RIGHT)) direction = Directions.SOUTHEAST;
		else if (Keys.directionPressed(canRun, Keys.KEY_DOWN, Keys.KEY_LEFT)) direction = Directions.SOUTHWEST;
		else if (Keys.directionPressed(canRun, Keys.KEY_UP, Keys.KEY_LEFT)) direction = Directions.NORTHWEST;

		else if (!this.parent.diagonal)
		{
			if (Keys.directionPressed(canRun, Keys.KEY_UP)) direction = Directions.NORTH;
			else if (Keys.directionPressed(canRun, Keys.KEY_DOWN)) direction = Directions.SOUTH;
			else if (Keys.directionPressed(canRun, Keys.KEY_LEFT)) direction = Directions.WEST;
			else if (Keys.directionPressed(canRun, Keys.KEY_RIGHT)) direction = Directions.EAST;
		}

		if (direction != -1)
		{
			Persistance.player.getDungeonLeader().setFacing(direction);
			if (!this.parent.rotating && Persistance.player.getDungeonLeader().tryMoveTo(direction, true)) return direction;
		}
		return -1;
	}

	private void drawArrow(Graphics2D g, short direction)
	{
		Point p = Directions.moveTo(0, 0, direction);
		BufferedImage img = DungeonHudSpriteset.instance.getArrow(direction);
		int x = this.parent.camera.x + (1 + p.x) * TILE_SIZE / 2 - img.getWidth() / 2 + this.rotationCounter / 3 * p.x;
		int y = this.parent.camera.y + (1 + p.y) * TILE_SIZE / 2 - img.getHeight() / 2 + this.rotationCounter / 3 * p.y;

		if (p.distance(0, 0) > 1)
		{
			x -= p.x * TILE_SIZE / 8;
			y -= p.y * TILE_SIZE / 8;
		}

		g.drawImage(img, x, y, null);
	}

	@Override
	public void onKeyPressed(short key)
	{
		if (key == Keys.KEY_MENU) Persistance.stateManager.setState(new DungeonMenuState(this.parent));
		if (key == Keys.KEY_ATTACK && (Persistance.player.getDungeonLeader().isFamished() || !Keys.isPressed(Keys.KEY_RUN))) Persistance.eventProcessor
				.processEvent(new MoveSelectionEvent(Persistance.floor, new LearnedMove(MoveRegistry.ATTACK.id), Persistance.player.getDungeonLeader()));
	}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.isMain())
		{
			if (this.parent.diagonal)
			{
				this.drawArrow(g, Directions.NORTHEAST);
				this.drawArrow(g, Directions.SOUTHEAST);
				this.drawArrow(g, Directions.SOUTHWEST);
				this.drawArrow(g, Directions.NORTHWEST);
			}
			if (this.parent.rotating) if (!this.parent.diagonal) this.drawArrow(g, Persistance.player.getDungeonLeader().facing());
		}
	}

	@Override
	public void update()
	{
		if (this.isMain())
		{
			if (Keys.isPressed(Keys.KEY_ATTACK) && Keys.isPressed(Keys.KEY_RUN) && !Persistance.player.getDungeonLeader().isFamished()) Persistance.eventProcessor
					.processEvent(new TurnSkippedEvent(Persistance.floor, Persistance.player.getDungeonLeader()));
			else
			{
				short direction = this.checkMovement();
				if (direction != -1) Persistance.eventProcessor.actorTravels(Persistance.player.getDungeonLeader(), direction, Keys.isPressed(Keys.KEY_RUN)
						&& !Persistance.player.getDungeonLeader().isFamished());
			}
		}

		++this.rotationCounter;
		if (this.rotationCounter > TILE_SIZE * 2 / 3) this.rotationCounter = 0;
	}
}
