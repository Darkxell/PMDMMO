package com.darkxell.common.test.tests;

import java.util.ArrayList;

import com.darkxell.common.dbobject.DBInventory;
import com.darkxell.common.dbobject.DBItemstack;
import com.darkxell.common.dbobject.DBLearnedmove;
import com.darkxell.common.dbobject.DBPlayer;
import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.common.dungeon.Dungeon;
import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.CommonEventProcessor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.EventCommunication;
import com.darkxell.common.event.action.PokemonRotateEvent;
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
	private Dungeon d = DungeonRegistry.find(4);
	private DungeonInstance dungeon = d.newInstance(798654123);
	private Floor floor;
	private Inventory inventory = new Inventory(new DBInventory(77, 30, new ArrayList<>()));
	private ItemStack item1 = new ItemStack(new DBItemstack(43, 2, 1)), item2 = new ItemStack(new DBItemstack(44, 2, 1)),
			item3 = new ItemStack(new DBItemstack(45, 10, 1));
	private LearnedMove move1 = new LearnedMove(new DBLearnedmove(50, 0, 1, 5, true, false, 0)),
			move2 = new LearnedMove(new DBLearnedmove(51, 1, 101, 5, true, false, 0));
	private Player player = new Player(new DBPlayer(69, "DaWae", "no U", 45, 99, 0, new ArrayList<>(), new ArrayList<>(), null, null, null));
	private DungeonPokemon pokemon = new DungeonPokemon(
			new Pokemon(new DBPokemon(42, 6, 0, 1, Pokemon.GENDERLESS, "My poke", 5, 126894, 111, false, 1, 2, 3, 4, 5, null, new ArrayList<>())));

	{
		this.player.setInventory(this.inventory);
		this.player.setLeaderPokemon(this.pokemon.originalPokemon);
		this.pokemon.setItem(this.item1);
		this.pokemon.setMove(0, this.move1);
		this.pokemon.setMove(1, this.move2);

		this.dungeon.eventProcessor = new CommonEventProcessor(this.dungeon);
		this.dungeon.addPlayer(this.player);
		this.floor = this.dungeon.initiateExploration();
		this.floor.summonPokemon(this.pokemon, 10, 12, new ArrayList<>());
	}

	public DungeonEventTransferTest()
	{
		super(ACTION_WARN);
	}

	private ArrayList<DungeonEvent> generateEvents()
	{
		ArrayList<DungeonEvent> events = new ArrayList<>();
		events.add(new PokemonRotateEvent(floor, pokemon, Direction.SOUTHWEST));
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
				if (success[i])
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
