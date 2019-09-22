package com.darkxell.common.item.effects;

import static com.darkxell.common.item.effects.ItemEffectTest.EID;
import static com.darkxell.common.testutils.TestUtils.generateALL;
import static com.darkxell.common.testutils.TestUtils.getEventProcessor;
import static com.darkxell.common.testutils.TestUtils.getFloor;
import static com.darkxell.common.testutils.TestUtils.getLeftPokemon;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.Item.ItemCategory;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;

public class PreventStatusEquipableItemEffectTest {

    @Before
    public void before() {
        generateALL();
        new PreventStatusEquipableItemEffect(EID, StatusConditions.Poisoned);
        Registries.items().register(new Item(EID, ItemCategory.EQUIPABLE, 0, 0, EID, 0, false, false));
        getLeftPokemon().setItem(new ItemStack(EID));
    }

    @Test
    public void test() {
        AppliedStatusCondition condition = new AppliedStatusCondition(StatusConditions.Poisoned, getLeftPokemon(), null,
                3);
        getEventProcessor().processEvent(new StatusConditionCreatedEvent(getFloor(), null, condition));
        Assert.assertFalse(getLeftPokemon().hasStatusCondition(StatusConditions.Poisoned));
    }

}
