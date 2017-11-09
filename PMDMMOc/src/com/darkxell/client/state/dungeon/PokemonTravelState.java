package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.TravelAnimation;
import com.darkxell.client.renderers.floor.PokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.pokemon.PokemonTravelEvent.PokemonTravel;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Directions;

/** Used for Pokémon travel animations. */
public class PokemonTravelState extends DungeonSubState
{

	public static final int DURATION_WALK = 15, DURATION_RUN = 3;

	private TravelAnimation[] animations;
	public final int duration;
	public final boolean running;
	private int tick;
	private PokemonTravel[] travels;

	public PokemonTravelState(DungeonState parent, boolean running, PokemonTravel... travels)
	{
		super(parent);
		this.travels = travels;
		this.running = running;
		this.duration = this.running ? DURATION_RUN : DURATION_WALK;
		this.animations = new TravelAnimation[this.travels.length];
		for (int i = 0; i < this.travels.length; i++)
			this.animations[i] = new TravelAnimation(this.travels[i].origin.location(), this.travels[i].destination.location());
		this.tick = 0;
	}

	@Override
	public void onEnd()
	{
		super.onEnd();

		if (this.running) for (PokemonTravel travel : this.travels)
			Persistance.dungeonState.pokemonRenderer.getRenderer(travel.pokemon).sprite.setTickingSpeed(1);
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
			PokemonRenderer renderer = Persistance.dungeonState.pokemonRenderer.getRenderer(travel.pokemon);
			if (renderer.sprite.getState() != PokemonSprite.STATE_MOVE) renderer.sprite.setState(PokemonSprite.STATE_MOVE);
			travel.pokemon.setFacing(travel.direction);
			if (this.running) renderer.sprite.setTickingSpeed(3);
		}
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{}

	private boolean shouldStopRunning(DungeonPokemon runner, PokemonTravel travel)
	{
		if (!runner.tryMoveTo(runner.facing())) return true;
		if (travel.destination.isInRoom() != travel.destination.adjacentTile(runner.facing()).isInRoom()) return true;
		if (travel.destination.type() == TileType.STAIR || travel.destination.trapRevealed || travel.destination.getItem() != null) return true;
		int origin = 0, destination = 0;
		short facing = runner.facing();

		for (short dir : Directions.isDiagonal(facing) ? new short[]
		{ facing, Directions.rotateClockwise(facing), Directions.rotateClockwise(facing), Directions.rotateCounterClockwise(facing) } : new short[]
		{ facing, Directions.rotateClockwise(facing), Directions.rotateClockwise(facing), Directions.rotateClockwise(Directions.rotateClockwise(facing)),
				Directions.rotateCounterClockwise(facing), Directions.rotateCounterClockwise(Directions.rotateCounterClockwise(facing)) })
		{
			Tile o = travel.origin.adjacentTile(dir);
			Tile d = travel.destination.adjacentTile(dir);
			if (!(Directions.isDiagonal(dir) && !o.isInRoom()) && o.type().canWalkOn(runner)) ++origin;
			if (!(Directions.isDiagonal(dir) && !d.isInRoom()) && d.type().canWalkOn(runner)) ++destination;
			if (d.type() == TileType.STAIR || d.trapRevealed || d.getItem() != null) return true;
		}

		if (destination > origin) return true;

		return false;
	}

	private void stopTravel()
	{
		for (PokemonTravel travel : this.travels)
			Persistance.dungeonState.pokemonRenderer.getSprite(travel.pokemon).setState(PokemonSprite.STATE_IDDLE);
		Persistance.eventProcessor.processPending();
	}

	@Override
	public void update()
	{
		++this.tick;
		float completion = this.tick * 1f / this.duration;
		for (int i = 0; i < this.travels.length; ++i)
		{
			this.animations[i].update(completion);
			if (this.travels[i].pokemon.pokemon == Persistance.player.getTeamLeader())
			{
				this.parent.camera.x = (int) (this.animations[i].current().getX() * AbstractDungeonTileset.TILE_SIZE);
				this.parent.camera.y = (int) (this.animations[i].current().getY() * AbstractDungeonTileset.TILE_SIZE);
			}
			Persistance.dungeonState.pokemonRenderer.getRenderer(this.travels[i].pokemon).setXY(this.animations[i].current().getX(),
					this.animations[i].current().getY());
		}

		if (this.tick >= this.duration)
		{
			boolean stairLand = false;
			for (PokemonTravel travel : this.travels)
			{
				Persistance.dungeonState.pokemonRenderer.getRenderer(travel.pokemon).setXY(travel.destination.x, travel.destination.y);
				if (travel.destination.type() == TileType.STAIR) stairLand = travel.pokemon == Persistance.player.getDungeonLeader();
			}
			Persistance.eventProcessor.landedOnStairs = stairLand;
			this.parent.setSubstate(this.parent.actionSelectionState);

			short direction = this.running ? -1 : this.parent.actionSelectionState.checkMovement();
			boolean shouldStop = false;
			PokemonTravel travel = null;
			if (this.running) for (PokemonTravel t : this.travels)
				if (t.pokemon.isTeamLeader())
				{
					travel = t;
					break;
				}

			if (this.running) shouldStop = this.shouldStopRunning(travel.pokemon, travel) || Persistance.eventProcessor.shouldStopMoving()
					|| Persistance.dungeon.getNextActor() != null;
			else shouldStop = direction == -1 || Persistance.eventProcessor.shouldStopMoving() || Persistance.dungeon.getNextActor() != null;

			if (shouldStop) this.stopTravel();
			else
			{
				boolean hasPending = Persistance.eventProcessor.shouldStopMoving();
				Persistance.eventProcessor.processEvents(Persistance.dungeon.endTurn());
				if (hasPending) this.stopTravel();
				else if (this.running) Persistance.eventProcessor.actorTravels(travel.pokemon.facing(), true);
			}
		}
	}
}
