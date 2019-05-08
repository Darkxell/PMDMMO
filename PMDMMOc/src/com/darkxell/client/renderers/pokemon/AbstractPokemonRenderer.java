package com.darkxell.client.renderers.pokemon;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.resources.images.Sprites.Res_Dungeon;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpriteFrame;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Logger;

/** Renders a Pokemon. This Renderer's Coordinates' units are Tiles. */
public class AbstractPokemonRenderer extends AbstractRenderer
{

	private float alpha = 1;
	private final ArrayList<PokemonAnimation> animations = new ArrayList<PokemonAnimation>();
	protected PokemonSprite sprite;

	public AbstractPokemonRenderer(Pokemon pokemon)
	{
		this(new PokemonSprite(PokemonSpritesets.getSpriteset(pokemon)));
	}

	public AbstractPokemonRenderer(PokemonSprite sprite)
	{
		super(0, 0, MasterDungeonRenderer.LAYER_POKEMON);
		this.sprite = sprite;
	}

	public void addAnimation(PokemonAnimation animation)
	{
		this.animations.add(animation);
	}

	protected PokemonAnimation[] animations()
	{
		return this.animations.toArray(new PokemonAnimation[this.animations.size()]);
	}

	public void clearAnimations()
	{
		this.animations.clear();
	}

	/** @return <code>true</code> if this Pokemon has an Animation with the input source. */
	public boolean hasAnimation(Object source)
	{
		for (PokemonAnimation a : this.animations)
			if (a.source == source) return true;
		return false;
	}

	public void removeAnimation(Object source)
	{
		this.animations.removeIf(new Predicate<AbstractAnimation>() {
			@Override
			public boolean test(AbstractAnimation t)
			{
				return t.source == source;
			}
		});
	}

	public void removeAnimation(PokemonAnimation animation)
	{
		this.animations.remove(animation);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		if (this.sprite.getCurrentSprite() != null)
		{
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.alpha);
			Composite c = g.getComposite();
			if (this.alpha != 1) g.setComposite(ac);

			PokemonSpriteFrame frame = this.sprite.getCurrentFrame();

			int xPos = (int) this.drawX(), yPos = (int) this.drawY();

			BufferedImage shadow = this.sprite.pointer.data.hasBigShadow ? Res_Dungeon.shadows.getBig(this.sprite.getShadowColor())
					: Res_Dungeon.shadows.getSmall(this.sprite.getShadowColor());
			g.drawImage(shadow, xPos - shadow.getWidth() / 2 + frame.shadowX, yPos + TILE_SIZE * 2 / 5 - shadow.getHeight() + frame.shadowY, null);

			for (int i = 0; i < this.animations.size(); ++i)
				this.animations.get(i).prerender(g, width, height);

			this.render(g, this.sprite, xPos, yPos);

			for (int i = 0; i < this.animations.size(); ++i)
				this.animations.get(i).postrender(g, width, height);

			if (this.alpha != 1) g.setComposite(c);
		} else Logger.w("Tried to render null sprite.");
	}

	/** Renders a Pokemon at the input x, y (centered) coordinates. width and height are the dimensions of the draw area. */
	public void render(Graphics2D g, PokemonSprite sprite, int x, int y)
	{
		// If you change those temp variables, check DungeonPokemonRenderer override
		PokemonSpriteFrame frame = sprite.getCurrentFrame();
		BufferedImage s = sprite.getCurrentSprite();

		int xPos = x - s.getWidth() / 2 + frame.spriteX + this.sprite.xOffset(), yPos = y - s.getHeight() / 2 + frame.spriteY + this.sprite.yOffset();
		if (sprite.getState().hasDash)
		{
			Point2D direction = sprite.getFacingDirection().move(0, 0);
			double dash = sprite.dashOffset();
			direction.setLocation(direction.getX() * TILE_SIZE * dash, direction.getY() * TILE_SIZE * dash);
			xPos += direction.getX();
			yPos += direction.getY();
		}
		g.drawImage(s, (frame.isFlipped ? s.getWidth() : 0) + xPos, yPos, (frame.isFlipped ? -1 : 1) * s.getWidth(), s.getHeight(), null);
	}

	public void setAlpha(float alpha)
	{
		this.alpha = alpha;
	}

	@Override
	public boolean shouldRender(int width, int height)
	{
		double screenX = Persistence.dungeonState.pokemonRenderer.x();
		double screenY = Persistence.dungeonState.pokemonRenderer.y();
		return this.x() + AbstractDungeonTileset.TILE_SIZE >= screenX - 1 && this.x() <= screenX + width + 1
				&& this.y() + AbstractDungeonTileset.TILE_SIZE >= screenY - 1 && this.y() <= screenY + height + 1;
	}

	public PokemonSprite sprite()
	{
		return this.sprite;
	}

	@Override
	public void update()
	{
		super.update();
		this.sprite.update();
	}

}
