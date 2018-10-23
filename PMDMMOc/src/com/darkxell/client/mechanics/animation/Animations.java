package com.darkxell.client.mechanics.animation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistance;
import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.renderers.pokemon.AbstractPokemonRenderer;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.state.dungeon.ProjectileAnimationState.ProjectileMovement;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.item.ItemRegistry;
import com.darkxell.common.move.Move;
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
	public static final int HURT = 1, HEAL = 3, THROW = 6, TELEPORT = 50;
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

	public static PokemonAnimation getAnimation(int id, HashMap<Integer, Element> registry, DungeonPokemon target, AnimationEndListener listener)
	{
		if (!registry.containsKey(id))
		{
			Logger.w("Animation not found: " + id);
			return null;
		}
		AbstractPokemonRenderer renderer = target == null ? null : Persistance.dungeonState.pokemonRenderer.getRenderer(target);
		String spritesPrefix = "";
		if (registry == items) spritesPrefix = "items/";
		else if (registry == moves) spritesPrefix = "moves/";
		else if (registry == moveTargets) spritesPrefix = "targets/";
		else if (registry == projectiles) spritesPrefix = "projectiles/";
		else if (registry == statuses) spritesPrefix = "status/";
		return new AnimationData(id, spritesPrefix, registry.get(id)).createAnimation(target, null, renderer, listener);
	}

	public static PokemonAnimation getCustomAnimation(DungeonPokemon target, int id, AnimationEndListener listener)
	{
		return getAnimation(id, custom, target, listener);
	}

	public static PokemonAnimation getCutsceneAnimation(int id, CutscenePokemon target, AnimationEndListener listener)
	{
		if (!custom.containsKey(id))
		{
			Logger.w("Animation not found: " + id);
			return null;
		}
		AbstractPokemonRenderer renderer = target == null ? null : (AbstractPokemonRenderer) Persistance.currentmap.cutsceneEntityRenderers.getRenderer(target);
		return new AnimationData(id, "", custom.get(id)).createAnimation(null, target, renderer, listener);
	}

	public static PokemonAnimation getItemAnimation(DungeonPokemon target, Item i, AnimationEndListener listener)
	{
		return getAnimation(i.id, items, target, listener);
	}

	public static PokemonAnimation getMoveAnimation(DungeonPokemon user, Move m, AnimationEndListener listener)
	{
		return getAnimation(m.id, moves, user, listener);
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

	public static AbstractAnimation getProjectileAnimationFromItem(DungeonPokemon pokemon, Item item, AnimationEndListener listener)
	{
		BufferedImage sprite = Sprites.Res_Dungeon.items.sprite(item);
		AnimationData data = new AnimationData(-1);
		data.backSpriteUsage = BackSpriteUsage.no;
		data.spriteOrder = new int[] { item.spriteID };
		data.gravityX = sprite.getWidth() / 2;
		data.gravityY = sprite.getHeight() / 2;
		data.spriteDuration = 10;
		SpritesetAnimation a = new SpritesetAnimation(data, null, Sprites.Res_Dungeon.items, data.spriteOrder, listener);
		a.plays = -1;
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
