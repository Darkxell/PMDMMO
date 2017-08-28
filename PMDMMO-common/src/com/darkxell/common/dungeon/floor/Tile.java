package com.darkxell.common.dungeon.floor;

import java.awt.Point;

import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.GameUtil;

/** Represents a single tile in a Floor. */
public class Tile
{

	public final Floor floor;
	/** The Item on this Tile. null if no Item. */
	private ItemStack item;
	/** The Pokémon standing on this Tile. null if no Pokémon. */
	private Pokemon pokemon;
	/** This Tile's type. */
	private TileType type;
	/** This Tile's coordinates. */
	public final int x, y;

	public Tile(Floor floor, int x, int y, TileType type)
	{
		this.floor = floor;
		this.x = x;
		this.y = y;
		this.type = type;
	}

	/** @return The Tile adjacent to this Tile in the input direction. See {@link GameUtil#NORTH}. */
	public Tile adjacentTile(byte direction)
	{
		Point p = GameUtil.moveTo(this.x, this.y, direction);
		return this.floor.tileAt(p.x, p.y);
	}

	/** @return The Item on this Tile. null if no Item. */
	public ItemStack getItem()
	{
		return this.item;
	}

	/** @return The Pokémon standing on this Tile. null if no Pokémon. */
	public Pokemon getPokemon()
	{
		return this.pokemon;
	}

	public void setItem(ItemStack item)
	{
		this.item = item;
	}

	public void setPokemon(Pokemon pokemon)
	{
		this.pokemon = pokemon;
	}

	/** Sets this Tile's type. */
	public Tile setType(TileType type)
	{
		this.type = type;
		return this;
	}

	/** @return This Tile's type. */
	public TileType type()
	{
		return this.type;
	}

}
