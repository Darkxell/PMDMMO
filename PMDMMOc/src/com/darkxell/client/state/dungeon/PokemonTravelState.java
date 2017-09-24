package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.animation.TravelAnimation;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.mechanics.event.StairLandingEvent;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.resources.images.PokemonSprite;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.pokemon.PokemonTravelEvent.PokemonTravel;

/** Used for Pok�mon travel animations. */
public class PokemonTravelState extends DungeonSubState
{

	public static final int DURATION = 5;

	private TravelAnimation[] animations;
	private int tick;
	private PokemonTravel[] travels;

	public PokemonTravelState(DungeonState parent, PokemonTravel... travels)
	{
		super(parent);
		this.travels = travels;
		this.animations = new TravelAnimation[this.travels.length];
		for (int i = 0; i < this.travels.length; i++)
			this.animations[i] = new TravelAnimation(this.travels[i].origin.location(), this.travels[i].destination.location());
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
		for (PokemonTravel travel : this.travels)
		{
			PokemonSprite sprite = DungeonPokemonRenderer.instance.register(travel.pokemon);
			if (sprite.getState() != PokemonSprite.STATE_MOVE) sprite.setState(PokemonSprite.STATE_MOVE);
			travel.pokemon.setFacing(travel.direction);
			travel.destination.removePokemon(travel.pokemon);
		}
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		for (int i = 0; i < this.travels.length; ++i)
			DungeonPokemonRenderer.instance.draw(g, this.travels[i].pokemon, this.animations[i].current().getX(), this.animations[i].current().getY());
	}

	private void stopTravel()
	{
		for (PokemonTravel travel : this.travels)
			DungeonPokemonRenderer.instance.getSprite(travel.pokemon).setState(PokemonSprite.STATE_IDDLE);
		ClientEventProcessor.processPending();
	}

	@Override
	public void update()
	{
		++this.tick;
		float completion = this.tick * 1f / DURATION;
		for (int i = 0; i < this.travels.length; ++i)
		{
			this.animations[i].update(completion);
			if (this.travels[i].pokemon.pokemon == DungeonPersistance.player.getPokemon())
			{
				this.parent.camera.x = (int) (this.animations[i].current().getX() * AbstractDungeonTileset.TILE_SIZE);
				this.parent.camera.y = (int) (this.animations[i].current().getY() * AbstractDungeonTileset.TILE_SIZE);
			}
		}

		if (this.tick >= DURATION)
		{
			boolean stairLand = false;
			for (PokemonTravel travel : this.travels)
			{
				travel.destination.setPokemon(travel.pokemon);
				if (travel.destination.type() == TileType.STAIR) stairLand = travel.pokemon == DungeonPersistance.player.getDungeonPokemon();
			}
			this.parent.setSubstate(this.parent.actionSelectionState);

			short direction = this.parent.actionSelectionState.checkMovement();
			boolean shouldStop = direction == -1 || ClientEventProcessor.hasPendingEvents() || DungeonPersistance.dungeon.getNextActor() != null;

			if (stairLand) ClientEventProcessor.addToPending(new StairLandingEvent());
			if (shouldStop) this.stopTravel();
			else
			{
				ClientEventProcessor.addToPending(DungeonPersistance.dungeon.endTurn());
				if (ClientEventProcessor.hasPendingEvents()) this.stopTravel();
			}
		}
	}
}
