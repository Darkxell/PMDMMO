package com.darkxell.common.item.effects;

import static com.darkxell.common.testutils.TestUtils.generateALL;
import static com.darkxell.common.testutils.TestUtils.getFloor;
import static com.darkxell.common.testutils.TestUtils.getLeftPokemon;
import static com.darkxell.common.testutils.TestUtils.getRightPokemon;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.event.item.ProjectileThrownEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.event.pokemon.IncreasedIQEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.event.stats.PPChangedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemEffect;
import com.darkxell.common.item.effects.ThrowableItemEffect.ThrowableTrajectory;
import com.darkxell.common.model.item.ItemCategory;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.testutils.AssertUtils;

public class ItemEffectTest {

    public static final int EID = 10000;

    static ArrayList<Event> runTest(ItemEffect effect) {
        return runTest(effect, false);
    }

    static ArrayList<Event> runTest(ItemEffect effect, boolean thrown) {
        Item item = new Item(EID, ItemCategory.FOOD, 0, 0, EID, 0, false, false, null);
        ItemUseEvent event = new ItemUseEvent(getFloor(), null, item, getLeftPokemon(), getRightPokemon(), thrown);
        return event.processServer();
    }

    @Before
    public void before() {
        generateALL();
    }

    @Test
    public void testCureStatus() {
        ItemEffect effect = new CureStatusFoodItemEffect(EID, 0, 0, 0, StatusConditions.Badly_poisoned);
        AppliedStatusCondition status = new AppliedStatusCondition(StatusConditions.Badly_poisoned, getRightPokemon(),
                null, 3);
        getRightPokemon().inflictStatusCondition(status);
        ArrayList<Event> events = runTest(effect);

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, StatusConditionEndedEvent.class));
        StatusConditionEndedEvent e = AssertUtils.getObjectOfClass(events, StatusConditionEndedEvent.class);
        Assert.assertEquals(status, e.condition);
    }

    @Test
    public void testDealDamage() {
        ItemEffect effect = new DealDamageFoodItemEffect(EID, 0, 0, 0, 5, 7);
        getRightPokemon().tile().adjacentTile(getRightPokemon().facing()).setPokemon(getLeftPokemon());
        ArrayList<Event> events = runTest(effect);

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent e = AssertUtils.getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(getLeftPokemon(), e.target);
        Assert.assertEquals(5, e.damage);

        events = runTest(effect, true);

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, DamageDealtEvent.class));
        e = AssertUtils.getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(getRightPokemon(), e.target);
        Assert.assertEquals(7, e.damage);
    }

    @Test
    public void testGummi() {
        ItemEffect effect = new GummiItemEffect(EID, 0, 0, 0, getRightPokemon().species().type1);
        ArrayList<Event> events = runTest(effect);

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, IncreasedIQEvent.class));
        IncreasedIQEvent e = AssertUtils.getObjectOfClass(events, IncreasedIQEvent.class);
        Assert.assertEquals(getRightPokemon(), e.pokemon);
        Assert.assertEquals(7, e.iq);
    }

    @Test
    public void testHeal() {
        getRightPokemon().setHP(getRightPokemon().getMaxHP() - 12);
        ItemEffect effect = new HealFoodItemEffect(EID, 0, 0, 0, 12, 2);
        ArrayList<Event> events = runTest(effect);

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, HealthRestoredEvent.class));
        HealthRestoredEvent e = AssertUtils.getObjectOfClass(events, HealthRestoredEvent.class);
        Assert.assertEquals(getRightPokemon(), e.target);
        Assert.assertEquals(12, e.health);
    }

    @Test
    public void testInflictStatus() {
        ItemEffect effect = new InflictStatusFoodItemEffect(EID, 0, 0, 0, StatusConditions.Poisoned);
        ArrayList<Event> events = runTest(effect);

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, StatusConditionCreatedEvent.class));
        StatusConditionCreatedEvent e = AssertUtils.getObjectOfClass(events, StatusConditionCreatedEvent.class);
        Assert.assertEquals(getRightPokemon(), e.condition.pokemon);
        Assert.assertEquals(StatusConditions.Poisoned, e.condition.condition);
    }

    @Test
    public void testOrb() {
        ItemEffect effect = new OrbItemEffect(EID, 1);
        ArrayList<Event> events = runTest(effect);

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, MoveSelectionEvent.class));
        MoveSelectionEvent e = AssertUtils.getObjectOfClass(events, MoveSelectionEvent.class);
        Assert.assertEquals(getLeftPokemon(), e.usedMove().user);
        Assert.assertEquals(1, e.usedMove().move.moveId());
    }

    @Test
    public void testRestorePP() {
        ItemEffect effect = new RestorePPItemEffect(EID, 0, 0, 0, 2);
        ArrayList<Event> events = runTest(effect);

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, PPChangedEvent.class));
        PPChangedEvent e = AssertUtils.getObjectOfClass(events, PPChangedEvent.class);
        Assert.assertEquals(PPChangedEvent.CHANGE_ALL_MOVES, e.moveIndex);
        Assert.assertEquals(2, e.pp);
    }

    @Test
    public void testThrowable() {
        ItemEffect effect = new ThrowableItemEffect(EID, 2, ThrowableTrajectory.Arc);
        ArrayList<Event> events = runTest(effect);

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, ProjectileThrownEvent.class));
        ProjectileThrownEvent e = AssertUtils.getObjectOfClass(events, ProjectileThrownEvent.class);
        Assert.assertEquals(e.thrower, getLeftPokemon());
    }

}
