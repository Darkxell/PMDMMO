package com.darkxell.common.event.move;

import static com.darkxell.common.testutils.TestUtils.generateALL;
import static com.darkxell.common.testutils.TestUtils.getFloor;
import static com.darkxell.common.testutils.TestUtils.getLeftPokemon;
import static com.darkxell.common.testutils.TestUtils.getRightPokemon;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.testutils.AssertUtils;
import com.darkxell.common.util.Direction;

public class MoveEventTest {

    @Before
    public void before() {
        generateALL();
    }

    @Test
    public void testMoveDiscoveredEvent() {
        for (int i = 0; i < 2; ++i)
            getRightPokemon().setMove(i, new LearnedMove(1));
        MoveDiscoveredEvent event = new MoveDiscoveredEvent(getFloor(), null, getRightPokemon().originalPokemon,
                Registries.moves().find(4));
        ArrayList<Event> result = event.processServer();

        Assert.assertEquals("Unexpected number of resulting Events.", 1, result.size());
        Assert.assertTrue("Created event is incorrect.",
                AssertUtils.containsObjectOfClass(result, MoveLearnedEvent.class));
        MoveLearnedEvent e = AssertUtils.getObjectOfClass(result, MoveLearnedEvent.class);
        Assert.assertEquals(4, e.move.id);
        Assert.assertEquals(getRightPokemon().originalPokemon, e.pokemon);
    }

    @Test
    public void testMoveDiscoveredEventWithFullMoveset() {
        for (int i = 0; i < 4; ++i)
            getRightPokemon().setMove(i, new LearnedMove(1));
        MoveDiscoveredEvent event = new MoveDiscoveredEvent(getFloor(), null, getRightPokemon().originalPokemon,
                Registries.moves().find(4));
        ArrayList<Event> result = event.processServer();

        Assert.assertEquals("Unexpected resulting Event.", 0, result.size());
    }

    @Test
    public void testMoveEnabledEvent() {
        new MoveEnabledEvent(getFloor(), null, getRightPokemon().move(0), false).processServer();
        Assert.assertFalse("Move wasn't disabled.", getRightPokemon().move(0).isEnabled());
        new MoveEnabledEvent(getFloor(), null, getRightPokemon().move(0), true).processServer();
        Assert.assertTrue("Move wasn't enabled.", getRightPokemon().move(0).isEnabled());
    }

    @Test
    public void testMoveLearnedEvent() {
        for (int i = 0; i < 2; ++i)
            getRightPokemon().setMove(i, new LearnedMove(1));
        MoveLearnedEvent event = new MoveLearnedEvent(getFloor(), null, getRightPokemon().originalPokemon,
                Registries.moves().find(4), 2);
        event.processServer();

        Assert.assertNotNull("Pokemon didn't learn the new move.", getRightPokemon().originalPokemon.move(2));
        Assert.assertEquals("Pokemon didn't learn the correct move.", 4,
                getRightPokemon().originalPokemon.move(2).moveId());
    }

    @Test
    public void testMoveSelectionEvent() {
        new MoveSelectionEvent(getFloor(), null, getLeftPokemon().originalPokemon.move(0), getLeftPokemon(),
                Direction.SOUTHWEST, true).processServer();
        Assert.assertEquals("Pokemon wasn't rotated.", Direction.SOUTHWEST, getLeftPokemon().facing());
        Assert.assertEquals("PP wasn't reduced.", getLeftPokemon().originalPokemon.move(0).move().pp - 1,
                getLeftPokemon().originalPokemon.move(0).pp());
    }

    @Test
    public void testMoveSwitchedEvent() {
        for (int i = 0; i < 2; ++i)
            getRightPokemon().setMove(i, new LearnedMove(i + 1));
        new MoveSwitchedEvent(getFloor(), null, getRightPokemon().originalPokemon, 0, 1).processServer();
        Assert.assertEquals("Move wasn't switched correctly.", 2, getRightPokemon().originalPokemon.move(0).moveId());
        Assert.assertEquals("Move wasn't switched correctly.", 1, getRightPokemon().originalPokemon.move(1).moveId());
    }

    @Test
    public void testMoveUseEvent() {
        new MoveUseEvent(getFloor(), null, new MoveUse(getFloor(), getLeftPokemon().originalPokemon.move(0),
                getLeftPokemon(), Direction.SOUTH, null), getRightPokemon()).processServer();
        // Nothing to check, just verifying there is no exception
    }

}
