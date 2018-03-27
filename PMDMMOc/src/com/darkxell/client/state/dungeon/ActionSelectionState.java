package com.darkxell.client.state.dungeon;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.images.others.DungeonHudSpriteset;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.client.state.mainstates.PrincipalMainState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.state.menu.dungeon.DungeonMenuState;
import com.darkxell.client.ui.Keys;
import com.darkxell.common.event.action.PokemonRotateEvent;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;

public class ActionSelectionState extends DungeonSubState
{

	public static final Message moves = new Message("moves.hint");
	public static final int MOVES_WINDOW_HEIGHT = 40;
	public static final int ROTATION_COUNTER_MAX = TILE_SIZE * 2 / 3;

	private short delay = 0;
	private byte hoveredMove = -1;
	private Rectangle[] moveLocations;
	MenuWindow movesWindow;
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
			DungeonPokemon leader = Persistance.player.getDungeonLeader();
			if (direction != leader.facing()) Persistance.eventProcessor.processEvent(new PokemonRotateEvent(Persistance.floor, leader, direction));
			if (!this.parent.rotating && leader.tryMoveTo(direction, true)) return direction;
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
	public void onEnd()
	{
		super.onEnd();
		this.delay = 0;
	}

	@Override
	public void onKeyPressed(short key)
	{
		DungeonPokemon leader = Persistance.player.getDungeonLeader();
		if (key == Keys.KEY_MENU)
		{
			if (Persistance.stateManager instanceof PrincipalMainState)
				((PrincipalMainState) Persistance.stateManager).setState(new DungeonMenuState(this.parent));
		} else if (key == Keys.KEY_ROTATE)
		{
			Direction d = leader.facing();
			do
			{
				d = d.rotateClockwise();
				if (leader.tile().adjacentTile(d).getPokemon() != null && !leader.player().isAlly(leader.tile().adjacentTile(d).getPokemon())) break;
			} while (d != leader.facing());
			if (d != leader.facing()) Persistance.eventProcessor.processEvent(new PokemonRotateEvent(Persistance.floor, leader, d));
		} else if (key == Keys.KEY_MOVE_1 && leader.move(0) != null)
			Persistance.eventProcessor.processEvent(new MoveSelectionEvent(Persistance.floor, leader.move(0), leader));
		else if (key == Keys.KEY_MOVE_2 && leader.move(1) != null)
			Persistance.eventProcessor.processEvent(new MoveSelectionEvent(Persistance.floor, leader.move(1), leader));
		else if (key == Keys.KEY_MOVE_3 && leader.move(2) != null)
			Persistance.eventProcessor.processEvent(new MoveSelectionEvent(Persistance.floor, leader.move(2), leader));
		else if (key == Keys.KEY_MOVE_4 && leader.move(3) != null)
			Persistance.eventProcessor.processEvent(new MoveSelectionEvent(Persistance.floor, leader.move(3), leader));

		if (key == Keys.KEY_ATTACK && (Persistance.player.getDungeonLeader().isFamished() || !Keys.isPressed(Keys.KEY_RUN))) Persistance.eventProcessor
				.processEvent(new MoveSelectionEvent(Persistance.floor, new LearnedMove(MoveRegistry.ATTACK.id), Persistance.player.getDungeonLeader()));
	}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void onMouseClick(int x, int y)
	{
		super.onMouseClick(x, y);
		for (byte i = 0; i < this.moveLocations.length; ++i)
			if (this.moveLocations[i] != null && this.moveLocations[i].contains(x, y))
			{
				Persistance.eventProcessor.processEvent(
						new MoveSelectionEvent(Persistance.floor, Persistance.player.getTeamLeader().move(i), Persistance.player.getDungeonLeader()));
				break;
			}
	}

	@Override
	public void onMouseMove(int x, int y)
	{
		super.onMouseMove(x, y);
		this.hoveredMove = -1;
		for (byte i = 0; i < this.moveLocations.length; ++i)
			if (this.moveLocations[i] != null && this.moveLocations[i].contains(x, y))
			{
				this.hoveredMove = i;
				break;
			}
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		int w = width - 40;
		if (this.movesWindow == null)
			this.movesWindow = new MenuWindow(new Rectangle((width - w) / 2, height - MOVES_WINDOW_HEIGHT - 5, w, MOVES_WINDOW_HEIGHT));

		if (this.isMain())
		{
			if (this.delay >= 3 && this.movesWindow != null && !this.parent.logger.isVisible())
			{
				this.movesWindow.render(g, moves, width, height);

				int y = this.movesWindow.inside().y + 5;
				int mw = (this.movesWindow.inside().width - 10) / 4;
				this.moveLocations = new Rectangle[4];
				Pokemon leader = Persistance.player.getTeamLeader();
				for (int i = 0; i < 4; ++i)
					if (leader.move(i) != null)
					{
						this.moveLocations[i] = new Rectangle(this.movesWindow.inside().x + 5 + i * mw - 1, y - 1,
								TextRenderer.width(leader.move(i).move().name()) + 2, TextRenderer.height() + 2);
						if (i == this.hoveredMove)
						{
							g.setColor(Palette.MENU_HOVERED);
							g.fillRect(this.moveLocations[i].x, this.moveLocations[i].y, this.moveLocations[i].width, this.moveLocations[i].height);
						}
						TextRenderer.render(g, Persistance.player.getDungeonLeader().move(i).move().name(), this.movesWindow.inside().x + 5 + i * mw, y);
					} else this.moveLocations[i] = null;
			}

			if (this.delay >= 3 && this.parent.diagonal)
			{
				this.drawArrow(g, width, height, Direction.NORTHEAST);
				this.drawArrow(g, width, height, Direction.SOUTHEAST);
				this.drawArrow(g, width, height, Direction.SOUTHWEST);
				this.drawArrow(g, width, height, Direction.NORTHWEST);
			}
			if (this.delay >= 3 && this.parent.rotating)
				if (!this.parent.diagonal) this.drawArrow(g, width, height, Persistance.player.getDungeonLeader().facing());
		}
	}

	@Override
	public void update()
	{
		if (this.isMain())
		{
			if (this.delay < 3) ++this.delay;
			if (Keys.isPressed(Keys.KEY_ATTACK) && Keys.isPressed(Keys.KEY_RUN) && !Persistance.player.getDungeonLeader().isFamished())
				Persistance.eventProcessor.processEvent(new TurnSkippedEvent(Persistance.floor, Persistance.player.getDungeonLeader()));
			else
			{
				Direction direction = this.checkMovement();
				if (direction != null)
				{
					DungeonPokemon leader = Persistance.player.getDungeonLeader();
					if (leader.tile().adjacentTile(direction).getPokemon() != null) Persistance.eventProcessor
							.addToPending(new PokemonTravelEvent(Persistance.floor, leader.tile().adjacentTile(direction).getPokemon(), direction.opposite()));
					Persistance.eventProcessor
							.processEvent(new PokemonTravelEvent(Persistance.floor, leader, Keys.isPressed(Keys.KEY_RUN) && !leader.isFamished(), direction));
				}
			}
		}

		++this.rotationCounter;
		if (this.rotationCounter > ROTATION_COUNTER_MAX) this.rotationCounter = 0;
	}
}
