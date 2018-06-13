package com.darkxell.common.test.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.common.dungeon.AutoDungeonInstance;
import com.darkxell.common.dungeon.DungeonCommunication;
import com.darkxell.common.event.CommonEventProcessor.State;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.GameTurn;
import com.darkxell.common.test.UTest;
import com.darkxell.common.util.Communicable.JsonReadingException;
import com.darkxell.common.util.Util;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

public class DungeonReplayTest extends UTest
{

	public static final String path = "resources/replays/dungeon-3-8529323484359270920.json";

	public DungeonReplayTest()
	{
		super(ACTION_WARN);
	}

	private AutoDungeonInstance createDungeon()
	{
		JsonValue json = null;
		AutoDungeonInstance dungeon = null;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			json = Json.parse(br);
		} catch (Exception e)
		{
			this.log(e.getMessage());
			return null;
		}

		if (!json.isObject())
		{
			this.log("Json data has invalid format.");
			return null;
		}

		try
		{
			dungeon = DungeonCommunication.readExploration(json.asObject());
		} catch (JsonReadingException e)
		{
			this.log(e.getMessage());
		}
		return dungeon;
	}

	public void logAllEvents(AutoDungeonInstance dungeon)
	{
		for (GameTurn turn : dungeon.listTurns())
			for (DungeonEvent event : turn.events())
				System.out.println(event.getClass().getSimpleName());
	}

	@Override
	protected int test()
	{
		Persistance.player = Util.createDefaultPlayer();
		AutoDungeonInstance dungeon = this.createDungeon();
		if (dungeon == null) return 1;
		Persistance.dungeon = dungeon;

		dungeon.eventProcessor = new ClientEventProcessor(dungeon);
		dungeon.addPlayer(Persistance.player);
		((ClientEventProcessor) dungeon.eventProcessor).setState(State.STOPPED);
		Persistance.floor = dungeon.initiateExploration();
		Persistance.stateManager.setState(new NextFloorState(null, 1));
		((ClientEventProcessor) dungeon.eventProcessor).setState(State.PROCESSING);

		return TEST_SUCCESSFUL;
	}

}
