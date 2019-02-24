package com.darkxell.common.item.effects;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.FaintedPokemonEvent;
import com.darkxell.common.event.pokemon.RevivedPokemonEvent;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Inventory;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.DungeonPokemon;

/** An Item that restores belly when eaten, and inflicts negative status effects. */
public class ReviverFoodItemEffect extends FoodItemEffect {

    public ReviverFoodItemEffect(int id, int food, int bellyIfFull, int belly) {
        super(id, food, bellyIfFull, belly);
    }

    @Override
    public boolean isUsedOnTeamMember() {
        return true;
    }

    @Override
    public void onPreEvent(Floor floor, DungeonEvent event, DungeonPokemon concerned, ArrayList<DungeonEvent> resultingEvents, ItemStack item, ItemContainer container, int containerIndex) {
        super.onPreEvent(floor, event, concerned, resultingEvents, item, container, containerIndex);

        boolean shouldRevive = event instanceof FaintedPokemonEvent;
        if (shouldRevive) {
            DungeonPokemon fainted = ((FaintedPokemonEvent) event).pokemon;
            if (container instanceof DungeonPokemon) shouldRevive &= fainted == container;
            else if (container instanceof Inventory) shouldRevive &= ((Inventory) container).owner.isAlly(fainted);
            else shouldRevive = false;
            if (shouldRevive) {
                event.consume();
                container.deleteItem(containerIndex);
                resultingEvents.add(new RevivedPokemonEvent(floor, event, ((FaintedPokemonEvent) event).pokemon));
            }
        }
    }

}
