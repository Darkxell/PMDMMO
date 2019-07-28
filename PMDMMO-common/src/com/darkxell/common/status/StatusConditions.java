package com.darkxell.common.status;

import java.util.HashMap;

import com.darkxell.common.move.effects.SolarBeamEffect;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.conditions.BoostCritStatusCondition;
import com.darkxell.common.status.conditions.BoostMoveTypeStatusCondition;
import com.darkxell.common.status.conditions.BoostStatOnHitStatusCondition;
import com.darkxell.common.status.conditions.CancelsUsingMovesOrbsStatusCondition;
import com.darkxell.common.status.conditions.ChangeAttackerStatStatusCondition;
import com.darkxell.common.status.conditions.ChargedMoveStatusCondition;
import com.darkxell.common.status.conditions.ConfusedStatusCondition;
import com.darkxell.common.status.conditions.ConstrictedStatusCondition;
import com.darkxell.common.status.conditions.ForceLastMoveStatusCondition;
import com.darkxell.common.status.conditions.FrozenStatusCondition;
import com.darkxell.common.status.conditions.IdentifiedStatusCondition;
import com.darkxell.common.status.conditions.ImmuneStatusCondition;
import com.darkxell.common.status.conditions.InfatuatedStatusCondition;
import com.darkxell.common.status.conditions.InflictConditionOnFinishStatusCondition;
import com.darkxell.common.status.conditions.IngrainStatusCondition;
import com.darkxell.common.status.conditions.ParalyzedStatusCondition;
import com.darkxell.common.status.conditions.PeriodicDamageStatusCondition;
import com.darkxell.common.status.conditions.PeriodicPercentDamageStatusCondition;
import com.darkxell.common.status.conditions.PetrifiedStatusCondition;
import com.darkxell.common.status.conditions.PreventActionStatusCondition;
import com.darkxell.common.status.conditions.PreventAilmentStatusCondition;
import com.darkxell.common.status.conditions.PreventOtherStatusCondition;
import com.darkxell.common.status.conditions.PreventsMovesStatusCondition;
import com.darkxell.common.status.conditions.RedirectAttacksStatusCondition;
import com.darkxell.common.status.conditions.StealsHpStatusCondition;
import com.darkxell.common.status.conditions.StoreDamageToDoubleStatusCondition;
import com.darkxell.common.status.conditions.TauntedStatusCondition;
import com.darkxell.common.status.conditions.TerrifiedStatusCondition;
import com.darkxell.common.status.conditions.WrappedStatusCondition;
import com.darkxell.common.status.conditions.WrappingStatusCondition;

public final class StatusConditions {
    static final HashMap<Integer, StatusCondition> _registry = new HashMap<>();

    // TODO: when Dive, check Whirlpool.

    public static final StatusCondition Poisoned = new PeriodicDamageStatusCondition(0, true, -1, -1, 10, 4);
    public static final StatusCondition Badly_poisoned = new PeriodicDamageStatusCondition(1, true, -1, -1, 2, 6);
    public static final StatusCondition Burn = new PeriodicDamageStatusCondition(2, true, -1, -1, 20, 5);
    public static final StatusCondition Asleep = new PreventActionStatusCondition(3, true, 3, 6);
    public static final StatusCondition Confused = new ConfusedStatusCondition(4, true, 7, 12);
    public static final StatusCondition Paralyzed = new ParalyzedStatusCondition(5, true, 3, 3);
    public static final StatusCondition Frozen = new FrozenStatusCondition(6, true, 4, 5);

    public static final StatusCondition Leech_seed = new StealsHpStatusCondition(10, true, 11, 12, 2, 10);
    public static final StatusCondition Ingrained = new IngrainStatusCondition(11, false, 3, 3, 15, 1);
    public static final StatusCondition Yawning = new InflictConditionOnFinishStatusCondition(12, true, 3, 3, Asleep);
    public static final StatusCondition Cursed = new PeriodicPercentDamageStatusCondition(13, true, 2, 2, 1, .25);

    public static final StatusCondition Constricted_fire = new ConstrictedStatusCondition(20, true, 4, 6, 2, 5);
    public static final StatusCondition Constricted_water = new ConstrictedStatusCondition(21, true, 4, 6, 2, 5);
    public static final StatusCondition Constricted = new ConstrictedStatusCondition(22, true, 4, 6, 2, 5);
    public static final StatusCondition Paused = new CancelsUsingMovesOrbsStatusCondition(24, true, 1, 1);
    public static final StatusCondition Cringed = new PreventsMovesStatusCondition(25, true, 1, 1);
    public static final StatusCondition Terrified = new TerrifiedStatusCondition(26, true, 9, 11);
    public static final StatusCondition Taunted = new TauntedStatusCondition(27, true, 10, 11);
    public static final StatusCondition Infatuated = new InfatuatedStatusCondition(28, true, 3, 4);
    public static final StatusCondition Petrified = new PetrifiedStatusCondition(29, true, 15, 15);
    public static final StatusCondition Wrapped = new WrappedStatusCondition(30, true, 4, 5, 2, 6);
    public static final StatusCondition Wrapping = new WrappingStatusCondition(31, true, 6, 6);// 6 So that it is always
                                                                                               // Wrapped that finishes
                                                                                               // first

    public static final StatusCondition Protect = new ImmuneStatusCondition(40, false, 2, 3);
    public static final StatusCondition Reflect = new ChangeAttackerStatStatusCondition(41, false, 11, 12, Stat.Attack,
            0, .5);
    public static final StatusCondition Light_screen = new ChangeAttackerStatStatusCondition(42, false, 11, 12,
            Stat.SpecialAttack, 0, .5);
    public static final StatusCondition Mirror_move = new RedirectAttacksStatusCondition(43, false, 2, 3, .5);

    public static final StatusCondition Skull_bash = new ChargedMoveStatusCondition(60, false, 1, 1, -50);
    public static final StatusCondition Solar_beam = new ChargedMoveStatusCondition(61, false, 1, 1,
            SolarBeamEffect.RESULTING_MOVE);
    public static final StatusCondition Bide = new StoreDamageToDoubleStatusCondition(68, false, 4, 4, -2);
    public static final StatusCondition Revenge = new StoreDamageToDoubleStatusCondition(69, false, 4, 4, -222);

    public static final StatusCondition Charging = new BoostMoveTypeStatusCondition(70, false, 1, 1,
            PokemonType.Electric);
    public static final StatusCondition Focus_energy = new BoostCritStatusCondition(71, false, 3, 3, 80);
    public static final StatusCondition Enraged = new BoostStatOnHitStatusCondition(72, false, 5, 10, Stat.Attack);

    public static final StatusCondition Identified = new IdentifiedStatusCondition(90);
    public static final StatusCondition Encore = new ForceLastMoveStatusCondition(91, true, 11, 11);

    public static final StatusCondition Sleepless = new PreventOtherStatusCondition(100, false, 11, 12, Asleep,
            Yawning);
    public static final StatusCondition Safeguard = new PreventAilmentStatusCondition(101, false, 11, 12);

    /** @return The Status Condition with the input ID. */
    public static StatusCondition find(int id) {
        return _registry.get(id);
    }

    private StatusConditions() {
    }

}
