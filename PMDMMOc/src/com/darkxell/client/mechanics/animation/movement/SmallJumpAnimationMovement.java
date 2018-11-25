package com.darkxell.client.mechanics.animation.movement;

import java.awt.geom.Point2D;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;
import com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.pokemon.DungeonPokemon;

public class SmallJumpAnimationMovement extends PokemonAnimationMovement
{
	public static final int TOTAL = 10, PAUSE = 2, MOVEMENT = 4;

	protected final Tile location;
	protected TravelAnimation travel;

	public SmallJumpAnimationMovement(PokemonAnimation animation, DungeonPokemon pokemon)
	{
		super(animation, pokemon, TOTAL);
		this.location = this.pokemon.tile();
		this.travel = this.createTravel();
	}

	protected TravelAnimation createTravel()
	{
		return new TravelAnimation(new Point2D.Double(0, 0), new Point2D.Double(0, -.3));
	}

	@Override
	public void onFinish()
	{
		super.onFinish();
		if (this.renderer != null) this.renderer.sprite().setXYOffset(0, 0);
	}

	@Override
	public void update()
	{
		float completion = this.tick();
		if (this.tick() <= MOVEMENT) /* Nothing */;
		else if (this.tick() >= PAUSE + MOVEMENT && this.tick() <= TOTAL) completion = TOTAL - completion;
		else completion = -1;

		if (completion != -1 && !this.isOver())
		{
			this.travel.update(completion / MOVEMENT);
			this.renderer.sprite().setXYOffset((int) (this.travel.current().getX() * AbstractDungeonTileset.TILE_SIZE),
					(int) (this.travel.current().getY() * AbstractDungeonTileset.TILE_SIZE));
		}
	}
}
