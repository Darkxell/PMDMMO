package com.darkxell.common.pokemon.ability;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.pokemon.DungeonPokemon;

public abstract class AbilityModifyMoveEffect extends Ability {

    public AbilityModifyMoveEffect(int id) {
        super(id);
    }

    /**
     * Called when a Move produces an Effect. This method may modify the Event produced by the Effect and return it.
     *
     * @param  effect       - The Effect created by the Move.
     * @param  moveEvent     - The Move Use context.
     * @param  missed       - <code>true</code> if the Move missed.
     * @param  isAdditional - <code>true</code> if this Effect is an additional Effect (=not the main effect of the
     *                      Move).
     * @param  amIUser      - <code>true</code> if this Ability belongs to the Move's user.
     * @param  directedAt   - The Pokemon this Effect is targeting (can be different than the Move's target). May be
     *                      <code>null</code> if no actual target.
     * @return              The new Effect after modification. May return <code>null<code> to cancel the Effect
     *                      completely.
     */
    public abstract DungeonEvent modify(DungeonEvent effect, MoveUseEvent moveEvent, boolean missed, boolean isAdditional,
            boolean amIUser, DungeonPokemon directedAt);

}
