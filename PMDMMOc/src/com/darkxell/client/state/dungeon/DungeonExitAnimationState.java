package com.darkxell.client.state.dungeon;

import java.awt.geom.Point2D;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.mechanics.animation.SpritesetAnimation;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;
import com.darkxell.client.renderers.pokemon.DungeonPokemonRenderer;
import com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset;
import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.pokemon.DungeonPokemon;

public class DungeonExitAnimationState extends AnimationState
{
	public static final double singleDuration, tileFadeDuration;
	public static final double speed = 0.2;
	/** Number of Tiles the Pokemon fades for when exiting. */
	public static final int tileFadeCount = 2;
	/** Number of Tiles the Pokemon goes up when exiting. */
	public static final int tileUpCount = 4;

	static
	{
		singleDuration = tileUpCount / speed;
		tileFadeDuration = tileFadeCount / speed;
	}

	private SpritesetAnimation currentAnimation;
	private int currentExiter = 0;
	private DungeonPokemonRenderer currentRenderer;
	private int duration;
	private DungeonPokemon[] exiters;
	/** Starting Gravities of the animation. */
	private int gy;
	private int tick = 0;
	private TravelAnimation travel, animTravel;

	public DungeonExitAnimationState(DungeonState parent, DungeonPokemon... exiters)
	{
		super(parent);
		this.exiters = exiters;
		this.duration = (int) (singleDuration * exiters.length);
		this.updateExiter();
	}

	@Override
	public void update()
	{
		super.update();
		++this.tick;
		this.travel.update(this.tick % singleDuration / singleDuration);
		this.animTravel.update(this.tick % singleDuration / singleDuration);
		this.currentRenderer.setXY(this.travel.current().getX() - .5, this.travel.current().getY() - .5);
		if (this.currentAnimation != null) this.currentAnimation.data().gravityY = (int) (this.animTravel.current().getY());
		if (this.tick % singleDuration >= (singleDuration - tileFadeDuration))
			this.currentRenderer.setAlpha((float) ((tileFadeDuration - (this.tick % singleDuration - (singleDuration - tileFadeDuration))) / tileFadeDuration));
		if (this.tick % singleDuration == 0)
		{
			this.currentRenderer.setAlpha(0);
			++this.currentExiter;
			this.updateExiter();
			if (this.tick == this.duration)
			{
				Persistance.dungeonState.setDefaultState();
				Persistance.eventProcessor().processPending();
			}
		}
	}

	private void updateExiter()
	{
		if (this.currentExiter >= this.exiters.length) return;
		this.currentRenderer = Persistance.dungeonState.pokemonRenderer.getRenderer(this.exiters[this.currentExiter]);
		PokemonAnimation a = Animations.getCustomAnimation(this.exiters[this.currentExiter], 4, null);
		if (a != null)
		{
			this.currentAnimation = ((SpritesetAnimation) a);
			this.currentRenderer.addAnimation(this.currentAnimation);
			this.currentAnimation.start();
			this.gy = this.currentAnimation.data().gravityY;
			this.animTravel = new TravelAnimation(new Point2D.Double(0, this.gy),
					new Point2D.Double(0, this.gy - tileUpCount * AbstractDungeonTileset.TILE_SIZE));
		}
		Tile start = this.exiters[this.currentExiter].tile();
		this.travel = new TravelAnimation(start, Persistance.floor.tileAt(start.x, start.y - tileUpCount));
	}

}
