package com.darkxell.common.dungeon;

import java.util.ArrayList;
import java.util.Random;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.layout.Layout;
import com.darkxell.common.dungeon.floor.layout.StaticLayout;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.GameTurn;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Logger;

public class DungeonInstance
{

	/** For each actor, the number of actions left it can take. */
	private ArrayList<Float> actionsLeft = new ArrayList<>();
	/** The Pok�mon to take turn in order. */
	private ArrayList<DungeonPokemon> actors = new ArrayList<DungeonPokemon>();
	/** The current Pok�mon taking its turn. */
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

	public DungeonInstance(int id, Random random)
	{
		this.id = id;
		this.random = random;
		this.currentFloor = this.createFloor(1);
		this.currentFloor.generate();
		this.endTurn();
		this.currentActor = -1;
	}

	/** Compares the input Pok�mon depending on their order of action. */
	public int compare(DungeonPokemon p1, DungeonPokemon p2)
	{
		return Integer.compare(this.actors.indexOf(p1), this.actors.indexOf(p2));
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
		return this.currentFloor;
	}

	/** Ends the current Turn.
	 * 
	 * @return The Events created for the start of the new turn. */
	public ArrayList<DungeonEvent> endTurn()
	{
		// Logger.d("Turn ended");
		// Checking for Pok�mon who didn't act
		{
			for (int i = 0; i < this.actors.size(); ++i)
				if (this.actionsLeft.get(i) >= 1) Logger.e("Turn ended but " + this.actors.get(i) + " had " + this.actionsLeft.get(i) + " actions left!");
		}

		if (this.currentTurn != null) this.pastTurns.add(this.currentTurn);
		this.currentActor = -1;
		this.currentTurn = new GameTurn(this.currentFloor);
		for (int i = 0; i < this.actionsLeft.size(); ++i)
			this.actionsLeft.set(i, this.actionsLeft.get(i) + this.actors.get(i).stats.getMoveSpeed());
		return this.currentFloor.onTurnStart();
	}

	/** Called when the input event is processed. */
	public void eventOccured(DungeonEvent event)
	{
		this.currentTurn.addEvent(event);
	}

	/** @return The Pok�mon taking its turn. null if there is no actor left, thus the turn is over. */
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

	/** @return The next Pok�mon to take its turn. null if there is no actor left, thus the turn is over. */
	public DungeonPokemon getNextActor()
	{
		for (int i = 0; i < this.actors.size(); ++i)
			if (this.actionsLeft.get(i) >= 1) return this.actors.get(i);
		return null;
	}

	public void insertActor(DungeonPokemon pokemon, int index)
	{
		if (this.actors.contains(pokemon)) return;
		this.actors.add(index, pokemon);
		this.actionsLeft.add(index, pokemon.stats.getMoveSpeed());
	}

	/** Proceeds to the next actor and returns it. */
	public DungeonPokemon nextActor()
	{
		DungeonPokemon p = this.getNextActor();
		if (p == null) this.currentActor = this.actors.size();
		else this.currentActor = this.actors.indexOf(p);
		return this.getActor();
	}

	public void onSpeedChange(DungeonPokemon pokemon, int stage)
	{
		if (!this.actors.contains(pokemon)) return;
		int index = this.actors.indexOf(pokemon);
		this.actionsLeft.set(index, Math.max(Math.min(this.actionsLeft.get(index), 1), this.actionsLeft.get(index) + stage));
	}

	public void registerActor(DungeonPokemon pokemon)
	{
		if (this.actors.contains(pokemon)) return;
		this.actors.add(pokemon);
		this.actionsLeft.add(pokemon.stats.getMoveSpeed());
	}

	public void resetAction(DungeonPokemon pokemon)
	{
		if (!this.actors.contains(pokemon)) return;
		this.actionsLeft.set(this.actors.indexOf(pokemon), pokemon.stats.getMoveSpeed());
	}

	public void takeAction(DungeonPokemon pokemon)
	{
		if (!this.actors.contains(pokemon)) return;
		int index = this.actors.indexOf(pokemon);
		this.actionsLeft.set(index, this.actionsLeft.get(index) - 1);
	}

	public void unregisterActor(DungeonPokemon pokemon)
	{
		if (!this.actors.contains(pokemon)) return;
		this.actionsLeft.remove(this.actors.indexOf(pokemon));
		this.actors.remove(pokemon);
	}

}
