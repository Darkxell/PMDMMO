package com.darkxell.client.mechanics.freezone.entity;

import com.darkxell.client.graphics.AbstractRenderer;
import com.darkxell.client.graphics.renderer.FreezonePokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.XMLUtils;
import org.jdom2.Element;

class PokemonFreezoneEntity extends DialogEntity {
	private PokemonSprite sprite;

	{
		this.solid = true;
	}

	@Override
	protected void deserialize(Element el) {
		super.deserialize(el);

		int id = XMLUtils.getAttribute(el, "id", 1);
		int directionID = XMLUtils.getAttribute(el, "direction", 0);

		this.sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(id));
		this.sprite.setFacingDirection(Direction.DIRECTIONS.get(directionID));
	}

	@Override
	public AbstractRenderer createRenderer() {
		return new FreezonePokemonRenderer(this, this.sprite);
	}

}
