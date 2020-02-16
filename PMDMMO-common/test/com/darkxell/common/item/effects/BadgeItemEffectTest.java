package com.darkxell.common.item.effects;

import static com.darkxell.common.item.effects.ItemEffectTest.EID;
import static com.darkxell.common.item.effects.ItemEffectTest.runTest;
import static com.darkxell.common.testutils.TestUtils.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.event.pokemon.RecruitAttemptEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.model.item.ItemCategory;
import com.darkxell.common.model.item.ItemModel;
import com.darkxell.common.testutils.AssertUtils;

public class BadgeItemEffectTest {

    private BadgeItemEffect effect;

    @Before
    public void before() {
        generateALL();
        getPlayer().friendAreas.add(getRightPokemon().species().friendArea());
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        this.effect = new BadgeItemEffect(EID);
    }

    @Test
    public void testBadge() {
        ArrayList<Event> events = runTest(this.effect);

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, RecruitAttemptEvent.class));
        RecruitAttemptEvent e = AssertUtils.getObjectOfClass(events, RecruitAttemptEvent.class);
        Assert.assertEquals(getRightPokemon(), e.target);
        Assert.assertEquals(getLeftPokemon(), e.recruiter);
    }

    @Test
    public void testBadgeWhenTargetIsTooFar() {
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).adjacentTile(getLeftPokemon().facing())
                .setPokemon(getRightPokemon());
        ArrayList<Event> events = runTest(this.effect);

        Assert.assertFalse(AssertUtils.containsObjectOfClass(events, RecruitAttemptEvent.class));
    }

    @Test
    public void testBadgeWhenUserIsNotALeader() {
        Item item = new Item(new ItemModel(EID, ItemCategory.FOOD, 0, 0, EID, 0, false, false, null));
        ItemUseEvent event = new ItemUseEvent(getFloor(), null, item, getRightPokemon(), null);
        ArrayList<Event> events = event.processServer();

        Assert.assertFalse(AssertUtils.containsObjectOfClass(events, RecruitAttemptEvent.class));
    }

    @Test
    public void testBadgeWithoutFriendArea() {
        getPlayer().friendAreas.remove(getRightPokemon().species().friendArea());
        ArrayList<Event> events = runTest(this.effect);

        Assert.assertFalse(AssertUtils.containsObjectOfClass(events, RecruitAttemptEvent.class));
    }

}
