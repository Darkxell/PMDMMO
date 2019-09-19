package com.darkxell.common.event.item;

import static com.darkxell.common.testutils.TestUtils.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.testutils.AssertUtils;
import com.darkxell.common.util.Direction;

public class ItemEventTest {

    private ItemStack moneyItem, item;

    @Before
    public void before() {
        generateALL();
        this.item = new ItemStack(1);
        this.moneyItem = new ItemStack(Item.POKEDOLLARS, 10);
    }

    @Test
    public void testItemMovedEvent() {
        getLeftPokemon().tile().setItem(this.item);
        getLeader().setItem(null);
        ItemMovedEvent event = new ItemMovedEvent(getFloor(), null, ItemAction.GET, getLeftPokemon(),
                getLeftPokemon().tile(), 0, getLeftPokemon(), 0, true);
        event.processServer();

        Assert.assertNull("Item is still on the Tile.", getLeftPokemon().tile().getItem());
        Assert.assertEquals("Item wasn't given to the Pokemon.", this.item, getLeftPokemon().getItem());
    }

    @Test
    public void testItemSelectionEvent() {
        getLeftPokemon().tile().setItem(this.item);
        ItemSelectionEvent event = new ItemSelectionEvent(getFloor(), null, this.item.item(), getLeftPokemon(),
                getLeftPokemon(), getLeftPokemon().tile(), 0, Direction.NORTH, true);
        ArrayList<Event> result = event.processServer();

        Assert.assertNull("The Item wasn't consumed.", getLeftPokemon().tile().getItem());
        Assert.assertEquals("Unexpected number of resulting events.", 1, result.size());
        Assert.assertTrue("Created Event isn't of expected class.",
                AssertUtils.containsObjectOfClass(result, ItemUseEvent.class));

        ItemUseEvent e = AssertUtils.getObjectOfClass(result, ItemUseEvent.class);
        Assert.assertEquals(e.item, this.item.item());
        Assert.assertEquals(e.user, event.user);
        Assert.assertEquals(e.target, event.target);
    }

    @Test
    public void testItemSwappedEvent() {
        getLeftPokemon().tile().setItem(this.item);
        getLeader().setItem(this.moneyItem);
        ItemSwappedEvent event = new ItemSwappedEvent(getFloor(), null, ItemAction.SWAP, getLeftPokemon(),
                getLeftPokemon().tile(), 0, getLeftPokemon(), 0);
        event.processServer();

        Assert.assertEquals("Items weren't properly swapped.", this.moneyItem, getLeftPokemon().tile().getItem());
        Assert.assertEquals("Items weren't properly swapped.", this.item, getLeftPokemon().getItem());
    }

    @Test
    public void testItemThrownToWallEvent() {
        getLeftPokemon().setItem(this.item);
        getLeftPokemon().setFacing(Direction.SOUTH);
        getLeftPokemon().tile().adjacentTile(Direction.SOUTH).adjacentTile(Direction.SOUTH).setType(TileType.WALL);
        ItemThrownEvent event = new ItemThrownEvent(getFloor(), null, getLeftPokemon(), getLeftPokemon(), 0);
        ArrayList<Event> result = event.processServer();

        Assert.assertNull("The Item wasn't consumed.", getLeftPokemon().getItem());
        Assert.assertEquals("Unexpected number of resulting events.", 1, result.size());
        Assert.assertTrue("Created Event isn't of expected class.",
                AssertUtils.containsObjectOfClass(result, ProjectileThrownEvent.class));

        ProjectileThrownEvent e = AssertUtils.getObjectOfClass(result, ProjectileThrownEvent.class);
        Assert.assertEquals(e.item, this.item.item());
        Assert.assertEquals(e.thrower, event.thrower());
        Assert.assertEquals(e.destination,
                getLeftPokemon().tile().adjacentTile(Direction.SOUTH).adjacentTile(Direction.SOUTH));

        result = e.processServer();
        Assert.assertEquals("Unexpected number of resulting events.", 1, result.size());
        Assert.assertTrue("Created Event isn't of expected class.",
                AssertUtils.containsObjectOfClass(result, ItemLandedEvent.class));
        ItemLandedEvent land = AssertUtils.getObjectOfClass(result, ItemLandedEvent.class);
        Assert.assertEquals("Item lands on incorrect Tile.", e.destination.adjacentTile(Direction.NORTH), land.tile);
    }

    @Test
    public void testItemUseEvent() {
        new ItemUseEvent(getFloor(), null, this.item.item(), getRightPokemon(), getRightPokemon(), false)
                .processServer();
        new ItemUseEvent(getFloor(), null, this.moneyItem.item(), getRightPokemon(), null, true).processServer();
        // Nothing special, just verifying there is no Exception
    }

