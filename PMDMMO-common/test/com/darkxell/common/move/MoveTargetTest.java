package com.darkxell.common.move;

import static com.darkxell.common.testutils.TestUtils.*;
import static com.darkxell.common.testutils.move.MoveTestUtils.assertNoTargets;
import static com.darkxell.common.testutils.move.MoveTestUtils.assertNotTargeted;
import static com.darkxell.common.testutils.move.MoveTestUtils.assertTargeted;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.move.Move.MoveRange;
import com.darkxell.common.move.Move.MoveTarget;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.registry.Registries;

public class MoveTargetTest {

    private DungeonPokemon ally;
    private Pokemon allyPokemon;

    @Before
    public void before() {
        generateDefaultObjects();
        getPlayer().addAlly(this.allyPokemon = Registries.species().find(99).generate(1));

        generateTestFloor();
        this.ally = this.allyPokemon.getDungeonPokemon();
    }

    private ArrayList<Event> runMoveSelectionEvent(Move move) {
        Registries.moves().register(move);
        MoveSelectionEvent event = new MoveSelectionEvent(getFloor(), null, new LearnedMove(move.id), getLeftPokemon());
        return event.processServer();
    }

    @Test
    public void testAll() {
        Move move = new MoveBuilder().withRange(MoveRange.Floor).withTargets(MoveTarget.All).build();
        ArrayList<Event> events = this.runMoveSelectionEvent(move);

        assertTargeted(events, getLeftPokemon());
        assertTargeted(events, getRightPokemon());
        assertTargeted(events, this.ally);
    }

    @Test
    public void testAllies() {
        Move move = new MoveBuilder().withRange(MoveRange.Floor).withTargets(MoveTarget.Allies).build();
        ArrayList<Event> events = this.runMoveSelectionEvent(move);

        assertNotTargeted(events, getLeftPokemon());
        assertNotTargeted(events, getRightPokemon());
        assertTargeted(events, this.ally);
    }

    @Test
    public void testFoes() {
        Move move = new MoveBuilder().withRange(MoveRange.Floor).withTargets(MoveTarget.Foes).build();
        ArrayList<Event> events = this.runMoveSelectionEvent(move);

        assertNotTargeted(events, getLeftPokemon());
        assertTargeted(events, getRightPokemon());
        assertNotTargeted(events, this.ally);
    }

    @Test
    public void testNone() {
        Move move = new MoveBuilder().withRange(MoveRange.Floor).withTargets(MoveTarget.None).build();
        ArrayList<Event> events = this.runMoveSelectionEvent(move);

        assertNoTargets(events);
    }

    @Test
    public void testOthers() {
        Move move = new MoveBuilder().withRange(MoveRange.Floor).withTargets(MoveTarget.Others).build();
        ArrayList<Event> events = this.runMoveSelectionEvent(move);

        assertNotTargeted(events, getLeftPokemon());
        assertTargeted(events, getRightPokemon());
        assertTargeted(events, this.ally);
    }

    @Test
    public void testTeam() {
        Move move = new MoveBuilder().withRange(MoveRange.Floor).withTargets(MoveTarget.Team).build();
        ArrayList<Event> events = this.runMoveSelectionEvent(move);

        assertTargeted(events, getLeftPokemon());
        assertNotTargeted(events, getRightPokemon());
        assertTargeted(events, this.ally);
    }

    @Test
    public void testUser() {
        Move move = new MoveBuilder().withRange(MoveRange.Floor).withTargets(MoveTarget.User).build();
        ArrayList<Event> events = this.runMoveSelectionEvent(move);

        assertTargeted(events, getLeftPokemon());
        assertNotTargeted(events, getRightPokemon());
        assertNotTargeted(events, this.ally);
    }

}
