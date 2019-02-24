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

public class UserPercentDamageEffect extends MoveEffect {

    public final double percent;

    public UserPercentDamageEffect(int id, double percent) {
        super(id);
        this.percent = percent;
    }

    @Override
    protected void mainEffects(MoveUse usedMove, DungeonPokemon target, String[] flags, Floor floor,
            MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.mainEffects(usedMove, target, flags, floor, calculator, missed, effects);

        effects.createEffect(
                new DamageDealtEvent(floor, eventSource, usedMove.user, usedMove,
                        DamageType.MOVE, (int) (usedMove.user.getMaxHP() * this.percent)),
                usedMove, target, floor, missed, false, usedMove.user);
    }

    @Override
    public Message descriptionBase(Move move) {
        String id = "move.info.damage_user_percent";
        return new Message(id).addReplacement("<percent>", String.valueOf(this.percent * 100));
    }

}
