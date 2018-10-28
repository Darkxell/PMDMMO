package com.darkxell.client.mechanics.animation.spritemovement;

import java.awt.geom.Point2D;

import com.darkxell.client.mechanics.animation.SpritesetAnimation;
import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.mechanics.animation.travel.TravelAnimation;
import com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset;
import com.darkxell.common.util.Direction;

public class FacingTileAnimationMovement extends SpritesetAnimationMovement
{

	private TravelAnimation travel;

	public FacingTileAnimationMovement(SpritesetAnimation animation)
	{
		super(animation);
	}

	@Override
	public void start()
	{
		super.start();
		Direction d = this.parentAnimation.renderer.sprite().getFacingDirection();
		if (d == Direction.NORTH || d == Direction.NORTHEAST || d == Direction.NORTHWEST) this.parentAnimation.data.backSpriteUsage = BackSpriteUsage.only;

		Point2D origin = new Point2D.Double(0, 0);
		this.travel = new TravelAnimation(origin, d.move(origin));
	}

	@Override
	public void update()
	{
		this.travel.update(this.completion());
		this.setSpriteLocation((int) (this.travel.current().getX() * AbstractDungeonTileset.TILE_SIZE),
				(int) (this.travel.current().getY() * AbstractDungeonTileset.TILE_SIZE));
	}

}
