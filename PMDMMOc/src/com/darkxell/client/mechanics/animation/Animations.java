package com.darkxell.client.mechanics.animation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.mechanics.animation.movement.PokemonAnimationMovement;
import com.darkxell.client.mechanics.animation.spritemovement.SpritesetAnimationMovement;
import com.darkxell.client.resources.images.RegularSpriteSet;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.client.state.dungeon.ProjectileAnimationState.ProjectileMovement;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;

public final class Animations
{
	private static final HashMap<Integer, Element> abilities, custom, items, moves, moveTargets, projectiles, statuses;

	public static final int ATTACK_DOWN, DEFENSE_DOWN, SP_ATTACK_DOWN, SP_DEFENSE_DOWN, SPEED_DOWN, EVASION_DOWN, ACCURACY_DOWN;
	public static final int ATTACK_UP = 10, DEFENSE_UP = 11, SP_ATTACK_UP = 12, SP_DEFENSE_UP = 13, SPEED_UP = 14, EVASION_UP = 15, ACCURACY_UP = 16;
	public static final int HURT = 1, HEAL = 3, TELEPORT = 50;
	public static final int STAT_UP_TO_DOWN = 10;

	static
	{
		ATTACK_DOWN = ATTACK_UP + STAT_UP_TO_DOWN;
		DEFENSE_DOWN = DEFENSE_UP + STAT_UP_TO_DOWN;
		SP_ATTACK_DOWN = SP_ATTACK_UP + STAT_UP_TO_DOWN;
		SP_DEFENSE_DOWN = SP_DEFENSE_UP + STAT_UP_TO_DOWN;
		SPEED_DOWN = SPEED_UP + STAT_UP_TO_DOWN;
		EVASION_DOWN = EVASION_UP + STAT_UP_TO_DOWN;
		ACCURACY_DOWN = ACCURACY_UP + STAT_UP_TO_DOWN;

		abilities = new HashMap<>();
		custom = new HashMap<>();
		items = new HashMap<>();
		moves = new HashMap<>();
		moveTargets = new HashMap<>();
		projectiles = new HashMap<>();
		statuses = new HashMap<>();
	}

	private static boolean existsAnimation(int id, HashMap<Integer, Element> registry)
	{
		return registry.containsKey(id);
	}

	public static boolean existsItemAnimation(Item item)
	{
		return existsAnimation(item.id, items);
	}

	public static boolean existsMoveAnimation(Move move)
	{
		return existsAnimation(move.id, moves);
	}

	public static boolean existsProjectileAnimation(int projectileID)
	{
		return existsAnimation(projectileID, projectiles);
	}

	public static boolean existsTargetAnimation(Move move)
	{
		return existsAnimation(move.id, moveTargets);
	}

	public static PokemonAnimation getAbilityAnimation(DungeonPokemon pokemon, Ability ability, AnimationEndListener listener)
	{
		return getAnimation(ability.id, abilities, pokemon, listener);
	}

	public static PokemonAnimation getAnimation(DungeonPokemon target, String id, AnimationEndListener listener)
	{
		while (id.startsWith("/"))
			id = id.substring(0);
		if (!id.contains("/")) return getCustomAnimation(target, Integer.parseInt(id), listener);
		String group = id.substring(0, id.indexOf("/"));
		int anim = Integer.parseInt(id.substring(id.indexOf("/") + 1, id.length()));
		switch (group)
		{
			case "custom":
				return getCustomAnimation(target, anim, listener);

			case "abilities":
				return getAbilityAnimation(target, Ability.find(anim), listener);

			case "items":
				return getItemAnimation(target, ItemRegistry.find(anim), listener);

			case "moves":
				return getMoveAnimation(target, MoveRegistry.find(anim), listener);

			case "projectiles":
				return getProjectileAnimation(target, anim, listener);

			case "statuses":
				return getStatusAnimation(target, StatusConditions.find(anim), listener);

			case "targets":
				return getMoveTargetAnimation(target, MoveRegistry.find(anim), listener);

			default:
				return null;
		}
	}

	private static PokemonAnimation getAnimation(int id, HashMap<Integer, Element> registry, DungeonPokemon target, AnimationEndListener listener)
	{
		return getAnimation(id, registry, target, null, listener);
	}

