package com.darkxell.common.test.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.darkxell.common.dungeon.AutoDungeonExploration;
import com.darkxell.common.dungeon.DungeonCommunication;
import com.darkxell.common.event.CommonEventProcessor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.turns.GameTurn;
import com.darkxell.common.test.UTest;
import com.darkxell.common.util.Communicable.JsonReadingException;
import com.darkxell.common.util.Util;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class AutoDungeonTest extends UTest
{

	public static final String path = "resources/replays/dungeon-1-4705260968184275559.json";
	private JsonObject dungeonSummary;

	public AutoDungeonTest()
	{
		super(ACTION_WARN);
	}

	private AutoDungeonExploration createDungeon()
	{
		JsonValue json = null;
		AutoDungeonExploration dungeon = null;
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
		this.dungeonSummary = json.asObject();

		try
		{
			dungeon = DungeonCommunication.readExploration(this.dungeonSummary);
		} catch (JsonReadingException e)
		{
			this.log(e.getMessage());
		}
		return dungeon;
	}

	public void logAllEvents(AutoDungeonExploration dungeon)
	{
		for (GameTurn turn : dungeon.listTurns())
			for (DungeonEvent event : turn.events())
				System.out.println(event.getClass().getSimpleName());
	}

	@Override
	protected int test()
	{
		AutoDungeonExploration dungeon = this.createDungeon();
		if (dungeon == null) return 1;

		dungeon.eventProcessor = new CommonEventProcessor(dungeon);
		dungeon.addPlayer(Util.createDefaultPlayer());

		long start = System.nanoTime();
		dungeon.initiateExploration();
		long duration = System.nanoTime() - start;
		// this.logAllEvents(dungeon);

		int eventCount = 0;
		for (GameTurn turn : dungeon.listTurns())
			eventCount += turn.events().length;
		System.out.println("Exploration duration: " + (duration * 1d / 1000000) + "ms, for " + eventCount + " events.");

		JsonObject summary = dungeon.communication.explorationSummary(true);
		if (!summary.equals(this.dungeonSummary))
		{
			this.log("Explorations are different!");
			this.log(this.dungeonSummary.toString());
			this.log(summary.toString());
			return 2;
		}

		return TEST_SUCCESSFUL;
	}

}
