package com.darkxell.common.status.conditions;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource.BaseEventSource;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;

public class DealDamageBackToAttackerStatusCondition extends StatusCondition {

    public final MoveCategory damageCategory;

    public DealDamageBackToAttackerStatusCondition(int id, boolean isAilment, int minDuration, int maxDuration,
            MoveCategory damageCategory) {
        super(id, isAilment, minDuration, maxDuration);
        this.damageCategory = damageCategory;
    }

    @Override
    public void onPostEvent(Floor floor, Event event, DungeonPokemon concerned, ArrayList<Event> resultingEvents) {
        super.onPostEvent(floor, event, concerned, resultingEvents);

        if (event instanceof DamageDealtEvent) {
            DamageDealtEvent e = (DamageDealtEvent) event;
            if (e.source instanceof MoveUse) {
                MoveUse move = (MoveUse) e.source;
                if (e.target.hasStatusCondition(this) && e.target == concerned && move.user != concerned
                        && move.move.move().category == this.damageCategory) {
                    resultingEvents.add(new DamageDealtEvent(floor, BaseEventSource.TRIGGER, move.user, this,
                            DamageType.MOVE, e.damage));
                }
            }
        }
    }

}
