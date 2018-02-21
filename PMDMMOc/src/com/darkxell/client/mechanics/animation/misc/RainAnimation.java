package com.darkxell.client.mechanics.animation.misc;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.AnimationEndListener;
import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.SpritesetAnimation;

public class RainAnimation extends AbstractAnimation
{

	public static final int DURATION = 120;

	private int dropDuration = -1;
	private HashMap<SpritesetAnimation, Boolean> drops;
	private final int spritesetID;

	public RainAnimation(int spritesetID, AnimationEndListener listener)
	{
		super(DURATION, listener);
		this.spritesetID = spritesetID;
		this.drops = new HashMap<SpritesetAnimation, Boolean>();
	}

	private void createDrop()
	{
		SpritesetAnimation a = (SpritesetAnimation) Animations.getCustomAnimation(null, this.spritesetID, null);
		this.drops.put(a, false);
		if (this.dropDuration == -1) this.dropDuration = a.duration();
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		for (SpritesetAnimation animation : this.drops.keySet())
		{
			if (!this.drops.get(animation))
			{
				animation.setXY(Math.random() * width - animation.spriteset.spriteWidth, Math.random() * height - animation.spriteset.spriteHeight
						* animation.spriteDuration);
				this.drops.put(animation, true);
			}
			animation.postrender(g, width, height);
		}
	}

	@Override
	public void update()
	{
		super.update();
		if (this.tick() < DURATION - this.dropDuration) this.createDrop();

		ArrayList<SpritesetAnimation> toremove = new ArrayList<SpritesetAnimation>();
		for (SpritesetAnimation animation : this.drops.keySet())
			if (this.drops.get(animation))
			{
				if (animation.isOver()) toremove.add(animation);
				animation.update();
				if (animation.index() == 0 || (this.spritesetID == 103 && animation.index() == 1)) animation.setXY(animation.getX() + 1, animation.getY() + 3);
			}

		for (SpritesetAnimation animation : toremove)
			this.drops.remove(animation);
	}

}
