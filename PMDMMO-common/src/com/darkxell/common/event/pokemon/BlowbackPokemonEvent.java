package com.darkxell.common.event.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DamageType;
import com.darkxell.common.event.pokemon.DamageDealtEvent.DefaultDamageSource;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.language.Message;

public class BlowbackPokemonEvent extends DungeonEvent
{

	private Tile destination;
	public final Direction direction;
	public final Tile origin;
	public final DungeonPokemon pokemon;
	private boolean wasHurt;

	public BlowbackPokemonEvent(Floor floor, DungeonPokemon pokemon, Direction direction)
	{
		super(floor);
		this.pokemon = pokemon;
		this.direction = direction;
		this.origin = this.pokemon.tile();
	}

	public Tile destination()
	{
		return this.destination;
	}

	@Override
	public String loggerMessage()
	{
		return this.pokemon + "was blown back " + this.direction;
	}

	@Override
	public ArrayList<DungeonEvent> processServer()
	{
		this.messages.add(new Message("pokemon.blowback").addReplacement("<pokemon>", this.pokemon.getNickname()));

		final int max = 10;
		Tile current = this.pokemon.tile();
		Tile temp = current.adjacentTile(this.direction);
		int count = 0;

		while (temp.type() != TileType.WALL && temp.type() != TileType.WALL_END && temp.getPokemon() == null && count < max)
		{
			++count;
			current = temp;
			temp = current.adjacentTile(direction);
		}

		this.wasHurt = count < max;
		this.destination = count < max ? temp : current;
		current.setPokemon(this.pokemon);
		if (count < max && (temp.isWall() || temp.getPokemon() != null))
		{
			this.resultingEvents.add(new DamageDealtEvent(this.floor, this.pokemon, new DefaultDamageSource(this.floor, null), DamageType.COLLISION, 5));
			if (temp.getPokemon() != null) this.resultingEvents
					.add(new DamageDealtEvent(this.floor, temp.getPokemon(), new DefaultDamageSource(this.floor, null), DamageType.COLLISION, 5));
		}

		return super.processServer();
	}

	public boolean wasHurt()
	{
		return this.wasHurt;
	}

}
