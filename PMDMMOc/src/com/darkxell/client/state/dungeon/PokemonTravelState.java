package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.TravelAnimation;
import com.darkxell.client.renderers.floor.PokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.pokemon.PokemonTravelEvent;

/** Used for Pokémon travel animations. */
public class PokemonTravelState extends DungeonSubState
{

	public static final int DURATION_WALK = 15, DURATION_RUN = 3;

	private TravelAnimation[] animations;
	public final int duration;
	public final boolean faraway;
	public final boolean running;
	private int tick;
	private PokemonTravelEvent[] travels;

	public PokemonTravelState(DungeonState parent, PokemonTravelEvent... travels)
	{
		super(parent);
		this.travels = travels;
		this.animations = new TravelAnimation[this.travels.length];
		for (int i = 0; i < this.travels.length; i++)
			this.animations[i] = new TravelAnimation(this.travels[i].origin.location(), this.travels[i].destination.location());
		this.tick = 0;

		boolean f = true, r = false;
		Tile camera = this.parent.getCameraPokemon().tile();
		for (PokemonTravelEvent t : travels)
		{
			if (t.running)
			{
				r = true;
				if (!f) break;
			}
			if ((Math.abs(t.origin.x - camera.x) < 10 && Math.abs(t.origin.y - camera.y) < 10)
					|| (Math.abs(t.destination.x - camera.x) < 10 && Math.abs(t.destination.y - camera.y) < 10))
			{
				f = false;
				if (r) break;
			}
		}
		this.running = r;
		this.faraway = f;
		this.duration = this.running ? DURATION_RUN : DURATION_WALK;
	}

	@Override
	public void onEnd()
	{
		super.onEnd();

		if (this.running) for (PokemonTravelEvent travel : this.travels)
			Persistance.dungeonState.pokemonRenderer.getRenderer(travel.pokemon).sprite.setTickingSpeed(1);
		for (PokemonTravelEvent travel : this.travels)
			Persistance.dungeonState.pokemonRenderer.getSprite(travel.pokemon).resetOnAnimationEnd();
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
		for (PokemonTravelEvent travel : this.travels)
		{
			PokemonRenderer renderer = Persistance.dungeonState.pokemonRenderer.getRenderer(travel.pokemon);
			renderer.sprite.setState(PokemonSpriteState.MOVE, true);
			travel.pokemon.setFacing(travel.direction);
			if (this.running) renderer.sprite.setTickingSpeed(3);
		}
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	@Override
	public void update()
	{
		++this.tick;
		if (this.faraway) this.tick = this.duration;

		float completion = this.tick * 1f / this.duration;
		for (int i = 0; i < this.travels.length; ++i)
		{
			this.animations[i].update(completion);
			Persistance.dungeonState.pokemonRenderer.getRenderer(this.travels[i].pokemon).setXY(this.animations[i].current().getX(),
					this.animations[i].current().getY());
		}

		if (this.tick >= this.duration)
		{
			boolean stairLand = false;
			for (PokemonTravelEvent travel : this.travels)
			{
				Persistance.dungeonState.pokemonRenderer.getRenderer(travel.pokemon).setXY(travel.destination.x, travel.destination.y);
				if (travel.destination.type() == TileType.STAIR) stairLand = travel.pokemon == Persistance.player.getDungeonLeader();
			}
			Persistance.eventProcessor.landedOnStairs = stairLand;
			Persistance.eventProcessor.animateDelayed();
		}
	}
}
