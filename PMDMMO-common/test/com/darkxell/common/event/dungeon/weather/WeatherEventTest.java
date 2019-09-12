package com.darkxell.common.event.dungeon.weather;

import static com.darkxell.common.testutils.TestUtils.generateALL;
import static com.darkxell.common.testutils.TestUtils.getFloor;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.testutils.AssertUtils;
import com.darkxell.common.weather.ActiveWeather;
import com.darkxell.common.weather.Weather;
import com.darkxell.common.weather.WeatherDamaging;
import com.darkxell.common.weather.WeatherSource;

public class WeatherEventTest {

    private ActiveWeather weather;

    @Before
    public void before() {
        generateALL();
        this.weather = new ActiveWeather(Weather.HAIL, (WeatherSource) Ability.CLOUD_NINE, getFloor(), 15);
        ;
    }

    @Test
    public void testWeatherCreatedAndEndedEvent() {
        WeatherCreatedEvent create = new WeatherCreatedEvent(this.weather, null);
        ArrayList<Event> result = create.processServer();

        Assert.assertEquals("Unexpected number of resulting events from WeatherCreatedEvent.", 1, result.size());
        Assert.assertTrue("Result didn't contain a WeatherChangedEvent.",
                AssertUtils.containsObjectOfClass(result, WeatherChangedEvent.class));
        Assert.assertTrue("Floor didn't get the Weather.", getFloor().currentWeather() == this.weather);

        WeatherCleanedEvent clean = new WeatherCleanedEvent(this.weather, null);
        result = clean.processServer();

        Assert.assertEquals("Unexpected number of resulting events from WeatherCreatedEvent.", 1, result.size());
        Assert.assertTrue("Result didn't contain a WeatherChangedEvent.",
                AssertUtils.containsObjectOfClass(result, WeatherChangedEvent.class));
        Assert.assertTrue("Floor didn't get the Weather removed.",
                getFloor().currentWeather().weather == Weather.CLEAR);
    }

    @Test
    public void testWeatherDamageEvent() {
        ArrayList<DungeonPokemon> pokemon = getFloor().listPokemon();
        WeatherDamageEvent event = new WeatherDamageEvent(getFloor(), null, (WeatherDamaging) this.weather.weather,
                pokemon, 5);
        ArrayList<Event> result = event.processServer();

        Assert.assertEquals("Unexpected number of resulting events.", pokemon.size(), result.size());
        for (int i = 0; i < result.size(); ++i) {
            Event e = result.get(i);
            Assert.assertTrue("Incorrect event created.", AssertUtils.isOfClass(e, DamageDealtEvent.class));
            Assert.assertEquals("Damage is dealt to the wrong Pokemon.", pokemon.get(i), ((DamageDealtEvent) e).target);
        }
    }

}
