package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.move.calculator.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;

public class RandomStatChangeEffect extends MoveEffect {
    public final int probability;
    public final int stage;

    public RandomStatChangeEffect(int stage, int probability) {
        this.stage = stage;
        this.probability = probability;
    }

    @Override
    public Message description() {
        return new Message(this.descriptionID()).addReplacement("<stage>", String.valueOf(Math.abs(this.stage)))
                .addReplacement("<percent>", String.valueOf(this.probability));
    }

    protected String descriptionID() {
        if (this.probability < 100) {
            if (this.stage < 0)
                return "move.info.stat_down_random_maybe";
            return "move.info.stat_up_random_maybe";
        }
        if (this.stage < 0)
            return "move.info.stat_down_random";
        return "move.info.stat_up_random";
    }

    @Override
    public void effects(MoveContext context, MoveEffectCalculator calculator, boolean missed, ArrayList<Event> effects,
            boolean createAdditionals) {

        if (!missed && context.floor.random.nextDouble() * 100 < this.probability
                && createAdditionals == context.move.dealsDamage) {
            DungeonPokemon changed = this.pokemonToChange(context, calculator, missed, effects);
            if (changed != null) {
                effects.add(new StatChangedEvent(context.floor, context.event, changed, this.stat(context.floor),
                        this.stage));
            }
        }

    }

    protected DungeonPokemon pokemonToChange(MoveContext context, MoveEffectCalculator calculator, boolean missed,
            ArrayList<Event> effects) {
        return context.target;
    }

    protected Stat stat(Floor floor) {
        ArrayList<Stat> stats = new ArrayList<>();
        for (Stat s : Stat.values())
            if (s != Stat.Health)
                stats.add(s);
        return RandomUtil.random(stats, floor.random);
    }

}
