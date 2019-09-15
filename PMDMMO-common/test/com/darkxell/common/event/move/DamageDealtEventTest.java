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
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.event.pokemon.FaintedPokemonEvent;
import com.darkxell.common.testutils.AssertUtils;
import com.darkxell.common.util.Direction;

public class DamageDealtEventTest {

    @Before
    public void before() {
        generateALL();
    }

    @Test
    public void testDamageEvent() {
        DamageDealtEvent event = new DamageDealtEvent(getFloor(), null, getLeftPokemon(),
                new MoveUse(getFloor(), getRightPokemon().move(0), getRightPokemon(), Direction.WEST, null),
                DamageType.MOVE, 5);
        int hp = getLeftPokemon().getHp();
        ArrayList<Event> result = event.processServer();
        Assert.assertEquals("Resulting HP is incorrect.", hp - 5, getLeftPokemon().getHp());
        Assert.assertEquals("Unexpected generated events.", 0, result.size());
    }

    @Test
    public void testLethalDamageEvent() {
        DamageDealtEvent event = new DamageDealtEvent(getFloor(), null, getLeftPokemon(),
                new MoveUse(getFloor(), getRightPokemon().move(0), getRightPokemon(), Direction.WEST, null),
                DamageType.MOVE, 5);
        getLeftPokemon().setHP(2);
        ArrayList<Event> result = event.processServer();
        Assert.assertEquals("Resulting HP is incorrect.", 0, getLeftPokemon().getHp());
        Assert.assertEquals("Unexpected generated events.", 1, result.size());
        Assert.assertTrue("Unexpected generated Events.",
                AssertUtils.containsObjectOfClass(result, FaintedPokemonEvent.class));
        FaintedPokemonEvent e = AssertUtils.getObjectOfClass(result, FaintedPokemonEvent.class);
        Assert.assertEquals("Fainted Pokemon is incorrect.", getLeftPokemon(), e.pokemon);
    }
}
