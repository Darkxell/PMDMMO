package com.darkxell.common.testutils.move;

import java.util.ArrayList;

import org.junit.Assert;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.testutils.AssertUtils;

public class MoveTestUtils {

    public static void assertDealtDamage(ArrayList<Event> events, DungeonPokemon pokemon) {
        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent event = AssertUtils.getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(pokemon, event.target);
    }

    public static void assertNoDamage(ArrayList<Event> events) {
        Assert.assertFalse(AssertUtils.containsObjectOfClass(events, DamageDealtEvent.class));
    }

}
