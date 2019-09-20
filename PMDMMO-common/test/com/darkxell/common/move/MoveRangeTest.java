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
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.registry.Registries;

public class MoveRangeTest {

    @After
    public void after() {
        resetDungeonToDefault();
    }

    @Before
    public void before() {
        ArrayList<FloorData> floorData = new ArrayList<>();
        floorData.add(new FloorData(new FloorSet(1, 1), 1, 1, Layout.LAYOUT_STATIC, 1, (byte) 0, PokemonType.Unknown, 1,
                "", 1, (short) 0, (short) 0, (short) 1, (short) 0, (short) 0, (short) 0, -1));
        setDungeon(new Dungeon(9999, 1, DungeonDirection.UP, false, 1000, 0, -1, new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), floorData, new HashMap<>(), 0, 0));
        generateALL();
    }

    private ArrayList<Event> runMoveSelectionEvent(Move move) {
        Registries.moves().register(move);
        MoveSelectionEvent event = new MoveSelectionEvent(getFloor(), null, new LearnedMove(move.id), getLeftPokemon());
        return event.processServer();
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

}
