package com.darkxell.common.status.conditions;

import static com.darkxell.common.testutils.TestUtils.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveBuilder;
import com.darkxell.common.move.behavior.MoveBehaviors;
import com.darkxell.common.move.effects.MoveEffectsTest;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.testutils.move.MoveTestBuilder;
import com.darkxell.common.util.Direction;

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
    public void testPreventStatLoss() {
        int atk = getRightPokemon().stats.getAttack();

        Move move = new MoveBuilder().withID(MoveEffectsTest.EID).withBehavior(MoveBehaviors.Lower_attack).build();
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        Registries.moves().register(move);
        getEventProcessor().processEvent(
                new MoveSelectionEvent(getFloor(), null, new LearnedMove(move.getID()), getLeftPokemon()));

        Assert.assertTrue(atk > getRightPokemon().stats.getAttack());
        atk = getRightPokemon().stats.getAttack();

        StatusCondition status = new PreventStatLossStatusCondition(SID, false, 10, 10);
        getRightPokemon().inflictStatusCondition(status.create(getFloor(), getRightPokemon(), null));

        getEventProcessor().processEvent(
                new MoveSelectionEvent(getFloor(), null, new LearnedMove(move.getID()), getLeftPokemon()));

        Assert.assertEquals(atk, getRightPokemon().stats.getAttack());
    }

    @Test
    public void testRedirectMoves() {
        getRightPokemon().setFacing(Direction.NORTHEAST);
        getEventProcessor().processEvent(new MoveSelectionEvent(getFloor(), null, getRightPokemon().move(0),
                getRightPokemon(), Direction.NORTHEAST, true));
        Assert.assertEquals(Direction.NORTHEAST, getRightPokemon().facing());

        StatusCondition status = new AttractsMovesStatusCondition(SID, false, 1, 1);
        getLeftPokemon().inflictStatusCondition(new AppliedStatusCondition(status, getLeftPokemon(), null, 1));
        getRightPokemon().setFacing(Direction.NORTHEAST);
        getEventProcessor().processEvent(new MoveSelectionEvent(getFloor(), null, getRightPokemon().move(0),
                getRightPokemon(), Direction.NORTHEAST, true));
        Assert.assertEquals(Direction.WEST, getRightPokemon().facing());
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
