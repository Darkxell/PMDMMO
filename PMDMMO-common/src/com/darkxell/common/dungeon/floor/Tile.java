package com.darkxell.common.dungeon.floor;

import static com.darkxell.common.dungeon.floor.TileType.AIR;
import static com.darkxell.common.dungeon.floor.TileType.LAVA;
import static com.darkxell.common.dungeon.floor.TileType.STAIR;
import static com.darkxell.common.dungeon.floor.TileType.TRAP;
import static com.darkxell.common.dungeon.floor.TileType.WALL_END;
import static com.darkxell.common.dungeon.floor.TileType.WARP_ZONE;
import static com.darkxell.common.dungeon.floor.TileType.WATER;
import static com.darkxell.common.dungeon.floor.TileType.WONDER_TILE;

import java.awt.Point;
import java.util.ArrayList;

import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.GameUtil;
import com.darkxell.common.util.Message;

import javafx.util.Pair;

/** Represents a single tile in a Floor. */
public class Tile implements ItemContainer
{

	/** Alternative tiles. */
	public byte alternate = 0;
	public final Floor floor;
	/** The Item on this Tile. null if no Item. */
	private ItemStack item;
	/** This Tile's neighbors connections. */
	private short neighbors = 0;
	/** The Pokémon standing on this Tile. null if no Pokémon. */
	private DungeonPokemon pokemon;
	/** This Tile's type. */
	private TileType type;
	/** This Tile's coordinates. */
	public final int x, y;

	public Tile(Floor floor, int x, int y, TileType type)
	{
		this.floor = floor;
		this.x = x;
		this.y = y;
		this.setType(type);
	}

	@Override
	public void addItem(ItemStack item)
	{
		this.setItem(item);
	}

	/** @return The Tile adjacent to this Tile in the input direction. See {@link GameUtil#NORTH}. */
	public Tile adjacentTile(short direction)
	{
		Point p = GameUtil.moveTo(this.x, this.y, direction);
		return this.floor.tileAt(p.x, p.y);
	}

	@Override
	public int canAccept(ItemStack item)
	{
		return (this.getItem() == null || (item.item().isStackable && this.getItem().id == item.id)) ? 0 : -1;
	}

	/** @return True if the input Pokémon can walk diagonally with this Tile as a corner. */
	public boolean canCross(DungeonPokemon pokemon)
	{
		return this.type == TileType.GROUND || this.type == WATER || this.type == LAVA || this.type == AIR || this.type == STAIR || this.type == WONDER_TILE
				|| this.type == TRAP || this.type == WARP_ZONE;
	}

	/** @param direction - The direction of the movement.
	 * @return True if the input Pokémon can walk on this Tile. */
	public boolean canMoveTo(DungeonPokemon pokemon, short direction)
	{
		if (!this.canWalkOn(pokemon)) return false;
		if (!GameUtil.isDiagonal(direction)) return true;
		Pair<Short, Short> corners = GameUtil.splitDiagonal(direction);
		return pokemon.tile.adjacentTile(corners.getKey()).canCross(pokemon) && pokemon.tile.adjacentTile(corners.getValue()).canCross(pokemon);
	}

	/** @return True if the input Pokémon can walk on this Tile. */
	public boolean canWalkOn(DungeonPokemon pokemon)
	{
		if (this.getPokemon() != null) return false;
		if (pokemon.pokemon.species.isType(PokemonType.GHOST)) return this.type != WALL_END;
		if (pokemon.pokemon.species.isType(PokemonType.WATER) && this.type == WATER) return true;
		if (pokemon.pokemon.species.isType(PokemonType.FIRE) && this.type == LAVA) return true;
		if (pokemon.pokemon.species.isType(PokemonType.FLYING) && this.type == AIR) return true;
		return this.canCross(pokemon);
	}

	@Override
	public Message containerName()
	{
		return new Message("menu.ground");
	}

	@Override
	public void deleteItem(int index)
	{
		this.setItem(null);
	}

	/** @return The Item on this Tile. null if no Item. */
	public ItemStack getItem()
	{
		return this.item;
	}

	@Override
	public ItemStack getItem(int index)
	{
		return this.getItem();
	}

	public short getNeighbors()
	{
		return this.neighbors;
	}

	/** @return The Pokémon standing on this Tile. null if no Pokémon. */
	public DungeonPokemon getPokemon()
	{
		return this.pokemon;
	}

	@Override
	public ArrayList<ItemAction> legalItemActions()
	{
		ArrayList<ItemAction> actions = new ArrayList<ItemAction>();
		actions.add(ItemAction.GET);
		actions.add(ItemAction.SWAP);
		return actions;
	}

	/** @return The coordinates of this Tile. */
	public Point location()
	{
		return new Point(this.x, this.y);
	}

	/** Called when an adjacent tile has its type changed.
	 * 
	 * @param direction - The direction of the Tile. See {@link GameUtil#NORTH}. */
	private void onNeighborTypeChange(short direction)
	{
		if (GameUtil.containsDirection(this.neighbors, direction)) this.neighbors -= direction;
		if (this.type.connectsTo(this.adjacentTile(direction).type)) this.neighbors += direction;
		this.neighbors = GameUtil.clean(this.neighbors);
	}

	/** Called when this Tile's type is changed. Reloads the connections of itself and its neighbors. */
	private void onTypeChanged()
	{
		if (this.floor.isGenerated())
		{
			this.updateNeighbors();
			for (short direction : GameUtil.directions())
			{
				Tile t = this.adjacentTile(direction);
				if (t != null) t.onNeighborTypeChange(GameUtil.oppositeOf(direction));
			}
			this.neighbors = GameUtil.clean(this.neighbors);
		}
	}

	@Override
	public void setItem(int index, ItemStack item)
	{
		this.setItem(item);
	}

	public void setItem(ItemStack item)
	{
		this.item = item;
	}

	/** Sets the Pokémon on this tile. Also changes this Pokémon's previous tile's Pokémon to null. */
	public void setPokemon(DungeonPokemon pokemon)
	{
		if (pokemon == null) this.pokemon = null;
		else
		{
			if (pokemon.tile != null) pokemon.tile.setPokemon(null);
			this.pokemon = pokemon;
			this.pokemon.tile = this;
		}
	}

	/** Sets this Tile's type. */
	public Tile setType(TileType type)
	{
		this.type = type;
		if (this.type == TileType.WALL || this.type == TileType.WALL_END)
		{
			this.alternate = (byte) (Math.random() * 10);
			if (this.alternate > 2) this.alternate = 0;
		}
		if (this.type == TileType.GROUND || this.type == TileType.TRAP)
		{
			this.alternate = (byte) (Math.random() * 10);
			if (this.alternate > 1) this.alternate = 0;
		}
		this.onTypeChanged();
		return this;
	}

	@Override
	public int size()
	{
		return this.getItem() == null ? 0 : 1;
	}

	/** @return This Tile's type. */
	public TileType type()
	{
		return this.type;
	}

	/** Checks each neighbor. */
	public void updateNeighbors()
	{
		this.neighbors = 0;
		for (short direction : GameUtil.directions())
		{
			Tile t = this.adjacentTile(direction);
			if (t == null || t.type.connectsTo(this.type)) this.neighbors += direction;
		}
		this.neighbors = GameUtil.clean(this.neighbors);
	}

}
