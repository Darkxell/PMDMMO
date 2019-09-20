package com.darkxell.common.move;

import static com.darkxell.common.testutils.TestUtils.generateALL;
import static com.darkxell.common.testutils.TestUtils.getLeftPokemon;
import static com.darkxell.common.testutils.TestUtils.getRightPokemon;
import static com.darkxell.common.testutils.move.MoveTestUtils.assertDealtDamage;
import static com.darkxell.common.testutils.move.MoveTestUtils.assertNoDamage;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.Event;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.testutils.move.MoveTestBuilder;

public class BasicMoveTest {

    @Before
    public void before() {
        generateALL();
    }

    @Test
    public void testBasicAttack() {
        MoveTestBuilder builder = new MoveTestBuilder(getLeftPokemon(), getRightPokemon())
                .withMove(Registries.moves().find(0));
        ArrayList<Event> events = builder.build();

        assertDealtDamage(events, getRightPokemon());
    }

    @Test
    public void testMoveWithNoEffects() {
        MoveTestBuilder builder = new MoveTestBuilder(getLeftPokemon(), getRightPokemon())
                .withMove(new MoveBuilder().withEffectID(1).withoutDamage().build());
        ArrayList<Event> events = builder.build();

        assertNoDamage(events);
    }

}
