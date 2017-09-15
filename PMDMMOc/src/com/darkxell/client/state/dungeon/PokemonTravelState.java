package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.animation.TravelAnimation;
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

	public static final int DURATION = 20;

	private DungeonPokemon[] pokemons;
	private int tick;
	private TravelAnimation[] travels;

	public PokemonTravelState(DungeonState parent, DungeonPokemon[] pokemons, TravelAnimation... travels)
	{
		super(parent);
		this.pokemons = pokemons;
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
		for (TravelAnimation travel : this.travels)
		{
			DungeonPokemon pokemon = this.pokemonFor(travel);
			PokemonSprite sprite = DungeonPokemonRenderer.instance.register(pokemon);
			if (sprite.getState() != PokemonSprite.STATE_MOVE) sprite.setState(PokemonSprite.STATE_MOVE);
			pokemon.tile.setPokemon(null);
		}
	}

	public void onTravelEnd(TravelAnimation travel)
	{
		DungeonPokemon pokemon = this.pokemonFor(travel);
		Tile t = pokemon.tile.adjacentTile(pokemon.facing());
		t.setPokemon(pokemon);

		if (t.getItem() != null)
		{
			String messageID = "ground.step";
			ItemStack i = t.getItem();

			if (i.id == Item.POKE && pokemon.player != null)
			{
				pokemon.player.money += i.getQuantity();
				messageID = "ground.poke";
				t.setItem(null);
			} else if (pokemon.player != null && pokemon.player.inventory.canAccept(i))
			{
				pokemon.player.inventory.add(i);
				messageID = "ground.inventory";
				t.setItem(null);
			} else if (pokemon.pokemon.getItem() == null)
			{
				pokemon.pokemon.setItem(i);
				messageID = "ground.pickup";
				t.setItem(null);
			}
			this.parent.logger
					.showMessage(new Message(messageID).addReplacement("<pokemon>", pokemon.pokemon.getNickname()).addReplacement("<item>", i.name()));
		}
	}

	private DungeonPokemon pokemonFor(TravelAnimation travel)
	{
		for (int i = 0; i < pokemons.length; ++i)
			if (this.travels[i] == travel) return this.pokemons[i];
		return this.pokemons[0];
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		for (TravelAnimation travel : this.travels)
			DungeonPokemonRenderer.instance.draw(g, this.pokemonFor(travel), travel.current().getX(), travel.current().getY());
	}

	@Override
	public void update()
	{
		++this.tick;
		float completion = this.tick * 1f / DURATION;
		for (TravelAnimation travel : this.travels)
		{
			travel.update(completion);
			if (this.pokemonFor(travel).player == DungeonPersistance.player)
			{
				this.parent.camera.x = (int) (travel.current().getX() * AbstractDungeonTileset.TILE_SIZE);
				this.parent.camera.y = (int) (travel.current().getY() * AbstractDungeonTileset.TILE_SIZE);
			}
		}

		if (this.tick >= DURATION)
		{
			for (TravelAnimation travel : this.travels)
				this.onTravelEnd(travel);

			if (!this.parent.actionSelectionState.checkMovement())
			{
				for (TravelAnimation travel : this.travels)
					DungeonPokemonRenderer.instance.getSprite(this.pokemonFor(travel)).setState(PokemonSprite.STATE_IDDLE);
				this.parent.setSubstate(this.parent.actionSelectionState);
			}
		}
	}
}