	public static PokemonAnimation getAnimation(int id, HashMap<Integer, Element> registry, DungeonPokemon target, PokemonSpriteState defaultState,
			AnimationEndListener listener)
	{
		if (!registry.containsKey(id))
		{
			Logger.w("Animation not found: " + id);
			return null;
		}
		Element xml = registry.get(id);
		Element defaultXml = xml.getChild("default", xml.getNamespace());

		String clone = XMLUtils.getAttribute(xml, "clone", null);
		if (clone != null) return getAnimation(target, clone, listener);

		boolean oriented = XMLUtils.getAttribute(xml, "oriented", false);
		if (target != null && xml.getChild(target.facing().name().toLowerCase(), xml.getNamespace()) != null)
			xml = xml.getChild(target.facing().name().toLowerCase(), xml.getNamespace());
		else xml = defaultXml;

		PokemonAnimation a;
		String sprites = XMLUtils.getAttribute(xml, "sprites", XMLUtils.getAttribute(defaultXml, "sprites", "default"));
		if (sprites.equals("none")) a = new PokemonAnimation(target, 0, listener);
		else
		{
			if (sprites.equals("default")) sprites = String.valueOf(id) + (oriented ? ("-" + target.facing().index()) : "");
			if (!sprites.contains("/"))
			{
				if (registry == abilities) sprites = "/items/" + sprites;
				else if (registry == items) sprites = "/items/" + sprites;
				else if (registry == moves) sprites = "/moves/" + sprites;
				else if (registry == moveTargets) sprites = "/targets/" + sprites;
				else if (registry == projectiles) sprites = "/projectiles/" + sprites;
				else if (registry == statuses) sprites = "/status/" + sprites;
				else sprites = "/" + sprites;
			}
			sprites = "/animations" + sprites;

			if ((xml.getAttribute("width") == null && defaultXml.getAttribute("width") == null)
					|| xml.getAttribute("height") == null && defaultXml.getAttribute("height") == null)
			{
				Logger.e("Missing dimension for animation " + id + "!");
				return null;
			}
			int width = XMLUtils.getAttribute(xml, "width", XMLUtils.getAttribute(defaultXml, "width", 0));
			int height = XMLUtils.getAttribute(xml, "height", XMLUtils.getAttribute(defaultXml, "height", 0));

			BackSpriteUsage backsprites = BackSpriteUsage.valueOf(XMLUtils.getAttribute(xml, "backsprites", XMLUtils.getAttribute(xml, "backsprites", "no")));
			RegularSpriteSet spriteset = new RegularSpriteSet(sprites + ".png", width, height, -1, -1);
			int x = XMLUtils.getAttribute(xml, "x", XMLUtils.getAttribute(defaultXml, "x", width / 2));
			int y = XMLUtils.getAttribute(xml, "y", XMLUtils.getAttribute(defaultXml, "y", height / 2));
			int spriteDuration = XMLUtils.getAttribute(xml, "spriteduration", XMLUtils.getAttribute(defaultXml, "spriteduration", 2));
			int[] spriteOrder = XMLUtils.readIntArray(xml);
			if (spriteOrder.length == 0) spriteOrder = XMLUtils.readIntArray(defaultXml);
			if (spriteOrder.length == 0 && spriteset.isLoaded())
			{
				spriteOrder = new int[backsprites == BackSpriteUsage.yes ? spriteset.spriteCount() / 2 : spriteset.spriteCount()];
				for (int i = 0; i < spriteOrder.length; ++i)
					spriteOrder[i] = i;
			}

			a = new SpritesetAnimation(target, spriteset, backsprites, spriteOrder, spriteDuration, x, y, listener);

			String movement = XMLUtils.getAttribute(xml, "movement", XMLUtils.getAttribute(defaultXml, "movement", "null"));
			((SpritesetAnimation) a).spritesetMovement = SpritesetAnimationMovement.create(movement, (SpritesetAnimation) a);

			((SpritesetAnimation) a).loopsFrom = XMLUtils.getAttribute(xml, "loopsfrom", XMLUtils.getAttribute(defaultXml, "loopsfrom", 0));
		}

		a.sound = XMLUtils.getAttribute(xml, "sound", XMLUtils.getAttribute(defaultXml, "sound", "null"));
		if (a.sound.equals("null")) a.sound = null;
		a.soundDelay = XMLUtils.getAttribute(xml, "sounddelay", XMLUtils.getAttribute(defaultXml, "sounddelay", 0));
		String state = XMLUtils.getAttribute(xml, "state", XMLUtils.getAttribute(defaultXml, "state", "null"));
		a.state = state.equals("null") ? defaultState : state.equals("none") ? null : PokemonSpriteState.valueOf(state.toUpperCase());

		String movement = XMLUtils.getAttribute(xml, "pkmnmovement", XMLUtils.getAttribute(defaultXml, "pkmnmovement", null));
		if (movement == null && a.state != null && a.state.hasDash) movement = "dash";

		if (movement != null)
		{
			a.movement = PokemonAnimationMovement.create(a, movement);
			a.duration = Math.max(a.duration, a.movement.duration);
		}

		a.stateDelay = XMLUtils.getAttribute(xml, "statedelay", XMLUtils.getAttribute(defaultXml, "statedelay", 0));
		a.delayTime = XMLUtils.getAttribute(xml, "delaytime", XMLUtils.getAttribute(defaultXml, "delaytime", a.duration));
		if (a.delayTime == 0) a.delayTime = 15;

		if (XMLUtils.getAttribute(xml, "alsoplay") != null || XMLUtils.getAttribute(defaultXml, "alsoplay") != null)
		{

			ArrayList<AbstractAnimation> anims = new ArrayList<>();
			ArrayList<Integer> delays = new ArrayList<>();
			AbstractAnimation tmp;

			String[] ids = XMLUtils.getAttribute(xml, "alsoplay", "").split(",");
			String[] ds = XMLUtils.getAttribute(xml, "alsoplaydelay", "").split(",");
			if (ds.length < ids.length)
			{
				String[] dstmp = ds;
				ds = new String[ids.length];
				for (int i = 0; i < ds.length; ++i)
					ds[i] = i < dstmp.length ? dstmp[i] : "";
			}

			for (int i = 0; i < ds.length; ++i)
				if (ds[i].equals("")) ds[i] = "0";

			for (int i = 0; i < ids.length; ++i)
			{
				if (ids[i].equals("")) continue;
				tmp = getAnimation(target, ids[i], null);
				if (tmp != null && !anims.contains(tmp))
				{
					anims.add(tmp);
					delays.add(Integer.parseInt(ds[i]));
				}
			}

			ids = XMLUtils.getAttribute(defaultXml, "alsoplay", "").split(",");
			ds = XMLUtils.getAttribute(defaultXml, "alsoplaydelay", "").split(",");
			if (ds.length < ids.length)
			{
				String[] dstmp = ds;
				ds = new String[ids.length];
				for (int i = 0; i < ds.length; ++i)
					ds[i] = i < dstmp.length ? dstmp[i] : "";
			}

			for (int i = 0; i < ds.length; ++i)
				if (ds[i].equals("")) ds[i] = "0";

			for (int i = 0; i < ids.length; ++i)
			{
				if (ids[i].equals("")) continue;
				tmp = getAnimation(target, ids[i], null);
				if (tmp != null && !anims.contains(tmp))
				{
					anims.add(tmp);
					delays.add(Integer.parseInt(ds[i]));
				}
			}

			if (!anims.isEmpty())
			{
				CompoundAnimation anim = new CompoundAnimation(target, listener);
				anim.add(a, 0);
				for (int i = 0; i < anims.size(); ++i)
					anim.add(anims.get(i), delays.get(i));
				a = anim;
			}

		}

		if (XMLUtils.getAttribute(xml, "overlay") != null || XMLUtils.getAttribute(defaultXml, "overlay") != null)
		{
			OverlayAnimation overlay = new OverlayAnimation(XMLUtils.getAttribute(xml, "overlay", XMLUtils.getAttribute(defaultXml, "overlay", 0)), a,
					listener);
			Persistance.dungeonState.staticAnimationsRenderer.add(overlay);
			overlay.start();
		}

		return a;
	}