    @Test
    public void testMoneyCollectedEvent() {
        MoneyCollectedEvent invalid = new MoneyCollectedEvent(getFloor(), null, getRightPokemon());
        Assert.assertFalse("MoneyCollectedEvent is valid with Pokemon without a Player.", invalid.isValid());

        getLeftPokemon().tile().setItem(this.moneyItem);
        MoneyCollectedEvent valid = new MoneyCollectedEvent(getFloor(), null, getLeftPokemon());
        Assert.assertTrue("MoneyCollectedEvent isn't valid when it should be.", valid.isValid());

        getLeftPokemon().tile().setItem(this.item);
        invalid = new MoneyCollectedEvent(getFloor(), null, getLeftPokemon());
        Assert.assertFalse("MoneyCollectedEvent is valid with a non-money item.", invalid.isValid());

        getLeftPokemon().tile().setItem(null);
        invalid = new MoneyCollectedEvent(getFloor(), null, getLeftPokemon());
        Assert.assertFalse("MoneyCollectedEvent is valid with a null item.", invalid.isValid());

        long previousMoney = getPlayer().moneyInBag();
        valid.processServer();
        Assert.assertEquals("Player's money wasn't increased properly.", previousMoney + this.moneyItem.quantity(),
                getPlayer().moneyInBag());
    }

    @Test
    public void testProjectileItemThrownToPokemon() {
        Item i = Registries.items().find(21);// Throwable Item
        ProjectileThrownEvent event = new ProjectileThrownEvent(getFloor(), null, i, getLeftPokemon(),
                getRightPokemon().tile());
        ArrayList<Event> result = event.processServer();

        Assert.assertNull("Pokemon caught the Item.", getRightPokemon().getItem());
        Assert.assertEquals("Unexpected number of resulting events.", 2, result.size()); // Don't forget experience
        Assert.assertTrue("Created Event isn't of expected class.",
                AssertUtils.containsObjectOfClass(result, DamageDealtEvent.class));
        DamageDealtEvent e = AssertUtils.getObjectOfClass(result, DamageDealtEvent.class);
        Assert.assertEquals("Wrong Pokemon got hit.", getRightPokemon(), e.target);
    }

    @Test
    public void testUnusableItemThrownToFullPokemon() {
        Item i = Registries.items().find(325);// Evolution Item
        getRightPokemon().setItem(this.item);
        ProjectileThrownEvent event = new ProjectileThrownEvent(getFloor(), null, i, getLeftPokemon(),
                getRightPokemon().tile());
        ArrayList<Event> result = event.processServer();

        Assert.assertNotEquals("Pokemon caught the Item.", i, getRightPokemon().getItem().item());
        Assert.assertEquals("Unexpected number of resulting events.", 1, result.size());
        Assert.assertTrue("Created Event isn't of expected class.",
                AssertUtils.containsObjectOfClass(result, ItemLandedEvent.class));
        ItemLandedEvent e = AssertUtils.getObjectOfClass(result, ItemLandedEvent.class);
        Assert.assertEquals("Item lands on incorrect Tile.", getRightPokemon().tile(), e.tile);
    }

    @Test
    public void testUnusableItemThrownToPokemon() {
        Item i = Registries.items().find(325);
        ProjectileThrownEvent event = new ProjectileThrownEvent(getFloor(), null, i, getLeftPokemon(),
                getRightPokemon().tile());// Evolution Item
        event.processServer();

        Assert.assertNotNull("Pokemon didn't catch the Item.", getRightPokemon().getItem());
        Assert.assertEquals("Pokemon didn't catch the correct Item.", i, getRightPokemon().getItem().item());
    }

    @Test
    public void testUsableItemThrownToPokemon() {
        ProjectileThrownEvent event = new ProjectileThrownEvent(getFloor(), null, this.item.item(), getLeftPokemon(),
                getRightPokemon().tile());
        ArrayList<Event> result = event.processServer();

        Assert.assertNull("Pokemon caught the Item.", getRightPokemon().getItem());
        Assert.assertEquals("Unexpected number of resulting events.", 1, result.size());
        Assert.assertTrue("Created Event isn't of expected class.",
                AssertUtils.containsObjectOfClass(result, ItemUseEvent.class));
        ItemUseEvent e = AssertUtils.getObjectOfClass(result, ItemUseEvent.class);
        Assert.assertEquals("Incorrect ItemUseEvent item.", this.item.item(), e.item);
        Assert.assertEquals("Incorrect ItemUseEvent user.", getLeftPokemon(), e.user);
        Assert.assertEquals("Incorrect ItemUseEvent target.", getRightPokemon(), e.target);
        Assert.assertTrue("Incorrect ItemUseEvent thrown flag.", e.thrown);
    }

}
