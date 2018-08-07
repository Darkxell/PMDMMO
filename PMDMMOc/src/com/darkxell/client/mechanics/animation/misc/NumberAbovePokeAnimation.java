package com.darkxell.client.mechanics.animation.misc;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.TextRenderer.FontMode;
import com.darkxell.common.pokemon.DungeonPokemon;

public class NumberAbovePokeAnimation extends PokemonAnimation
{

	public final FontMode fontMode;
	public final int number;

	public NumberAbovePokeAnimation(DungeonPokemon target, int number, FontMode fontMode)
	{
		super(target, 60, null);
		this.number = number;
		this.fontMode = fontMode;
	}

	private double getHealthPos()
	{
		return (60 - this.tick()) / 4;
	}

	@Override
	public void postrender(Graphics2D g, int width, int height)
	{
		super.postrender(g, width, height);

		String text = (this.number < 0 ? "" : "+") + Integer.toString(this.number);
		int xPos = (int) (this.x - TextRenderer.width(text) / 2);
		int yPos = (int) (this.y + this.getHealthPos() - this.renderer.sprite().getCurrentSprite().getHeight() / 2 - TextRenderer.height() / 2);
		TextRenderer.render(g, text, xPos, yPos, this.fontMode);
	}

}
