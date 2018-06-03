package com.darkxell.common.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.darkxell.common.ai.AI;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.dungeon.floor.layout.StaticLayout;
import com.darkxell.common.event.Actor;
import com.darkxell.common.event.Actor.Action;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.GameTurn;
import com.darkxell.common.event.action.PokemonRotateEvent;
import com.darkxell.common.event.action.TurnSkippedEvent;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.Logger;

public class DungeonInstance
{

	private HashMap<DungeonPokemon, Actor> actorMap = new HashMap<>();
	/** The Pokémon to take turn in order. */
	private ArrayList<Actor> actors = new ArrayList<>();
	/** The current Pokémon taking its turn. */
	private int currentActor;
	/** The current Floor. */
	private Floor currentFloor;
	private int currentSubTurn;
	/** The current turn. */
	private GameTurn currentTurn;
	/** The Players that are currently exploring this Dungeon. */
	private ArrayList<Player> exploringPlayers = new ArrayList<>();
	/** ID of the Dungeon. */
	public final int id;
	/** True if this Dungeon is currently generating a floor. Used for Actor registering. */
	private boolean isGeneratingFloor;
	public final TempIDRegistry<ItemStack> itemIDs = new TempIDRegistry<>();
	public final TempIDRegistry<LearnedMove> moveIDs = new TempIDRegistry<>();
	/** Lists the previous turns. */
	private ArrayList<GameTurn> pastTurns = new ArrayList<>();
	public final TempIDRegistry<Pokemon> pokemonIDs = new TempIDRegistry<>();
	/** RNG for floor generation. */
	public final Random random;
	/** All the Players that started exploring this Dungeon, even if they left. */
	private ArrayList<Player> startingPlayers = new ArrayList<>();

	public DungeonInstance(int id, Random random)
	{
		this.id = id;
		this.random = random;
	}

	/** Adds the input Player to the list of Players currently exploring this Dungeon. */
	public void addPlayer(Player player)
	{
		if (!this.startingPlayers.contains(player))
		{
			this.startingPlayers.add(player);
			this.exploringPlayers.add(player);
		}
	}

	public void clearAllTempIDs()
	{
		itemIDs.clear();
		moveIDs.clear();
		pokemonIDs.clear();
	}

	/** Unregisters the input Pokemon and the item and moves it has. */
	public void clearIDDeep(Pokemon p)
	{
		pokemonIDs.unregister(p.getData().id);
		if (p.getItem() != null) itemIDs.unregister(p.getItem().getData().id);
		for (int i = 0; i < p.moveCount(); ++i)
			moveIDs.unregister(p.move(i).getData().id);
	}

	/** Compares the input Pokémon depending on their order of action. */
	public int compare(DungeonPokemon p1, DungeonPokemon p2)
	{
		return Integer.compare(this.indexOf(p1), this.indexOf(p2));
	}

	private Floor createFloor(int floorID)
	{
		Layout l = this.dungeon().getData(floorID).layout();
		return new Floor(floorID, l, this, new Random(this.random.nextLong()), (l instanceof StaticLayout));
	}

	public Floor currentFloor()
	{
		return this.currentFloor;
	}

	public GameTurn currentTurn()
	{
		return this.currentTurn;
	}

	public Dungeon dungeon()
	{
		return DungeonRegistry.find(this.id);
	}

	/** Ends the current Floor, creates the next and returns it. */
	public Floor endFloor()
	{
		this.currentSubTurn = 0;
		this.currentActor = -1;
		this.generateNextFloor();
		return this.currentFloor;
	}

	/** Ends the current Turn.
	 * 
	 * @return The Events created for the start of the new turn. */
	public ArrayList<DungeonEvent> endSubTurn()
	{
		// Logger.i("Subturn end!");
		for (Actor a : this.actors)
		{
			if (!a.hasSubTurnTriggered()) Logger.e("Subturn ended but " + a + " wasn't called!");
			a.subTurnEnded();
		}

		this.currentActor = -1;
		this.nextActor();
		++this.currentSubTurn;

		ArrayList<DungeonEvent> events = new ArrayList<>();
		boolean turnEnd = this.currentSubTurn == GameTurn.SUB_TURNS;

		if (turnEnd) this.endTurn(events);

		return events;
	}

	private void endTurn(ArrayList<DungeonEvent> events)
	{
		Direction d;
		for (Actor a : this.actors)
		{
			AI ai = this.currentFloor.aiManager.getAI(a.pokemon);
			if (ai == null) continue;
			d = ai.mayRotate();
			if (d != null && d != a.pokemon.facing()) events.add(new PokemonRotateEvent(this.currentFloor, a.pokemon, d));
		}

		for (Actor a : this.actors)
			if (a.actionThisSubturn() != Action.NO_ACTION) a.onTurnEnd(this.currentFloor, events);

		// Logger.i("Turn end --------------------------");
		this.currentFloor.onTurnStart(events);
		if (this.currentTurn != null) this.pastTurns.add(this.currentTurn);
		this.currentTurn = new GameTurn(this.currentFloor);
		this.currentSubTurn = 0;
	}