	public static PokemonAnimation getCustomAnimation(DungeonPokemon target, int id, AnimationEndListener listener)
	{
		return getAnimation(id, custom, target, listener);
	}

	public static PokemonAnimation getItemAnimation(DungeonPokemon target, Item i, AnimationEndListener listener)
	{
		return getAnimation(i.id, items, target, listener);
	}

	public static PokemonAnimation getMoveAnimation(DungeonPokemon user, Move m, AnimationEndListener listener)
	{
		return getAnimation(m.id, moves, user, m.category == MoveCategory.Physical ? PokemonSpriteState.ATTACK : PokemonSpriteState.SPECIAL, listener);
	}

	public static PokemonAnimation getMoveTargetAnimation(DungeonPokemon target, Move m, AnimationEndListener listener)
	{
		return getAnimation(m.id, moveTargets, target, listener);
	}

	public static PokemonAnimation getProjectileAnimation(DungeonPokemon pokemon, int projectileID, AnimationEndListener listener)
	{
		PokemonAnimation a = getAnimation(projectileID, projectiles, pokemon, listener);
		if (a != null) a.plays = -1;
		return a;
	}

	public static PokemonAnimation getStatChangeAnimation(StatChangedEvent event, AnimationEndListener listener)
	{
		int statID;

		switch (event.stat)
		{
			case Defense:
				statID = DEFENSE_UP;
				break;
			case SpecialAttack:
				statID = SP_ATTACK_UP;
				break;
			case SpecialDefense:
				statID = SP_DEFENSE_UP;
				break;
			case Speed:
				statID = SPEED_UP;
				break;
			case Evasiveness:
				statID = EVASION_UP;
				break;
			case Accuracy:
				statID = ACCURACY_UP;
				break;

			default:
				statID = ATTACK_UP;
				break;
		}

		if (event.stage < 0) statID += STAT_UP_TO_DOWN;
		return getCustomAnimation(event.target, statID, listener);
	}

