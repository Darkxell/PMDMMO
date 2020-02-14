package com.darkxell.common.item.effects;

import static com.darkxell.common.item.effects.ItemEffectTest.EID;
import static com.darkxell.common.testutils.TestUtils.generateALL;
import static com.darkxell.common.testutils.TestUtils.getEventProcessor;
import static com.darkxell.common.testutils.TestUtils.getFloor;
import static com.darkxell.common.testutils.TestUtils.getLeftPokemon;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.pokemon.FaintedPokemonEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.model.item.ItemCategory;
import com.darkxell.common.registry.Registries;

public class ReviverItemEffectTest {

    @Before
    public void before() {
        generateALL();
        new ReviverFoodItemEffect(EID, 0, 0, 0);
        Registries.items().register(new Item(EID, ItemCategory.FOOD, 0, 0, EID, 0, false, false, null));
        getLeftPokemon().setItem(new ItemStack(EID));
    }

    @Test
    public void test() {
        getEventProcessor().processEvent(new FaintedPokemonEvent(getFloor(), null, getLeftPokemon(), null));
        Assert.assertFalse(getLeftPokemon().isFainted());
    }

}
