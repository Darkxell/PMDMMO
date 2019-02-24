package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.dungeon.MissionClearedEvent;
import com.darkxell.common.mission.DungeonMission;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable;
import com.darkxell.common.util.language.Message;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class PokemonRescuedEvent extends DungeonEvent implements Communicable {

    protected DungeonMission mission;
    protected DungeonPokemon rescued;
    protected Player rescuer;

    public PokemonRescuedEvent(Floor floor, DungeonPokemon rescued, Player rescuer) {
        super(floor, eventSource);
        this.rescued = rescued;
        this.rescuer = rescuer;
        this.mission = this.floor.dungeon.findRescueMission(this.floor, this.rescued);
    }

    @Override
    public String loggerMessage() {
        return this.rescued + " rescued";
    }

    @Override
    public ArrayList<DungeonEvent> processServer() {
        if (this.rescued.type != DungeonPokemonType.RESCUEABLE)
            return super.processServer();
        this.floor.unsummonPokemon(this.rescued);
        this.resultingEvents.add(new MessageEvent(this.floor,
                eventSource, new Message("mission.rescued").addReplacement("<pokemon>", this.rescued.getNickname())));
        this.resultingEvents.add(new MissionClearedEvent(this.floor, this.mission));
        return super.processServer();
    }

    @Override
    public void read(JsonObject value) throws JsonReadingException {
        if (value.get("player") == null)
            throw new JsonReadingException("No value for Player ID!");
        if (value.get("mission") == null)
            throw new JsonReadingException("No value for Mission ID!");
        if (!value.get("mission").isString())
            throw new JsonReadingException("Invalid value for mission: " + value.get("mission"));
        if (value.get("pokemon") == null)
            throw new JsonReadingException("No value for rescued Pokemon ID!");
        if (!value.get("pokemon").isNumber())
            throw new JsonReadingException("Invalid value for rescued Pokemon ID: " + value.get("pokemon"));
        try {
            int player = value.getInt("player", -1);
            for (Player p : this.floor.dungeon.exploringPlayers())
                if (p.getData().id == player)
                    this.rescuer = p;
        } catch (Exception e) {
            throw new JsonReadingException("Wrong value for Player ID: " + value.get("player"));
        }
        this.mission = this.floor.dungeon.findMission(value.getString("mission", null));
        if (this.mission == null)
            throw new JsonReadingException(
                    "Mission couldn't be find in active Dungeon Missions: " + value.get("mission"));
        Pokemon p = this.floor.dungeon.communication.pokemonIDs.get(value.getLong("pokemon", 0));
        if (p == null)
            throw new JsonReadingException("Pokemon couldn't be find in current Dungeon: " + value.get("pokemon"));
        this.rescued = p.getDungeonPokemon();
    }

    public DungeonPokemon rescued() {
        return this.rescued;
    }

    @Override
    public JsonObject toJson() {
        return Json.object().add("player", this.rescuer.getData().id)
                .add("mission", this.mission.missionData.toString()).add("pokemon", this.rescued.id());
    }

}
