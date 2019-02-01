package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Message;

public class CompoundEffect extends MoveEffect
{

	public final MoveEffect[] effects;

	public CompoundEffect(int id, MoveEffect... effects)
	{
		super(id);
		this.effects = effects;
	}

	@Override
	public MoveEffectCalculator buildCalculator(MoveUse usedMove, DungeonPokemon target, Floor floor, String[] flags) {
		MoveEffectCalculator calculator = super.buildCalculator(usedMove, target, floor, flags);
		for (MoveEffect effect : this.effects) {
			MoveEffectCalculator c = effect.buildCalculator(usedMove, target, floor, flags);
			if (c != null) calculator = c;
		}
		return calculator;
	}

	@Override
	public void additionalEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor, MoveEffectCalculator calculator, boolean missed,
			MoveEvents effects)
	{
		super.additionalEffects(usedMove, target, flags, floor, calculator, missed, effects);

		for (MoveEffect e : this.effects)
			e.additionalEffects(usedMove, target, flags, floor, calculator, missed, effects);
	}

	@Override
	public Message description(Move move)
	{
		Message m = super.description(move);
		if (Localization.containsKey("move.info." + this.id)) return m;
		for (MoveEffect e : this.effects)
		{
			Message em = e.descriptionBase(move);
			em.addReplacement("<move>", move.name());
			m.addSuffix(" <br>");
			m.addSuffix(em);
		}
		return m;
	}

}
