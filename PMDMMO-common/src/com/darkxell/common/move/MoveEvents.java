package com.darkxell.common.move;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.ability.AbilityModifyMoveEffect;

/** Handles Event creation from Move Effects. */
public class MoveEvents {

    public final ArrayList<DungeonEvent> events = new ArrayList<>();

    /** Creates a Move Effect.
     *
     * @param effect - The created Effect.
     * @param moveEvent - The Move Use context.
     * @param missed - <code>true</code> if the Move missed.
     * @param isAdditional - <code>true</code> if this Effect is an Additional Effect.
     * @param directedAt - The Pokemon this Effect is targeting (can be different than the Move's target). May be <code>null</code> if no actual target. This may be important in the triggering of certain abilities. */
    public void createEffect(DungeonEvent effect, MoveUseEvent moveEvent, boolean missed, boolean isAdditional,
            DungeonPokemon directedAt) {
        if (moveEvent.usedMove.user.ability() instanceof AbilityModifyMoveEffect)
            effect = ((AbilityModifyMoveEffect) moveEvent.usedMove.user.ability()).modify(effect, moveEvent, missed,
                    isAdditional, true, directedAt);

        if (effect != null && moveEvent.target != null && moveEvent.target.ability() instanceof AbilityModifyMoveEffect)
            effect = ((AbilityModifyMoveEffect) moveEvent.target.ability()).modify(effect, moveEvent, missed,
                    isAdditional, false, directedAt);

        if (effect != null) this.events.add(effect);
    }

}
