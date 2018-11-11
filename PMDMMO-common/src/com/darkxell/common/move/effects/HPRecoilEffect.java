package com.darkxell.common.move.effects;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class HPRecoilEffect extends MoveEffect
{

	public final double percent;

	public HPRecoilEffect(int id, double percent)
	{
		super(id);
		this.percent = percent;
	}

	@Override
	public void additionalEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor, MoveEffectCalculator calculator, boolean missed,
			MoveEvents effects)
	{
		super.additionalEffects(usedMove, target, flags, floor, calculator, missed, effects);
		if (!missed)
		{
			int damage = usedMove.user.getMaxHP();
			damage *= this.percent / 100;
			effects.createEffect(new DamageDealtEvent(floor, usedMove.user, usedMove, DamageType.RECOIL, damage), usedMove, target, floor, missed, true, usedMove.user);
		}
	}

	@Override
	public Message descriptionBase(Move move)
	{
		return new Message("move.info.recoil_hp").addReplacement("<percent>", String.valueOf(this.percent));
	}

}
