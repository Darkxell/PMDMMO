package com.darkxell.common.dungeon.floor;

import static com.darkxell.common.dungeon.floor.TileType.*;

import java.awt.Point;

import javafx.util.Pair;

import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonType;
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

	/** @return The Tile adjacent to this Tile in the input direction. See {@link GameUtil#NORTH}. */
	public Tile adjacentTile(short direction)
	{
		Point p = GameUtil.moveTo(this.x, this.y, direction);
		return this.floor.tileAt(p.x, p.y);
	}

	/** @param direction - The direction of the movement.
	 * @return True if the input Pokémon can walk on this Tile. */
	public boolean canMoveTo(DungeonPokemon pokemon, short direction)
	{
		if (!this.canWalkOn(pokemon)) return false;
		if (!GameUtil.isDiagonal(direction)) return true;
		Pair<Short, Short> corners = GameUtil.splitDiagonal(direction);
		TileType t1 = pokemon.tile.adjacentTile(corners.getKey()).type();
		TileType t2 = pokemon.tile.adjacentTile(corners.getValue()).type();
		return t1 != WALL && t1 != WALL_END && t2 != WALL && t2 != WALL_END;
	}

	/** @return True if the input Pokémon can walk on this Tile. */
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
	public DungeonPokemon getPokemon()
	{
		return this.pokemon;
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
