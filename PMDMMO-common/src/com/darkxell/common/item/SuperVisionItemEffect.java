package com.darkxell.common.item;

import java.util.HashSet;

import com.darkxell.common.ai.visibility.Visibility.VisibleObjectType;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.pokemon.DungeonPokemon;

public class SuperVisionItemEffect extends ItemEffect {

    private HashSet<VisibleObjectType> supervision = new HashSet<>();

    public SuperVisionItemEffect(int id, VisibleObjectType... objects) {
        super(id);
        for (VisibleObjectType object : objects)
            this.supervision.add(object);
    }

    @Override
    public boolean hasSuperVision(Floor floor, DungeonPokemon pokemon, VisibleObjectType object) {
        return this.supervision.contains(object);
    }

}
