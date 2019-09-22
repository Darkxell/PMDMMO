package com.darkxell.common.status.conditions;

import static com.darkxell.common.testutils.TestUtils.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveBuilder;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.testutils.move.MoveTestBuilder;

public class StatusConditionTest {

    public static final int SID = 10000;

    @Before
    public void before() {
        generateALL();
    }

    @Test
    public void testBoostCrit() {
        StatusCondition status = new BoostMoveTypeStatusCondition(SID, false, 2, 2, PokemonType.Water);

        Move move = new MoveBuilder().withType(PokemonType.Water).withPower(100).build();
        MoveTestBuilder test = new MoveTestBuilder(getLeftPokemon(), getRightPokemon()).withMove(move);
        test.build();

        int damageWithoutStatus = test.getDamageDealt();
        getLeftPokemon().inflictStatusCondition(new AppliedStatusCondition(status, getLeftPokemon(), null, 2));
        test.build();
        int damageWithStatus = test.getDamageDealt();

        Assert.assertTrue(damageWithoutStatus < damageWithStatus);
    }

    @Test
    public void testBoostCritCorrectType() {
        StatusCondition status = new BoostMoveTypeStatusCondition(SID, false, 2, 2, PokemonType.Water);
        getLeftPokemon().inflictStatusCondition(new AppliedStatusCondition(status, getLeftPokemon(), null, 2));

        Move move = new MoveBuilder().withType(PokemonType.Normal).withPower(100).build();
        MoveTestBuilder test = new MoveTestBuilder(getLeftPokemon(), getRightPokemon()).withMove(move);
        test.build();

        int damageNormal = test.getDamageDealt();
        move = new MoveBuilder().withType(PokemonType.Water).withPower(100).build();
        test = new MoveTestBuilder(getLeftPokemon(), getRightPokemon()).withMove(move);
        test.build();
        int damageWater = test.getDamageDealt();

        Assert.assertTrue(damageNormal < damageWater);
    }

    @Test
    public void testBoostStatOnHit() {
        StatusCondition status = new BoostStatOnHitStatusCondition(SID, false, 2, 2, Stat.SpecialAttack);
        getLeftPokemon().inflictStatusCondition(new AppliedStatusCondition(status, getLeftPokemon(), null, 2));

        int atkBeforeDamage = getLeftPokemon().stats.getSpecialAttack();
        DamageDealtEvent damage = new DamageDealtEvent(getFloor(), null, getLeftPokemon(), null, DamageType.MOVE, 2);
        getEventProcessor().processEvent(damage);
        int atkAfterDamage = getLeftPokemon().stats.getSpecialAttack();

        Assert.assertTrue(atkBeforeDamage < atkAfterDamage);
    }

    @Test
    public void testCancelUsingMovesOrbs() {
        /*
         * StatusCondition status = new CancelsUsingMovesOrbsStatusCondition(SID, true, 2, 2);
         * getLeftPokemon().inflictStatusCondition(new AppliedStatusCondition(status, getLeftPokemon(), null, 2));
         * MoveSelectionEvent move = new MoveSelectionEvent(getFloor(), null, getLeftPokemon().move(0),
         * getLeftPokemon()); getEventProcessor().processEvent(move); Assert.assertTrue(move.isConsumed());
         */
        // TODO when Abilities can be changed.
    }

}
