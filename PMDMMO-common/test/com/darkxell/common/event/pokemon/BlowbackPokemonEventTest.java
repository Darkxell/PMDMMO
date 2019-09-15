package com.darkxell.common.event.pokemon;

import static com.darkxell.common.testutils.TestUtils.generateALL;
import static com.darkxell.common.testutils.TestUtils.getFloor;
import static com.darkxell.common.testutils.TestUtils.getLeftPokemon;
import static com.darkxell.common.testutils.TestUtils.getRightPokemon;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.testutils.AssertUtils;
import com.darkxell.common.util.Direction;

public class BlowbackPokemonEventTest {

    @Before
    public void before() {
        generateALL();
    }

    @Test
    public void testBlowbackIntoNothingEvent() {
        Tile start = getLeftPokemon().tile(), destination = start;
        for (int i = 0; i < BlowbackPokemonEvent.MAX_BLOWBACK_DISTANCE; ++i)
            destination = destination.adjacentTile(Direction.WEST);

        BlowbackPokemonEvent event = new BlowbackPokemonEvent(getFloor(), null, getLeftPokemon(), Direction.WEST);
        ArrayList<Event> result = event.processServer();
        Assert.assertNull("Start Tile still has a Pokemon.", start.getPokemon());
        Assert.assertEquals("Pokemon didn't reach the correct Tile.", getLeftPokemon(), destination.getPokemon());
        Assert.assertEquals("Unexpected resulting events.", 0, result.size());
    }

    @Test
    public void testBlowbackIntoPokemonEvent() {
        Tile start = getLeftPokemon().tile(), destination = start;
        for (int i = 0; i < BlowbackPokemonEvent.MAX_BLOWBACK_DISTANCE - 2; ++i)
            destination = destination.adjacentTile(Direction.WEST);
        destination.adjacentTile(Direction.WEST).setPokemon(getRightPokemon());

        BlowbackPokemonEvent event = new BlowbackPokemonEvent(getFloor(), null, getLeftPokemon(), Direction.WEST);
        ArrayList<Event> result = event.processServer();
        Assert.assertNull("Start Tile still has a Pokemon.", start.getPokemon());
        Assert.assertEquals("Pokemon didn't reach the correct Tile.", getLeftPokemon(), destination.getPokemon());
        Assert.assertEquals("Unexpected resulting events.", 2, result.size());

        boolean foundLeft = false, foundRight = false;
        for (Event r : result) {
            Assert.assertTrue("Unexpected event type.", r instanceof DamageDealtEvent);
            DamageDealtEvent d = (DamageDealtEvent) r;
            if (d.target == getLeftPokemon()) {
                Assert.assertFalse("Blown Pokemon was damaged twice.", foundLeft);
                foundLeft = true;
                Assert.assertEquals("Damage type is incorrect.", DamageType.COLLISION, d.damageType);
            } else if (d.target == getRightPokemon()) {
                Assert.assertFalse("Other Pokemon was damaged twice.", foundRight);
                foundRight = true;
                Assert.assertEquals("Damage type is incorrect.", DamageType.COLLISION, d.damageType);
            }
        }

        Assert.assertTrue("Blown Pokemon wasn't damaged.", foundLeft);
        Assert.assertTrue("Other Pokemon wasn't damaged.", foundRight);
    }

    @Test
    public void testBlowbackIntoWallEvent() {
        Tile start = getLeftPokemon().tile(), destination = start;
        for (int i = 0; i < BlowbackPokemonEvent.MAX_BLOWBACK_DISTANCE - 2; ++i)
            destination = destination.adjacentTile(Direction.WEST);
        destination.adjacentTile(Direction.WEST).setType(TileType.WALL);

        BlowbackPokemonEvent event = new BlowbackPokemonEvent(getFloor(), null, getLeftPokemon(), Direction.WEST);
        ArrayList<Event> result = event.processServer();
        Assert.assertNull("Start Tile still has a Pokemon.", start.getPokemon());
        Assert.assertEquals("Pokemon didn't reach the correct Tile.", getLeftPokemon(), destination.getPokemon());
        Assert.assertEquals("Unexpected resulting events.", 1, result.size());
        Assert.assertTrue("Unexpected created event.",
                AssertUtils.containsObjectOfClass(result, DamageDealtEvent.class));
        DamageDealtEvent e = AssertUtils.getObjectOfClass(result, DamageDealtEvent.class);
        Assert.assertEquals("Damaged Pokemon is incorrect.", getLeftPokemon(), e.target);
        Assert.assertEquals("Damage type is incorrect.", DamageType.COLLISION, e.damageType);
    }

}
