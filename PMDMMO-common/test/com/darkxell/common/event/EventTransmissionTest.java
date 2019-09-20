package com.darkxell.common.event;

import static com.darkxell.common.testutils.TestUtils.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.action.PokemonRotateEvent;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.dungeon.NextFloorEvent;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.item.ItemSelectionEvent;
import com.darkxell.common.event.item.ItemThrownEvent;
import com.darkxell.common.event.move.MoveEnabledEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveSwitchedEvent;
import com.darkxell.common.event.pokemon.PokemonRescuedEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.mission.InvalidParammetersException;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.mission.MissionReward;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.testutils.AssertUtils;
import com.darkxell.common.util.Direction;

public class EventTransmissionTest {

    private DungeonPokemon rescuable;

    @Before
    public void before() {
        generateDefaultObjects();
        try {
            getPlayer().getData().missionsids.add(new Mission("A", getDungeon().id, 1, 45, 45, -1,
                    new MissionReward(0, new int[] {}, new int[] {}, 0, new String[] {}), Mission.TYPE_RESCUEME)
                            .toString());
        } catch (InvalidParammetersException e) {
            e.printStackTrace();
        }
        getPlayer().inventory().addItem(new ItemStack(118));
        getPlayer().inventory().addItem(new ItemStack(1));

        generateTestFloor();
        for (DungeonPokemon p : getFloor().listPokemon())
            if (p.species().id == 45)
                this.rescuable = p;
    }

