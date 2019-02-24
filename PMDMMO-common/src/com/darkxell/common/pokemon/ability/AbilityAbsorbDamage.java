package com.darkxell.common.pokemon.ability;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.event.pokemon.TriggeredAbilityEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

public class AbilityAbsorbDamage extends AbilityPreventAdditionalEffectsOnSelf {

    public final PokemonType type;

    public AbilityAbsorbDamage(int id, PokemonType type) {
        super(id);
        this.type = type;
    }

    @Override
    public void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned,
            ArrayList<DungeonEvent> resultingEvents) {
        super.onPreEvent(floor, event, concerned, resultingEvents);

        if (event instanceof DamageDealtEvent) {
            DamageDealtEvent e = (DamageDealtEvent) event;
            if (e.target == concerned && concerned.ability() == this && e.source instanceof MoveUse) {
                MoveUse move = (MoveUse) e.source;
                if (move.move.move().type == this.type) {
                    TriggeredAbilityEvent abilityevent = new TriggeredAbilityEvent(floor, event, concerned);
                    resultingEvents.add(abilityevent);
                    event.consume();
                    resultingEvents.add(new HealthRestoredEvent(floor, abilityevent, concerned, e.damage));
                }
            }
        }

    }

    @Override
    protected boolean shouldPrevent(Floor floor, DungeonEvent event, DungeonPokemon concerned) {
        return super.shouldPrevent(floor, event, concerned)
                && ((MoveUseEvent) event.eventSource).usedMove.move.move().type == this.type;
    }

}