	/** Called when the input event is processed. */
	public void eventOccured(DungeonEvent event)
	{
		this.currentTurn.addEvent(event);
		if (event.actor() != null && this.actorMap.containsKey(event.actor()))
		{
			if (event instanceof TurnSkippedEvent) this.actorMap.get(event.actor()).skip();
			else this.actorMap.get(event.actor()).act();
		}
	}

	private void generateNextFloor()
	{
		this.isGeneratingFloor = true;
		if (this.currentFloor != null) this.currentFloor.dispose();
		this.currentFloor = this.createFloor(this.currentFloor == null ? 1 : this.currentFloor.id + 1);
		this.currentFloor.generate();
		this.currentFloor.placePlayers(this.exploringPlayers);

		this.actors.clear();
		this.actorMap.clear();
		// Register Player first
		for (Player player : this.exploringPlayers)
			for (DungeonPokemon pokemon : player.getDungeonTeam())
				this.registerActor(pokemon);
		// Then Wild pokemon
		for (DungeonPokemon pokemon : this.currentFloor.listPokemon())
			if (pokemon.player() == null) this.registerActor(pokemon);

		this.isGeneratingFloor = false;
	}

	/** @return The Pokémon taking its turn. null if there is no actor left, thus the turn is over. */
	public DungeonPokemon getActor()
	{
		if (this.currentActor >= this.actors.size() || this.currentActor < 0) return null;
		return this.actors.get(this.currentActor).pokemon;
	}

	/** @return The last past turn. null if this is the first turn. */
	public GameTurn getLastTurn()
	{
		if (this.pastTurns.isEmpty()) return null;
		return this.pastTurns.get(this.pastTurns.size() - 1);
	}

	private int indexOf(DungeonPokemon pokemon)
	{
		if (this.actorMap.containsKey(pokemon)) return this.actors.indexOf(this.actorMap.get(pokemon));
		return -1;
	}

	public void initiateExploration()
	{
		if (this.currentFloor != null)
		{
			Logger.e("Tried to start the Dungeon again!");
			return;
		}
		this.generateNextFloor();
		this.currentSubTurn = GameTurn.SUB_TURNS - 1;
		this.endTurn(new ArrayList<>());
	}

	/* public void insertActor(DungeonPokemon pokemon, int index) { if (this.actorMap.containsKey(pokemon)) return; this.actorMap.put(pokemon, new Actor(pokemon)); this.actors.add(index, this.actorMap.get(pokemon)); } */

	public boolean isGeneratingFloor()
	{
		return this.isGeneratingFloor;
	}

	/** Proceeds to the next actor and returns it. */
	public DungeonPokemon nextActor()
	{
		this.nextActorIndex();
		return this.getActor();
	}

	private void nextActorIndex()
	{
		if (this.currentActor == this.actors.size()) return;

		// Make sure subturn() doesn't get called if actedThisSubturn() returns true
		if (this.currentActor != -1)
		{
			boolean acts = this.actors.get(this.currentActor).actionThisSubturn() == Action.NO_ACTION;
			if (!this.actors.get(this.currentActor).hasSubTurnTriggered()) acts = this.actors.get(this.currentActor).subTurn() && acts;
			if (acts) return;
		}
		++this.currentActor;
		this.nextActorIndex();
	}

	public void onSpeedChange(DungeonPokemon pokemon)
	{
		if (!this.actorMap.containsKey(pokemon)) return;
		this.actorMap.get(pokemon).onSpeedChange();
	}

	public void registerActor(DungeonPokemon pokemon)
	{
		if (this.actorMap.containsKey(pokemon))
		{
			Logger.e("Actor " + pokemon + " already registered!");
			return;
		}
		this.actorMap.put(pokemon, new Actor(pokemon));
		this.actors.add(this.actorMap.get(pokemon));
	}

	/** Removes the input Player from this Dungeon. Called when that Player exits the Dungeon by winning, escaping or losing. */
	public void removePlayer(Player player)
	{
		this.exploringPlayers.remove(player);
	}

	public void unregisterActor(DungeonPokemon pokemon)
	{
		if (!this.actorMap.containsKey(pokemon))
		{
			Logger.e("Actor " + pokemon + " isn't registered: can't unregister!");
			return;
		}
		this.actors.remove(this.actorMap.get(pokemon));
		this.actorMap.remove(pokemon);
	}

}
