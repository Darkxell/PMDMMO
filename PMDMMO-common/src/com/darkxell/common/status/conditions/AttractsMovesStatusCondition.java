package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.ai.AIUtils;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.MoveRange;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;

public class AttractsMovesStatusCondition extends StatusCondition {

    public AttractsMovesStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public void onPreEvent(Floor floor, Event event, DungeonPokemon concerned, ArrayList<Event> resultingEvents) {
        super.onPreEvent(floor, event, concerned, resultingEvents);

        if (event.hasFlag("redirected")) {
            return; // Prevent looping
        }

        if (event instanceof MoveUseEvent) {
            // Remove if room/floor-wide and doesn't target inflicted pokemon
            MoveUseEvent e = (MoveUseEvent) event;
            MoveRange range = e.usedMove.move.move().getRange();
            if (range != MoveRange.Floor && range != MoveRange.Room) {
                return;
            } else if (e.usedMove.user.isAlliedWith(concerned)) {
                return;
            } else if (e.target != concerned) {
                e.consume();
            }

        } else if (event instanceof MoveSelectionEvent) {
            // Change direction to inflicted pokemon if is adjacent
            MoveSelectionEvent e = (MoveSelectionEvent) event;
            Tile userTile = e.usedMove().user.tile(), thisTile = concerned.tile();
            if (userTile.isAdjacentTo(thisTile) && !e.usedMove().user.isAlliedWith(concerned)) {
                e.consume();
                MoveSelectionEvent newEvent = new MoveSelectionEvent(e.floor, e.eventSource, e.usedMove().move,
                        e.usedMove().user, AIUtils.generalDirection(userTile, thisTile), e.actor() != null);
                newEvent.addFlag("redirected");
                resultingEvents.add(newEvent);
            }
        }
    }

}
