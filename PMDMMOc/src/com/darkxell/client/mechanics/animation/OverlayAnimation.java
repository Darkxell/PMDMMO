package com.darkxell.client.mechanics.animation;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;

public class OverlayAnimation extends AbstractAnimation
{
	public static final int FADE = 10;

	private int fadeTick = 0;
	public final int id;
	private AbstractAnimation listened;
	private SpritesetAnimation[] sprites;

	public OverlayAnimation(int id, AbstractAnimation listened, AnimationEndListener listener)
	{
		super(-1, listener);
		this.id = id;
		this.listened = listened;
	}

	@Override
	public boolean isOver()
	{
		return this.listened.isOver() && this.fadeTick == 0;
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		if (this.sprites == null)
		{
			SpritesetAnimation a = (SpritesetAnimation) Animations.getCustomAnimation(null, this.id, null);
			int cols = width / a.spriteset.spriteWidth + 1, lines = height / a.spriteset.spriteHeight + 1;
			if (width % a.spriteset.spriteWidth != 0) ++cols;
			if (height % a.spriteset.spriteHeight != 0) ++lines;
			this.sprites = new SpritesetAnimation[lines * cols];
			for (int x = 0; x < cols; ++x)
				for (int y = 0; y < lines; ++y)
				{
					this.sprites[x + y * cols] = (SpritesetAnimation) Animations.getCustomAnimation(null, this.id, null);
					this.sprites[x + y * cols].setXY(x * a.spriteset.spriteWidth, y * a.spriteset.spriteHeight);
				}
		}

		float alpha = this.fadeTick <= OverlayAnimation.FADE ? this.fadeTick * 1f / OverlayAnimation.FADE : 0;
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		Composite c = g.getComposite();
		if (alpha != 1) g.setComposite(ac);
		for (SpritesetAnimation animation : this.sprites)
			animation.postrender(g, width, height);
		if (alpha != 1) g.setComposite(c);
	}

	@Override
	public void update()
	{
		super.update();
		if (this.sprites != null)
		{
			for (SpritesetAnimation animation : this.sprites)
				animation.update();
		}

		if (this.listened.isOver())
		{
			if (!this.isOver()) --this.fadeTick;
		} else if (this.fadeTick < OverlayAnimation.FADE) ++this.fadeTick;
	}

}
