package com.darkxell.common.trap;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.DungeonEvent.MessageEvent;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class WonderTileTrap extends Trap {

    public WonderTileTrap(int id) {
        super(id);
    }

    @Override
    public void onPokemonStep(Floor floor, DungeonPokemon pokemon, ArrayList<DungeonEvent> events) {
        for (Stat s : Stat.values())
            if (s != Stat.Speed) {
                int stage = pokemon.stats.getStage(s);
                if (stage < Stat.DEFAULT_STAGE)
                    pokemon.stats.setStage(s, Stat.DEFAULT_STAGE);
            }

        if (pokemon.stats.getMoveSpeed() < 1)
            pokemon.stats.resetSpeed();

        events.add(new MessageEvent(floor, new Message("stat.reset").addReplacement("<pokemon>", pokemon.getNickname()),
                pokemon.player()));
    }

}
