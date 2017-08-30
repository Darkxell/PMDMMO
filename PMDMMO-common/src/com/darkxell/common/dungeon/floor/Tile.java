package com.darkxell.common.dungeon.floor;

import java.awt.Point;

import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.PokemonD;
import com.darkxell.common.util.GameUtil;

/** Represents a single tile in a Floor. */
public class Tile
{

	/** Alternative tiles. */
	public byte alternate = 0;
	public final Floor floor;
	/** The Item on this Tile. null if no Item. */
	private ItemStack item;
	/** This Tile's neighbors connections. */
	private short neighbors = 0;
	/** The Pokémon standing on this Tile. null if no Pokémon. */
	private PokemonD pokemon;
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

	/** @return The Tile adjacent to this Tile in the input direction. See {@link GameUtil#NORTH}. */
	public Tile adjacentTile(short direction)
	{
		Point p = GameUtil.moveTo(this.x, this.y, direction);
		return this.floor.tileAt(p.x, p.y);
	}

	/** @return The Item on this Tile. null if no Item. */
	public ItemStack getItem()
	{
		return this.item;
	}

	public short getNeighbors()
	{
		return this.neighbors;
	}

	/** @return The Pokémon standing on this Tile. null if no Pokémon. */
	public PokemonD getPokemon()
	{
		return this.pokemon;
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

	public void setItem(ItemStack item)
	{
		this.item = item;
	}

	public void setPokemon(PokemonD pokemon)
	{
		this.pokemon = pokemon;
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
