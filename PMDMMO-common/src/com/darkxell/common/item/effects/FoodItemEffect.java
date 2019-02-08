package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.BellyChangedEvent;
import com.darkxell.common.event.stats.BellySizeChangedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemEffect;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

/** An Item that restores belly when eaten. */
public class FoodItemEffect extends ItemEffect {

    /** The increase of belly size given by this Food when eaten. */
    public final int belly;
    /** The increase of belly size given by this Food when eaten while the belly is full. */
    public final int bellyIfFull;
    /** The amount of food given by this Food when eaten. */
    public final int food;

    public FoodItemEffect(int id, int food, int belly, int bellyIfFull) {
        super(id);
        this.food = food;
        this.belly = belly;
        this.bellyIfFull = bellyIfFull;
    }

    @Override
    protected String getUseEffectID() {
        return "item.eaten";
    }

    @Override
    public Message getUseName() {
        return new Message("item.eat");
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public boolean isUsedOnTeamMember() {
        return true;
    }

    @Override
    public void use(Floor floor, Item item, DungeonPokemon pokemon, DungeonPokemon target,
            ArrayList<DungeonEvent> events) {
        int increase = this.belly;
        if (target.getBelly() < target.getBellySize())
            events.add(new BellyChangedEvent(floor, target, this.food));
        else
            increase += this.bellyIfFull;

        if (increase != 0)
            events.add(new BellySizeChangedEvent(floor, target, increase));
    }

}
