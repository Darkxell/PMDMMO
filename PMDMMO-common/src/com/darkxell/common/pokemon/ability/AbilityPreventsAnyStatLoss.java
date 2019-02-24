package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.AppliedStatusCondition;

public class AbilityPreventsAnyStatLoss extends Ability {

    public AbilityPreventsAnyStatLoss(int id) {
        super(id);
    }

    protected boolean isPrevented(Floor floor, StatChangedEvent event, DungeonPokemon concerned,
            ArrayList<DungeonEvent> resultingEvents) {
        if (event.stage >= 0)
            return false;
        if (event.eventSource instanceof MoveUseEvent) {
            MoveUse u = (MoveUse) ((MoveUseEvent) event.eventSource).usedMove;
            if (u.user != concerned)
                return true;
        }
        if (event.eventSource instanceof AppliedStatusCondition) {
            AppliedStatusCondition c = (AppliedStatusCondition) event.eventSource;
            if (c.pokemon != concerned)
                return true;
        }
        return false;
    }

    @Override
    public void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned,
            ArrayList<DungeonEvent> resultingEvents) {
        super.onPreEvent(floor, event, concerned, resultingEvents);
        if (event instanceof StatChangedEvent) {
            StatChangedEvent e = (StatChangedEvent) event;
            if (this.isPrevented(floor, e, concerned, resultingEvents)) {
                event.consume();
                resultingEvents.add(new TriggeredAbilityEvent(floor, event, concerned));
            }
        }
    }

}
