package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class PlayerLosesEvent extends DungeonEvent
{

	public final Player player;

	public PlayerLosesEvent(Floor floor, Player player)
	{
		super(floor);
		this.player = player;
		this.messages.add(new Message("player.loses").addReplacement("<player>", this.player.name()));
	}

	@Override
	public String loggerMessage()
	{
		return this.player + " loses this Dungeon exploration.";
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		ArrayList<DungeonPokemon> existing = this.floor.listPokemon();
		for (DungeonPokemon pokemon : this.player.getDungeonTeam())
			if (existing.contains(pokemon)) this.floor.unsummonPokemon(pokemon);
		if (this.floor.dungeon.removePlayer(this.player)) this.resultingEvents.add(new ExplorationStopEvent(this.floor));
		return super.processServer();
	}

}
