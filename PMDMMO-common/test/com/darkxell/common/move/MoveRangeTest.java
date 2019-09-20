package com.darkxell.common.move;

import static com.darkxell.common.testutils.TestUtils.*;
import static com.darkxell.common.testutils.move.MoveTestUtils.assertNotTargeted;
import static com.darkxell.common.testutils.move.MoveTestUtils.assertTargeted;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.Dungeon.DungeonDirection;
import com.darkxell.common.dungeon.data.FloorData;
import com.darkxell.common.dungeon.data.FloorSet;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.move.Move.MoveRange;
import com.darkxell.common.move.Move.MoveTarget;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.Direction;

public class MoveRangeTest {

    private DungeonPokemon ally;
    private Pokemon allyPokemon;

    @After
    public void after() {
        resetDungeonToDefault();
    }

    @Before
    public void before() {
        ArrayList<FloorData> floorData = new ArrayList<>();
        floorData.add(new FloorData(new FloorSet(1, 1), 1, 1, Layout.LAYOUT_STATIC, 1, FloorData.NO_SHADOW,
                PokemonType.Unknown, 1, "", 1, (short) 0, (short) 0, (short) 1, (short) 0, (short) 0, (short) 0, -1));
        setDungeon(new Dungeon(9999, 1, DungeonDirection.UP, false, 1000, 0, -1, new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), floorData, new HashMap<>(), 0, 0));

        generateDefaultObjects();
        getPlayer().addAlly(this.allyPokemon = Registries.species().find(99).generate(1));

