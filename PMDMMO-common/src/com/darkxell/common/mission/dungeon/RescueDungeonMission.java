package com.darkxell.common.mission.dungeon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.Event;
import com.darkxell.common.mission.DungeonMission;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.language.Message;

public class RescueDungeonMission extends DungeonMission {

    private DungeonPokemon rescueme;

    public RescueDungeonMission(Player owner, Mission missionData) {
        super(owner, missionData);
    }

    @Override
    public Message clearedMessage() {
        return new Message("mission.rescued.you").addReplacement("<pokemon>", this.rescueme.getNickname());
    }

    @Override
    protected boolean clearsMissionTF(Event event) {
        return false;
    }

    @Override
    protected void onTargetFloorStart(Floor floor, ArrayList<Event> events) {
        super.onTargetFloorStart(floor, events);

        this.rescueme = new DungeonPokemon(
                Registries.species().find(this.missionData.getTargetPokemon()).generate(floor.random, 1, 0));
        this.rescueme.type = DungeonPokemonType.RESCUEABLE;
        Tile spawn = floor.randomEmptyTile(true, true, TileType.GROUND, floor.random);
        floor.summonPokemon(this.rescueme, spawn.x, spawn.y, events);
    }

    public DungeonPokemon pokemonToRescue() {
        return this.rescueme;
    }

}
