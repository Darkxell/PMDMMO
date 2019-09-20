package com.darkxell.common.testutils.move;

import java.util.ArrayList;

import org.junit.Assert;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.testutils.AssertUtils;

public class MoveTestUtils {

    public static void assertDealtDamage(ArrayList<Event> events, DungeonPokemon pokemon) {
        boolean targeted = false;
        for (Event e : events)
            if (e instanceof DamageDealtEvent && ((DamageDealtEvent) e).target == pokemon) {
                targeted = true;
                break;
            }
        Assert.assertTrue(targeted);
    }

    public static void assertNoDamage(ArrayList<Event> events) {
        Assert.assertFalse(AssertUtils.containsObjectOfClass(events, DamageDealtEvent.class));
    }

    public static void assertNoTargets(ArrayList<Event> events) {
        Assert.assertFalse(AssertUtils.containsObjectOfClass(events, MoveUseEvent.class));
    }

    public static void assertNotTargeted(ArrayList<Event> events, DungeonPokemon pokemon) {
        boolean targeted = false;
        for (Event e : events)
            if (e instanceof MoveUseEvent && ((MoveUseEvent) e).target == pokemon) {
                targeted = true;
                break;
            }
        Assert.assertFalse(targeted);
    }

    public static void assertTargeted(ArrayList<Event> events, DungeonPokemon pokemon) {
        boolean targeted = false;
        for (Event e : events)
            if (e instanceof MoveUseEvent && ((MoveUseEvent) e).target == pokemon) {
                targeted = true;
                break;
            }
        Assert.assertTrue(targeted);
    }

}
