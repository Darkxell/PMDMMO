package com.darkxell.client.mechanics.animation;

import java.util.ArrayList;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.mechanics.animation.movement.PokemonAnimationMovement;
import com.darkxell.client.mechanics.animation.spritemovement.SpritesetAnimationMovement;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.XMLUtils;

public class AnimationData
{
	String[] alsoPlay = new String[0];
	int[] alsoPlayDelay = new int[0];
	String animationMovement = null;
	BackSpriteUsage backSprites = BackSpriteUsage.no;
	String clones = null;
	private int delayTime = -1;
	int id;
	int loopsFrom = 0;
	int overlay = -1;
	String pokemonMovement = null;
	PokemonSpriteState pokemonState = null;
	int pokemonStateDelay = 0;
	/** The ID of the sound to play when this Animation is played. */
	protected String sound = null;
	/** The number of ticks to wait before playing the sound. */
	int soundDelay = 0;
	int spriteDuration = 2;
	int[] spriteOrder = null;
	String sprites = null;
	private String spritesPrefix;
	/** Variants depending on the orientation of the Pokémon, if any. */
	AnimationData[] variants = new AnimationData[Direction.values().length];
	/** Dimensions of a single sprite on the spritesheet, if any. */
	int width = 0, height = 0;
	int xOffset = -1, yOffset = -1;

	public AnimationData(int id)
	{
		this.id = id;
		for (int i = 0; i < this.variants.length; ++i)
			this.variants[i] = this;
	}

	public AnimationData(int id, String spritesPrefix, Element xml)
	{
		this.id = id;
		this.sprites = "/animations/" + this.id;
		this.spritesPrefix = spritesPrefix;
		this.load(xml, this);
		if (this.xOffset == -1) this.xOffset = this.width / 2;
		if (this.yOffset == -1) this.yOffset = this.height / 2;
		for (Direction d : Direction.directions)
		{
			if (xml.getChild(d.name().toLowerCase(), xml.getNamespace()) != null)
			{
				this.variants[d.index()] = new AnimationData(this.id);
				this.variants[d.index()].load(xml.getChild(d.name().toLowerCase(), xml.getNamespace()), this);
				this.variants[d.index()].spritesPrefix = this.spritesPrefix;
			}
		}
	}

	public PokemonAnimation createAnimation(DungeonPokemon target, AbstractPokemonRenderer renderer, AnimationEndListener listener)
	{
		if (this.clones != null) return Animations.getAnimation(target, this.clones, listener);

		PokemonAnimation a = null;
		if (this.sprites.equals("none"))
		{
			a = new PokemonAnimation(renderer, 0, listener);
			a.delayTime = this.delayTime;
		} else
		{
			String actualSprites = this.sprites;
			if (!this.sprites.contains("/")) actualSprites = this.spritesPrefix + this.sprites;
			actualSprites = "/animations" + actualSprites;

			RegularSpriteSet spriteset = new RegularSpriteSet(actualSprites + ".png", this.width, this.height, -1, -1);
			int[] order = this.spriteOrder.clone();
			if (order.length == 0 && spriteset.isLoaded())
			{
				order = new int[this.backSprites == BackSpriteUsage.yes ? spriteset.spriteCount() / 2 : spriteset.spriteCount()];
				for (int i = 0; i < order.length; ++i)
					order[i] = i;
			}
			a = new SpritesetAnimation(renderer, spriteset, this.backSprites, order, this.spriteDuration, this.xOffset, this.yOffset, listener);
			((SpritesetAnimation) a).spritesetMovement = SpritesetAnimationMovement.create(this.animationMovement, (SpritesetAnimation) a);
			((SpritesetAnimation) a).loopsFrom = this.loopsFrom;
			if (this.delayTime <= 0) a.delayTime = this.spriteDuration * order.length;
			else a.delayTime = this.delayTime;
		}

		if (a.delayTime <= 0) a.delayTime = 15;

		if (this.pokemonMovement != null)
		{
			a.movement = PokemonAnimationMovement.create(a, target, this.pokemonMovement);
			a.duration = Math.max(a.duration, a.movement.duration);
		}

		if (this.alsoPlay.length > 0)
		{
			ArrayList<AbstractAnimation> anims = new ArrayList<>();
			ArrayList<Integer> delays = new ArrayList<>();
			AbstractAnimation tmp;

			for (int i = 0; i < this.alsoPlay.length; ++i)
			{
				if (this.alsoPlay[i].equals("")) continue;
				tmp = Animations.getAnimation(target, this.alsoPlay[i], null);
				if (tmp != null && !anims.contains(tmp))
				{
					anims.add(tmp);
					delays.add(this.alsoPlayDelay[i]);
				}
			}

			if (!anims.isEmpty())
			{
				CompoundAnimation anim = new CompoundAnimation(renderer, listener);
				anim.add(a, 0);
				for (int i = 0; i < anims.size(); ++i)
					anim.add(anims.get(i), delays.get(i));
				a = anim;
			}
		}

		if (this.overlay != -1)
		{
			OverlayAnimation overlay = new OverlayAnimation(this.overlay, a, listener);
			Persistance.dungeonState.staticAnimationsRenderer.add(overlay);
			overlay.start();
		}

		return a;
	}

