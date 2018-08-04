package com.darkxell.common.move;

import java.util.ArrayList;

import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.move.Move.MoveRange;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;

public class MoveEffect implements AffectsPokemon
{
	public final int id;

	public MoveEffect(int id)
	{
		this.id = id;
		MoveEffects.effects.put(this.id, this);
	}

	/** Removes all Pokemon this move is not supposed to target. */
	protected void filterTargets(Floor floor, Move move, DungeonPokemon user, ArrayList<DungeonPokemon> targets)
	{
		switch (move.targets)
		{
			case All:
				break;

			case Allies:
				targets.removeIf((DungeonPokemon p) -> p == user || !p.isAlliedWith(user));
				break;

			case Foes:
				targets.removeIf((DungeonPokemon p) -> p == user || p.isAlliedWith(user));
				break;

			case Others:
				targets.remove(user);
				break;

			case Team:
				targets.removeIf((DungeonPokemon p) -> !p.isAlliedWith(user));
				break;

			case User:
				targets.removeIf((DungeonPokemon p) -> p != user);
				break;

			case None:
				targets.clear();
				break;
		}
	}

	/** @param move
	 * @param user - The Pokemon using this Move.
	 * @param floor - The Floor context.
	 * @return The Pokemon affected by this Move. */
	public DungeonPokemon[] getTargets(Move move, DungeonPokemon user, Floor floor)
	{
		ArrayList<DungeonPokemon> targets = new ArrayList<DungeonPokemon>();
		Tile t = user.tile(), front = t.adjacentTile(user.facing());

		switch (move.range)
		{
			case Ambient:
				targets.add(null);
				break;

			case Around2:
				for (int x = -2; x < 3; ++x)
					for (int y = -2; y < 3; ++y)
						if (x == -2 || x == 2 || y == -2 || y == 2)
						{
							Tile t2 = floor.tileAt(t.x + x, t.y + y);
							if (t2.getPokemon() != null) targets.add(t2.getPokemon());
						}

			case Around:
				for (Direction d : Direction.directions)
					if (t.adjacentTile(d).getPokemon() != null) targets.add(t.adjacentTile(d).getPokemon());
				break;

			case Floor:
				targets.addAll(floor.listPokemon());
				break;

			case Front_row:
				for (Direction d : new Direction[] { user.facing().rotateCounterClockwise(), user.facing(), user.facing().rotateClockwise() })
					if (t.adjacentTile(d).getPokemon() != null) targets.add(t.adjacentTile(d).getPokemon());
				break;

			case Line:
				int distance = 0;
				boolean done;
				Tile current = t;
				do
				{
					current = current.adjacentTile(user.facing());
					if (current.getPokemon() != null) targets.add(current.getPokemon());
					++distance;
					done = !targets.isEmpty() || distance > 10 || current.type() == TileType.WALL || current.type() == TileType.WALL_END;
				} while (!done);
				break;

			case Room:
				Room r = floor.roomAt(user.tile().x, user.tile().y);
				if (r == null)
				{
					for (Tile tile : AIUtils.visibleTiles(floor, user))
						if (tile.getPokemon() != null) targets.add(tile.getPokemon());
				} else for (Tile t2 : r.listTiles())
					if (t2.getPokemon() != null) targets.add(t2.getPokemon());
				break;

			case Self:
				targets.add(user);
				break;

			case Two_tiles:
				if (front.getPokemon() != null) targets.add(front.getPokemon());
				else if (front.type() != TileType.WALL && front.type() != TileType.WALL_END)
				{
					Tile behind = front.adjacentTile(user.facing());
					if (behind.getPokemon() != null) targets.add(behind.getPokemon());
				}
				break;

			case Front:
			case Front_corners:
			default:
				DungeonPokemon f = user.tile().adjacentTile(user.facing()).getPokemon();
				if (f != null)
				{
					boolean valid = true;
					if (user.facing().isDiagonal() && move.range != MoveRange.Front_corners)
					{
						Tile t1 = user.tile().adjacentTile(user.facing().rotateClockwise());
						if (t1.type() == TileType.WALL || t1.type() == TileType.WALL_END) valid = false;
						t1 = user.tile().adjacentTile(user.facing().rotateCounterClockwise());
						if (t1.type() == TileType.WALL || t1.type() == TileType.WALL_END) valid = false;
					}
					if (valid) targets.add(f);
				}
		}

		this.filterTargets(floor, move, user, targets);
		if (move.range == MoveRange.Room || move.range == MoveRange.Floor)
			targets.sort((DungeonPokemon p1, DungeonPokemon p2) -> floor.dungeon.compare(p1, p2));

		return targets.toArray(new DungeonPokemon[targets.size()]);
	}

	/** Main method called when a Pokemon uses a Move on a target. */
	public void mainUse(MoveUse usedMove, DungeonPokemon target, Floor floor, ArrayList<DungeonEvent> events)
	{
		Move move = usedMove.move.move();
		MoveEffectCalculator calculator = new MoveEffectCalculator(usedMove, target, floor);
		boolean missed = calculator.misses(events);
		double effectiveness = calculator.effectiveness();
		if (effectiveness == PokemonType.NO_EFFECT) events.add(new MessageEvent(floor, move.unaffectedMessage(target)));
		else
		{
			if (!missed && this != MoveEffects.Basic_attack) target.receiveMove(usedMove.move.isLinked() ? DungeonPokemon.LINKED_MOVES : DungeonPokemon.MOVES);
			if (!missed && move.category != MoveCategory.Status)
			{
				if (effectiveness >= PokemonType.SUPER_EFFECTIVE)
					events.add(new MessageEvent(floor, new Message("move.effectiveness.super").addReplacement("<pokemon>", target.getNickname())));
				else if (effectiveness <= PokemonType.NOT_VERY_EFFECTIVE)
					events.add(new MessageEvent(floor, new Message("move.effectiveness.not_very").addReplacement("<pokemon>", target.getNickname())));
			}
			this.useOn(usedMove, target, floor, calculator, missed, events);
		}
	}

	public void prepareUse(MoveUse move, Floor floor, ArrayList<DungeonEvent> events)
	{
		Move m = move.move.move();

		DungeonPokemon[] pokemon = this.getTargets(m, move.user, floor);
		for (int i = 0; i < pokemon.length; ++i)
			events.add(new MoveUseEvent(floor, move, pokemon[i]));

		if (events.size() == 0 && this != MoveEffects.Basic_attack) events.add(new MessageEvent(floor, new Message("move.no_target")));
	}

	protected void useOn(MoveUse usedMove, DungeonPokemon target, Floor floor, MoveEffectCalculator calculator, boolean missed, ArrayList<DungeonEvent> events)
	{
		if (missed) events.add(new MessageEvent(floor, new Message("move.miss").addReplacement("<pokemon>", target.getNickname())));
		else if (usedMove.move.move().category != MoveCategory.Status) events.add(new DamageDealtEvent(floor, target, usedMove, calculator.compute(events)));
	}

}
