package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.DungeonOutcome;
import com.darkxell.common.dungeon.DungeonOutcome.Outcome;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class PlayerLosesEvent extends DungeonEvent
{

	public final int moveID;
	public final Player player;

	public PlayerLosesEvent(Floor floor, Player player, int moveID)
	{
		super(floor);
		this.player = player;
		this.moveID = moveID;
	}

	@Override
	public String loggerMessage()
	{
		return this.player + " loses this Dungeon exploration.";
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.messages.add(new Message("player.loses").addReplacement("<player>", this.player.name()));

		ArrayList<DungeonPokemon> existing = this.floor.listPokemon();
		for (DungeonPokemon pokemon : this.player.getDungeonTeam())
			if (existing.contains(pokemon)) this.floor.unsummonPokemon(pokemon);
		if (this.floor.dungeon.removePlayer(this.player))
		{
			DungeonOutcome outcome = new DungeonOutcome(Outcome.KO, this.floor.dungeon.id, this.moveID);
			this.resultingEvents.add(new ExplorationStopEvent(this.floor, outcome));
		}
		return super.processServer();
	}

}
