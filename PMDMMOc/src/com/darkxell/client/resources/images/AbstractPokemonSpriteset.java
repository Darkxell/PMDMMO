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

	/**
	 * Creates a new spritesheet for a pokemon. A spritesheet contains al the
	 * textures needed for a pokemon at any point.
	 * 
	 * @param path
	 *            The path of the source image used for the tileset.
	 * @param gravityX
	 *            The X coordinate for the center of gravity of the sprite. The
	 *            center of gravity is the center of the pokemon hitbox, and
	 *            draw method when drawn.
	 * @param gravityY
	 *            The Y coordinate for the center of gravity of the sprite. The
	 *            center of gravity is the center of the pokemon hitbox, and
	 *            draw method when drawn.
	 * @param width
	 *            The width of each sprite in the tileset
	 * @param height
	 *            The height of each sprite in the tileset
	 * @param iddleAnimation
	 *            An array of integers describing how the iddle animation is
	 *            supposed to play. This array should contain an entry for each
	 *            iddle sprite, corresponding to the time (in 60th of seconds)
	 *            needed to go to the next sprite in the iddle animation loop.
	 * @param moveFrames
	 *            Describes how many sprites this pokemon has for it's move
	 *            animation.
	 * @param attackFrames
	 *            Describes how many sprites this pokemon has for it's attack
	 *            animation.
	 * @param specialFrames
	 *            Describes how many sprites this pokemon has for it's special
	 *            animation.
	 * @param special2Frames
	 *            Describes how many sprites this pokemon has for it's secondary
	 *            special animation. Note that this value might be 0, meaning
	 *            the pokemon doesn't have a second special animation.
	 * @param hasAmbiantSprites
	 *            Is true if the pokemon has flavor sprites, such as eating,
	 *            acting, enhanced sleeping and more. This is the case for
	 *            starters pokemons that might use these sprites during
	 *            cutscenes.
	 * @param ambiantwakeFrames
	 *            Describes how many sprites this pokemon has for it's waking
	 *            animation, if the pokemon has ambiant sprites. 0 otherwise.
	 * @param ambiantvictoryFrames
	 *            Describes how many sprites this pokemon has for it's victory
	 *            animation, if the pokemon has ambiant sprites. 0 otherwise.
	 */
	protected AbstractPokemonSpriteset(String path, int gravityX, int gravityY, int width, int height,
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

	/**
	 * Creates a new spritesheet for a pokemon. A spritesheet contains al the
	 * textures needed for a pokemon at any point. This constructor doesn't
	 * allow parsing of ambiant sprites or complex sizes, see the full
	 * constructor for details.
	 * 
	 * @param path
	 *            The path of the source image used for the tileset.
	 * @param gravityX
	 *            The X coordinate for the center of gravity of the sprite. The
	 *            center of gravity is the center of the pokemon hitbox, and
	 *            draw method when drawn.
	 * @param gravityY
	 *            The Y coordinate for the center of gravity of the sprite. The
	 *            center of gravity is the center of the pokemon hitbox, and
	 *            draw method when drawn.
	 * @param size
	 *            The height and width of each sprite in the tileset. To specify
	 *            a different height and width, use the complete constructor.
	 * @param iddleAnimation
	 *            An array of integers describing how the iddle animation is
	 *            supposed to play. This array should contain an entry for each
	 *            iddle sprite, corresponding to the time (in 60th of seconds)
	 *            needed to go to the next sprite in the iddle animation loop.
	 * @param moveFrames
	 *            Describes how many sprites this pokemon has for it's move
	 *            animation.
	 * @param attackFrames
	 *            Describes how many sprites this pokemon has for it's attack
	 *            animation.
	 * @param specialFrames
	 *            Describes how many sprites this pokemon has for it's special
	 *            animation.
	 * @param special2Frames
	 *            Describes how many sprites this pokemon has for it's secondary
	 *            special animation. Note that this value might be 0, meaning
	 *            the pokemon doesn't have a second special animation.
	 */
	protected AbstractPokemonSpriteset(String path, int gravityX, int gravityY, int size, int[] iddleAnimation,
			int moveFrames, int attackFrames, int specialFrames, int special2Frames) {
		this(path, gravityX, gravityY, size, size, iddleAnimation, moveFrames, attackFrames, specialFrames,
				special2Frames, false, 0, 0);
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
		return this.sprites.getSubimage(facing * spriteWidth, spriteHeight, spriteWidth, spriteHeight);
	}

	public BufferedImage getIddleSprite(byte facing, int variant) {
		return this.sprites.getSubimage(variant * spriteWidth, spriteHeight * (2+facing), spriteWidth, spriteHeight);
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
