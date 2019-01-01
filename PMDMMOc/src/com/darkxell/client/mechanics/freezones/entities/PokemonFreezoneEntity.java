package com.darkxell.client.mechanics.freezones.entities;

import com.darkxell.client.renderers.AbstractRenderer;
import com.darkxell.client.renderers.pokemon.FreezonePokemonRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;
import com.darkxell.client.state.dialog.DialogScreen;
import com.darkxell.common.util.Direction;

public class PokemonFreezoneEntity extends DialogEntity {

	/** The intelligent sprite of the pokemon. */
	protected PokemonSprite pkmnsprite;

	public PokemonFreezoneEntity(double x, double y, int spriteid) {
		this(x, y, new PokemonSprite(PokemonSpritesets.getSpriteset(spriteid)));
	}

	public PokemonFreezoneEntity(double x, double y, int spriteid, Direction facing) {
		this(x, y, new PokemonSprite(PokemonSpritesets.getSpriteset(spriteid)));
	}

	public PokemonFreezoneEntity(double x, double y, int spriteid, Direction facing, DialogScreen... dialogs) {
		this(x, y, new PokemonSprite(PokemonSpritesets.getSpriteset(spriteid)), facing, dialogs);
	}

	public PokemonFreezoneEntity(double x, double y, PokemonSprite sprite) {
		this(x, y, sprite, Direction.SOUTH, null);
	}

	public PokemonFreezoneEntity(double x, double y, PokemonSprite sprite, Direction facing, DialogScreen[] dialogs) {
		super(true, x, y, dialogs);
		this.pkmnsprite = sprite;
		this.pkmnsprite.setFacingDirection(facing);
	}

	@Override
	public AbstractRenderer createRenderer() {
		return new FreezonePokemonRenderer(this, this.pkmnsprite);
	}

}
