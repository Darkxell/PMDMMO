package com.darkxell.client.mechanics.animation;

import java.util.ArrayList;
import java.util.Arrays;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.mechanics.animation.movement.PokemonAnimationMovement;
import com.darkxell.client.mechanics.animation.spritemovement.SpritesetAnimationMovement;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Direction;
import com.darkxell.common.util.XMLUtils;

public class AnimationData implements Comparable<AnimationData>
{
	/** List of animation IDs to play with this Animation. */
	public String[] alsoPlay = new String[0];
	/** For each other animation to play, the number of ticks to wait before starting it. */
	public int[] alsoPlayDelay = new int[0];
	/** The name of the movement of the animation. <code>null</code> if no movement. */
	public String animationMovement = null;
	/** Whether to display sprites in front of or behind the Pokemon if any. */
	public BackSpriteUsage backSpriteUsage = BackSpriteUsage.no;
	/** If not <code>null</code>, this Animation is an exact copy of the Animation with this ID. */
	public String clones = null;
	/** The time to wait for this Animation to play. */
	public int delayTime = 0;
	/** The x and y offset to apply to the sprites. -1 defaults to the center of the sprite. */
	public int gravityX = -1, gravityY = -1;
	/** The ID of this Animation. */
	public int id;
	/** If this Animation plays several times, the index in the order of the sprites to start at in the restarts. */
	public int loopsFrom = 0;
	/** If not -1, the ID of the animation to play as an overlay. */
	public int overlay = -1;
	/** If true and this is a Move Animation, this Animation plays for each target hit by the Move. */
	public boolean playsForEachTarget = false;
	/** The movement applied to the Pokemon, if any. */
	public String pokemonMovement = null;
	/** The state applied to the Pokemon, if any. */
	public PokemonSpriteState pokemonState = null;
	/** The delay before applying the Pokemon State. */
	public int pokemonStateDelay = 0;
	/** The ID of the sound to play when this Animation is played. */
	public String sound = null;
	/** The number of ticks to wait before playing the sound. */
	public int soundDelay = 0;
	/** The duration of each sprite. */
	public int spriteDuration = 2;
	/** The order of the sprites in the sprite set. */
	public int[] spriteOrder = null;
	/** The ID of the sprites. <code>null</code> if no sprites. */
	public String sprites = null;
	/** A prefix to apply to the sprites path. Does not apply if the sprites ID contains '/' */
	private String spritesPrefix;
	/** Variants depending on the orientation of the Pokemon, if any. */
	public AnimationData[] variants = new AnimationData[Direction.values().length];
	/** Dimensions of a single sprite on the spritesheet, if any. */
	public int width = 0, height = 0;

	public AnimationData(AnimationData data)
	{
		this.alsoPlay = data.alsoPlay;
		this.alsoPlayDelay = data.alsoPlayDelay;
		this.animationMovement = data.animationMovement;
		this.backSpriteUsage = data.backSpriteUsage;
		this.clones = data.clones;
		this.delayTime = data.delayTime;
		this.gravityX = data.gravityX;
		this.gravityY = data.gravityY;
		this.id = data.id;
		this.loopsFrom = data.loopsFrom;
		this.overlay = data.overlay;
		this.playsForEachTarget = data.playsForEachTarget;
		this.pokemonMovement = data.pokemonMovement;
		this.pokemonState = data.pokemonState;
		this.pokemonStateDelay = data.pokemonStateDelay;
		this.sound = data.sound;
		this.soundDelay = data.soundDelay;
		this.spriteDuration = data.spriteDuration;
		this.spriteOrder = data.spriteOrder;
		this.sprites = data.sprites;
		this.spritesPrefix = data.spritesPrefix;
		this.width = data.width;
		this.height = data.height;
	}

	public AnimationData(int id)
	{
		this.id = id;
		for (int i = 0; i < this.variants.length; ++i)
			this.variants[i] = new AnimationData(this);
	}

	public AnimationData(int id, String spritesPrefix, Element xml)
	{
		this.id = id;
		this.sprites = "" + this.id;
		this.spritesPrefix = spritesPrefix;
		this.clones = XMLUtils.getAttribute(xml, "clone", null);
		if (this.clones == null && xml.getChild("default", xml.getNamespace()) != null)
		{
			this.load(xml.getChild("default", xml.getNamespace()), this);
			if (this.gravityX == -1) this.gravityX = this.width / 2;
			if (this.gravityY == -1) this.gravityY = this.height / 2;
			for (Direction d : Direction.directions)
			{
				this.variants[d.index()] = new AnimationData(this);
				if (xml.getChild(d.name().toLowerCase(), xml.getNamespace()) != null)
				{
					this.variants[d.index()].load(xml.getChild(d.name().toLowerCase(), xml.getNamespace()), this);
					this.variants[d.index()].spritesPrefix = this.spritesPrefix;
				}
			}
		} else for (Direction d : Direction.directions)
			this.variants[d.index()] = new AnimationData(this);
	}

