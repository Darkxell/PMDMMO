package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.menu.freezone.DungeonSelectionMapState;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.JsonObject;

public class DungeonStartConfirmHandler extends MessageHandler
{

	@Override
	public void handleMessage(JsonObject message)
	{
		int dungeon = -1;
		long seed = 0;

		if (message.get("dungeon") == null) Logger.e("Message is missing dungeon ID!");
		else try
		{
			dungeon = message.getInt("dungeon", -1);
		} catch (Exception e)
		{
			Logger.e("Message has invalid dungeon ID! " + message.get("dungeon"));
		}

		if (message.get("seed") == null) Logger.e("Message is missing seed!");
		else try
		{
			seed = message.getLong("seed", -1);
		} catch (Exception e)
		{
			Logger.e("Message has invalid seed! " + message.get("seed"));
		}

		AbstractState state = Persistance.stateManager.getCurrentState();
		if (state != null && state instanceof DungeonSelectionMapState) ((DungeonSelectionMapState) state).onDungeonStart(dungeon, seed);
	}

}
