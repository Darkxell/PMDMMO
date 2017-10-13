package com.darkxell.client.mechanics.animation.moves;

import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.TravelAnimation;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.pokemon.DungeonPokemon;

public class BasicAttackAnimation extends PokemonAnimation
{
	public static final int TOTAL = 30, CHARGE = TOTAL / 3, MOVEMENT = TOTAL / 6;

	private final Tile location;
	private TravelAnimation travel;

	public BasicAttackAnimation(DungeonPokemon user, AnimationEndListener listener)
	{
		super(user, TOTAL, listener);
		this.location = this.target.tile;
		this.travel = new TravelAnimation(this.location.location(), this.location.adjacentTile(this.target.facing()).location());
	}

	@Override
	public void onFinish()
	{
		super.onFinish();
		this.renderer.setXY(this.location.x, this.location.y);
	}

	@Override
	public void start()
	{
		super.start();
		this.renderer.sprite.setState(PokemonSprite.STATE_ATTACK);
	}

	@Override
	public void update()
	{
		super.update();
		float completion = this.tick() * 1f;
		if (this.tick() >= CHARGE && this.tick() <= CHARGE + MOVEMENT) completion -= CHARGE;
		else if (this.tick() >= CHARGE * 2 + MOVEMENT && this.tick() <= TOTAL) completion = 30 - completion;
		else completion = -1;

		if (completion != -1 && !this.isOver())
		{
			this.travel.update(completion * 0.75f / MOVEMENT);
			this.renderer.setXY(this.travel.current().getX(), this.travel.current().getY());
		}
	}
}
