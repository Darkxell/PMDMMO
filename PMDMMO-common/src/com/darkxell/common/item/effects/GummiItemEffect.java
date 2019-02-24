package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.item.ItemUseEvent;
import com.darkxell.common.event.pokemon.IncreasedIQEvent;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;

/** A Gummi restores belly, increases stats and IQ, depending on the Pokemon's type. */
public class GummiItemEffect extends FoodItemEffect {

    /** The type of the gummy. */
    public final PokemonType type;

    public GummiItemEffect(int id, int food, int belly, int bellyIfFull, PokemonType type) {
        super(id, food, belly, bellyIfFull);
        this.type = type;
    }

    private int iqIncrease(DungeonPokemon pokemon) {
        return iqIncrease(pokemon.species().type1) + iqIncrease(pokemon.species().type2);
    }

    private int iqIncrease(PokemonType type) {
        if (type == null)
            return 0;
        if (type == this.type)
            return 7;
        double effectiveness = this.type.effectivenessOn(type);
        if (effectiveness == PokemonType.NO_EFFECT)
            return 1;
        if (effectiveness == PokemonType.NOT_VERY_EFFECTIVE)
            return 2;
        if (effectiveness == PokemonType.SUPER_EFFECTIVE)
            return 4;
        return 3;
    }

    @Override
    public boolean isUsedOnTeamMember() {
        return true;
    }

    @Override
    public void use(ItemUseEvent itemEvent, ArrayList<DungeonEvent> events) {
        events.add(new IncreasedIQEvent(itemEvent, eventSource, pokemon, this.iqIncrease(target)));
    }

}
