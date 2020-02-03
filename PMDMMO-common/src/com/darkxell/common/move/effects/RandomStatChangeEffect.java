package com.darkxell.common.move.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.move.effect.MoveEffectCalculator;
import com.darkxell.common.move.effect.MoveEvents;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;

public class RandomStatChangeEffect extends MoveEffect {
    public final int probability;
    public final int stage;

    public RandomStatChangeEffect(int id, int stage, int probability) {
        super(id);
        this.stage = stage;
        this.probability = probability;
    }

    @Override
    public void additionalEffects(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        super.additionalEffects(moveEvent, calculator, missed, effects);

        if (!missed && moveEvent.floor.random.nextDouble() * 100 < this.probability) {
            DungeonPokemon changed = this.pokemonToChange(moveEvent, calculator, missed, effects);
            effects.createEffect(new StatChangedEvent(moveEvent.floor, moveEvent, changed, this.stat(moveEvent.floor), this.stage),
                    moveEvent.usedMove.move.move().dealsDamage);
        }

    }

    @Override
    public Message descriptionBase(Move move) {
        return new Message(this.descriptionID()).addReplacement("<stage>", String.valueOf(Math.abs(this.stage))).addReplacement("<percent>",
                String.valueOf(this.probability));
    }

    protected String descriptionID() {
        if (this.probability < 100) {
            if (this.stage < 0) return "move.info.stat_down_random_maybe";
            return "move.info.stat_up_random_maybe";
        }
        if (this.stage < 0) return "move.info.stat_down_random";
        return "move.info.stat_up_random";
    }

    protected DungeonPokemon pokemonToChange(MoveUseEvent moveEvent, MoveEffectCalculator calculator, boolean missed, MoveEvents effects) {
        return moveEvent.target;
    }

    protected Stat stat(Floor floor) {
        ArrayList<Stat> stats = new ArrayList<>();
        for (Stat s : Stat.values())
            if (s != Stat.Health) stats.add(s);
        return RandomUtil.random(stats, floor.random);
    }

}
