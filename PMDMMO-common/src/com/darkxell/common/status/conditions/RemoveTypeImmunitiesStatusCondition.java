package com.darkxell.common.status.conditions;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.move.MoveContext;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.language.Message;

public class RemoveTypeImmunitiesStatusCondition extends StatusCondition {

    public final PokemonType type;

    public RemoveTypeImmunitiesStatusCondition(int id, boolean isAilment, int durationMin, int durationMax,
            PokemonType type) {
        super(id, isAilment, durationMin, durationMax);
        this.type = type;
    }

    @Override
    public Pair<Boolean, Message> affects(Floor floor, AppliedStatusCondition condition, DungeonPokemon pokemon) {
        if (!pokemon.species().isType(this.type))
            return new Pair<>(false, new Message("status.immune.isnttype")
                    .addReplacement("<pokemon>", pokemon.getNickname()).addReplacement("<type>", this.type.getName()));
        return super.affects(floor, condition, pokemon);
    }

    @Override
    public double applyEffectivenessModifications(double effectiveness, MoveContext context, boolean isUser) {
        if (context.target.hasStatusCondition(this) && effectiveness == PokemonType.NO_EFFECT) {
            PokemonType other = null;
            if (context.target.species().getType1() != this.type)
                other = context.target.species().getType1();
            else if (context.target.species().getType2() != this.type)
                other = context.target.species().getType2();

            if (other != null)
                effectiveness = context.move.getType(context.user.usedPokemon).effectivenessOn(other);
            else
                effectiveness = PokemonType.NORMALLY_EFFECTIVE;
        }
        return super.applyEffectivenessModifications(effectiveness, context, isUser);
    }

}
