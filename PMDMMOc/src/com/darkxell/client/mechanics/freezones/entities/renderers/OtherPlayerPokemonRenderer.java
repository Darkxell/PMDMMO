package com.darkxell.client.mechanics.freezones.entities.renderers;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.freezones.entities.OtherPlayerEntity;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.renderers.pokemon.FreezonePokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpriteFrame;

public class OtherPlayerPokemonRenderer extends FreezonePokemonRenderer
{

	public OtherPlayerPokemonRenderer(OtherPlayerEntity entity, PokemonSprite sprite)
	{
		super(entity, sprite);
	}

	@Override
	public void render(Graphics2D g, int width, int height)
	{
		super.render(g, width, height);
		PokemonSpriteFrame frame = this.sprite.getCurrentFrame();
		String name = ((OtherPlayerEntity) entity).name;
		int namewidth = TextRenderer.width(name);
		TextRenderer.render(g, name, (int) (this.drawX() - (namewidth / 2)), (int) (this.drawY() + frame.spriteY - 20));

	}

}
