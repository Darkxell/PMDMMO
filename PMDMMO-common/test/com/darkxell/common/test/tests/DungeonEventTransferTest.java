package com.darkxell.common.test.tests;

import java.util.ArrayList;

import com.darkxell.common.dbobject.DBInventory;
import com.darkxell.common.dbobject.DBItemstack;
import com.darkxell.common.dbobject.DBLearnedmove;
import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.common.dungeon.DungeonExploration;
import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.CommonEventProcessor;
import com.darkxell.common.event.DungeonEvent;
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
import com.darkxell.common.test.UTest;
import com.darkxell.common.util.Direction;

public class DungeonEventTransferTest extends UTest
{
	private Player _player = new Player(new DBPlayer(69, "DaWae", "no U", 45, 99, 0, new ArrayList<>(), new ArrayList<>(), null, null, null, null, 0));
	private Dungeon d = DungeonRegistry.find(4);
	private DungeonExploration dungeon = d.newInstance(798654123);
	private Floor floor;
	private Inventory inventory = new Inventory(new DBInventory(77, 30, new ArrayList<>()), _player);
	private ItemStack item1 = new ItemStack(new DBItemstack(43, 2, 1)), item2 = new ItemStack(new DBItemstack(44, 2, 1)),
			item3 = new ItemStack(new DBItemstack(45, 10, 1));
	private LearnedMove move1 = new LearnedMove(new DBLearnedmove(50, 0, 1, 5, true, false, 0)),
			move2 = new LearnedMove(new DBLearnedmove(51, 1, 101, 5, true, false, 0));
	private DungeonPokemon pokemon = new DungeonPokemon(
			new Pokemon(new DBPokemon(42, 6, 0, 1, Pokemon.GENDERLESS, "My poke", 5, 126894, 111, false, 1, 2, 3, 4, 5, null, new ArrayList<>())));

	{
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

	public DungeonEventTransferTest()
	{
		super(ACTION_WARN);
	}

	private ArrayList<DungeonEvent> generateEvents()
	{
		ArrayList<DungeonEvent> events = new ArrayList<>();

		events.add(new PokemonRotateEvent(floor, pokemon, Direction.SOUTHWEST));
		events.add(new PokemonTravelEvent(floor, pokemon, Direction.SOUTHWEST));
		events.add(new TurnSkippedEvent(floor, pokemon));

		events.add(new ItemMovedEvent(floor, ItemAction.GET, this.pokemon, this.floor.tileAt(10, 12), 0, this.pokemon.player().inventory(), 1));
		events.add(new ItemSwappedEvent(floor, ItemAction.SWAP, this.pokemon, this.floor.tileAt(10, 12), 0, this.pokemon.player().inventory(), 1));
		events.add(new ItemSelectionEvent(floor, this.item1.item(), pokemon, pokemon, pokemon, 0, Direction.EAST, true));

		events.add(new MoveEnabledEvent(floor, move1, false));
		events.add(new MoveSwitchedEvent(floor, pokemon.originalPokemon, 0, 1));
		events.add(new MoveSelectionEvent(floor, move1, pokemon, Direction.SOUTH));

		return events;
	}

	@Override
	protected int test()
	{
		ArrayList<DungeonEvent> toTest = new ArrayList<>(), returned = new ArrayList<>();
		toTest.addAll(this.generateEvents());
		boolean[] success = new boolean[toTest.size()];
		int failureCount = 0;

		for (int i = 0; i < success.length; ++i)
		{
			returned.add(EventCommunication.read(EventCommunication.prepareToSend(toTest.get(i)), floor));
			success[i] = toTest.get(i).equals(returned.get(i));
			if (!success[i]) ++failureCount;
		}

		if (failureCount != 0)
		{
			String message = failureCount + " events haven't been transferred successfully:\n";
			boolean first = true;
			for (int i = 0; i < success.length; ++i)
				if (!success[i])
				{
					if (first) first = false;
					else message += ", ";
					message += toTest.get(i).getClass().getSimpleName();
				}
			this.log(message);
			return 1;
		}

		return TEST_SUCCESSFUL;
	}

}
