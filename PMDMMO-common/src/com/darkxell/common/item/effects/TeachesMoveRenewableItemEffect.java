package com.darkxell.common.item.effects;

import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemEffect;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.util.language.Message;

/** An Item that teaches a move to a Pokemon when used. */
public class TeachesMoveRenewableItemEffect extends ItemEffect {

    /** The move this TM teaches. */
    public final int moveID;

    public TeachesMoveRenewableItemEffect(int id, int moveID) {
        super(id);
        this.moveID = moveID;
    }

    @Override
    public Message description(Item item) {
        return new Message("item.info.hm").addReplacement("<move>", this.move().name());
    }

    @Override
    public boolean isConsummable() {
        return false;
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public boolean isUsableOnCatch() {
        return false;
    }

    @Override
    public boolean isUsedOnTeamMember() {
        return true;
    }

    /** @return The Move this TM teaches. */
    public Move move() {
        return Registries.moves().find(this.moveID);
    }

    @Override
    public Message name(Item item) {
        if (this.move() == null || this.move().type == PokemonType.Unknown)
            return new Message("move." + this.moveID).addPrefix("<tm" + 0 + ">");
        return new Message("move." + this.moveID).addPrefix("<tm" + this.move().type.id + ">");
    }

}
