package com.darkxell.client.mechanics.animation.moves;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.TravelAnimation;
import com.darkxell.client.renderers.DungeonPokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.pokemon.DungeonPokemon;

public class BasicAttackAnimation extends AbstractAnimation
{
	public static final int TOTAL = 30, CHARGE = TOTAL / 3, MOVEMENT = TOTAL / 6;

	private final Tile location;
	private PokemonSprite sprite;
	private TravelAnimation travel;
	public final DungeonPokemon user;

	public BasicAttackAnimation(DungeonPokemon user, AnimationEndListener listener)
	{
		super(TOTAL, listener);
		this.user = user;
		this.location = this.user.tile;
		this.sprite = DungeonPokemonRenderer.instance.getSprite(this.user);
		this.travel = new TravelAnimation(this.user.tile.location(), this.user.tile.adjacentTile(this.user.facing()).location());
	}

	@Override
	public void onFinish()
	{
		super.onFinish();
		this.location.setPokemon(this.user);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		DungeonPokemonRenderer.instance.draw(g, this.user, this.travel.current().getX(), this.travel.current().getY());
	}

	@Override
	public void start()
	{
		super.start();
		this.location.setPokemon(null);
		this.sprite.setState(PokemonSprite.STATE_ATTACK);
	}

	@Override
	public void update()
	{
		super.update();
		float completion = this.tick() * 1f;
		if (this.tick() >= CHARGE && this.tick() <= CHARGE + MOVEMENT) completion -= CHARGE;
		else if (this.tick() >= CHARGE * 2 + MOVEMENT && this.tick() <= TOTAL) completion = 30 - completion;
		else completion = -1;

		if (completion != -1) this.travel.update(completion * 0.75f / MOVEMENT);
	}
}
