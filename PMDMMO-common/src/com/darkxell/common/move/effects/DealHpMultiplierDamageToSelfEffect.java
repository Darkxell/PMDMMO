package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;

public class DealHpMultiplierDamageToSelfEffect extends MoveEffect {

    public final double hpMultiplier;

    public DealHpMultiplierDamageToSelfEffect(double hpMultiplier) {
        this.hpMultiplier = hpMultiplier;
    }

    @Override
    public void additionalEffectsOnUse(MoveSelectionEvent moveEvent, Move move, ArrayList<Event> events) {
        super.additionalEffectsOnUse(moveEvent, move, events);
        int damage = (int) Math.round(moveEvent.usedMove().user.getHp() * this.hpMultiplier);
        events.add(new DamageDealtEvent(moveEvent.floor, moveEvent, moveEvent.usedMove().user, moveEvent.usedMove(),
                DamageType.MOVE, damage));
    }

    @Override
    public void effects(MoveContext moveEvent, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects, boolean createAdditionals) {}

}
