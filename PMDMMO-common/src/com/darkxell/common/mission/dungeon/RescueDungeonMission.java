package com.darkxell.common.mission.dungeon;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.mission.DungeonMission;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.pokemon.PokemonRegistry;

public class RescueDungeonMission extends DungeonMission
{

	public RescueDungeonMission(Player owner, Mission missionData)
	{
		super(owner, missionData);
	}

	@Override
	protected boolean clearsMissionTF(DungeonEvent event)
	{
		return false;
	}

	@Override
	protected void onTargetFloorStart(Floor floor)
	{
		super.onTargetFloorStart(floor);

		DungeonPokemon rescueme = new DungeonPokemon(PokemonRegistry.find(this.missionData.getPokemonid1()).generate(floor.random, 1));
		rescueme.type = DungeonPokemonType.RESCUEABLE;
		floor.randomEmptyTile(true, true, TileType.GROUND, floor.random).setPokemon(rescueme);
	}

}
