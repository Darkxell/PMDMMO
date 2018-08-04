package com.darkxell.common.dungeon.floor.layout;

import java.awt.Point;
import java.util.Random;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.dungeon.floor.room.Room;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemEffects;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.util.RandomUtil;

/** Represents a Floor's layout. */
public abstract class Layout
{

	public static final int LAYOUT_STATIC = 0;
	public static final int LAYOUT_SINGLEROOM = 1;
	public static final int LAYOUT_GRIDROOMS_2x2 = 2;

	/** @return A new layout from the input ID. */
	public static Layout find(int id)
	{
		switch (id)
		{
			case 0:
				return new StaticLayout();
			case 1:
				return new SingleRoomLayout();
			case 2:
				return new GridRoomsLayout(id, 2, 2, 5, 5, 14, 14, 2);
			default:
				return null;
		}
	}

	/** Temporary storage for the generating Floor. */
	protected Floor floor;
	/** RNG */
	protected Random random;

	protected void createDefaultTiles(int width, int height)
	{
		this.floor.tiles = new Tile[width][height];
		for (int x = 0; x < this.floor.tiles.length; ++x)
			for (int y = 0; y < this.floor.tiles[x].length; ++y)
				if (x == 0 || x == this.floor.tiles.length - 1 || y == 0 || y >= this.floor.tiles[x].length - 1)
					this.floor.tiles[x][y] = new Tile(this.floor, x, y, TileType.WALL_END);
				else this.floor.tiles[x][y] = new Tile(this.floor, x, y, TileType.WALL);
	}

	protected void fillRoomsWithGround()
	{
		for (Room r : this.floor.rooms)
			for (Tile t : r.listTiles())
					t.setType(TileType.GROUND);
	}

	/** Generates a Floor.
	 * 
	 * @param floor - The Floor to build.
	 * @return The generated Tiles and Rooms. */
	public void generate(Floor floor)
	{
		this.floor = floor;
		this.random = this.floor.random;
		this.generateRooms();
		this.generatePaths();
		this.generateLiquids();
		this.placeStairs();
		this.placeTraps();
		this.placeItems();
		this.placeBuriedItems();
		this.summonPokemon();

		this.placeTeam();
	}

	/** Creates Water, Lava or Air. */
	protected abstract void generateLiquids();

	/** Creates paths between the rooms. */
	protected abstract void generatePaths();

	/** Creates the rooms. */
	protected abstract void generateRooms();

	/** @return The quantity of Poké in a single stack. */
	protected int getMoneyQuantity()
	{
		final int[] moneyTable = new int[] { 4, 6, 10, 14, 22, 26, 37, 38, 46, 58, 62, 74, 82, 86, 94, 106, 118, 122, 134, 142, 146, 158, 166, 178, 194, 202,
				206, 214, 218, 226, 254, 262, 274, 278, 298, 302, 314, 326, 334, 346, 358, 362, 382, 386, 394, 398, 422, 454, 458, 466, 478, 482, 502, 514, 526,
				538, 542, 554, 562, 566, 586, 614, 622, 626, 634, 662, 674, 694, 698, 706, 718, 734, 746, 758, 768, 778, 794, 802, 818, 838, 842, 862, 866, 878,
				886, 898, 914, 922, 926, 934, 958, 974, 982, 998, 1000, 1100, 1300, 1500, 20000 };

		int random = this.random.nextInt(99) + 1;
		int value = 0;
		int max = this.floor.data.baseMoney() * 40;

		for (int i = 0; i < 200; ++i)
		{
			value = moneyTable[random - 1];
			if (value <= max) return value;
			else random /= 2;
		}
		return moneyTable[0];
	}

	/** Places buried Items. */
	protected void placeBuriedItems()
	{
		int items = this.floor.data.buriedItemDensity();
		if (items == 0) return;
		items = RandomUtil.randomize(items, this.random);
		if (items < 1) items = 1;
		if (items > 63) items = 63;
		for (int i = 0; i < items; ++i)
		{
			Tile tile = this.floor.randomEmptyTile(false, false, TileType.WALL, this.random);
			Item item = this.floor.randomBuriedItem(this.random);
			int quantity = 0;
			if (item.effect() == ItemEffects.Pokedollars) quantity = this.getMoneyQuantity();
			else if (item.isStackable) quantity = RandomUtil.nextGaussian(10, 7, this.random);

			tile.setItem(new ItemStack(item.id).setQuantity(quantity));
		}
	}

	/** Places Items. */
	protected void placeItems()
	{
		int items = this.floor.data.itemDensity();
		if (items == 0) return;
		items = RandomUtil.randomize(items, this.random);
		if (items < 1) items = 1;
		if (items > 63) items = 63;
		for (int i = 0; i < items; ++i)
		{
			Tile tile = this.floor.randomEmptyTile(true, false, TileType.GROUND, this.random);
			Item item = this.floor.randomItem(this.random);
			int quantity = 0;
			if (item.effect() == ItemEffects.Pokedollars) quantity = this.getMoneyQuantity();
			else if (item.isStackable) quantity = RandomUtil.nextGaussian(10, 7, this.random);
			if (quantity <= 0) quantity = 1;

			tile.setItem(new ItemStack(item.id).setQuantity(quantity));
		}
	}

	/** Randomly places the Stairs tile in a random room. */
	protected void placeStairs()
	{
		this.floor.randomEmptyTile(true, false, this.random).setType(TileType.STAIR);
	}

	protected void placeTeam()
	{
		Tile t = this.floor.randomRoom(this.random).randomTile(this.random);
		this.floor.teamSpawn = new Point(t.x, t.y);
	}

	/** Places traps. */
	protected void placeTraps()
	{
		int traps = this.floor.data.trapDensity();
		if (traps == 0) return;
		traps = RandomUtil.randomize(traps, this.random);
		if (traps < 1) traps = 1;
		if (traps > 63) traps = 63;
		for (int i = 0; i < traps; ++i)
		{
			Tile t = this.floor.randomEmptyTile(true, false, TileType.GROUND, this.random);
			t.trap = this.floor.randomTrap(this.random);
			t.trapRevealed = t.trap == TrapRegistry.WONDER_TILE;
		}
	}

	/** Summons Pokemon. */
	protected void summonPokemon()
	{}

}
