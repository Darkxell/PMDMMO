package com.darkxell.client.mechanics.animation.misc;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;

import com.darkxell.client.mechanics.animation.PokemonAnimation;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.TextRenderer.FontMode;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.language.Message;

public class TextAbovePokeAnimation extends PokemonAnimation
{
	public static final int DURATION = 60;
	public static final int FADE = 40;

	public final FontMode fontMode;
	public final Message text;

	public TextAbovePokeAnimation(DungeonPokemon target, Message text, FontMode fontMode)
	{
		super(target, DURATION, null);
		this.text = text;
		this.fontMode = fontMode;
	}

	private double getHealthPos()
	{
		return (DURATION - this.tick()) / 4;
	}

	@Override
	public void postrender(Graphics2D g, int width, int height)
	{
		super.postrender(g, width, height);

		float alpha = 1;
		if (this.tick() >= FADE) alpha = (DURATION - this.tick()) * 1f / (DURATION - FADE);

		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		Composite c = g.getComposite();
		if (alpha != 1) g.setComposite(ac);

		int xPos = (int) (this.x - TextRenderer.width(this.text.toString(), this.fontMode) / 2);
		int yPos = (int) (this.y + this.getHealthPos() - this.renderer.sprite().getCurrentSprite().getHeight() / 2 - TextRenderer.height() / 2);
		TextRenderer.render(g, this.text.toString(), xPos, yPos, this.fontMode);

		if (alpha != 1) g.setComposite(c);
	}

}
