package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Message;

/** Used for Pokémon travel animations. */
public class PokemonTravelState extends DungeonSubState
{

	/** Represents the travel of a Pokémon from a Tile to another. */
	public static class Travel
	{

		Point2D current;
		public final short direction;
		public final Point origin, arrival, distance;
		public final DungeonPokemon pokemon;

		public Travel(DungeonPokemon pokemon, short direction)
		{
			this.pokemon = pokemon;
			this.direction = direction;
			this.origin = this.pokemon.tile.location();
			this.arrival = this.pokemon.tile.adjacentTile(direction).location();
			this.distance = new Point(this.arrival.x - this.origin.x, this.arrival.y - this.origin.y);
			this.current = new Point2D.Float(this.origin.x, this.origin.y);
		}

		public void onEnd(DungeonState state)
		{
			Tile t = this.pokemon.tile.adjacentTile(this.direction);
			t.setPokemon(this.pokemon);

			if (t.getItem() != null)
			{
				String messageID = "ground.step";
				ItemStack i = t.getItem();

				if (i.id == Item.POKE && this.pokemon.player != null)
				{
					this.pokemon.player.money += i.getQuantity();
					messageID = "ground.poke";
					t.setItem(null);
				} else if (this.pokemon.player != null && this.pokemon.player.inventory.canAccept(i))
				{
					this.pokemon.player.inventory.add(i);
					messageID = "ground.inventory";
					t.setItem(null);
				} else if (this.pokemon.pokemon.getItem() == null)
				{
					this.pokemon.pokemon.setItem(i);
					messageID = "ground.pickup";
					t.setItem(null);
				}
				state.logger.showMessage(new Message(messageID).addReplacement("<pokemon>", this.pokemon.pokemon.getNickname()).addReplacement("<item>",
						i.name()));
			}
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
	public void onKeyPressed(short key)
	{}

	@Override
	public void onKeyReleased(short key)
	{}

	@Override
	public void onStart()
	{
		super.onStart();
		for (Travel travel : this.travels)
		{
			PokemonSprite sprite = DungeonPokemonRenderer.instance.register(travel.pokemon);
			if (sprite.getState() != PokemonSprite.STATE_MOVE) sprite.setState(PokemonSprite.STATE_MOVE);
			travel.pokemon.tile.setPokemon(null);
		}
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		for (Travel travel : this.travels)
			DungeonPokemonRenderer.instance.draw(g, travel.pokemon, travel.current.getX(), travel.current.getY());
	}

	@Override
	public void update()
	{
		++this.tick;
		float completion = this.tick * 1f / DURATION;
		for (Travel travel : this.travels)
		{
			travel.current = new Point2D.Float(travel.origin.x + travel.distance.x * completion, travel.origin.y + travel.distance.y * completion);
			if (travel.pokemon.player == DungeonPersistance.player)
			{
				this.parent.camera.x = (int) (travel.current.getX() * AbstractDungeonTileset.TILE_SIZE);
				this.parent.camera.y = (int) (travel.current.getY() * AbstractDungeonTileset.TILE_SIZE);
			}
		}

		if (this.tick >= DURATION)
		{
			for (Travel travel : this.travels)
				travel.onEnd(this.parent);

			if (!this.parent.actionSelectionState.checkMovement())
			{
				for (Travel travel : this.travels)
					DungeonPokemonRenderer.instance.getSprite(travel.pokemon).setState(PokemonSprite.STATE_IDDLE);
				this.parent.setSubstate(this.parent.actionSelectionState);
			}
		}
	}
}
