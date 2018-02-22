package com.darkxell.client.renderers.floor;

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
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpriteFrame;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.resources.images.pokemon.ShadowSprites;
import com.darkxell.client.resources.images.tilesets.AbstractDungeonTileset;
import com.darkxell.common.pokemon.DungeonPokemon;

/** Renders a Pok�mon. This Renderer's Coordinates' units are Tiles. */
public class PokemonRenderer extends AbstractRenderer
{

	private final ArrayList<PokemonAnimation> animations = new ArrayList<PokemonAnimation>();
	public final DungeonPokemon pokemon;
	public final PokemonSprite sprite;

	public PokemonRenderer(DungeonPokemon pokemon)
	{
		super(pokemon.tile().x, pokemon.tile().y, MasterDungeonRenderer.LAYER_POKEMON);
		this.pokemon = pokemon;
		this.sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(this.pokemon.pokemon));
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
		if (!Persistance.dungeonState.floorVisibility.isVisible(this.pokemon)) return;

		if (this.pokemon.stateChanged)
		{
			this.sprite.setFacingDirection(this.pokemon.facing());
			pokemon.stateChanged = false;
		}

		if (this.sprite.getCurrentSprite() != null)
		{
			PokemonSpriteFrame frame = this.sprite.getCurrentFrame();
			BufferedImage sprite = this.sprite.getCurrentSprite();

			int xPos = (int) (this.x() + TILE_SIZE / 2 - sprite.getWidth() / 2 + frame.spriteX),
					yPos = (int) (this.y() + TILE_SIZE / 2 - sprite.getHeight() / 2 + frame.spriteY);

			BufferedImage shadow = this.sprite.pointer.hasBigShadow ? ShadowSprites.instance.getBig(this.sprite.getShadowColor())
					: ShadowSprites.instance.getSmall(this.sprite.getShadowColor());
			g.drawImage(shadow, (int) this.x() + TILE_SIZE / 2 - shadow.getWidth() / 2 + frame.shadowX,
					(int) this.y() + TILE_SIZE * 9 / 10 - shadow.getHeight() + frame.shadowY, null);

			for (PokemonAnimation animation : this.animations)
				animation.prerender(g, width, height);

			g.drawImage(sprite, (frame.isFlipped ? sprite.getWidth() : 0) + xPos, yPos, (frame.isFlipped ? -1 : 1) * sprite.getWidth(), sprite.getHeight(),
					null);

			int h = this.sprite.getHealthChange();
			if (h != 0)
			{
				String text = (h < 0 ? "" : "+") + Integer.toString(h);
				xPos = (int) (this.x() + TILE_SIZE / 2 - TextRenderer.width(text) / 2);
				yPos = (int) (this.y() - this.sprite.getHealthPos() - TextRenderer.height() / 2);
				TextRenderer.render(g, text, xPos, yPos, true);
			}

			for (PokemonAnimation animation : this.animations)
				animation.postrender(g, width, height);
		}
	}

	@Override
	public boolean shouldRender(int width, int height)
	{
		double screenX = Persistance.dungeonState.pokemonRenderer.x();
		double screenY = Persistance.dungeonState.pokemonRenderer.y();
		return this.x() + AbstractDungeonTileset.TILE_SIZE >= screenX - 1 && this.x() <= screenX + width + 1
				&& this.y() + AbstractDungeonTileset.TILE_SIZE >= screenY - 1 && this.y() <= screenY + height + 1;
	}

	@Override
	public double x()
	{
		return super.x() * TILE_SIZE;
	}

	@Override
	public double y()
	{
		return super.y() * TILE_SIZE;
	}

}