	public AnimationData getVariant(Direction direction)
	{
		return this.variants[direction.index()];
	}

	private void load(Element xml, AnimationData defaultData)
	{
		this.clones = XMLUtils.getAttribute(xml, "clone", defaultData.clones);
		if (this.clones != null) return;

		this.sprites = XMLUtils.getAttribute(xml, "sprites", defaultData.sprites);
		if (this.sprites != null && this.sprites.equals("none")) this.sprites = null;
		if (this.sprites != null)
		{
			this.width = XMLUtils.getAttribute(xml, "width", defaultData.width);
			this.height = XMLUtils.getAttribute(xml, "height", defaultData.height);
			this.xOffset = XMLUtils.getAttribute(xml, "x", defaultData.xOffset);
			this.yOffset = XMLUtils.getAttribute(xml, "y", defaultData.yOffset);
			this.spriteDuration = XMLUtils.getAttribute(xml, "spriteduration", defaultData.spriteDuration);
			this.backSprites = BackSpriteUsage.valueOf(XMLUtils.getAttribute(xml, "backsprites", defaultData.backSprites.name()));
			this.spriteOrder = XMLUtils.readIntArray(xml);
			this.animationMovement = XMLUtils.getAttribute(xml, "movement", defaultData.animationMovement);
			this.loopsFrom = XMLUtils.getAttribute(xml, "loopsfrom", defaultData.loopsFrom);
		}

		this.delayTime = XMLUtils.getAttribute(xml, "delaytime", defaultData.delayTime);

		this.sound = XMLUtils.getAttribute(xml, "sound", defaultData.sound);
		if (this.sound != null && this.sound.equals("null")) this.sound = null;
		this.soundDelay = XMLUtils.getAttribute(xml, "sounddelay", defaultData.soundDelay);

		String state = XMLUtils.getAttribute(xml, "state", "null");
		this.pokemonState = state.equals("null") ? defaultData.pokemonState : state.equals("none") ? null : PokemonSpriteState.valueOf(state.toUpperCase());
		this.pokemonStateDelay = XMLUtils.getAttribute(xml, "statedelay", defaultData.pokemonStateDelay);

		this.pokemonMovement = XMLUtils.getAttribute(xml, "pkmnmovement", null);
		if (this.pokemonMovement == null && this.pokemonState != null && this.pokemonState != defaultData.pokemonState && this.pokemonState.hasDash)
			this.pokemonMovement = "dash";
		else if (this.pokemonMovement == null) this.pokemonMovement = defaultData.pokemonMovement;

		if (XMLUtils.getAttribute(xml, "alsoplay") != null)
		{
			this.alsoPlay = XMLUtils.getAttribute(xml, "alsoplay", "").split(",");
			this.alsoPlayDelay = new int[this.alsoPlay.length];
			String[] ds = XMLUtils.getAttribute(xml, "alsoplaydelay", "").split(",");
			if (ds.length < this.alsoPlay.length)
			{
				String[] dstmp = ds;
				ds = new String[this.alsoPlay.length];
				for (int i = 0; i < ds.length; ++i)
					ds[i] = i < dstmp.length ? dstmp[i] : "";
			}

			for (int i = 0; i < ds.length; ++i)
				if (!ds[i].equals("")) this.alsoPlayDelay[i] = Integer.parseInt(ds[i]);
		} else
		{
			this.alsoPlay = defaultData.alsoPlay.clone();
			this.alsoPlayDelay = defaultData.alsoPlayDelay.clone();
		}

		this.overlay = XMLUtils.getAttribute(xml, "overlay", defaultData.overlay);
	}

}
