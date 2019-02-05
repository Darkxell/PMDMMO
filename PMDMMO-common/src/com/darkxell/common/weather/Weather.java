package com.darkxell.common.weather;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.pokemon.AffectsPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.RandomUtil;
import com.darkxell.common.util.language.Message;

public class Weather implements AffectsPokemon, Comparable<Weather> {

    private static final HashMap<Integer, Weather> _weatherRegistry = new HashMap<Integer, Weather>();
    public static final byte random = -1;

    public static final Weather CLEAR = new Weather(0, null);
    public static final Weather CLOUDS = new Weather(6, new Color(200, 200, 200, 50));
    public static final Weather FOG = new Weather(5, new Color(150, 150, 150, 50));
    public static final Weather HAIL = new WeatherDamaging(2, new Color(150, 150, 255, 50), 10, 5, PokemonType.Ice);
    public static final Weather RAIN = new Weather(7, new Color(0, 0, 255, 50));
    public static final Weather SANDSTORM = new WeatherDamaging(4, new Color(255, 255, 0, 50), 10, 5,
            PokemonType.Ground, PokemonType.Rock, PokemonType.Steel);
    public static final Weather SNOW = new Weather(1, new Color(220, 220, 220, 75));
    public static final Weather SUNNY = new Weather(3, new Color(255, 255, 255, 50));

    public static Weather find(int id) {
        if (!_weatherRegistry.containsKey(id))
            return CLEAR;
        return _weatherRegistry.get(id);
    }

    public static Weather random(Random random) {
        ArrayList<Weather> w = new ArrayList<>(_weatherRegistry.values());
        return RandomUtil.random(w, random);
    }

    public final int id;
    /** The Color to fill the screen with as a Weather Layer. May be null if no Layer. */
    public final Color layer;

    public Weather(int id, Color layer) {
        this.id = id;
        this.layer = layer;
        _weatherRegistry.put(this.id, this);
    }

    @Override
    public int compareTo(Weather o) {
        return Integer.compare(this.id, o.id);
    }

    public ActiveWeather create(Floor floor, WeatherSource source, int duration) {
        return new ActiveWeather(this, source, floor, duration);
    }

    public static ArrayList<Weather> list() {
        ArrayList<Weather> list = new ArrayList<>(_weatherRegistry.values());
        list.sort(Comparator.naturalOrder());
        return list;
    }

    public Message name() {
        return new Message("weather." + this.id);
    }

    @Override
    public String toString() {
        return this.name().toString();
    }

    /** Called whenever this Weather ticks. */
    public ArrayList<DungeonEvent> weatherTick(Floor floor, int tick) {
        return new ArrayList<DungeonEvent>();
    }

}
