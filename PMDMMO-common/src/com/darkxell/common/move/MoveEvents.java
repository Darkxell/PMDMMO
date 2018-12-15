package com.darkxell.common.move;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.ability.AbilityModifyMoveEffect;

import java.util.ArrayList;

/** Handles Event creation from Move Effects. */
public class MoveEvents
{

	public final ArrayList<DungeonEvent> events = new ArrayList<>();

	/** Creates a Move Effect.
	 * 
	 * @param effect - The created Effect.
	 * @param usedMove - The Move Use context.
	 * @param target - The Pokemon targeted by the Move.
	 * @param floor - The Floor context.
	 * @param missed - <code>true</code> if the Move missed.
	 * @param isAdditional - <code>true</code> if this Effect is an Additional Effect.
	 * @param directedAt - The Pokemon this Effect is targeting (can be different than the Move's target). May be <code>null</code> if no actual target. This may be important in the triggering of certain abilities. */
	public void createEffect(DungeonEvent effect, MoveUse usedMove, DungeonPokemon target, Floor floor, boolean missed, boolean isAdditional,
			DungeonPokemon directedAt)
	{
		if (usedMove.user.ability() instanceof AbilityModifyMoveEffect)
			effect = ((AbilityModifyMoveEffect) usedMove.user.ability()).modify(effect, usedMove, target, floor, missed, isAdditional, true, directedAt);

		if (effect != null && target != null && target.ability() instanceof AbilityModifyMoveEffect)
			effect = ((AbilityModifyMoveEffect) target.ability()).modify(effect, usedMove, target, floor, missed, isAdditional, false, directedAt);

		if (effect != null) this.events.add(effect);
	}

}
