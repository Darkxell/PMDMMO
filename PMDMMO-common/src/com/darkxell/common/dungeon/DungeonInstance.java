package com.darkxell.common.dungeon;

import java.util.ArrayList;
import java.util.Random;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.GameTurn;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Logger;

public class DungeonInstance
{

	/** The Pokémon to take turn in order. */
	private ArrayList<DungeonPokemon> actors = new ArrayList<DungeonPokemon>();
	/** The current Pokémon taking its turn. */
	private int currentActor;
	/** The current Floor. */
	private Floor currentFloor;
	/** The current turn. */
	private GameTurn currentTurn;
	/** ID of the Dungeon. */
	public final int id;
	/** Lists the previous turns. */
	private ArrayList<GameTurn> pastTurns = new ArrayList<GameTurn>();
	/** RNG for floor generation. */
	public final Random random;
	/** For each actor, whether it has taken its action. */
	private ArrayList<Boolean> wasActionTaken = new ArrayList<Boolean>();

	public DungeonInstance(int id, Random random)
	{
		this.id = id;
		this.random = random;
		this.currentFloor = this.createFloor(1);
		this.currentFloor.generate();
		this.endTurn();
		this.currentActor = -1;
	}

	private Floor createFloor(int floorID)
	{
		return new Floor(floorID, this.dungeon().getData(floorID).layout(), this, new Random(this.random.nextLong()));
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
		return this.currentFloor;
	}

	/** Ends the current Turn.
	 * 
	 * @return The Events created for the start of the new turn. */
	public ArrayList<DungeonEvent> endTurn()
	{
		// Checking for Pokémon who didn't act
		{
			for (int i = 0; i < this.actors.size(); ++i)
				if (!this.wasActionTaken.get(i)) Logger.e("Turn ended but " + this.actors.get(i) + " couldn't act!");
		}

		if (this.currentTurn != null) this.pastTurns.add(this.currentTurn);
		this.currentActor = -1;
		this.currentTurn = new GameTurn(this.currentFloor);
		for (int i = 0; i < this.wasActionTaken.size(); ++i)
			this.wasActionTaken.set(i, false);
		return this.currentFloor.onTurnStart();
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
		return this.actors.get(this.currentActor);
	}

	/** @return The last past turn. null if this is the first turn. */
	public GameTurn getLastTurn()
	{
		if (this.pastTurns.isEmpty()) return null;
		return this.pastTurns.get(this.pastTurns.size() - 1);
	}

	/** @return The next Pokémon to take its turn. null if there is no actor left, thus the turn is over. */
	public DungeonPokemon getNextActor()
	{
		for (int i = 0; i < this.actors.size(); ++i)
			if (!this.wasActionTaken.get(i)) return this.actors.get(i);
		return null;
	}

	public void insertActor(DungeonPokemon pokemon, int index)
	{
		if (this.actors.contains(pokemon)) return;
		this.actors.add(index, pokemon);
		this.wasActionTaken.add(index, true);
	}

	/** Proceeds to the next actor and returns it. */
	public DungeonPokemon nextActor()
	{
		DungeonPokemon p = this.getNextActor();
		if (p == null) this.currentActor = this.actors.size();
		else this.currentActor = this.actors.indexOf(p);
		return this.getActor();
	}

	public void registerActor(DungeonPokemon pokemon)
	{
		if (this.actors.contains(pokemon)) return;
		this.actors.add(pokemon);
		this.wasActionTaken.add(true);
	}

	public void resetAction(DungeonPokemon pokemon)
	{
		if (!this.actors.contains(pokemon)) return;
		this.wasActionTaken.set(this.actors.indexOf(pokemon), false);
	}

	public void takeAction(DungeonPokemon pokemon)
	{
		if (!this.actors.contains(pokemon)) return;
		this.wasActionTaken.set(this.actors.indexOf(pokemon), true);
	}

	public void unregisterActor(DungeonPokemon pokemon)
	{
		if (!this.actors.contains(pokemon)) return;
		this.wasActionTaken.remove(this.actors.indexOf(pokemon));
		this.actors.remove(pokemon);
	}

}