    @Test
    public void testCommunicateDungeonExitEvent() {
        DungeonExitEvent e = new DungeonExitEvent(getFloor(), null, getPlayer());
        Event received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, DungeonExitEvent.class));
        DungeonExitEvent e1 = (DungeonExitEvent) received;
        Assert.assertEquals("Received Player isn't correct.", e.player(), e1.player());
    }

    @Test
    public void testCommunicateItemMovedEvent() {
        ItemMovedEvent e = new ItemMovedEvent(getFloor(), null, ItemAction.GET, getLeftPokemon(),
                getLeftPokemon().tile(), 0, getPlayer().inventory(), 0);
        Event received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, ItemMovedEvent.class));
        ItemMovedEvent e1 = (ItemMovedEvent) received;
        Assert.assertEquals("Received Action isn't correct.", e.action(), e1.action());
        Assert.assertEquals("Received mover Pokemon isn't correct.", e.mover(), e1.mover());
        Assert.assertEquals("Received source container isn't correct.", e.source(), e1.source());
        Assert.assertEquals("Received destination container isn't correct.", e.destination(), e1.destination());
        Assert.assertEquals("Received source index isn't correct.", e.sourceIndex(), e1.sourceIndex());
        Assert.assertEquals("Received destination index isn't correct.", e.destinationIndex(), e1.destinationIndex());
    }

    @Test
    public void testCommunicateItemSelectionEventWithNoTargets() {
        ItemSelectionEvent e = new ItemSelectionEvent(getFloor(), null, Registries.items().find(118), getLeftPokemon(),
                null, getPlayer().inventory(), 0, Direction.SOUTH, true);
        Event received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, ItemSelectionEvent.class));
        ItemSelectionEvent e1 = (ItemSelectionEvent) received;
        Assert.assertEquals("Received Item isn't correct.", e.item(), e1.item());
        Assert.assertEquals("Received user Pokemon isn't correct.", e.user(), e1.user());
        Assert.assertNull("Received target Pokemon isn't correct: it should be null", e1.target());
        Assert.assertEquals("Received source container isn't correct.", e.source(), e1.source());
        Assert.assertEquals("Received source index isn't correct.", e.sourceIndex(), e1.sourceIndex());
        Assert.assertEquals("Received Direction isn't correct.", e.direction(), e1.direction());
    }

    @Test
    public void testCommunicateItemSelectionEventWithTarget() {
        ItemSelectionEvent e = new ItemSelectionEvent(getFloor(), null, Registries.items().find(1), getLeftPokemon(),
                getRightPokemon(), getPlayer().inventory(), 1, Direction.SOUTH, false);
        Event received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, ItemSelectionEvent.class));
        ItemSelectionEvent e1 = (ItemSelectionEvent) received;
        Assert.assertEquals("Received Item isn't correct.", e.item(), e1.item());
        Assert.assertEquals("Received user Pokemon isn't correct.", e.user(), e1.user());
        Assert.assertNotNull("Received target Pokemon is null", e1.target());
        Assert.assertEquals("Received target Pokemon isn't correct.", e.target(), e1.target());
        Assert.assertEquals("Received source container isn't correct.", e.source(), e1.source());
        Assert.assertEquals("Received source index isn't correct.", e.sourceIndex(), e1.sourceIndex());
        Assert.assertEquals("Received Direction isn't correct.", e.direction(), e1.direction());
    }

    @Test
    public void testCommunicateItemThrownEvent() {
        ItemThrownEvent e = new ItemThrownEvent(getFloor(), null, getLeftPokemon(), getPlayer().inventory(), 0);
        Event received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, ItemThrownEvent.class));
        ItemThrownEvent e1 = (ItemThrownEvent) received;
        Assert.assertEquals("Received Item isn't correct.", e.item(), e1.item());
        Assert.assertEquals("Received user Pokemon isn't correct.", e.actor(), e1.actor());
        Assert.assertEquals("Received user Pokemon isn't correct.", e.thrower(), e1.thrower());
        Assert.assertEquals("Received source container isn't correct.", e.source(), e1.source());
        Assert.assertEquals("Received source index isn't correct.", e.sourceIndex(), e1.sourceIndex());
    }

    @Test
    public void testCommunicateMoveEnabledEvent() {
        MoveEnabledEvent e = new MoveEnabledEvent(getFloor(), null, getLeftPokemon().move(0), true);
        Event received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, MoveEnabledEvent.class));
        MoveEnabledEvent e1 = (MoveEnabledEvent) received;
        Assert.assertEquals("Received Move isn't correct.", e.move(), e1.move());
        Assert.assertEquals("Received Enabled status isn't correct.", e.enabled(), e1.enabled());

        e = new MoveEnabledEvent(getFloor(), null, getLeftPokemon().move(0), false);
        received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, MoveEnabledEvent.class));
        e1 = (MoveEnabledEvent) received;
        Assert.assertEquals("Received Move isn't correct.", e.move(), e1.move());
        Assert.assertEquals("Received Enabled status isn't correct.", e.enabled(), e1.enabled());
    }

    @Test
    public void testCommunicateMoveSelectionEvent() {
        MoveSelectionEvent e = new MoveSelectionEvent(getFloor(), null, getLeftPokemon().move(0), getLeftPokemon(),
                Direction.NORTHEAST, true);
        Event received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, MoveSelectionEvent.class));
        MoveSelectionEvent e1 = (MoveSelectionEvent) received;
        Assert.assertEquals("Received Move use context isn't correct.", e.usedMove(), e1.usedMove());
        Assert.assertEquals("Received consumesPP flag isn't correct.", e.consumesPP(), e1.consumesPP());
    }

    @Test
    public void testCommunicateMoveSwitchedEvent() {
        MoveSwitchedEvent e = new MoveSwitchedEvent(getFloor(), null, getLeftPokemon().originalPokemon, 0, 1);
        Event received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, MoveSwitchedEvent.class));
        MoveSwitchedEvent e1 = (MoveSwitchedEvent) received;
        Assert.assertEquals("Received Move switcher isn't correct.", e.pokemon(), e1.pokemon());
        Assert.assertEquals("Received move 'from' index isn't correct.", e.from(), e1.from());
        Assert.assertEquals("Received move 'to' index isn't correct.", e.to(), e1.to());
    }

    @Test
    public void testCommunicateNextFloorEvent() {
        NextFloorEvent e = new NextFloorEvent(getFloor(), null, getPlayer());
        Event received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, NextFloorEvent.class));
        NextFloorEvent e1 = (NextFloorEvent) received;
        Assert.assertEquals("Received Player isn't correct.", e.player(), e1.player());
    }

    @Test
    public void testCommunicatePokemonRescuedEvent() {
        PokemonRescuedEvent e = new PokemonRescuedEvent(getFloor(), null, rescuable, getPlayer());
        Event received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, PokemonRescuedEvent.class));
        PokemonRescuedEvent e1 = (PokemonRescuedEvent) received;
        Assert.assertEquals("Received Rescuer isn't correct.", e.rescuer(), e1.rescuer());
        Assert.assertEquals("Received Rescued Pokemon isn't correct.", e.rescued(), e1.rescued());
        Assert.assertEquals("Received Mission isn't correct.", e.mission(), e1.mission());
    }

    @Test
    public void testCommunicateRotateEvent() {
        PokemonRotateEvent e = new PokemonRotateEvent(getFloor(), null, getLeftPokemon(), Direction.SOUTH);
        Event received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, PokemonRotateEvent.class));
        PokemonRotateEvent e1 = (PokemonRotateEvent) received;
        Assert.assertEquals("Received Pokemon isn't the correct actor.", e.actor(), e1.actor());
        Assert.assertEquals("Received Direction isn't correct.", e.direction(), e1.direction());
    }

    @Test
    public void testCommunicateSkipTurnEvent() {
        TurnSkippedEvent e = new TurnSkippedEvent(getFloor(), null, getLeftPokemon());
        Event received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, TurnSkippedEvent.class));
        TurnSkippedEvent e1 = (TurnSkippedEvent) received;
        Assert.assertEquals("Received Pokemon isn't the correct actor.", e.actor(), e1.actor());
    }

    @Test
    public void testCommunicateTravelEvent() {
        PokemonTravelEvent e = new PokemonTravelEvent(getFloor(), null, getLeftPokemon(), true, Direction.NORTHWEST);
        Event received = EventCommunication.read(EventCommunication.prepareToSend(e), getFloor());

        Assert.assertTrue("Received event isn't of the correct class. Its class is " + received.getClass().getName(),
                AssertUtils.isOfClass(received, PokemonTravelEvent.class));
        PokemonTravelEvent e1 = (PokemonTravelEvent) received;
        Assert.assertEquals("Received Pokemon isn't the correct actor.", e.actor(), e1.actor());
        Assert.assertEquals("Received Direction isn't correct.", e.direction(), e1.direction());
        Assert.assertTrue("The Pokemon isn't running, but should be.", e1.running());
    }

}
