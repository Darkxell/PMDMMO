package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.pokemon.FreezonePokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.XMLUtils;
import org.jdom2.Element;

class PokemonFreezoneEntity extends DialogEntity {
	private PokemonSprite sprite;

	{
		this.isSolid = true;
	}

	@Override
	protected void onInitialize(Element el) {
		super.onInitialize(el);

		int id = XMLUtils.getAttribute(el, "id", 1);
		int directionID = XMLUtils.getAttribute(el, "direction", 0);

		this.sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(id));
		this.sprite.setFacingDirection(Direction.directions[directionID]);
	}

	@Override
	public AbstractRenderer createRenderer() {
		return new FreezonePokemonRenderer(this, this.sprite);
	}

}
