package com.darkxell.client.state.dungeon;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.resources.images.others.DungeonHudSpriteset;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.dungeon.DungeonMenuState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.event.TurnSkippedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.util.Directions;

public class ActionSelectionState extends DungeonSubState
{

	public static final int ROTATION_COUNTER_MAX = TILE_SIZE * 2 / 3;

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

	private void drawArrow(Graphics2D g, int width, int height, short direction)
	{
		Point p = Directions.moveTo(0, 0, direction);
		BufferedImage img = DungeonHudSpriteset.instance.getArrow(direction);
		double rotation = (ROTATION_COUNTER_MAX + this.rotationCounter) * 1d / ROTATION_COUNTER_MAX * 3 / 4;
		int x = (int) (width / 2 + p.x * TILE_SIZE / 2 * rotation) - TILE_SIZE / 8;
		int y = (int) (height / 2 + p.y * TILE_SIZE / 2 * rotation) - TILE_SIZE / 8;

		g.drawImage(img, x, y, null);
	}

	@Override
	public void onKeyPressed(short key)
	{
		if (key == Keys.KEY_MENU) if (Persistance.stateManager instanceof PrincipalMainState)
			((PrincipalMainState) Persistance.stateManager).setState(new DungeonMenuState(this.parent));
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
				this.drawArrow(g, width, height, Directions.NORTHEAST);
				this.drawArrow(g, width, height, Directions.SOUTHEAST);
				this.drawArrow(g, width, height, Directions.SOUTHWEST);
				this.drawArrow(g, width, height, Directions.NORTHWEST);
			}
			if (this.parent.rotating) if (!this.parent.diagonal) this.drawArrow(g, width, height, Persistance.player.getDungeonLeader().facing());
		}
	}

	@Override
	public void update()
	{
		if (this.isMain())
		{
			if (Keys.isPressed(Keys.KEY_ATTACK) && Keys.isPressed(Keys.KEY_RUN) && !Persistance.player.getDungeonLeader().isFamished())
				Persistance.eventProcessor.processEvent(new TurnSkippedEvent(Persistance.floor, Persistance.player.getDungeonLeader()));
			else
			{
				short direction = this.checkMovement();
				if (direction != -1) Persistance.eventProcessor.actorTravels(Persistance.player.getDungeonLeader(), direction,
						Keys.isPressed(Keys.KEY_RUN) && !Persistance.player.getDungeonLeader().isFamished());
			}
		}

		++this.rotationCounter;
		if (this.rotationCounter > ROTATION_COUNTER_MAX) this.rotationCounter = 0;
	}
}
