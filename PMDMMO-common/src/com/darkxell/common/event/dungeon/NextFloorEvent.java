package com.darkxell.common.event.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.EventSource;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class NextFloorEvent extends Event implements Communicable {

    protected Player player;

    public NextFloorEvent(Floor floor, EventSource eventSource) {
        super(floor, eventSource);
    }

    public NextFloorEvent(Floor floor, EventSource eventSource, Player player) {
        super(floor, eventSource);
        this.player = player;
    }

    @Override
    public String loggerMessage() {
        return this.player.name() + " went to the next floor.";
    }

    @Override
    public boolean isValid() {
        for (DungeonPokemon p : this.player.getDungeonTeam())
            if (!p.isFainted() && p.tile().type() == TileType.STAIR)
                return true;
        return false;
    }

    public Player player() {
        return this.player;
    }

    @Override
    public ArrayList<Event> processServer() {
        this.floor.dungeon.endFloor();
        this.floor.dungeon.currentFloor().onFloorStart(this.resultingEvents);
        return super.processServer();
    }

    @Override
    public void read(JsonObject value) throws JsonReadingException {
        if (value.get("player") == null)
            throw new JsonReadingException("No value for Player ID!");
        try {
            int player = value.getInt("player", -1);
            for (Player p : this.floor.dungeon.exploringPlayers())
                if (p.getData().id == player)
                    this.player = p;
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for Player ID: " + value.get("player"));
        }
    }

    @Override
    public JsonObject toJson() {
        return Json.object().add("player", this.player.getData().id);
    }

}
