package com.darkxell.client.mechanics.freezones.entities;

import java.awt.Graphics2D;

import com.darkxell.client.mechanics.freezones.FreezoneEntity;
import com.darkxell.client.renderers.TextRenderer;
import com.darkxell.client.resources.images.pokemon.PokemonSprite;
import com.darkxell.client.resources.images.pokemon.PokemonSpritesets;

public class OtherPlayerEntity extends FreezoneEntity {

	private int spriteID;
	private PokemonSprite sprite;
	public final String name;
	private double destinationX;
	private double destinationY;

	public OtherPlayerEntity(double x, double y, int spriteID, String name) {
		super(false, true, x, y);
		this.sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(spriteID));
		this.spriteID = spriteID;
		this.name = name;
		this.destinationX = x;
		this.destinationY = y;
	}

	@Override
	public void onInteract() {

	}

	@Override
	public void print(Graphics2D g) {
		g.drawImage(sprite.getCurrentSprite(), (int) (super.posX * 8 - sprite.pointer.gravityX),
				(int) (super.posY * 8 - sprite.pointer.gravityY), null);
		int namewidth = TextRenderer.instance.width(this.name);
		TextRenderer.instance.render(g, this.name,(int)(super.posX * 8 - (namewidth / 2)), (int)(super.posY * 8 - sprite.pointer.gravityY - 20));
	}

	@Override
	public void update() {
		this.sprite.update();
		double movespeed = 0.2d;
		if (destinationX > posX + 1)
			posX += movespeed;
		else if (destinationX < posX - 1)
			posX -= movespeed;
		if (destinationY > posY + 1)
			posY += movespeed;
		else if (destinationY < posY - 1)
			posY -= movespeed;

	}

	public void applyServerUpdate(double x, double y, int spriteID) {
		this.destinationX = x;
		this.destinationY = y;
		if (this.spriteID != spriteID) {
			this.spriteID = spriteID;
			this.sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(spriteID));
		}
	}

}
