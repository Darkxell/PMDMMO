package com.darkxell.common.event.stats;

import static com.darkxell.common.testutils.TestUtils.generateALL;
import static com.darkxell.common.testutils.TestUtils.getFloor;
import static com.darkxell.common.testutils.TestUtils.getLeftPokemon;
import static com.darkxell.common.testutils.TestUtils.getPlayer;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.Event;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.testutils.AssertUtils;

public class StatEventTest {

    @Before
    public void before() {
        generateALL();
    }

    @Test
    public void testAllPPChangedEvent() {
        new PPChangedEvent(getFloor(), null, getLeftPokemon(), -2, 10).processServer();
        for (int i = 0; i < getLeftPokemon().moveCount(); ++i)
            Assert.assertEquals("PP wasn't changed correctly.", getLeftPokemon().move(i).maxPP() - 2,
                    getLeftPokemon().move(i).pp());
    }

    @Test
    public void testBellyChangedEvent() {
        double belly = getLeftPokemon().getBelly();
        new BellyChangedEvent(getFloor(), null, getLeftPokemon(), -10).processServer();
        Assert.assertEquals("Belly wasn't updated correctly.", belly - 10, getLeftPokemon().getBelly(), 0);

        new BellyChangedEvent(getFloor(), null, getLeftPokemon(), 20).processServer();
        Assert.assertEquals("Belly wasn't updated correctly.", getLeftPokemon().getBellySize(),
                getLeftPokemon().getBelly(), 0);
    }

    @Test
    public void testBellySizeChangedEvent() {
        int belly = getLeftPokemon().getBellySize();
        new BellySizeChangedEvent(getFloor(), null, getLeftPokemon(), -10).processServer();
        Assert.assertEquals("Belly size wasn't updated correctly.", belly - 10, getLeftPokemon().getBellySize());
        Assert.assertEquals("Belly wasn't updated correctly.", getLeftPokemon().getBellySize(),
                getLeftPokemon().getBelly(), 1);
    }

    @Test
    public void testExperienceGainedEvent() {
        Pokemon pokemon = getLeftPokemon().originalPokemon;
        new ExperienceGainedEvent(getFloor(), null, pokemon, 2).processServer();
        Assert.assertEquals("Experience wasn't updated correctly.", 2, pokemon.experience());

        ArrayList<Event> result = new ExperienceGainedEvent(getFloor(), null, pokemon,
                getLeftPokemon().species().experienceToNextLevel(pokemon.level())).processServer();
        Assert.assertTrue("Missing LevelupEvent.", AssertUtils.containsObjectOfClass(result, LevelupEvent.class));
    }

    @Test
    public void testExperienceGeneratedEvent() {
        ExperienceGeneratedEvent event = new ExperienceGeneratedEvent(getFloor(), null, getPlayer());
        ArrayList<Event> result = event.processServer();
        Assert.assertEquals("Unexpected generated events.", 0, result.size());
        event.experience += 5;
        result = event.processServer();
        Assert.assertTrue("Missing ExperienceGainedEvent.",
                AssertUtils.containsObjectOfClass(result, ExperienceGainedEvent.class));
    }

    @Test
    public void testLevelupEvent() {
        int level = getLeftPokemon().level();
        new LevelupEvent(getFloor(), null, getLeftPokemon().originalPokemon).processServer();
        Assert.assertEquals("Pokemon didn't level up.", level + 1, getLeftPokemon().level());
    }

    @Test
    public void testPPChangedEvent() {
        new PPChangedEvent(getFloor(), null, getLeftPokemon(), -1, 0).processServer();
        Assert.assertEquals("PP wasn't changed correctly.", getLeftPokemon().move(0).maxPP() - 1,
                getLeftPokemon().move(0).pp());
    }

    @Test
    public void testSpeedChangedEvent() {
        new SpeedChangedEvent(getFloor(), null, getLeftPokemon()).processServer();
        // Nothing more to test, just checking there is no exception.
    }

    @Test
    public void testStatChangedEvent() {
        int atk = getLeftPokemon().stats.getAttack();
        new StatChangedEvent(getFloor(), null, getLeftPokemon(), Stat.Attack, -1).processServer();
        Assert.assertTrue("Stat wasn't changed correctly.", getLeftPokemon().stats.getAttack() < atk);
    }

}
