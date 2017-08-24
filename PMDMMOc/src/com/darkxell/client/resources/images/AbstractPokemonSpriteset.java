package com.darkxell.client.resources.images;

import java.awt.image.BufferedImage;

import com.darkxell.client.resources.Res;

public abstract class AbstractPokemonSpriteset {

	private BufferedImage sprites;
	public final int spriteWidth;
	public final int spriteHeight;
	public final int gravityX;
	public final int gravityY;
	public final int[] iddleAnimation;
	public final int moveFrames;
	public final int attackFrames;
	public final int specialFrames;
	public final int special2Frames;
	public final boolean hasAmbiantSprites;
	public final int ambiantwakeFrames;
	public final int ambiantvictoryFrames;

	public AbstractPokemonSpriteset(String path, int gravityX, int gravityY, int width, int height,
			int[] iddleAnimation, int moveFrames, int attackFrames, int specialFrames, int special2Frames,
			boolean hasAmbiantSprites, int ambiantwakeFrames, int ambiantvictoryFrames) {
		this.spriteWidth = width;
		this.spriteHeight = height;
		this.iddleAnimation = iddleAnimation;
		this.moveFrames = moveFrames;
		this.attackFrames = attackFrames;
		this.specialFrames = specialFrames;
		this.special2Frames = special2Frames;
		this.hasAmbiantSprites = hasAmbiantSprites;
		this.ambiantwakeFrames = ambiantwakeFrames;
		this.ambiantvictoryFrames = ambiantvictoryFrames;
		this.gravityX = gravityX;
		this.gravityY = gravityY;
		this.sprites = Res.getBase(path);
	}

	public AbstractPokemonSpriteset(String path, int gravityX, int gravityY, int size,
			int[] iddleAnimation, int moveFrames, int attackFrames, int specialFrames, int special2Frames,
			boolean hasAmbiantSprites, int ambiantwakeFrames, int ambiantvictoryFrames) {
		this(path, gravityX, gravityY, size, size, iddleAnimation, moveFrames, attackFrames, specialFrames, special2Frames, hasAmbiantSprites, ambiantwakeFrames, ambiantvictoryFrames);
	}

	public static final byte FACING_N = 0;
	public static final byte FACING_NE = 1;
	public static final byte FACING_E = 2;
	public static final byte FACING_SE = 3;
	public static final byte FACING_S = 4;
	public static final byte FACING_SW = 5;
	public static final byte FACING_W = 6;
	public static final byte FACING_NW = 7;

	public BufferedImage getSleepSprite(byte facing, int variant) {
		return this.sprites.getSubimage(variant * spriteWidth, 0, spriteWidth, spriteHeight);
	}

	public BufferedImage getHurtSprite(byte facing, int variant) {
		return this.sprites.getSubimage(variant * spriteWidth, spriteHeight, spriteWidth, spriteHeight);
	}

	public BufferedImage getIddleSprite(byte facing, int variant) {
		return this.sprites.getSubimage(variant * spriteWidth, spriteHeight * 2, spriteWidth, spriteHeight);
	}

	public BufferedImage getMoveSprite(byte facing, int variant) {
		return this.sprites.getSubimage((variant + 4) * spriteWidth, spriteHeight * (2 + facing), spriteWidth,
				spriteHeight);
	}

	public BufferedImage getAttackSprite(byte facing, int variant) {
		return this.sprites.getSubimage((variant + 8) * spriteWidth, spriteHeight * (2 + facing), spriteWidth,
				spriteHeight);
	}

	public BufferedImage getSpecialSprite(byte facing, int variant) {
		return this.sprites.getSubimage(variant * spriteWidth, spriteHeight * (10 + facing), spriteWidth, spriteHeight);
	}

	public BufferedImage getSpecial2Sprite(byte facing, int variant) {
		return this.sprites.getSubimage((variant + 8) * spriteWidth, spriteHeight * (10 + facing), spriteWidth,
				spriteHeight);
	}

	public BufferedImage getRestSprite(byte facing, int variant) {
		if (hasAmbiantSprites)
			return this.sprites.getSubimage(variant * spriteWidth, spriteHeight * (18 + facing), spriteWidth,
					spriteHeight);
		return this.sprites.getSubimage(variant * spriteWidth, 0, spriteWidth, spriteHeight);
	}

	public BufferedImage getWakeSprite(byte facing, int variant) {
		if (hasAmbiantSprites)
			return this.sprites.getSubimage((variant + 2) * spriteWidth, spriteHeight * (18 + facing), spriteWidth,
					spriteHeight);
		return null;
	}

	public BufferedImage getdeathSprite(byte facing, int variant) {
		if (hasAmbiantSprites)
			return this.sprites.getSubimage(variant * spriteWidth, spriteHeight * (19 + facing), spriteWidth,
					spriteHeight);
		return null;
	}

	public BufferedImage getVictorySprite(byte facing, int variant) {
		if (hasAmbiantSprites)
			return this.sprites.getSubimage((variant + 2) * spriteWidth, spriteHeight * (19 + facing), spriteWidth,
					spriteHeight);
		return null;
	}

	public BufferedImage getJewelsSprite(byte facing, int variant) {
		if (hasAmbiantSprites)
			return this.sprites.getSubimage((variant + 5) * spriteWidth, spriteHeight * (19 + facing), spriteWidth,
					spriteHeight);
		return null;
	}

}
