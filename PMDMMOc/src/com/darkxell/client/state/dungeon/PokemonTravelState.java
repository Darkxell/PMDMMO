package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;
import java.awt.Point;

import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.resources.images.tilesets.CommonDungeonTileset;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.common.pokemon.PokemonD;

/** Used for Pokémon travel animations. */
public class PokemonTravelState extends DungeonSubState
{

	/** Represents the travel of a Pokémon from a Tile to another. */
	public static class Travel
	{

		Point current;
		public final short direction;
		public final Point origin, arrival, distance;
		public final PokemonD pokemon;

		public Travel(PokemonD pokemon, short direction)
		{
			this.pokemon = pokemon;
			this.direction = direction;
			this.origin = this.pokemon.tile.location();
			this.arrival = this.pokemon.tile.adjacentTile(direction).location();
			this.distance = new Point(this.arrival.x - this.origin.x, this.arrival.y - this.origin.y);
			this.current = new Point(this.origin.x * AbstractDungeonTileset.TILE_SIZE, this.origin.y * AbstractDungeonTileset.TILE_SIZE);
		}
	}

	public static final int DURATION = 20;

	private int tick;
	private Travel[] travels;

	public PokemonTravelState(DungeonState parent, Travel... travels)
	{
		super(parent);
		this.travels = travels;
		this.tick = 0;
	}

	@Override
	public void onEnd()
	{
		for (Travel travel : this.travels)
			travel.pokemon.tile.adjacentTile(travel.direction).setPokemon(travel.pokemon);
	}

	@Override
	public void onKeyPressed(short key)
	{}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void onStart()
	{
		for (Travel travel : this.travels)
			travel.pokemon.tile.setPokemon(null);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		for (Travel travel : this.travels)
			g.drawImage(CommonDungeonTileset.INSTANCE.shop(), travel.current.x, travel.current.y, null);
	}

	@Override
	public void update()
	{
		++this.tick;
		float completion = this.tick * 1f / DURATION;
		for (Travel travel : this.travels)
		{
			travel.current = new Point((int) ((travel.origin.x + travel.distance.x * completion) * AbstractDungeonTileset.TILE_SIZE),
					(int) ((travel.origin.y + travel.distance.y * completion) * AbstractDungeonTileset.TILE_SIZE));
			if (travel.pokemon == this.parent.player)
			{
				this.parent.camera.x = travel.current.x;
				this.parent.camera.y = travel.current.y;
			}
		}

		if (this.tick >= DURATION) this.parent.setSubstate(this.parent.actionSelectionState);
	}
}