        generateTestFloor();
        this.ally = this.allyPokemon.getDungeonPokemon();

    }

    private ArrayList<Event> runMoveSelectionEvent(Move move) {
        Registries.moves().register(move);
        MoveSelectionEvent event = new MoveSelectionEvent(getFloor(), null, new LearnedMove(move.id), getLeftPokemon());
        return event.processServer();
    }

    @Test
    public void testAmbient() {
        Move move = new MoveBuilder().withRange(MoveRange.Ambient).withTargets(MoveTarget.All).build();
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        ArrayList<Event> events = runMoveSelectionEvent(move);
        assertNotTargeted(events, getLeftPokemon());
        assertNotTargeted(events, getRightPokemon());
    }

    @Test
    public void testAround() {
        Move move = new MoveBuilder().withRange(MoveRange.Around).withTargets(MoveTarget.All).build();
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing().opposite()).setPokemon(getRightPokemon());
        ArrayList<Event> events = runMoveSelectionEvent(move);
        assertNotTargeted(events, getLeftPokemon());
        assertTargeted(events, getRightPokemon());
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing().rotateClockwise()).setPokemon(getRightPokemon());
        events = runMoveSelectionEvent(move);
        assertTargeted(events, getRightPokemon());
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        events = runMoveSelectionEvent(move);
        assertTargeted(events, getRightPokemon());
    }

    @Test
    public void testAround2() {
        Move move = new MoveBuilder().withRange(MoveRange.Around2).withTargets(MoveTarget.All).build();
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing().opposite()).setPokemon(getRightPokemon());
        ArrayList<Event> events = runMoveSelectionEvent(move);
        assertNotTargeted(events, getLeftPokemon());
        assertTargeted(events, getRightPokemon());
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing().rotateClockwise())
                .adjacentTile(getLeftPokemon().facing().opposite()).setPokemon(getRightPokemon());
        events = runMoveSelectionEvent(move);
        assertTargeted(events, getRightPokemon());
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).adjacentTile(getLeftPokemon().facing())
                .setPokemon(getRightPokemon());
        events = runMoveSelectionEvent(move);
        assertTargeted(events, getRightPokemon());
    }

    @Test
    public void testFloor() {
        Move move = new MoveBuilder().withRange(MoveRange.Floor).withTargets(MoveTarget.All).build();
        getFloor().tileAt(13, 7).setPokemon(getRightPokemon());
        ArrayList<Event> events = runMoveSelectionEvent(move);
        assertTargeted(events, getLeftPokemon());
        assertTargeted(events, getRightPokemon());
    }

    @Test
    public void testFront() {
        Move move = new MoveBuilder().withRange(MoveRange.Front).withTargets(MoveTarget.All).build();
        ArrayList<Event> events = runMoveSelectionEvent(move);
        assertNotTargeted(events, getLeftPokemon());
        assertNotTargeted(events, getRightPokemon());

        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        events = runMoveSelectionEvent(move);
        assertTargeted(events, getRightPokemon());
    }

    @Test
    public void testFrontCorners() {
        getLeftPokemon().setFacing(Direction.NORTHEAST);
        getFloor().tileAt(5, 5).setPokemon(getLeftPokemon());
        getFloor().tileAt(6, 4).setPokemon(getRightPokemon());

        Move move = new MoveBuilder().withRange(MoveRange.Front).withTargets(MoveTarget.All).build();
        ArrayList<Event> events = runMoveSelectionEvent(move);
        assertNotTargeted(events, getLeftPokemon());
        assertNotTargeted(events, getRightPokemon());

        move = new MoveBuilder().withRange(MoveRange.Front_corners).withTargets(MoveTarget.All).build();
        events = runMoveSelectionEvent(move);
        assertTargeted(events, getRightPokemon());
    }

    @Test
    public void testFrontRow() {
        Move move = new MoveBuilder().withRange(MoveRange.Front_row).withTargets(MoveTarget.All).build();
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing().rotateClockwise()).setPokemon(getRightPokemon());
        ArrayList<Event> events = runMoveSelectionEvent(move);
        assertNotTargeted(events, getLeftPokemon());
        assertTargeted(events, getRightPokemon());

        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        events = runMoveSelectionEvent(move);
        assertTargeted(events, getRightPokemon());

        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing().rotateCounterClockwise())
                .setPokemon(getRightPokemon());
        events = runMoveSelectionEvent(move);
        assertTargeted(events, getRightPokemon());
    }

    @Test
    public void testLine() {
        Move move = new MoveBuilder().withRange(MoveRange.Line).withTargets(MoveTarget.All).build();
        getLeftPokemon().setFacing(Direction.NORTH);
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).adjacentTile(getLeftPokemon().facing())
                .adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        ArrayList<Event> events = runMoveSelectionEvent(move);
        assertNotTargeted(events, getLeftPokemon());
        assertTargeted(events, getRightPokemon());
    }

    @Test
    public void testRandomAlly() {
        Move move = new MoveBuilder().withRange(MoveRange.Random_ally).withTargets(MoveTarget.All).build();
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        ArrayList<Event> events = runMoveSelectionEvent(move);
        assertNotTargeted(events, getLeftPokemon());
        assertNotTargeted(events, getRightPokemon());
        assertTargeted(events, this.ally);
    }

    @Test
    public void testRoom() {
        Move move = new MoveBuilder().withRange(MoveRange.Room).withTargets(MoveTarget.All).build();
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        ArrayList<Event> events = runMoveSelectionEvent(move);
        assertTargeted(events, getLeftPokemon());
        assertTargeted(events, getRightPokemon());

        getFloor().tileAt(12, 5).setPokemon(getRightPokemon());
        events = runMoveSelectionEvent(move);
        assertNotTargeted(events, getRightPokemon());
    }

    @Test
    public void testSelf() {
        Move move = new MoveBuilder().withRange(MoveRange.Self).withTargets(MoveTarget.All).build();
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        ArrayList<Event> events = runMoveSelectionEvent(move);
        assertTargeted(events, getLeftPokemon());
        assertNotTargeted(events, getRightPokemon());
    }

    @Test
    public void testtwoTiles() {
        Move move = new MoveBuilder().withRange(MoveRange.Two_tiles).withTargets(MoveTarget.All).build();
        getLeftPokemon().setFacing(Direction.NORTH);
        getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        ArrayList<Event> events = runMoveSelectionEvent(move);
        assertNotTargeted(events, getLeftPokemon());
        assertTargeted(events, getRightPokemon());

        getRightPokemon().tile().adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        events = runMoveSelectionEvent(move);
        assertTargeted(events, getRightPokemon());

        getRightPokemon().tile().adjacentTile(getLeftPokemon().facing()).setPokemon(getRightPokemon());
        events = runMoveSelectionEvent(move);
        assertNotTargeted(events, getRightPokemon());
    }

}
