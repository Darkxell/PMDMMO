package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveEffect;
import com.darkxell.common.move.MoveEffectCalculator;
import com.darkxell.common.move.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;

public class MultipleAttacksEffect extends MoveEffect
{

	public final int attacksMin, attacksMax;

	public MultipleAttacksEffect(int id, int attacksMin, int attacksMax)
	{
		super(id);
		this.attacksMin = attacksMin;
		this.attacksMax = attacksMax;
	}

	@Override
	public void additionalEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor, MoveEffectCalculator calculator, boolean missed,
			MoveEvents effects)
	{
		super.additionalEffects(usedMove, target, flags, floor, calculator, missed, effects);

		int attacksleft = 0;
		for (String flag : flags)
		{
			if (flag.startsWith("attacksleft="))
			{
				attacksleft = Integer.parseInt(flag.substring("attacksleft=".length()));
				break;
			}
		}
		if (this.shouldContinue(attacksleft, usedMove, target, flags, floor, calculator, missed, effects))
		{
			--attacksleft;
			MoveUseEvent e = new MoveUseEvent(floor, usedMove, target);
			e.addFlag("attacksleft=" + attacksleft);
			effects.createEffect(e, usedMove, target, floor, missed, false, null);
		}
	}

	@Override
	public Message description(Move move)
	{
		return new Message(this.descriptionID()).addReplacement("<min>", String.valueOf(this.attacksMin)).addReplacement("<max>",
				String.valueOf(this.attacksMax));
	}

	protected String descriptionID()
	{
		if (this.attacksMin == this.attacksMax) return "move.info.multiple_attacks";
		return "move.info.multiple_attacks_random";
	}

	protected boolean shouldContinue(int attacksleft, MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor, MoveEffectCalculator calculator,
			boolean missed, MoveEvents effects)
	{
		return attacksleft > 0;
	}

	@Override
	protected void useOn(MoveUse move, DungeonPokemon target, Floor floor, ArrayList<DungeonEvent> events)
	{
		super.useOn(move, target, floor, events);
		for (DungeonEvent e : events)
			if (e instanceof MoveUseEvent)
			{
				MoveUseEvent event = (MoveUseEvent) e;
				if (event.usedMove.move.move().effect() == this)
				{
					int attacks = RandomUtil.nextIntInBounds(this.attacksMin, this.attacksMax, floor.random);
					event.addFlag("attacksleft=" + attacks);
				}
			}
	}

}
