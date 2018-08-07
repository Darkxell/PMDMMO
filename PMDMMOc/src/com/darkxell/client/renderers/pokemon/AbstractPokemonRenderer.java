package com.darkxell.client.renderers.pokemon;

import static com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset.TILE_SIZE;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.AbstractAnimation;
import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.MasterDungeonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpriteFrame;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.resources.images.pokemon.ShadowSprites;
import com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Logger;

/** Renders a Pokemon. This Renderer's Coordinates' units are Tiles. */
public class AbstractPokemonRenderer extends AbstractRenderer
{

	/** Renders a Pokemon at the input x, y (centered) coordinates. width and height are the dimensions of the draw area. */
	public static void render(Graphics2D g, PokemonSprite sprite, int x, int y)
	{
		PokemonSpriteFrame frame = sprite.getCurrentFrame();
		BufferedImage s = sprite.getCurrentSprite();

		int xPos = x - s.getWidth() / 2 + frame.spriteX, yPos = y - s.getHeight() / 2 + frame.spriteY;

		g.drawImage(s, (frame.isFlipped ? s.getWidth() : 0) + xPos, yPos, (frame.isFlipped ? -1 : 1) * s.getWidth(), s.getHeight(), null);
	}

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
			PokemonSpriteFrame frame = this.sprite.getCurrentFrame();

			int xPos = (int) this.drawX(), yPos = (int) this.drawY();

			BufferedImage shadow = this.sprite.pointer.hasBigShadow ? ShadowSprites.instance.getBig(this.sprite.getShadowColor())
					: ShadowSprites.instance.getSmall(this.sprite.getShadowColor());
			g.drawImage(shadow, xPos - shadow.getWidth() / 2 + frame.shadowX, yPos + TILE_SIZE * 2 / 5 - shadow.getHeight() + frame.shadowY, null);

			for (PokemonAnimation animation : this.animations)
				animation.prerender(g, width, height);

			render(g, this.sprite, xPos, yPos);

			for (PokemonAnimation animation : this.animations)
				animation.postrender(g, width, height);
		} else Logger.w("Tried to render null sprite.");
	}

	@Override
	public boolean shouldRender(int width, int height)
	{
		double screenX = Persistance.dungeonState.pokemonRenderer.x();
		double screenY = Persistance.dungeonState.pokemonRenderer.y();
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