	@Override
	public int compareTo(AnimationData o)
	{
		return Integer.compare(this.id, o.id);
	}

	public PokemonAnimation createAnimation(DungeonPokemon dungeon, CutscenePokemon cutscene, AbstractPokemonRenderer renderer, AnimationEndListener listener)
	{
		if (this.clones != null)
		{
			if (dungeon != null) return Animations.getAnimation(dungeon, this.clones, listener);
			else return Animations.getCutsceneAnimation(Integer.parseInt(this.clones), cutscene, listener);
		}

		PokemonAnimation a = null;
		if (this.sprites == null)
		{
			a = new PokemonAnimation(this, renderer, 0, listener);
			a.delayTime = this.delayTime;
		} else
		{
			String actualSprites = this.sprites;
			if (!this.sprites.contains("/")) actualSprites = "/" + this.spritesPrefix + this.sprites;
			actualSprites = "/animations" + actualSprites;

			RegularSpriteSet spriteset = new RegularSpriteSet(actualSprites + ".png", this.width, this.height, -1, -1);
			int[] order = this.spriteOrder.clone();
			if (order.length == 0 && spriteset.isLoaded())
			{
				order = new int[this.backSpriteUsage == BackSpriteUsage.yes ? spriteset.spriteCount() / 2 : spriteset.spriteCount()];
				for (int i = 0; i < order.length; ++i)
					order[i] = i;
			}
			a = new SpritesetAnimation(this, renderer, spriteset, order, listener);
			((SpritesetAnimation) a).spritesetMovement = SpritesetAnimationMovement.create(this.animationMovement, (SpritesetAnimation) a);
			if (this.delayTime <= 0) a.delayTime = this.spriteDuration * order.length;
			else a.delayTime = this.delayTime;
		}

		if (a.delayTime <= 0) a.delayTime = 15;

		if (this.pokemonMovement != null && dungeon != null)
		{
			// TODO add state for each animation, defaultState was deleted
			// TODO add sprite path for each direction, oriented was deleted
			String movement = this.pokemonMovement;
			if (movement == null && this.pokemonState != null && this.pokemonState.hasDash) movement = "dash";
			a.movement = PokemonAnimationMovement.create(a, dungeon, movement);
			a.duration = Math.max(a.duration, a.movement.duration);
		}

		if (this.alsoPlay.length > 0 && dungeon != null)
		{
			ArrayList<AbstractAnimation> anims = new ArrayList<>();
			ArrayList<Integer> delays = new ArrayList<>();
			AbstractAnimation tmp;

			for (int i = 0; i < this.alsoPlay.length; ++i)
			{
				if (this.alsoPlay[i].equals("")) continue;
				tmp = Animations.getAnimation(dungeon, this.alsoPlay[i], null);
				if (tmp != null && !anims.contains(tmp))
				{
					anims.add(tmp);
					delays.add(this.alsoPlayDelay[i]);
				}
			}

			if (!anims.isEmpty())
			{
				CompoundAnimation anim = new CompoundAnimation(this, renderer, listener);
				anim.add(a, 0);
				for (int i = 0; i < anims.size(); ++i)
					anim.add(anims.get(i), delays.get(i));
				a = anim;
			}
		}

		if (this.overlay != -1)
		{
			OverlayAnimation overlay = new OverlayAnimation(this, this.overlay, a, listener);
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
		this.sprites = XMLUtils.getAttribute(xml, "sprites", defaultData.sprites);
		if (this.sprites != null && this.sprites.equals("none")) this.sprites = null;
		this.width = XMLUtils.getAttribute(xml, "width", defaultData.width);
		this.height = XMLUtils.getAttribute(xml, "height", defaultData.height);
		this.gravityX = XMLUtils.getAttribute(xml, "x", defaultData.gravityX);
		this.gravityY = XMLUtils.getAttribute(xml, "y", defaultData.gravityY);
		this.spriteDuration = XMLUtils.getAttribute(xml, "spriteduration", defaultData.spriteDuration);
		this.backSpriteUsage = BackSpriteUsage.valueOf(XMLUtils.getAttribute(xml, "backsprites", defaultData.backSpriteUsage.name()));
		this.spriteOrder = XMLUtils.readIntArray(xml);
		if (this.spriteOrder.length == 0) this.spriteOrder = defaultData.spriteOrder.clone();
		this.animationMovement = XMLUtils.getAttribute(xml, "movement", defaultData.animationMovement);
		this.loopsFrom = XMLUtils.getAttribute(xml, "loopsfrom", defaultData.loopsFrom);

		this.delayTime = XMLUtils.getAttribute(xml, "delaytime", defaultData.delayTime);

		this.sound = XMLUtils.getAttribute(xml, "sound", defaultData.sound);
		if (this.sound != null && this.sound.equals("null")) this.sound = null;
		this.soundDelay = XMLUtils.getAttribute(xml, "sounddelay", defaultData.soundDelay);
		this.playsForEachTarget = XMLUtils.getAttribute(xml, "playsforeachtarget", defaultData.playsForEachTarget);

		String state = XMLUtils.getAttribute(xml, "state", "null");
		this.pokemonState = state.equals("null") ? defaultData.pokemonState : state.equals("none") ? null : PokemonSpriteState.valueOf(state.toUpperCase());
		this.pokemonStateDelay = XMLUtils.getAttribute(xml, "statedelay", defaultData.pokemonStateDelay);
		this.pokemonMovement = XMLUtils.getAttribute(xml, "pkmnmovement", defaultData.pokemonMovement);

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

	public void toXML(Element root)
	{
		root.setAttribute("id", String.valueOf(this.id));
		if (this.clones != null)
		{
			root.setAttribute("clone", this.clones);
			return;
		}
		Element self = new Element("default");
		this.toXML(root, self, new AnimationData(this.id), false);
		root.addContent(self);
		for (Direction d : Direction.directions)
		{
			if (this.variants[d.index()] == this) continue;
			Element variant = new Element(d.getName().toLowerCase());
			this.variants[d.index()].toXML(root, variant, this, true);

			boolean hasDifference = !variant.getText().equals("") && !self.getText().equals(variant.getText());
			hasDifference |= !variant.getAttributes().isEmpty();

			if (hasDifference) root.addContent(variant);
		}
	}

	private void toXML(Element root, Element self, AnimationData defaultData, boolean isVariant)
	{
		if (defaultData.sprites == null && !isVariant) defaultData.sprites = "" + this.id;
		XMLUtils.setAttribute(self, "sprites", this.sprites == null ? "none" : this.sprites, defaultData.sprites == null ? "none" : defaultData.sprites);
		XMLUtils.setAttribute(self, "width", this.width, defaultData.width);
		XMLUtils.setAttribute(self, "height", this.height, defaultData.height);
		if (defaultData.gravityX == -1 && !isVariant) defaultData.gravityX = this.width / 2;
		if (defaultData.gravityY == -1 && !isVariant) defaultData.gravityY = this.height / 2;
		XMLUtils.setAttribute(self, "x", this.gravityX, defaultData.gravityX);
		XMLUtils.setAttribute(self, "y", this.gravityY, defaultData.gravityY);
		XMLUtils.setAttribute(self, "spriteduration", this.spriteDuration, defaultData.spriteDuration);
		XMLUtils.setAttribute(self, "backsprites", this.backSpriteUsage.name(), defaultData.backSpriteUsage.name());
		if (this.spriteOrder != null && !Arrays.equals(this.spriteOrder, defaultData.spriteOrder))
			self.setText(XMLUtils.toXML("order", this.spriteOrder).getText());
		XMLUtils.setAttribute(self, "movement", this.animationMovement, defaultData.animationMovement);
		XMLUtils.setAttribute(self, "loopsfrom", this.loopsFrom, defaultData.loopsFrom);

		XMLUtils.setAttribute(self, "delaytime", this.delayTime, defaultData.delayTime);
		XMLUtils.setAttribute(self, "sound", this.sound, defaultData.sound);
		XMLUtils.setAttribute(self, "sounddelay", this.soundDelay, defaultData.soundDelay);
		XMLUtils.setAttribute(self, "playsforeachtarget", this.playsForEachTarget, defaultData.playsForEachTarget);

		if (this.pokemonState != null)
			XMLUtils.setAttribute(self, "state", this.pokemonState.name(), defaultData.pokemonState == null ? null : defaultData.pokemonState.name());
		XMLUtils.setAttribute(self, "statedelay", this.pokemonStateDelay, defaultData.pokemonStateDelay);
		XMLUtils.setAttribute(self, "pkmnmovement", this.pokemonMovement, defaultData.pokemonMovement);

		if (this.alsoPlay.length > 0)
		{
			if (!Arrays.equals(this.alsoPlay, defaultData.alsoPlay))
			{
				String alsoPlay = "";
				for (int i = 0; i < this.alsoPlay.length; ++i)
				{
					if (i != 0) alsoPlay += ",";
					alsoPlay += this.alsoPlay[i];
				}
				self.setAttribute("alsoplay", alsoPlay);
			}
			boolean shouldStoreDelay = !Arrays.equals(this.alsoPlayDelay, defaultData.alsoPlayDelay);
			if (shouldStoreDelay)
			{
				shouldStoreDelay = false;
				for (int d : this.alsoPlayDelay)
					if (d != 0)
					{
						shouldStoreDelay = true;
						break;
					}
			}
			if (shouldStoreDelay) self.setAttribute("alsoplaydelay", XMLUtils.toXML("delay", this.alsoPlayDelay).getText());
		}

		XMLUtils.setAttribute(self, "overlay", this.overlay, defaultData.overlay);
	}

}
