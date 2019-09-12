package com.darkxell.common.event.item;

import static com.darkxell.common.testutils.TestUtils.generateALL;
import static com.darkxell.common.testutils.TestUtils.getFloor;
import static com.darkxell.common.testutils.TestUtils.getLeftPokemon;
import static com.darkxell.common.testutils.TestUtils.getPlayer;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.Event;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.testutils.AssertUtils;

public class ItemCreatedEventTest {

    private ItemStack item, item2;

    @Before
    public void before() {
        generateALL();
        getLeftPokemon().tile().setType(TileType.GROUND);
        this.item = new ItemStack(1);
        this.item2 = new ItemStack(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testItemCreatedEventThrowsExceptionWhenUsingPokemon() {
        new ItemCreatedEvent(getFloor(), null, this.item, getLeftPokemon().originalPokemon);
    }

    @Test
    public void testItemCreatedInInventoryEvent() {
        ItemCreatedEvent event = new ItemCreatedEvent(getFloor(), null, this.item, getPlayer().inventory());
        event.processServer();

        boolean flag = false;
        for (ItemStack item : getPlayer().inventory().list())
            if (item == this.item) {
                flag = true;
                break;
            }
        Assert.assertTrue("The Item wasn't inserted in the inventory.", flag);
    }

    @Test
    public void testItemCreatedOnFullPokemonEvent() {
        getLeftPokemon().setItem(this.item2);
        ItemCreatedEvent event = new ItemCreatedEvent(getFloor(), null, this.item, getLeftPokemon());
        ArrayList<Event> result = event.processServer();

        Assert.assertEquals("The new item replaced the old item.", getLeftPokemon().getItem(), this.item2);
        Assert.assertEquals("Unexpected number of resulting events.", result.size(), 1);
        Assert.assertTrue("No ItemLandedEvent created.",
                AssertUtils.containsObjectOfClass(result, ItemLandedEvent.class));
        Assert.assertEquals("Item lands on wrong Tile.", getLeftPokemon().tile(),
                AssertUtils.getObjectOfClass(result, ItemLandedEvent.class).tile);
    }

    @Test
    public void testItemCreatedOnFullTileEvent() {
        getLeftPokemon().tile().setItem(this.item2);
        ItemCreatedEvent event = new ItemCreatedEvent(getFloor(), null, this.item, getLeftPokemon().tile());
        ArrayList<Event> result = event.processServer();

        Assert.assertEquals("The new item replaced the old item.", getLeftPokemon().tile().getItem(), this.item2);
        Assert.assertEquals("Unexpected number of resulting events.", result.size(), 1);
        Assert.assertTrue("No ItemLandedEvent created.",
                AssertUtils.containsObjectOfClass(result, ItemLandedEvent.class));
        Assert.assertEquals("Item lands on wrong Tile.", getLeftPokemon().tile(),
                AssertUtils.getObjectOfClass(result, ItemLandedEvent.class).tile);
    }

    @Test
    public void testItemCreatedOnInventoryTileEvent() {
        while (getPlayer().inventory().canAccept(this.item) != -1)
            getPlayer().inventory().addItem(new ItemStack(3));
        ItemCreatedEvent event = new ItemCreatedEvent(getFloor(), null, this.item, getPlayer().inventory());
        ArrayList<Event> result = event.processServer();

        boolean flag = false;
        for (ItemStack item : getPlayer().inventory().items())
            if (item.equals(this.item))
                flag = true;
        Assert.assertFalse("The new item replaced an old item.", flag);
        Assert.assertEquals("Unexpected number of resulting events.", result.size(), 1);
        Assert.assertTrue("No ItemLandedEvent created.",
                AssertUtils.containsObjectOfClass(result, ItemLandedEvent.class));
        Assert.assertEquals("Item lands on wrong Tile.", getPlayer().getDungeonLeader().tile(),
                AssertUtils.getObjectOfClass(result, ItemLandedEvent.class).tile);
    }

    @Test
    public void testItemCreatedOnPokemonEvent() {
        ItemCreatedEvent event = new ItemCreatedEvent(getFloor(), null, this.item, getLeftPokemon());
        event.processServer();

        Assert.assertEquals("The Pokemon didn't get the Item correctly.", getLeftPokemon().getItem(), this.item);
    }

    @Test
    public void testItemCreatedOnTileEvent() {
        ItemCreatedEvent event = new ItemCreatedEvent(getFloor(), null, this.item, getLeftPokemon().tile());
        event.processServer();

        Assert.assertEquals("The Tile didn't get the Item correctly.", getLeftPokemon().tile().getItem(), this.item);
    }

}
