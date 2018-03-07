package com.darkxell.common.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.dungeon.floor.layout.StaticLayout;
import com.darkxell.common.event.Actor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.GameTurn;
import com.darkxell.common.pokemon.DungeonPokemon;

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
	/** ID of the Dungeon. */
	public final int id;
	/** Lists the previous turns. */
	private ArrayList<GameTurn> pastTurns = new ArrayList<>();
	/** RNG for floor generation. */
	public final Random random;

	public DungeonInstance(int id, Random random)
	{
		this.id = id;
		this.random = random;
		this.currentFloor = this.createFloor(1);
		this.currentFloor.generate();
		this.currentSubTurn = GameTurn.SUB_TURNS - 1;
		this.endSubTurn();
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
		this.currentFloor.dispose();
		this.currentFloor = this.createFloor(this.currentFloor.id + 1);
		this.currentFloor.generate();
		this.actors.clear();
		this.actorMap.clear();
		return this.currentFloor;
	}

	/** Ends the current Turn.
	 * 
	 * @return The Events created for the start of the new turn. */
	public ArrayList<DungeonEvent> endSubTurn()
	{
		/* // Checking for Pokémon who didn't act { for (int i = 0; i < this.actors.size(); ++i) if (this.actors.get(i).ac >= 1) Logger.e("Turn ended but " + this.actors.get(i) + " had " + this.actionsLeft.get(i) + " actions left!"); } */

		this.currentActor = 0;
		++this.currentSubTurn;
		// Logger.i("Subturn end!");

		ArrayList<DungeonEvent> events = new ArrayList<>();
		boolean turnEnd = this.currentSubTurn == GameTurn.SUB_TURNS;

		for (Actor a : this.actors)
			if (turnEnd || a.actedThisSubturn()) a.onTurnEnd(this.currentFloor, events);

		if (turnEnd)
		{
			// Logger.i("Turn end --------------------------");
			this.currentFloor.onTurnStart(events);
			if (this.currentTurn != null) this.pastTurns.add(this.currentTurn);
			this.currentTurn = new GameTurn(this.currentFloor);
			this.currentSubTurn = 0;
		}
		return events;
	}

	/** Called when the input event is processed. */
	public void eventOccured(DungeonEvent event)
	{
		this.currentTurn.addEvent(event);
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

	public void insertActor(DungeonPokemon pokemon, int index)
	{
		if (this.actorMap.containsKey(pokemon)) return;
		this.actorMap.put(pokemon, new Actor(pokemon));
		this.actors.add(index, this.actorMap.get(pokemon));
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

		// Make sure 2nd test doesn't get called if first returns true
		if (!this.actors.get(this.currentActor).actedThisSubturn()) if (this.actors.get(this.currentActor).subTurn()) return;
		++this.currentActor;
		this.nextActorIndex();
	}

	public void onSpeedChange(DungeonPokemon pokemon)
	{
		if (!this.actorMap.containsKey(pokemon)) return;
		this.actorMap.get(pokemon).onSpeedChange();
	}

	public void previousActor()
	{
		if (this.currentActor == -1) return;
		if (this.currentActor < this.actors.size()) this.actors.get(this.currentActor).cancelSubTurn();
		--this.currentActor;
	}

	public void registerActor(DungeonPokemon pokemon)
	{
		if (this.actorMap.containsKey(pokemon)) return;
		this.actorMap.put(pokemon, new Actor(pokemon));
		this.actors.add(this.actorMap.get(pokemon));
	}

	public void unregisterActor(DungeonPokemon pokemon)
	{
		if (!this.actorMap.containsKey(pokemon)) return;
		this.actors.remove(this.indexOf(pokemon));
		this.actorMap.remove(pokemon);
	}

}
