package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.PlayerLoadingState;
import com.darkxell.client.state.PlayerLoadingState.PlayerLoadingEndListener;
import com.darkxell.client.state.StateManager;
import com.darkxell.common.dungeon.DungeonOutcome;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.zones.FreezoneInfo;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class DungeonEndState extends AbstractState
{

	public final DungeonOutcome outcome;

	public DungeonEndState(DungeonOutcome outcome)
	{
		this.outcome = outcome;
	}

	public void onConfirmMessage(JsonObject message)
	{
		Persistance.isCommunicating = false;
		Persistance.stateManager.setState(new PlayerLoadingState(Persistance.player.getData().id, new PlayerLoadingEndListener() {
			@Override
			public void onPlayerLoadingEnd(PlayerLoadingState state)
			{
				Persistance.player.resetDungeonTeam();
				StateManager.setExploreState(FreezoneInfo.BASE, -1, -1);
			}
		}));
	}

	@Override
	public void onKeyPressed(short key)
	{}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void onStart()
	{
		super.onStart();

		if (Persistance.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED)
		{
			Persistance.isCommunicating = true;
			JsonObject json = Json.object();
			json.add("action", "dungeonend");
			json.add("outcome", this.outcome.toJson());
			json.add("player", Persistance.player.getData().toJson());
			json.add("inventory", Persistance.player.inventory().getData().toJson());

			JsonArray array = new JsonArray();
			for (Pokemon pokemon : Persistance.player.getTeam())
				array.add(pokemon.getData().toJson());
			json.add("team", array);

			array = new JsonArray();
			for (Pokemon pokemon : Persistance.player.getTeam())
				for (int m = 0; m < pokemon.moveCount(); ++m)
					array.add(pokemon.move(m).getData().toJson());
			json.add("moves", array);

			array = new JsonArray();
			for (Pokemon pokemon : Persistance.player.getTeam())
				if (pokemon.getItem() != null) array.add(pokemon.getItem().getData().toJson());
			for (ItemStack item : Persistance.player.inventory().items())
				array.add(item.getData().toJson());
			json.add("items", array);

			Persistance.socketendpoint.sendMessage(json.toString());
		} else
		{
			Persistance.player.resetDungeonTeam();
			StateManager.setExploreState(FreezoneInfo.BASE, -1, -1);
		}
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	@Override
	public void update()
	{}

}
