package com.darkxell.common.test;

import com.darkxell.common.dungeon.DungeonRegistry;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.PokemonRegistry;
import com.darkxell.common.test.tests.AutoDungeonTest;
import com.darkxell.common.test.tests.DBObjecttransferTest;
import com.darkxell.common.test.tests.DungeonEventTransferTest;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Lang;

public class CommonUnitTests
{

	public static void main(String[] args)
	{
		Logger.loadServer();
		Lang.load();
		PokemonRegistry.load();
		DungeonRegistry.load();
		ItemRegistry.load();
		MoveRegistry.load();
		
		new DBObjecttransferTest().execute();
		new DungeonEventTransferTest().execute();
		new AutoDungeonTest().execute();
	}

}
