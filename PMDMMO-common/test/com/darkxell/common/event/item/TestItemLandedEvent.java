package com.darkxell.common.event.item;

import static com.darkxell.common.testutils.TestUtils.generateALL;
import static com.darkxell.common.testutils.TestUtils.getFloor;
import static com.darkxell.common.testutils.TestUtils.getLeftPokemon;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.util.Direction;

public class TestItemLandedEvent {

    private ItemStack item, item2;
    private Tile tile;

    @Before
    public void before() {
        generateALL();
        this.tile = getLeftPokemon().tile();
        this.tile.setType(TileType.GROUND);
        this.item = new ItemStack(1);
        this.item2 = new ItemStack(2);
    }

    private void doTest(Tile expectedLandingTile) {
        ItemLandedEvent event = new ItemLandedEvent(getFloor(), null, this.item, this.tile);
        event.processServer();
        Assert.assertEquals("Item landed on the wrong Tile.", expectedLandingTile, event.destination());
        Assert.assertEquals("The Item wasn't properly set to the Tile.", this.item, expectedLandingTile.getItem());
    }

    @Test
    public void testLandsOnTile() {
        doTest(this.tile);
    }

    @Test
    public void testTileAndWestAreWalls() {
        this.tile.setType(TileType.WALL);
        this.tile.adjacentTile(Direction.WEST).setType(TileType.WALL);
        doTest(this.tile.adjacentTile(Direction.NORTH));
    }

    @Test
    public void testTielAndSurroundingAreWalls() {
        this.tile.setType(TileType.WALL);
        for (Direction d : Direction.values())
            this.tile.adjacentTile(d).setType(TileType.WALL);
        doTest(this.tile.adjacentTile(Direction.WEST).adjacentTile(Direction.WEST));
    }

    @Test
    public void testTileHasItem() {
        this.tile.setItem(this.item2);
        doTest(this.tile.adjacentTile(Direction.WEST));
    }

    @Test
    public void testTileIsWall() {
        this.tile.setType(TileType.WALL);
        doTest(this.tile.adjacentTile(Direction.WEST));
    }

}
