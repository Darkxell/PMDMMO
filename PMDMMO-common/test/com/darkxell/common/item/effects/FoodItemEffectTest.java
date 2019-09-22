package com.darkxell.common.item.effects;

import static com.darkxell.common.item.effects.ItemEffectTest.EID;
import static com.darkxell.common.item.effects.ItemEffectTest.runTest;
import static com.darkxell.common.testutils.TestUtils.generateALL;
import static com.darkxell.common.testutils.TestUtils.getRightPokemon;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.stats.BellyChangedEvent;
import com.darkxell.common.event.stats.BellySizeChangedEvent;
import com.darkxell.common.item.ItemEffect;
import com.darkxell.common.testutils.AssertUtils;

public class FoodItemEffectTest {

    @Before
    public void before() {
        generateALL();
    }

    @Test
    public void testFeed() {
        getRightPokemon().increaseBelly(-13);
        ItemEffect effect = new FoodItemEffect(EID, 12, 0, 0);
        ArrayList<Event> events = runTest(effect);

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, BellyChangedEvent.class));
        BellyChangedEvent e = AssertUtils.getObjectOfClass(events, BellyChangedEvent.class);
        Assert.assertEquals(getRightPokemon(), e.pokemon);
        Assert.assertEquals(12, e.quantity, 0);
    }

    @Test
    public void testIncreaseBelly() {
        ItemEffect effect = new FoodItemEffect(EID, 0, 10, 0);
        ArrayList<Event> events = runTest(effect);

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, BellySizeChangedEvent.class));
        BellySizeChangedEvent e = AssertUtils.getObjectOfClass(events, BellySizeChangedEvent.class);
        Assert.assertEquals(getRightPokemon(), e.pokemon);
        Assert.assertEquals(10, e.quantity);
    }

    @Test
    public void testIncreaseBellyIfFull() {
        ItemEffect effect = new FoodItemEffect(EID, 0, 0, 10);
        ArrayList<Event> events = runTest(effect);

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, BellySizeChangedEvent.class));
        BellySizeChangedEvent e = AssertUtils.getObjectOfClass(events, BellySizeChangedEvent.class);
        Assert.assertEquals(getRightPokemon(), e.pokemon);
        Assert.assertEquals(10, e.quantity);
    }

    @Test
    public void testIncreaseBellyIfFullWhenNotFull() {
        getRightPokemon().increaseBelly(-13);
        ItemEffect effect = new FoodItemEffect(EID, 0, 0, 10);
        ArrayList<Event> events = runTest(effect);

        Assert.assertFalse(AssertUtils.containsObjectOfClass(events, BellySizeChangedEvent.class));
    }

}
