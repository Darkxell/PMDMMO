package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.PlayerLoadingState;
import com.darkxell.client.state.PlayerLoadingState.PlayerLoadingEndListener;
import com.darkxell.client.state.StateManager;
import com.darkxell.client.state.freezone.cutscenes.MissionResultsState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.dungeon.DungeonOutcome;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.mission.DungeonMission;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Direction;
import com.darkxell.common.zones.FreezoneInfo;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class DungeonEndState extends AbstractState
{

	public static void finish()
	{
		Persistance.stateManager.setState(new PlayerLoadingState(Persistance.player.getData().id, new PlayerLoadingEndListener() {
			@Override
			public void onPlayerLoadingEnd(PlayerLoadingState state)
			{
				StateManager.setExploreState(FreezoneInfo.BASE, Direction.SOUTH, -1, -1);
			}
		}));
	}

	private ArrayList<Mission> completedMissions = new ArrayList<>();
	public final DungeonOutcome outcome;

	public DungeonEndState(DungeonOutcome outcome)
	{
		this.outcome = outcome;
	}

	public void onConfirmMessage(JsonObject message)
	{
		Persistance.isCommunicating = false;
		this.onFinish();
	}

	private void onFinish()
	{
		Persistance.player.resetDungeonTeam();
		if (this.completedMissions.isEmpty()) finish();
		else
		{
			Persistance.cutsceneState = new MissionResultsState(this.completedMissions);
			Persistance.cutsceneState.cutscene.creation.create();
			Persistance.stateManager.setState(Persistance.cutsceneState);
		}
	}

	@Override
	public void onKeyPressed(Key key)
	{}

	@Override
	public void onKeyReleased(Key key)
	{}

	@Override
	public void onStart()
	{
		super.onStart();

		for (DungeonMission m : Persistance.dungeon.activeMissions)
			if (m.isCleared() && m.owner == Persistance.player) this.completedMissions.add(m.missionData);

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

			array = new JsonArray();
			for (Mission m : this.completedMissions)
				array.add(m.toString());
			json.add("completedmissions", array);

			Persistance.socketendpoint.sendMessage(json.toString());
		} else this.onFinish();
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	@Override
	public void update()
	{}

}
