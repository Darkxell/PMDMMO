package com.darkxell.common.status.conditions;

import java.util.ArrayList;
import java.util.List;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.RandomUtil;

public class ConfusedStatusCondition extends StatusCondition {

    public ConfusedStatusCondition(int id, boolean isAilment, int durationMin, int durationMax) {
        super(id, isAilment, durationMin, durationMax);
    }

    @Override
    public void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned,
            ArrayList<DungeonEvent> resultingEvents) {
        super.onPreEvent(floor, event, concerned, resultingEvents);
        if (event instanceof PokemonTravelEvent && ((PokemonTravelEvent) event).pokemon() == concerned
                && ((PokemonTravelEvent) event).pokemon().hasStatusCondition(this) && !event.hasFlag("confused"))
            this.randomize(floor, ((PokemonTravelEvent) event), resultingEvents);
        if (event instanceof MoveSelectionEvent && ((MoveSelectionEvent) event).usedMove().user == concerned
                && ((MoveSelectionEvent) event).usedMove().user.hasStatusCondition(this) && !event.hasFlag("confused"))
            this.randomize(floor, ((MoveSelectionEvent) event), resultingEvents);
    }

    private void randomize(Floor floor, MoveSelectionEvent event, ArrayList<DungeonEvent> resultingEvents) {
        event.consume();
        Direction direction = Direction.randomDirection(floor.random);
        MoveSelectionEvent e = new MoveSelectionEvent(floor, event.usedMove().move, event.usedMove().user, direction);
        e.cloneFlags(event);
        e.addFlag("confused");
        resultingEvents.add(e);
    }

    private void randomize(Floor floor, PokemonTravelEvent event, ArrayList<DungeonEvent> resultingEvents) {
        event.consume();
        List<Direction> candidates = new ArrayList<>();
        for (Direction d : Direction.DIRECTIONS)
            candidates.add(d);
        for (int i = 0; i < candidates.size(); ++i) {
            Direction d = candidates.get(i);
            if (!event.pokemon().tile().adjacentTile(d).canMoveTo(event.pokemon(), d, false)) {
                candidates.remove(i);
                --i;
            }
        }
        Direction direction = RandomUtil.random(candidates, floor.random);
        PokemonTravelEvent e = new PokemonTravelEvent(floor, event.pokemon(), false, direction);
        e.cloneFlags(event);
        e.addFlag("confused");
        resultingEvents.add(e);
    }

}
