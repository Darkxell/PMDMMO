package com.darkxell.common.event.move;

import static com.darkxell.common.testutils.TestUtils.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.dungeon.BossDefeatedEvent;
import com.darkxell.common.event.dungeon.PlayerLosesEvent;
import com.darkxell.common.event.move.MoveSelectionEvent.MoveUse;
import com.darkxell.common.event.pokemon.FaintedPokemonEvent;
import com.darkxell.common.event.pokemon.RecruitAttemptEvent;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.testutils.AssertUtils;
import com.darkxell.common.util.Direction;

public class FaintedPokemonEventTest {

    @Before
    public void before() {
        generateALL();
        getPlayer().friendAreas.add(getRightPokemon().species().friendArea());
    }

    @Test
    public void testBossDefeatedOnFaintEvent() {
        getRightPokemon().type = DungeonPokemonType.BOSS;
        FaintedPokemonEvent event = new FaintedPokemonEvent(getFloor(), null, getRightPokemon(), null);
        ArrayList<Event> result = event.processServer();
        Assert.assertTrue("Missing BossDefeatedEvent.",
                AssertUtils.containsObjectOfClass(result, BossDefeatedEvent.class));
    }

    @Test
    public void testFaintEvent() {
        Tile tile = getRightPokemon().tile();
        FaintedPokemonEvent event = new FaintedPokemonEvent(getFloor(), null, getRightPokemon(), null);
        ArrayList<Event> result = event.processServer();
        Assert.assertNull("Pokemon wasn't removed from Tile.", tile.getPokemon());
        Assert.assertNull("Pokemon AI wasn't deleted.", getFloor().aiManager.getAI(getRightPokemon()));
        Assert.assertEquals("Unexpected generated Events.", 0, result.size());
    }

    @Test
    public void testItemDropsOnFaintEvent() {
        Tile tile = getRightPokemon().tile();
        ItemStack item = new ItemStack(1);
        getRightPokemon().setItem(item);
        FaintedPokemonEvent event = new FaintedPokemonEvent(getFloor(), null, getRightPokemon(), null);
        /* ArrayList<Event> result = */ event.processServer();
        Assert.assertEquals("Item didn't drop onto the Tile.", item, tile.getItem());
    }

    @Test
    public void testPlayerLosesOnTeamMemberFaintEvent() {
        FaintedPokemonEvent event = new FaintedPokemonEvent(getFloor(), null, getLeftPokemon(), null);
        ArrayList<Event> result = event.processServer();
        Assert.assertTrue("Missing PlayerLosesEvent.",
                AssertUtils.containsObjectOfClass(result, PlayerLosesEvent.class));
        PlayerLosesEvent e = AssertUtils.getObjectOfClass(result, PlayerLosesEvent.class);
        Assert.assertEquals("Incorrect Player loses.", getPlayer(), e.player);
    }

    @Test
    public void testRecruitOnFaintEvent() {
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        FaintedPokemonEvent event = new FaintedPokemonEvent(getFloor(), null, getRightPokemon(),
                new MoveUse(getFloor(), getLeftPokemon().move(0), getLeftPokemon(), Direction.EAST, null));
        ArrayList<Event> result = event.processServer();
        Assert.assertTrue("Missing RecruitAttemptEvent.",
                AssertUtils.containsObjectOfClass(result, RecruitAttemptEvent.class));
        RecruitAttemptEvent e = AssertUtils.getObjectOfClass(result, RecruitAttemptEvent.class);
        Assert.assertEquals("Recruiter Pokemon is incorrect.", getLeftPokemon(), e.recruiter);
        Assert.assertEquals("Recruited Pokemon is incorrect.", getRightPokemon(), e.target);
    }

}
