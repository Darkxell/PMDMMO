package com.darkxell.common.event.action;

import static com.darkxell.common.testutils.TestUtils.generateALL;
import static com.darkxell.common.testutils.TestUtils.getFloor;
import static com.darkxell.common.testutils.TestUtils.getLeftPokemon;
import static com.darkxell.common.testutils.TestUtils.getRightPokemon;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.Registries;
import com.darkxell.common.dungeon.data.DungeonEncounter.CreatedEncounter;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.stats.BellyChangedEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.testutils.AssertUtils;
import com.darkxell.common.util.Direction;

public class ActionPokemonEventTest {

    @Before
    public void before() {
        generateALL();
    }

    @Test
    public void testRotateEvent() {
        new PokemonRotateEvent(getFloor(), null, getLeftPokemon(), Direction.SOUTH).processServer();
        Direction actual = getLeftPokemon().facing();
        Assert.assertEquals("Pokemon rotates to " + actual + " instead of South.", actual, Direction.SOUTH);

        new PokemonRotateEvent(getFloor(), null, getLeftPokemon(), Direction.WEST).processServer();
        actual = getLeftPokemon().facing();
        Assert.assertEquals("Pokemon rotates to " + actual + " instead of West.", actual, Direction.WEST);
    }

    @Test
    public void testSkipTurnEvent() {
        ArrayList<Event> result = new TurnSkippedEvent(getFloor(), null, getLeftPokemon()).processServer();
        Assert.assertTrue("Leader pokemon doesn't have its belly emptied when skipping a turn, but it should.",
                AssertUtils.containsObjectOfClass(result, BellyChangedEvent.class));

        result = new TurnSkippedEvent(getFloor(), null, getRightPokemon()).processServer();
        Assert.assertFalse("Wild pokemon has its belly emptied when skipping a turn, but it shouldn't.",
                AssertUtils.containsObjectOfClass(result, BellyChangedEvent.class));
    }

    @Test
    public void testSpawnedEvent() {
        Pokemon p = Registries.species().find(200).generate(5);
        p.createDungeonPokemon();
        DungeonPokemon pokemon = p.getDungeonPokemon();
        Tile tile = getFloor().tileAt(15, 15);
        CreatedEncounter encounter = new CreatedEncounter(pokemon, tile, null);
        new PokemonSpawnedEvent(getFloor(), null, encounter).processServer();

        Assert.assertNotNull("The tile doesn't have any Pokemon after spawn.", tile.getPokemon());
        Assert.assertNotNull("The spawned Pokemon doesn't have an AI.", getFloor().aiManager.getAI(pokemon));
        Assert.assertNotNull("The Pokemon doesn't have any tile after spawn.", pokemon.tile());
        Assert.assertEquals("The tile the Pokemon spawned on doesn't have the correct Pokemon on it.",
                tile.getPokemon(), pokemon);
        Assert.assertEquals("The spawned Pokemon doesn't have the correct tile.", pokemon.tile(), tile);
    }

    @Test
    public void testTravelEvent() {
        DungeonPokemon pokemon = getLeftPokemon();
        pokemon.setFacing(Direction.SOUTH);
        Tile origin = pokemon.tile(), destination = origin.adjacentTile(Direction.NORTH);
        ArrayList<Event> result = new PokemonTravelEvent(getFloor(), null, pokemon, Direction.NORTH).processServer();
        Tile actualDestination = pokemon.tile();

        Assert.assertNull("The tile the Pokemon left still has a Pokemon.", origin.getPokemon());
        Assert.assertNotNull("The destination tile doesn't have any Pokemon.", destination.getPokemon());
        Assert.assertEquals("The Pokemon didn't go to the intended destination tile.", destination, actualDestination);
        Assert.assertEquals("The destination tile doesn't have the correct Pokemon on it.", destination.getPokemon(),
                pokemon);
        Assert.assertTrue("Leader pokemon doesn't have its belly emptied when travelling, but it should.",
                AssertUtils.containsObjectOfClass(result, BellyChangedEvent.class));
    }

}
