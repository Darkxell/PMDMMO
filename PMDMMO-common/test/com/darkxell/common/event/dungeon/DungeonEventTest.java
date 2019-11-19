package com.darkxell.common.event.dungeon;

import static com.darkxell.common.testutils.TestUtils.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.dungeon.DungeonOutcome;
import com.darkxell.common.dungeon.DungeonOutcome.Outcome;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.Event;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.mission.DungeonMission;
import com.darkxell.common.mission.InvalidParammetersException;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.mission.MissionReward;
import com.darkxell.common.status.ActiveFloorStatus;
import com.darkxell.common.status.FloorStatuses;
import com.darkxell.common.testutils.AssertUtils;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.Direction;

public class DungeonEventTest {

    private DungeonMission dungeonMission;
    private Mission mission;

    @Before
    public void before() {
        generateDefaultObjects();
        try {
            this.mission = new Mission("A", getDungeon().id, 1, 45, 45, -1,
                    new MissionReward(0, new int[] {}, new int[] {}, 0, new String[] {}), Mission.TYPE_RESCUEME);
            getPlayer().getData().missionsids.add(this.mission.toString());
        } catch (InvalidParammetersException e) {
            e.printStackTrace();
        }
        getPlayer().inventory().addItem(new ItemStack(118));
        getPlayer().inventory().addItem(new ItemStack(1));

        generateTestFloor();
        this.dungeonMission = getExploration().findMission(this.mission.toString());
    }

    @Test
    public void testDungeonExitEvent() {
        DungeonExitEvent event = new DungeonExitEvent(getFloor(), null, getPlayer());
        ArrayList<Event> result = event.processServer();

        Assert.assertTrue("DungeonExitEvent doesn't trigger an ExplorationStopEvent",
                AssertUtils.containsObjectOfClass(result, ExplorationStopEvent.class));
        DungeonOutcome outcome = AssertUtils.getObjectOfClass(result, ExplorationStopEvent.class).outcome;
        Assert.assertEquals("Outcome dungeon is different from exploration dungeon.", outcome.dungeon(), getDungeon());
        Assert.assertEquals("Outcome isn't 'Dungeon_cleared'.", outcome.getOutcome(), Outcome.DUNGEON_CLEARED);
        Assert.assertNull("Outcome KO move isn't null.", outcome.koMove());

    }

    @Test
    public void testFloorStatusEvent() {
        ActiveFloorStatus status = new ActiveFloorStatus(FloorStatuses.Reduce_fire, null, 2);
        FloorStatusCreatedEvent event = new FloorStatusCreatedEvent(getFloor(), null, status);
        event.processServer();

        boolean flag = false;
        for (ActiveFloorStatus s : getFloor().activeStatuses())
            if (s == status)
                flag = true;
        Assert.assertTrue("Floor didn't get the status.", flag);

        event.processServer();
        Assert.assertEquals("Floor got the status twice.", 1, getFloor().activeStatuses().length);

        ActiveFloorStatus otherStatus = new ActiveFloorStatus(FloorStatuses.Reduce_electric, null, 2);
        Assert.assertFalse("FloorStatusEndedEvent is valid when the Floor doesn't have the status.",
                new FloorStatusEndedEvent(getFloor(), null, otherStatus).isValid());

        new FloorStatusEndedEvent(getFloor(), null, status).processServer();
        Assert.assertEquals("The floor status wasn't cleared correctly.", 0, getFloor().activeStatuses().length);
    }

    @Test
    public void testMissionClearedEvent() {
        MissionClearedEvent event = new MissionClearedEvent(getFloor(), null, this.dungeonMission);
        event.processServer();
        Assert.assertTrue("Mission wasn't cleared properly.", this.dungeonMission.isCleared());
    }

    @Test
    public void testNextFloor() {
        NextFloorEvent event = new NextFloorEvent(getFloor(), null, getPlayer());
        getPlayer().getDungeonLeader().tile().setType(TileType.WATER);
        Assert.assertFalse("NextFloorEvent is valid without stairs.", event.isValid());

        getPlayer().getDungeonLeader().tile().setType(TileType.STAIR);
        Assert.assertTrue("NextFloorEvent isn't valid but should be.", event.isValid());

        getPlayer().getDungeonLeader().setHP(0);
        Assert.assertFalse("NextFloorEvent is valid with fainted Pokemon.", event.isValid());

        event.processServer();
        Assert.assertEquals("NextFloorEvent doesn't switch floor correctly.", getExploration().currentFloor().id,
                getFloor().id + 1);
    }

    @Test
    public void testPlayerLosesEvent() {
        Tile tile = getLeftPokemon().tile();
        PlayerLosesEvent event = new PlayerLosesEvent(getFloor(), null, getPlayer(), 12);
        ArrayList<Event> result = event.processServer();

        Assert.assertNull("Pokemon wasn't despawned.", tile.getPokemon());
        Assert.assertEquals("Unexpected number of result events.", 1, result.size());
        Assert.assertTrue("Created Event is incorrect.",
                AssertUtils.containsObjectOfClass(result, ExplorationStopEvent.class));
        ExplorationStopEvent e = AssertUtils.getObjectOfClass(result, ExplorationStopEvent.class);
        Assert.assertEquals("Invalid exploration outcome.", Outcome.KO, e.outcome.getOutcome());
        Assert.assertEquals("Invelid exploration outcome KO move ID.", 12, e.outcome.getMoveID());
    }

    @Test
    public void testTileTypeChangedEvent() {
        getFloor().tileAt(0, 0).setType(TileType.LAVA);
        new TileTypeChangedEvent(getFloor(), null, getFloor().tileAt(0, 0), TileType.WATER).processServer();

        Assert.assertEquals(TileType.WATER, getFloor().tileAt(0, 0).type());
    }

    @Test
    public void testTrapDestroyedEvent() {
        Tile tile = getFloor().tileAt(20, 20);
        tile.trap = null;
        TrapDestroyedEvent event = new TrapDestroyedEvent(getFloor(), null, tile);
        Assert.assertFalse("TrapDestroyedEvent is valid without a Trap.", event.isValid());

        tile.trap = TrapRegistry.WONDER_TILE;
        tile.trapRevealed = true;
        Assert.assertTrue("TrapDestroyedEvent isn't valid but should be.", event.isValid());
        event.processServer();

        Assert.assertNull("TrapDestroyedEvent doesn't destroy the trap correctly.", tile.trap);
        Assert.assertFalse("TrapDestroyedEvent doesn't reset the 'trapRevealed' status to false.", tile.trapRevealed);
    }

    @Test
    public void testTrapSteppedOnEvent() {
        Tile tile = getLeftPokemon().tile().adjacentTile(Direction.NORTH);
        tile.trap = null;
        tile.trapRevealed = false;
        TrapSteppedOnEvent event = new TrapSteppedOnEvent(getFloor(), null, getLeftPokemon(), tile);

        Assert.assertFalse("TrapSteppedOnEvent is valid even though there is no trap.", event.isValid());

        tile.trap = TrapRegistry.WONDER_TILE;
        Assert.assertFalse("TrapSteppedOnEvent is valid even though the Pokemon isn't on the correct tile.",
                event.isValid());

        tile.setPokemon(getLeftPokemon());
        Assert.assertTrue("TrapSteppedOnEvent isn't valid when it should be.", event.isValid());
        event.processServer();
        Assert.assertTrue("TrapSteppedOnEvent doesn't reveal the trap.", tile.trapRevealed);
    }

}
