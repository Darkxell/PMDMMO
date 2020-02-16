package com.darkxell.common.move.effect;

import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.move.effects.ApplyStatusConditionEffect;
import com.darkxell.common.move.effects.StatChangeEffect;
import com.darkxell.common.status.StatusCondition;

/** Utility class with shortcut methods to build move effects. */
public class MoveEffectBuilder {

    public static StatChangeEffect accuracy(int stage) {
        return new StatChangeEffect(Stat.Accuracy, stage, 100);
    }

    public static StatChangeEffect attack(int stage) {
        return new StatChangeEffect(Stat.Attack, stage, 100);
    }

    public static StatChangeEffect defense(int stage) {
        return new StatChangeEffect(Stat.Defense, stage, 100);
    }

    public static StatChangeEffect evasiveness(int stage) {
        return new StatChangeEffect(Stat.Evasiveness, stage, 100);
    }

    public static StatChangeEffect specialAttack(int stage) {
        return new StatChangeEffect(Stat.SpecialAttack, stage, 100);
    }

    public static StatChangeEffect specialDefense(int stage) {
        return new StatChangeEffect(Stat.SpecialDefense, stage, 100);
    }

    public static StatChangeEffect speed(int stage) {
        return new StatChangeEffect(Stat.Speed, stage, 100);
    }

    public static ApplyStatusConditionEffect status(StatusCondition status) {
        return status(status, 100);
    }

    public static ApplyStatusConditionEffect status(StatusCondition status, int probability) {
        return new ApplyStatusConditionEffect(status, probability);
    }

}
