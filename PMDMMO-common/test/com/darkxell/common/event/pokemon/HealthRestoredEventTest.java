package com.darkxell.common.event.pokemon;

import static com.darkxell.common.testutils.TestUtils.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.pokemon.HealthRestoredEvent;

public class HealthRestoredEventTest {

    @Before
    public void before() {
        generateALL();
    }

    @Test
    public void testHealthRestoredEvent() {
        getLeftPokemon().setHP(getLeftPokemon().getMaxHP() - 8);
        new HealthRestoredEvent(getFloor(), null, getLeftPokemon(), 5).processServer();
        Assert.assertEquals("Incorrect resulting HP.", getLeftPokemon().getMaxHP() - 3, getLeftPokemon().getHp());
    }

    @Test
    public void testHealthRestoredOutOfBoundsEvent() {
        getLeftPokemon().setHP(getLeftPokemon().getMaxHP() - 3);
        new HealthRestoredEvent(getFloor(), null, getLeftPokemon(), 5).processServer();
        Assert.assertEquals("Incorrect resulting HP.", getLeftPokemon().getMaxHP(), getLeftPokemon().getHp());
    }

}
