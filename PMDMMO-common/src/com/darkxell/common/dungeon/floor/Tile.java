package com.darkxell.common.dungeon.floor;

import static com.darkxell.common.dungeon.floor.TileType.*;

import java.awt.Point;
import java.util.ArrayList;

import javafx.util.Pair;

import com.darkxell.common.item.Item.ItemAction;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.util.GameUtil;
import com.darkxell.common.util.Message;

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
	/** The Pok�mon standing on this Tile. null if no Pok�mon. */
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

	/** @param direction - The direction of the movement.
	 * @return True if the input Pok�mon can walk on this Tile. */
	public boolean canMoveTo(DungeonPokemon pokemon, short direction)
	{
		if (!this.canWalkOn(pokemon)) return false;
		if (!GameUtil.isDiagonal(direction)) return true;
		Pair<Short, Short> corners = GameUtil.splitDiagonal(direction);
		TileType t1 = pokemon.tile.adjacentTile(corners.getKey()).type();
		TileType t2 = pokemon.tile.adjacentTile(corners.getValue()).type();
		return t1 != WALL && t1 != WALL_END && t2 != WALL && t2 != WALL_END;
	}

	/** @return True if the input Pok�mon can walk on this Tile. */
	public boolean canWalkOn(DungeonPokemon pokemon)
	{
		// todo: test if ally
		if (this.getPokemon() != null) return false;
		if (pokemon.pokemon.species.isType(PokemonType.GHOST)) return this.type != WALL_END;
		if (pokemon.pokemon.species.isType(PokemonType.WATER) && this.type == WATER) return true;
		if (pokemon.pokemon.species.isType(PokemonType.FIRE) && this.type == LAVA) return true;
		if (pokemon.pokemon.species.isType(PokemonType.FLYING) && this.type == AIR) return true;
		return this.type == TileType.GROUND || this.type == STAIR || this.type == WONDER_TILE || this.type == TRAP || this.type == WARP_ZONE;
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

	/** @return The Pok�mon standing on this Tile. null if no Pok�mon. */
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

	/** Sets the Pok�mon on this tile. Also changes this Pok�mon's previous tile's Pok�mon to null. */
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
