package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculators.FixedDamageCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Message;

public class RandomFixedDamageEffect extends MoveEffect {

    private final int[] possibilities;

    public RandomFixedDamageEffect(int... possibilities) {
        this.possibilities = possibilities;
    }

    @Override
    public void additionalEffectsOnUse(MoveSelectionEvent context, Move move, ArrayList<Event> events) {
        int chosen = context.floor.random.nextInt(this.possibilities.length);
        int damage = this.possibilities[chosen];

        events.add(0, new MessageEvent(context.floor, context,
                new Message("move.rfd.chosen." + context.usedMove().move.moveId() + "." + chosen)));

        for (Event event : events) {
            if (event instanceof MoveUseEvent) {
                event.addFlag("rfd=" + damage);
            }
        }
    }

    @Override
    public MoveEffectCalculator buildCalculator(MoveContext context) {

        for (String flag : context.event.flags())
            if (flag.startsWith("rfd=")) {
                return new FixedDamageCalculator(context, Integer.parseInt(flag.substring("rfd=".length())));
            }

        Logger.e("RandomFixedDamage Effect had no rfd flag");
        return super.buildCalculator(context);
    }

    public int[] possibilities() {
        return this.possibilities.clone();
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {
    }

}
