package com.darkxell.client.state.dungeon;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.animation.TravelAnimation;
import com.darkxell.client.mechanics.event.ClientEventProcessor;
import com.darkxell.client.persistance.DungeonPersistance;
import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.resources.images.AbstractDungeonTileset;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.state.dungeon.DungeonState.DungeonSubState;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.dungeon.floor.TileType;
import com.darkxell.common.event.pokemon.PokemonTravelEvent.PokemonTravel;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.GameUtil;

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

	private boolean shouldStopRunning(DungeonPokemon runner, PokemonTravel travel)
	{
		if (!runner.tryMoveTo(runner.facing())) return true;
		if (travel.destination.isInRoom() != travel.destination.adjacentTile(runner.facing()).isInRoom()) return true;
		if (travel.destination.type() == TileType.STAIR || travel.destination.type() == TileType.WONDER_TILE || travel.destination.getItem() != null) return true;
		int origin = 0, destination = 0;
		short facing = runner.facing();

		for (short dir : GameUtil.isDiagonal(facing) ? new short[]
		{ facing, GameUtil.rotateClockwise(facing), GameUtil.rotateClockwise(facing), GameUtil.rotateCounterClockwise(facing) } : new short[]
		{ facing, GameUtil.rotateClockwise(facing), GameUtil.rotateClockwise(facing), GameUtil.rotateClockwise(GameUtil.rotateClockwise(facing)),
				GameUtil.rotateCounterClockwise(facing), GameUtil.rotateCounterClockwise(GameUtil.rotateCounterClockwise(facing)) })
		{
			Tile o = travel.origin.adjacentTile(dir);
			Tile d = travel.destination.adjacentTile(dir);
			if (!(GameUtil.isDiagonal(dir) && !o.isInRoom()) && o.type().canWalkOn(runner)) ++origin;
			if (!(GameUtil.isDiagonal(dir) && !d.isInRoom()) && d.type().canWalkOn(runner)) ++destination;
			if (d.type() == TileType.STAIR || d.type() == TileType.WONDER_TILE || d.getItem() != null) return true;
		}

		if (destination > origin) return true;

		return false;
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
		float completion = this.tick * 1f / this.duration;
		for (int i = 0; i < this.travels.length; ++i)
		{
			this.animations[i].update(completion);
			if (this.travels[i].pokemon.pokemon == DungeonPersistance.player.getPokemon())
			{
				this.parent.camera.x = (int) (this.animations[i].current().getX() * AbstractDungeonTileset.TILE_SIZE);
				this.parent.camera.y = (int) (this.animations[i].current().getY() * AbstractDungeonTileset.TILE_SIZE);
			}
		}

		if (this.tick >= this.duration)
		{
			boolean stairLand = false;
			for (PokemonTravel travel : this.travels)
			{
				travel.destination.setPokemon(travel.pokemon);
				if (travel.destination.type() == TileType.STAIR) stairLand = travel.pokemon == DungeonPersistance.player.getDungeonPokemon();
			}
			ClientEventProcessor.landedOnStairs = stairLand;
			this.parent.setSubstate(this.parent.actionSelectionState);

			short direction = this.running ? -1 : this.parent.actionSelectionState.checkMovement();
			boolean shouldStop = false;
			PokemonTravel travel = null;
			if (this.running) for (PokemonTravel t : this.travels)
				if (t.pokemon.pokemon.player.getDungeonPokemon() == t.pokemon)
				{
					travel = t;
					break;
				}

			if (this.running) shouldStop = this.shouldStopRunning(travel.pokemon, travel) || ClientEventProcessor.hasPendingEvents()
					|| DungeonPersistance.dungeon.getNextActor() != null;
			else shouldStop = direction == -1 || ClientEventProcessor.hasPendingEvents() || DungeonPersistance.dungeon.getNextActor() != null;

			if (shouldStop) this.stopTravel();
			else
			{
				ClientEventProcessor.addToPending(DungeonPersistance.dungeon.endTurn());
				if (ClientEventProcessor.hasPendingEvents()) this.stopTravel();
				else if (this.running) ClientEventProcessor.actorTravels(travel.pokemon.facing(), true);
			}
		}
	}
}
