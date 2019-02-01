package com.darkxell.common.event;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.turns.GameTurn;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;

public class DungeonUtils {

	/** @return The last move used by the input Pokemon on this Floor, or null if it hasn't used a move yet. */
	public static LearnedMove findLastMove(Floor floor, DungeonPokemon pokemon) {
		ArrayList<GameTurn> turns = floor.dungeon.listTurns();
		turns.removeIf(turn -> turn.floor.id != floor.id);
		for (int i = turns.size() - 1; i >= 0; --i) {
			GameTurn turn = turns.get(i);
			DungeonEvent[] events = turn.events();
			for (int e = events.length - 1; e >= 0; --e) {
				DungeonEvent event = events[e];
				if (event instanceof MoveSelectionEvent) {
					MoveSelectionEvent moveEvent = (MoveSelectionEvent) event;
					if (moveEvent.usedMove().user == pokemon) {
						LearnedMove move = moveEvent.usedMove().move;
						for (int m = 0; m < pokemon.moveCount(); ++m)
							if (pokemon.move(m).moveId() == move.moveId()) return move;
					}
				}
			}
		}

		return null;
	}

}
