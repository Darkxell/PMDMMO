package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;

public class MultipleAttacksEffect extends MoveEffect {

    public final int attacksMin, attacksMax;

    public MultipleAttacksEffect(int attacksMin, int attacksMax) {
        this.attacksMin = attacksMin;
        this.attacksMax = attacksMax;
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {

        int attacksleft = 0;
        for (String flag : context.event.flags()) {
            if (flag.startsWith("attacksleft=")) {
                attacksleft = Integer.parseInt(flag.substring("attacksleft=".length()));
                break;
            }
        }
        --attacksleft;
        if (this.shouldContinue(attacksleft, context, calculator, missed, effects)) {
            MoveUseEvent e = new MoveUseEvent(context.floor, context.event, context.event.usedMove, context.target);
            e.addFlag("attacksleft=" + attacksleft);
            effects.add(e);
        }
    }

    @Override
    public Message description() {
        return new Message(this.descriptionID()).addReplacement("<min>", String.valueOf(this.attacksMin))
                .addReplacement("<max>", String.valueOf(this.attacksMax));
    }

    protected String descriptionID() {
        if (this.attacksMin == this.attacksMax)
            return "move.info.multiple_attacks";
        return "move.info.multiple_attacks_random";
    }

    protected boolean shouldContinue(int attacksleft, MoveContext context, MoveEffectCalculator calculator,
            boolean missed, ArrayList<Event> effects) {
        return attacksleft > 0;
    }

    @Override
    public void additionalEffectsOnUse(MoveSelectionEvent context, Move move, ArrayList<Event> events) {
        for (Event e : events)
            if (e instanceof MoveUseEvent) {
                MoveUseEvent event = (MoveUseEvent) e;
                if (event.usedMove.move.move().behavior().hasEffect(this)) {
                    int attacks = RandomUtil.nextIntInBounds(this.attacksMin, this.attacksMax, context.floor.random);
                    event.addFlag("attacksleft=" + attacks);
                }
            }
    }

}