	public static PokemonAnimation getStatusAnimation(DungeonPokemon target, StatusCondition s, AnimationEndListener listener)
	{
		PokemonAnimation a = getAnimation(s.id, statuses, target, listener);
		if (a != null) a.plays = -1;
		return a;
	}

	public static String[] listAnimations()
	{
		ArrayList<String> anims = new ArrayList<>();

		for (Integer key : abilities.keySet())
			anims.add("abilities/" + key);
		for (Integer key : custom.keySet())
			anims.add("custom/" + key);
		for (Integer key : items.keySet())
			anims.add("items/" + key);
		for (Integer key : moves.keySet())
			anims.add("moves/" + key);
		for (Integer key : moveTargets.keySet())
			anims.add("targets/" + key);
		for (Integer key : projectiles.keySet())
			anims.add("projectiles/" + key);
		for (Integer key : statuses.keySet())
			anims.add("statuses/" + key);

		anims.sort(Comparator.naturalOrder());

		return anims.toArray(new String[anims.size()]);
	}

	public static void loadData()
	{
		abilities.clear();
		custom.clear();
		items.clear();
		moves.clear();
		moveTargets.clear();
		projectiles.clear();
		statuses.clear();

		Element xml = XMLUtils.read(Animations.class.getResourceAsStream("/data/animations.xml"));
		for (Element a : xml.getChild("abilities", xml.getNamespace()).getChildren("a", xml.getNamespace()))
			abilities.put(Integer.parseInt(a.getAttributeValue("id")), a);
		for (Element c : xml.getChild("custom", xml.getNamespace()).getChildren("c", xml.getNamespace()))
			custom.put(Integer.parseInt(c.getAttributeValue("id")), c);
		for (Element item : xml.getChild("items", xml.getNamespace()).getChildren("item", xml.getNamespace()))
			items.put(Integer.parseInt(item.getAttributeValue("id")), item);
		for (Element move : xml.getChild("moves", xml.getNamespace()).getChildren("move", xml.getNamespace()))
			moves.put(Integer.parseInt(move.getAttributeValue("id")), move);
		for (Element move : xml.getChild("movetargets", xml.getNamespace()).getChildren("movetarget", xml.getNamespace()))
			moveTargets.put(Integer.parseInt(move.getAttributeValue("id")), move);
		for (Element move : xml.getChild("projectiles", xml.getNamespace()).getChildren("projectile", xml.getNamespace()))
			projectiles.put(Integer.parseInt(move.getAttributeValue("id")), move);
		for (Element move : xml.getChild("statuses", xml.getNamespace()).getChildren("status", xml.getNamespace()))
			statuses.put(Integer.parseInt(move.getAttributeValue("id")), move);
	}

	public static boolean movePlaysForEachTarget(Move move)
	{
		if (move == null) return false;
		Element e = moves.get(move.id);
		if (e == null) return false;
		String clone = XMLUtils.getAttribute(e, "clone", null);
		if (clone != null)
		{
			if (clone.startsWith("moves/")) return movePlaysForEachTarget(MoveRegistry.find(Integer.parseInt(clone.substring("moves/".length()))));
			return false;
		}
		return XMLUtils.getAttribute(e.getChild("default", e.getNamespace()), "playsforeachtarget", false);
	}

	public static ProjectileMovement projectileMovement(int id)
	{
		if (!existsProjectileAnimation(id)) return ProjectileMovement.STRAIGHT;
		Element e = projectiles.get(id);
		if (e == null) return ProjectileMovement.STRAIGHT;
		String movement = null;
		if (XMLUtils.getAttribute(e, "clone") != null)
		{
			if (XMLUtils.getAttribute(e, "clone", "").startsWith("projectiles/"))
				return projectileMovement(Integer.parseInt(XMLUtils.getAttribute(e, "clone", "projectiles/4").substring("projectiles/".length())));
			else return ProjectileMovement.STRAIGHT;
		} else movement = XMLUtils.getAttribute(e.getChild("default", e.getNamespace()), "movement", "STRAIGHT");
		if (movement != null) try
		{
			return ProjectileMovement.valueOf(movement.toUpperCase());
		} catch (Exception e2)
		{}
		return ProjectileMovement.STRAIGHT;
	}

	private Animations()
	{}

}
