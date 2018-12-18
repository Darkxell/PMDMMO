package com.darkxell.client.launchable.messagehandlers;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.PlayerLoadingState;
import com.darkxell.client.state.menu.freezone.FriendSelectionState;
import com.darkxell.common.dbobject.DBItemstack;
import com.darkxell.common.dbobject.DBLearnedmove;
import com.darkxell.common.dbobject.DBPokemon;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class MonsterRequestHandler extends MessageHandler
{

	public static Pokemon readMonster(JsonObject message)
	{
		DBPokemon dbp = new DBPokemon();
		dbp.read(message.get("object").asObject());
		Pokemon p = new Pokemon(dbp);

		if (message.get("item") != null && message.get("item").isObject())
		{
			DBItemstack item = new DBItemstack();
			item.read(message.get("item").asObject());
			p.setItem(new ItemStack(item));
		}

		if (message.get("moves") != null && message.get("moves").isArray())
		{
			int index = 0;
			for (JsonValue mv : message.get("moves").asArray())
			{
				if (mv.isObject())
				{
					DBLearnedmove move = new DBLearnedmove();
					move.read(mv.asObject());
					p.setMove(index, new LearnedMove(move));
				}
				++index;
				if (index >= 4) break;
			}
		}

		return p;
	}

	@Override
	public void handleMessage(JsonObject message)
	{
		AbstractState state = Persistence.stateManager.getCurrentState();
		if (state != null)
		{
			if (state instanceof PlayerLoadingState) ((PlayerLoadingState) state).onMonsterReceived(message);
			else if (state instanceof FriendSelectionState) ((FriendSelectionState) state).onPokemonReceived(message);
		}
	}

}
