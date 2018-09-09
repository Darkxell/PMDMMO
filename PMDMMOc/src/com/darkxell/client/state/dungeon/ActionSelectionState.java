package com.darkxell.client.state.dungeon;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.Palette;
import com.darkxell.client.resources.images.Sprites.Res_Dungeon;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.client.state.menu.components.MenuWindow;
import com.darkxell.client.state.menu.dungeon.DungeonMenuState;
import com.darkxell.client.state.menu.item.ItemContainersMenuState;
import com.darkxell.client.ui.Keys;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.action.PokemonRotateEvent;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.pokemon.PokemonRescuedEvent;
import com.darkxell.common.mission.DungeonMission;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
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

		if (Keys.directionPressed(canRun, Key.UP, Key.RIGHT)) direction = Direction.NORTHEAST;
		else if (Keys.directionPressed(canRun, Key.DOWN, Key.RIGHT)) direction = Direction.SOUTHEAST;
		else if (Keys.directionPressed(canRun, Key.DOWN, Key.LEFT)) direction = Direction.SOUTHWEST;
		else if (Keys.directionPressed(canRun, Key.UP, Key.LEFT)) direction = Direction.NORTHWEST;

		else if (!this.parent.diagonal)
		{
			if (Keys.directionPressed(canRun, Key.UP)) direction = Direction.NORTH;
			else if (Keys.directionPressed(canRun, Key.DOWN)) direction = Direction.SOUTH;
			else if (Keys.directionPressed(canRun, Key.LEFT)) direction = Direction.WEST;
			else if (Keys.directionPressed(canRun, Key.RIGHT)) direction = Direction.EAST;
		}

		if (direction != null)
		{
			DungeonPokemon leader = Persistance.player.getDungeonLeader();
			if (direction != leader.facing()) Persistance.eventProcessor().processEvent(new PokemonRotateEvent(Persistance.floor, leader, direction).setPAE());
			if (!this.parent.rotating && leader.tryMoveTo(direction, true)) return direction;
		}
		return null;
	}

	private void drawArrow(Graphics2D g, int width, int height, Direction direction)
	{
		Point2D p = direction.move(0, 0);
		BufferedImage img = Res_Dungeon.dungeonHud.getArrow(direction);
		double rotation = (ROTATION_COUNTER_MAX + this.rotationCounter) * 1d / ROTATION_COUNTER_MAX * 3 / 4;

		DungeonPokemon leader = Persistance.player.getDungeonLeader();
		Tile t = leader.tile() == null ? Persistance.floor.tileAt(0, 0) : leader.tile();

		int x = (int) (t.x * TILE_SIZE + TILE_SIZE / 2 + p.getX() * TILE_SIZE / 2 * rotation) - TILE_SIZE / 8;
		int y = (int) (t.y * TILE_SIZE + TILE_SIZE / 2 + p.getY() * TILE_SIZE / 2 * rotation) - TILE_SIZE / 8;

		g.drawImage(img, x, y, null);
	}

	@Override
	public void onEnd()
	{
		super.onEnd();
		this.delay = 0;
	}

	@Override
	public void onKeyPressed(Key key)
	{
		DungeonPokemon leader = Persistance.player.getDungeonLeader();
		DungeonMenuState s = new DungeonMenuState(this.parent);
		if (key == Key.MENU) Persistance.stateManager.setState(s);
		else if (key == Key.INVENTORY)
		{
			ItemContainersMenuState i = s.createInventoryState();
			if (i != null) Persistance.stateManager.setState(i);
		} else if (key == Key.PARTY) Persistance.stateManager.setState(s.createPartyState());
		else if (key == Key.RUN && Key.ROTATE.isPressed()) this.parent.setSubstate(new DungeonFullLoggerState(this.parent));
		else if (key == Key.ROTATE)
		{
			Direction d = leader.facing();
			do
			{
				d = d.rotateClockwise();
				if (leader.tile().adjacentTile(d).getPokemon() != null && !leader.player().isAlly(leader.tile().adjacentTile(d).getPokemon())) break;
			} while (d != leader.facing());
			if (d != leader.facing()) Persistance.eventProcessor().processEvent(new PokemonRotateEvent(Persistance.floor, leader, d).setPAE());
		}

		if (Persistance.player.getDungeonLeader().canAttack(Persistance.floor) && key == Key.MOVE_1 || key == Key.MOVE_2 || key == Key.MOVE_3
				|| key == Key.MOVE_4 || key == Key.ATTACK)
		{
			boolean done = false;
			if (key != Key.ATTACK && Persistance.player.getDungeonLeader().isStruggling())
			{
				Persistance.eventProcessor()
						.processEvent(new MoveSelectionEvent(Persistance.floor, new LearnedMove(MoveRegistry.STRUGGLE.id), leader).setPAE());
				done = true;
			}

			if (!done)
			{
				LearnedMove move = null;
				if (key == Key.MOVE_1 && leader.move(0) != null) move = leader.move(0);
				else if (key == Key.MOVE_2 && leader.move(1) != null) move = leader.move(1);
				else if (key == Key.MOVE_3 && leader.move(2) != null) move = leader.move(2);
				else if (key == Key.MOVE_4 && leader.move(3) != null) move = leader.move(3);

				if (move != null)
				{
					if (move.pp() == 0) this.parent.logger.showMessage(new Message("moves.no_pp").addReplacement("<move>", move.move().name()));
					else if (!leader.canUse(move, Persistance.floor))
						this.parent.logger.showMessage(new Message("moves.cant_use").addReplacement("<move>", move.move().name()));
					else Persistance.eventProcessor().processEvent(new MoveSelectionEvent(Persistance.floor, move, leader).setPAE());
				}

				if (key == Key.ATTACK && (!Key.RUN.isPressed() || Persistance.player.getDungeonLeader().isFamished()))
				{
					DungeonPokemon facing = Persistance.player.getDungeonLeader().tile().adjacentTile(Persistance.player.getDungeonLeader().facing())
							.getPokemon();
					if (facing != null && facing.type == DungeonPokemonType.RESCUEABLE)
					{
						DungeonMission m = Persistance.dungeon.findRescueMission(Persistance.floor, facing);
						if (m != null && m.owner == Persistance.player)
						{
							Persistance.eventProcessor().processEvent(new PokemonRescuedEvent(Persistance.floor, facing, Persistance.player).setPAE());
							return;
						}
					}
					if (Persistance.player.getDungeonLeader().canAttack(Persistance.floor)) Persistance.eventProcessor().processEvent(
							new MoveSelectionEvent(Persistance.floor, new LearnedMove(MoveRegistry.ATTACK.id), Persistance.player.getDungeonLeader()).setPAE());
				}
			}
		}
	}

	@Override
	public void onKeyReleased(Key key)
	{}

	@Override
	public void onMouseClick(int x, int y)
	{
		super.onMouseClick(x, y);
		if (this.moveLocations != null) for (byte i = 0; i < this.moveLocations.length; ++i)
			if (this.moveLocations[i] != null && this.moveLocations[i].contains(x, y))
			{
				Persistance.eventProcessor().processEvent(
						new MoveSelectionEvent(Persistance.floor, Persistance.player.getTeamLeader().move(i), Persistance.player.getDungeonLeader()).setPAE());
				break;
			}
	}

	@Override
	public void onMouseMove(int x, int y)
	{
		super.onMouseMove(x, y);
		this.hoveredMove = -1;
		if (this.moveLocations != null) for (byte i = 0; i < this.moveLocations.length; ++i)
			if (this.moveLocations[i] != null && this.moveLocations[i].contains(x, y))
			{
				this.hoveredMove = i;
				break;
			}
	}

	@Override
	public void prerender(Graphics2D g, int width, int height)
	{
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
						String name = "Unknown Move";
						if (leader.move(i).move() != null) name = leader.move(i).move().name().toString();
						this.moveLocations[i] = new Rectangle(this.movesWindow.inside().x + 5 + i * mw - 1, y - 1, TextRenderer.width(name) + 2,
								TextRenderer.height() + 2);
						if (i == this.hoveredMove)
						{
							g.setColor(Palette.MENU_HOVERED);
							g.fillRect(this.moveLocations[i].x, this.moveLocations[i].y, this.moveLocations[i].width, this.moveLocations[i].height);
						}
						TextRenderer.render(g, name, this.movesWindow.inside().x + 5 + i * mw, y);
					} else this.moveLocations[i] = null;
			}
		}
	}

	@Override
	public void update()
	{
		if (this.isMain())
		{
			if (this.delay < 3) ++this.delay;
			if (Key.ATTACK.isPressed() && Key.RUN.isPressed() && !Persistance.player.getDungeonLeader().isFamished())
				Persistance.eventProcessor().processEvent(new TurnSkippedEvent(Persistance.floor, Persistance.player.getDungeonLeader()).setPAE());
			else
			{
				Direction direction = this.checkMovement();
				if (direction != null && Persistance.player.getDungeonLeader().canMove(Persistance.floor))
				{
					DungeonPokemon leader = Persistance.player.getDungeonLeader();
					Persistance.eventProcessor()
							.processEvent(new PokemonTravelEvent(Persistance.floor, leader, Key.RUN.isPressed() && !leader.isFamished(), direction).setPAE());
				}
			}
		}

		++this.rotationCounter;
		if (this.rotationCounter > ROTATION_COUNTER_MAX) this.rotationCounter = 0;
	}
}
