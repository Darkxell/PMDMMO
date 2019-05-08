package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.darkxell.client.launchable.GameSocketEndpoint;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.launchable.StoryPositionSetup;
import com.darkxell.client.state.AbstractState;
import com.darkxell.client.state.PlayerLoadingState;
import com.darkxell.client.state.PlayerLoadingState.PlayerLoadingEndListener;
import com.darkxell.client.state.TransitionState;
import com.darkxell.client.state.freezone.cutscenes.MissionResultsState;
import com.darkxell.client.ui.Keys.Key;
import com.darkxell.common.dungeon.DungeonOutcome;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.mission.DungeonMission;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.pokemon.Pokemon;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class DungeonEndState extends AbstractState
{

	public static void finish()
	{
		TransitionState t = new TransitionState(Persistence.stateManager.getCurrentState(), null) {
			@Override
			public void onTransitionHalf()
			{
				super.onTransitionHalf();
				Persistence.stateManager.setState(new PlayerLoadingState(Persistence.player.getData().id, new PlayerLoadingEndListener() {
					@Override
					public void onPlayerLoadingEnd(PlayerLoadingState state)
					{
						StoryPositionSetup.trigger(Persistence.player.storyPosition(), false);
					}
				}));
			}
		};
		Persistence.stateManager.setState(t);
	}

	private ArrayList<Mission> completedMissions = new ArrayList<>();
	public final DungeonOutcome outcome;

	public DungeonEndState(DungeonOutcome outcome)
	{
		this.outcome = outcome;
	}

	public void onConfirmMessage(JsonObject message)
	{
		Persistence.isCommunicating = false;
		this.onFinish();
	}

	private void onFinish()
	{
		Persistence.player.resetDungeonTeam();
		if (this.completedMissions.isEmpty()) finish();
		else
		{
			Persistence.cutsceneState = new MissionResultsState(this.completedMissions);
			Persistence.cutsceneState.cutscene.creation.create();
			Persistence.stateManager.setState(Persistence.cutsceneState);
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

		for (DungeonMission m : Persistence.dungeon.activeMissions)
			if (m.isCleared() && m.owner == Persistence.player) this.completedMissions.add(m.missionData);

		if (Persistence.socketendpoint.connectionStatus() == GameSocketEndpoint.CONNECTED)
		{
			Persistence.isCommunicating = true;
			JsonObject json = Json.object();
			json.add("action", "dungeonend");
			json.add("outcome", this.outcome.toJson());
			json.add("success", this.outcome.isSuccess());
			json.add("player", Persistence.player.getData().toJson());
			json.add("inventory", Persistence.player.inventory().getData().toJson());

			JsonArray array = new JsonArray();
			for (Pokemon pokemon : Persistence.player.getTeam())
				array.add(pokemon.getData().toJson());
			json.add("team", array);

			array = new JsonArray();
			for (Pokemon pokemon : Persistence.player.getTeam())
				for (int m = 0; m < pokemon.moveCount(); ++m)
					array.add(pokemon.move(m).getData().toJson());
			json.add("moves", array);

			array = new JsonArray();
			for (Pokemon pokemon : Persistence.player.getTeam())
				if (pokemon.getItem() != null) array.add(pokemon.getItem().getData().toJson());
			for (ItemStack item : Persistence.player.inventory().items())
				array.add(item.getData().toJson());
			json.add("items", array);

			array = new JsonArray();
			for (Mission m : this.completedMissions)
				array.add(m.toString());
			json.add("completedmissions", array);

			Persistence.socketendpoint.sendMessage(json.toString());
		} else this.onFinish();
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	@Override
	public void update()
	{}

}
