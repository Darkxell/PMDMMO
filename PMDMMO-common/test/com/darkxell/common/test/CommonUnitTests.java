package com.darkxell.common.test;

import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.test.tests.DBObjecttransferTest;
import com.darkxell.common.test.tests.MissionCompressionTest;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Lang;

public class CommonUnitTests
{

	public static void executeTests()
	{
		Logger.loadServer();
		Lang.load();
		PokemonRegistry.load();
		DungeonRegistry.load();
		ItemRegistry.load();
		MoveRegistry.load();

		new DBObjecttransferTest().execute();
		// new DungeonEventTransferTest().execute();
		// new AutoDungeonTest().execute();
		new MissionCompressionTest().execute();
	}

	public static void main(String[] args)
	{
		executeTests();
	}

}
