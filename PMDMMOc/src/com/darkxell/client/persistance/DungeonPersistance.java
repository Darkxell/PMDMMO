package com.darkxell.client.persistance;

import java.util.Random;

import com.darkxell.client.state.dungeon.DungeonState;
import com.darkxell.common.dungeon.DungeonInstance;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.PokemonRegistry;

public class DungeonPersistance
{

	public static DungeonInstance dungeon;
	public static DungeonState dungeonState;
	public static Floor floor;
	public static Player player = new Player(0, PokemonRegistry.find(260).generate(new Random(), 1));

}
