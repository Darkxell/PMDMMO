package com.darkxell.common.tests.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.darkxell.common.Registries;
import com.darkxell.common.dbobject.*;
import com.darkxell.common.dungeon.DungeonExploration;
import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.CommonEventProcessor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.event.EventCommunication;
import com.darkxell.common.event.action.PokemonRotateEvent;
import com.darkxell.common.event.action.PokemonTravelEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.item.ItemSelectionEvent;
import com.darkxell.common.event.item.ItemSwappedEvent;
import com.darkxell.common.event.move.MoveEnabledEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveSwitchedEvent;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Inventory;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.tests.CommonSetup;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;

public class DungeonEventTransferTest {
    private Player _player = new Player(new DBPlayer(69, "DaWae", "no U", 45, 99, 0, new ArrayList<>(),
            new ArrayList<>(), null, null, null, null, 0, false, false));
    private Dungeon d = Registries.dungeons().find(4);
    private DungeonExploration dungeon = d.newInstance(798654123);
    private Floor floor;
    private Inventory inventory = new Inventory(new DBInventory(77, 30, new ArrayList<>()), _player);
    private ItemStack item1 = new ItemStack(new DBItemstack(43, 2, 1)),
            item2 = new ItemStack(new DBItemstack(44, 2, 1)), item3 = new ItemStack(new DBItemstack(45, 10, 1));
    private LearnedMove move1 = new LearnedMove(new DBLearnedmove(50, 0, 1, 5, true, false, 0)),
            move2 = new LearnedMove(new DBLearnedmove(51, 1, 101, 5, true, false, 0));
    private DungeonPokemon pokemon = new DungeonPokemon(new Pokemon(new DBPokemon(42, 6, 0, 1, Pokemon.GENDERLESS,
            "My poke", 5, 126894, 111, false, 1, 2, 3, 4, 5, null, new ArrayList<>())));

    @BeforeAll
    public static void setUp() {
        CommonSetup.setUp();
    }

    @BeforeEach
    public void instanceSetup() {
        this._player.setInventory(this.inventory);
        this._player.setLeaderPokemon(this.pokemon.originalPokemon);
        this.pokemon.setItem(this.item1);
        this.pokemon.setMove(0, this.move1);
        this.pokemon.setMove(1, this.move2);
        this.inventory.addItem(this.item3);

        this.dungeon.eventProcessor = new CommonEventProcessor(this.dungeon);
        this.dungeon.addPlayer(this._player);
        this.floor = this.dungeon.initiateExploration();
        this.floor.summonPokemon(this.pokemon, 10, 12, new ArrayList<>());
        this.floor.tileAt(10, 12).setItem(this.item2);
        this.floor.dungeon.communication.itemIDs.register(this.item2, this.pokemon.usedPokemon);
        this.floor.dungeon.communication.itemIDs.register(this.item3, null);
    }

    private DungeonEvent[] generateEvents() {
        return new DungeonEvent[] {
                new PokemonRotateEvent(floor, DungeonEventSource.PLAYER_ACTION, pokemon, Direction.SOUTHWEST),
                new PokemonTravelEvent(floor, DungeonEventSource.PLAYER_ACTION, pokemon, Direction.SOUTHWEST),
                new TurnSkippedEvent(floor, DungeonEventSource.PLAYER_ACTION, pokemon),
                new ItemMovedEvent(floor, DungeonEventSource.PLAYER_ACTION, ItemAction.GET, this.pokemon,
                        this.floor.tileAt(10, 12), 0, this.pokemon.player().inventory(), 1),
                new ItemSwappedEvent(floor, DungeonEventSource.PLAYER_ACTION, ItemAction.SWAP, this.pokemon,
                        this.floor.tileAt(10, 12), 0, this.pokemon.player().inventory(), 1),
                new ItemSelectionEvent(floor, DungeonEventSource.PLAYER_ACTION, this.item1.item(), pokemon, pokemon,
                        pokemon, 0, Direction.EAST, true),
                new MoveEnabledEvent(floor, eventSource, move1, false),
                new MoveSwitchedEvent(floor, eventSource, pokemon.originalPokemon, 0, 1),
                new MoveSelectionEvent(floor, eventSource, move1, pokemon, Direction.SOUTH) };
    }

    @Test
    protected void test() {
        DungeonEvent[] events = this.generateEvents();
        ArrayList<DungeonEvent[]> mismatches = new ArrayList<>();

        Logger.d("Events:" + Arrays.stream(events).map(e -> "\n\t" + e.loggerMessage()).collect(Collectors.joining())
                + "\n");

        for (DungeonEvent event : events) {
            DungeonEvent returned = EventCommunication.read(EventCommunication.prepareToSend(event), floor);
            if (!event.equals(returned)) mismatches.add(new DungeonEvent[] { event, returned });
        }

        assertTrue(mismatches.size() == 0, () -> {
            return "Events have not been transferred successfully:\n"
                    + mismatches.stream().map(pair -> "\t(event: " + pair[0].loggerMessage() + ",\n\t\treturned: "
                            + pair[1].loggerMessage() + "),\n").collect(Collectors.joining());
        });
    }
}
