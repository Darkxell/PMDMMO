package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import com.darkxell.client.renderers.PokemonRenderer;
import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.common.pokemon.PokemonD;

/** Used for Pok�mon travel animations. */
public class PokemonTravelState extends DungeonSubState
{

	/** Represents the travel of a Pok�mon from a Tile to another. */
	public static class Travel
	{

		Point2D current;
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
			this.current = new Point2D.Float(this.origin.x, this.origin.y);
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
	{}

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
		{
			PokemonSprite sprite = PokemonRenderer.instance.register(travel.pokemon);
			if (sprite.getState() != PokemonSprite.STATE_MOVE) sprite.setState(PokemonSprite.STATE_MOVE);
			travel.pokemon.tile.setPokemon(null);
		}
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		for (Travel travel : this.travels)
			PokemonRenderer.instance.draw(g, travel.pokemon, travel.current.getX(), travel.current.getY());
	}

	@Override
	public void update()
	{
		++this.tick;
		float completion = this.tick * 1f / DURATION;
		for (Travel travel : this.travels)
		{
			travel.current = new Point2D.Float(travel.origin.x + travel.distance.x * completion, travel.origin.y + travel.distance.y * completion);
			if (travel.pokemon == this.parent.player)
			{
				this.parent.camera.x = (int) (travel.current.getX() * AbstractDungeonTileset.TILE_SIZE);
				this.parent.camera.y = (int) (travel.current.getY() * AbstractDungeonTileset.TILE_SIZE);
			}
		}

		if (this.tick >= DURATION)
		{
			for (Travel travel : this.travels)
				travel.pokemon.tile.adjacentTile(travel.direction).setPokemon(travel.pokemon);

			if (!this.parent.actionSelectionState.checkMovement())
			{
				for (Travel travel : this.travels)
					PokemonRenderer.instance.getSprite(travel.pokemon).setState(PokemonSprite.STATE_IDDLE);
				this.parent.setSubstate(this.parent.actionSelectionState);
			}
		}
	}
}
