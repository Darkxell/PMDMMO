package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEvents;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;

public class MultipleAttacksEffect extends MoveEffect {

    public final int attacksMin, attacksMax;

    public MultipleAttacksEffect(int id, int attacksMin, int attacksMax) {
        super(id);
        this.attacksMin = attacksMin;
        this.attacksMax = attacksMax;
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed,
            MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        int attacksleft = 0;
        for (String flag : moveEvent.flags()) {
            if (flag.startsWith("attacksleft=")) {
                attacksleft = Integer.parseInt(flag.substring("attacksleft=".length()));
                break;
            }
        }
        --attacksleft;
        if (this.shouldContinue(attacksleft, moveEvent, calculator, missed, effects)) {
            MoveUseEvent e = new MoveUseEvent(moveEvent.floor, moveEvent, moveEvent.usedMove, moveEvent.target);
            e.addFlag("attacksleft=" + attacksleft);
            effects.createEffect(e, false);
        }
    }

    @Override
    public Message description(Move move) {
        return new Message(this.descriptionID()).addReplacement("<min>", String.valueOf(this.attacksMin))
                .addReplacement("<max>", String.valueOf(this.attacksMax));
    }

    protected String descriptionID() {
        if (this.attacksMin == this.attacksMax)
            return "move.info.multiple_attacks";
        return "move.info.multiple_attacks_random";
    }

    protected boolean shouldContinue(int attacksleft, MoveUseEvent moveEvent, MoveEffectCalculator calculator,
            boolean missed, MoveEvents effects) {
        return attacksleft > 0;
    }

    @Override
    protected void useOn(MoveSelectionEvent moveEvent, DungeonPokemon target, ArrayList<Event> events) {
        super.useOn(moveEvent, target, events);
        for (Event e : events)
            if (e instanceof MoveUseEvent) {
                MoveUseEvent event = (MoveUseEvent) e;
                if (event.usedMove.move.move().effect() == this) {
                    int attacks = RandomUtil.nextIntInBounds(this.attacksMin, this.attacksMax, moveEvent.floor.random);
                    event.addFlag("attacksleft=" + attacks);
                }
            }
    }

}
