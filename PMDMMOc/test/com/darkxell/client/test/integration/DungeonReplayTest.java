package com.darkxell.client.test.integration;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.resources.Res;
import com.darkxell.client.state.dungeon.NextFloorState;
import com.darkxell.client.test.ClientSetup;
import com.darkxell.common.dungeon.AutoDungeonExploration;
import com.darkxell.common.dungeon.DungeonCommunication;
import com.darkxell.common.event.CommonEventProcessor.State;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.turns.GameTurn;
import com.darkxell.common.util.Communicable.JsonReadingException;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.Util;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

public class DungeonReplayTest {
    @BeforeAll
    public static void setUp() {
        ClientSetup.setUp();
    }

    private static final String REPLAYS_DIR = "replays";

    private void logAllEvents(AutoDungeonExploration dungeon) {
        for (GameTurn turn : dungeon.listTurns())
            for (Event event : turn.events())
                System.out.println(event.getClass().getSimpleName());
    }

    private void replayDungeon(String path) throws IOException, JsonReadingException {
        Persistence.player = Util.createDefaultPlayer();
        JsonValue replayData = Json.parse(new InputStreamReader(Res.get(path)));
        assertTrue("Replay data must be a JSON object", replayData.isObject());

        AutoDungeonExploration dungeon = DungeonCommunication.readExploration(replayData.asObject());
        assertNotNull("Dungeon at " + path + " should have been able to be created", dungeon);

        Persistence.dungeon = dungeon;

        dungeon.eventProcessor = new ClientEventProcessor(dungeon);
        dungeon.addPlayer(Persistence.player);
        ((ClientEventProcessor) dungeon.eventProcessor).setState(State.STOPPED);
        Persistence.floor = dungeon.initiateExploration();
        Persistence.stateManager.setState(new NextFloorState(null, 1));
        ((ClientEventProcessor) dungeon.eventProcessor).setState(State.PROCESSING);
    }

    @Test
    public void replayDungeon() throws IOException, JsonReadingException {
        String[] replays = Res.getResourceFiles(REPLAYS_DIR);
        assertTrue("There should be replays in the resource directory", replays.length > 0);
        for (String path : replays) {
            Logger.d("Replaying " + path);
            this.replayDungeon(path);
        }
    }
}
