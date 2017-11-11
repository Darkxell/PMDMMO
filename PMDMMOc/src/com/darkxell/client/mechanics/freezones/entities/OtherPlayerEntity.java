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
	/** The nano timestamp of the last update. */
	public long lastupdate;

	public OtherPlayerEntity(double x, double y, int spriteID, String name, long timestamp) {
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
		int namewidth = TextRenderer.width(this.name);
		TextRenderer.render(g, this.name, (int) (super.posX * 8 - (namewidth / 2)),
				(int) (super.posY * 8 - sprite.pointer.gravityY - 20));
	}

	@Override
	public void update() {
		this.sprite.update();
		// Calculates the movespeed of the pokemon
		double movespeed = 0.2d;
		boolean up = false, right = false, down = false, left = false;
		if (destinationX > posX + 5 || destinationX < posX - 5 || destinationY > posY + 5 || destinationY < posY - 5)
			movespeed *= 2;
		// Moves the pokemon accordingly
		if (destinationX > posX + 0.5) {
			posX += movespeed;
			right = true;
		} else if (destinationX < posX - 0.5) {
			posX -= movespeed;
			left = true;
		}
		if (destinationY > posY + 0.5) {
			posY += movespeed;
			down = true;
		} else if (destinationY < posY - 0.5) {
			posY -= movespeed;
			up = true;
		}
		// Sets the rotation of the pokemonSprite used
		if (up && right)
			this.sprite.setFacingDirection(PokemonSprite.FACING_NE);
		else if (right && down)
			this.sprite.setFacingDirection(PokemonSprite.FACING_SE);
		else if (down && left)
			this.sprite.setFacingDirection(PokemonSprite.FACING_SW);
		else if (left && up)
			this.sprite.setFacingDirection(PokemonSprite.FACING_NW);
		else if (up)
			this.sprite.setFacingDirection(PokemonSprite.FACING_N);
		else if (right)
			this.sprite.setFacingDirection(PokemonSprite.FACING_E);
		else if (down)
			this.sprite.setFacingDirection(PokemonSprite.FACING_S);
		else if (left)
			this.sprite.setFacingDirection(PokemonSprite.FACING_W);
		// Sets the running/idle state
		if (up || right || down || left)
			this.sprite.setState(PokemonSprite.STATE_MOVE);
		else
			this.sprite.setState(PokemonSprite.STATE_IDLE);
	}

	public void applyServerUpdate(double x, double y, int spriteID) {
		this.destinationX = x;
		this.destinationY = y;
		if (this.spriteID != spriteID) {
			this.spriteID = spriteID;
			this.sprite = new PokemonSprite(PokemonSpritesets.getSpriteset(spriteID));
		}
		this.lastupdate = System.nanoTime();
	}

}
