package com.darkxell.common.event.action;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.Event;
import com.darkxell.common.testutils.TestUtils;
import com.darkxell.common.util.Direction;

public class ActionPokemonEventTest {

    @Before
    public void before() {
        TestUtils.generateTestFloor();
    }

    @Test
    public void testRotateEvent() {
        Event e = new PokemonRotateEvent(TestUtils.getFloor(), null, TestUtils.getLeftPokemon(), Direction.SOUTHWEST);
        TestUtils.getEventProcessor().processEvent(e);
        Assert.assertEquals(TestUtils.getLeftPokemon().facing(), Direction.SOUTHWEST);

        e = new PokemonRotateEvent(TestUtils.getFloor(), null, TestUtils.getLeftPokemon(), Direction.WEST);
        TestUtils.getEventProcessor().processEvent(e);
        Assert.assertEquals(TestUtils.getLeftPokemon().facing(), Direction.WEST);
    }

}
