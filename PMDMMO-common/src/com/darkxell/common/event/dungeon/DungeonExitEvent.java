package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.DungeonOutcome;
import com.darkxell.common.dungeon.DungeonOutcome.Outcome;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.DungeonEventSource;
import com.darkxell.common.player.Player;
import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class DungeonExitEvent extends Event implements Communicable {

    protected Player player;

    public DungeonExitEvent(Floor floor, DungeonEventSource eventSource, Player player) {
        super(floor, eventSource);
        this.player = player;
    }

    @Override
    public String loggerMessage() {
        return this.player.name() + " exited the Dungeon.";
    }

    public Player player() {
        return this.player;
    }

    @Override
    public ArrayList<Event> processServer() {
        DungeonOutcome outcome = new DungeonOutcome(Outcome.DUNGEON_CLEARED, this.floor.dungeon.id);
        this.resultingEvents.add(new ExplorationStopEvent(this.floor, this, outcome));
        return this.resultingEvents;
    }

    @Override
    public void read(JsonObject value) throws JsonReadingException {
        if (value.get("player") == null) throw new JsonReadingException("No value for Player ID!");
        try {
            int player = value.getInt("player", -1);
            for (Player p : this.floor.dungeon.exploringPlayers())
                if (p.getData().id == player) this.player = p;
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for Player ID: " + value.get("player"));
        }
    }

    @Override
    public JsonObject toJson() {
        return Json.object().add("player", this.player.getData().id);
    }

}
