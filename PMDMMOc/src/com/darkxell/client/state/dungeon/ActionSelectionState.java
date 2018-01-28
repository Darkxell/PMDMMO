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
import com.darkxell.common.util.Direction;

public class ActionSelectionState extends DungeonSubState
{

	public static final int ROTATION_COUNTER_MAX = TILE_SIZE * 2 / 3;

	private int rotationCounter = 0;

	public ActionSelectionState(DungeonState parent)
	{
		super(parent);
	}

	public Direction checkMovement()
	{
		Direction direction = null;
		boolean canRun = !Persistance.player.getDungeonLeader().isFamished();

		if (Keys.directionPressed(canRun, Keys.KEY_UP, Keys.KEY_RIGHT)) direction = Direction.NORTHEAST;
		else if (Keys.directionPressed(canRun, Keys.KEY_DOWN, Keys.KEY_RIGHT)) direction = Direction.SOUTHEAST;
		else if (Keys.directionPressed(canRun, Keys.KEY_DOWN, Keys.KEY_LEFT)) direction = Direction.SOUTHWEST;
		else if (Keys.directionPressed(canRun, Keys.KEY_UP, Keys.KEY_LEFT)) direction = Direction.NORTHWEST;

		else if (!this.parent.diagonal)
		{
			if (Keys.directionPressed(canRun, Keys.KEY_UP)) direction = Direction.NORTH;
			else if (Keys.directionPressed(canRun, Keys.KEY_DOWN)) direction = Direction.SOUTH;
			else if (Keys.directionPressed(canRun, Keys.KEY_LEFT)) direction = Direction.WEST;
			else if (Keys.directionPressed(canRun, Keys.KEY_RIGHT)) direction = Direction.EAST;
		}

		if (direction != null)
		{
			Persistance.player.getDungeonLeader().setFacing(direction);
			if (!this.parent.rotating && Persistance.player.getDungeonLeader().tryMoveTo(direction, true)) return direction;
		}
		return null;
	}

	private void drawArrow(Graphics2D g, int width, int height, Direction direction)
	{
		Point p = direction.move(0, 0);
		BufferedImage img = DungeonHudSpriteset.instance.getArrow(direction);
		double rotation = (ROTATION_COUNTER_MAX + this.rotationCounter) * 1d / ROTATION_COUNTER_MAX * 3 / 4;
		int x = (int) (width / 2 + p.x * TILE_SIZE / 2 * rotation) - TILE_SIZE / 8;
		int y = (int) (height / 2 + p.y * TILE_SIZE / 2 * rotation) - TILE_SIZE / 8;

		g.drawImage(img, x, y, null);
	}

	@Override
	public void onKeyPressed(short key)
	{
		if (key == Keys.KEY_MENU)
		{
			if (Persistance.stateManager instanceof PrincipalMainState)
				((PrincipalMainState) Persistance.stateManager).setState(new DungeonMenuState(this.parent));
		}
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
				this.drawArrow(g, width, height, Direction.NORTHEAST);
				this.drawArrow(g, width, height, Direction.SOUTHEAST);
				this.drawArrow(g, width, height, Direction.SOUTHWEST);
				this.drawArrow(g, width, height, Direction.NORTHWEST);
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
				Direction direction = this.checkMovement();
				if (direction != null) Persistance.eventProcessor.pokemonTravels(Persistance.player.getDungeonLeader(), direction,
						Keys.isPressed(Keys.KEY_RUN) && !Persistance.player.getDungeonLeader().isFamished());
			}
		}

		++this.rotationCounter;
		if (this.rotationCounter > ROTATION_COUNTER_MAX) this.rotationCounter = 0;
	}
}
