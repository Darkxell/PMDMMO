package com.darkxell.client.mechanics.freezones.entities;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.resources.images.PokemonSprite;

public class PokemonFreezoneEntity extends FreezoneEntity {

	/** The intelligent sprite of the pokemon. */
	private PokemonSprite pkmnsprite;

	public PokemonFreezoneEntity(int x, int y, PokemonSprite sprite) {
		super(true, true, x, y);
		this.pkmnsprite = sprite;
	}

	@Override
	public void onInteract() {
		// TODO : Generate text!
	}

	@Override
	public void print(Graphics2D g) {
		g.drawImage(pkmnsprite.getCurrentSprite(), (int) (super.posX * 8 - pkmnsprite.pointer.gravityX),
				(int) (super.posY * 8 - pkmnsprite.pointer.gravityY), null);
	}

	@Override
	public void update() {
		pkmnsprite.update();
	}

}
