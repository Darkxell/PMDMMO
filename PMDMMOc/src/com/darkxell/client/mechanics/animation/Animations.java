package com.darkxell.client.mechanics.animation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.mechanics.cutscene.entity.CutscenePokemon;
import com.darkxell.client.graphics.renderer.AbstractPokemonRenderer;
import com.darkxell.client.resources.images.Sprites;
import com.darkxell.client.state.dungeon.ProjectileAnimationState.ProjectileMovement;
import com.darkxell.common.Registries;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.XMLUtils;

public final class Animations
{
	public static enum AnimationGroup
	{
		Abilities,
		Custom,
		Items,
		Moves,
		MoveTargets,
		Projectiles,
		Statuses;
	}

	private static final HashMap<Integer, AnimationData> abilities, custom, items, moves, moveTargets, projectiles, statuses;

	public static final int ATTACK_DOWN, DEFENSE_DOWN, SP_ATTACK_DOWN, SP_DEFENSE_DOWN, SPEED_DOWN, EVASION_DOWN, ACCURACY_DOWN;
	public static final int ATTACK_UP = 10, DEFENSE_UP = 11, SP_ATTACK_UP = 12, SP_DEFENSE_UP = 13, SPEED_UP = 14, EVASION_UP = 15, ACCURACY_UP = 16;
	public static final int HURT = 1, HEAL = 3, THROW = 6, TELEPORT = 50, REVIVE = 51;
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

	private static boolean existsAnimation(int id, HashMap<Integer, AnimationData> registry)
	{
		return registry.containsKey(id);
	}

	public static boolean existsAnimation(Pair<Integer, AnimationGroup> id)
	{
		if (id == null) return false;
		return registry(id.second).containsKey(id.first);
	}

	public static boolean existsAnimation(String id)
	{
		Pair<Integer, AnimationGroup> split = splitID(id);
		return existsAnimation(split);
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

		MoveRegistry moves = Registries.moves();

		switch (group.toLowerCase())
		{
			case "custom":
				return getCustomAnimation(target, anim, listener);

			case "abilities":
				return getAbilityAnimation(target, Ability.find(anim), listener);

			case "items":
				return getItemAnimation(target, Registries.items().find(anim), listener);

			case "moves":
				return getMoveAnimation(target, moves.find(anim), listener);

			case "projectiles":
				return getProjectileAnimation(target, anim, listener);

			case "statuses":
				return getStatusAnimation(target, StatusConditions.find(anim), listener);

			case "movetargets":
			case "targets":
				return getMoveTargetAnimation(target, moves.find(anim), listener);

			default:
				return null;
		}
	}

	public static PokemonAnimation getAnimation(int id, HashMap<Integer, AnimationData> registry, DungeonPokemon target, AnimationEndListener listener)
	{
		if (!registry.containsKey(id))
		{
			Logger.w("Animation not found: " + id);
			return null;
		}
		AbstractPokemonRenderer renderer = target == null ? null : Persistence.dungeonState.pokemonRenderer.getRenderer(target);
		return registry.get(id).createAnimation(target, null, renderer, listener);
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
		AbstractPokemonRenderer renderer = target == null ? null : (AbstractPokemonRenderer) Persistence.currentmap.cutsceneEntityRenderers.getRenderer(target);
		return custom.get(id).createAnimation(null, target, renderer, listener);
	}

	public static AnimationData getData(int id, AnimationGroup group)
	{
		return registry(group).get(id);
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
		{
			int id = Integer.parseInt(a.getAttributeValue("id"));
			abilities.put(id, new AnimationData(id, "abilities/", a));
		}
		for (Element c : xml.getChild("custom", xml.getNamespace()).getChildren("c", xml.getNamespace()))
		{
			int id = Integer.parseInt(c.getAttributeValue("id"));
			custom.put(id, new AnimationData(id, "", c));
		}
		for (Element item : xml.getChild("items", xml.getNamespace()).getChildren("item", xml.getNamespace()))
		{
			int id = Integer.parseInt(item.getAttributeValue("id"));
			items.put(id, new AnimationData(id, "items/", item));
		}
		for (Element move : xml.getChild("moves", xml.getNamespace()).getChildren("move", xml.getNamespace()))
		{
			int id = Integer.parseInt(move.getAttributeValue("id"));
			moves.put(id, new AnimationData(id, "moves/", move));
		}
		for (Element move : xml.getChild("movetargets", xml.getNamespace()).getChildren("movetarget", xml.getNamespace()))
		{
			int id = Integer.parseInt(move.getAttributeValue("id"));
			moveTargets.put(id, new AnimationData(id, "targets/", move));
		}
		for (Element move : xml.getChild("projectiles", xml.getNamespace()).getChildren("projectile", xml.getNamespace()))
		{
			int id = Integer.parseInt(move.getAttributeValue("id"));
			projectiles.put(id, new AnimationData(id, "projectiles/", move));
		}
		for (Element status : xml.getChild("statuses", xml.getNamespace()).getChildren("status", xml.getNamespace()))
		{
			int id = Integer.parseInt(status.getAttributeValue("id"));
			statuses.put(id, new AnimationData(id, "status/", status));
		}
	}

	public static boolean movePlaysForEachTarget(Move move)
	{
		if (move == null) return false;
		AnimationData data = moves.get(move.id);
		if (data == null) return false;
		if (data.clones != null)
		{
			if (data.clones.startsWith("moves/")) return movePlaysForEachTarget(Registries.moves().find(Integer.parseInt(data.clones.substring("moves/".length()))));
			return false;
		}
		return data.playsForEachTarget;
	}

	public static ProjectileMovement projectileMovement(int id)
	{
		if (!existsProjectileAnimation(id)) return ProjectileMovement.STRAIGHT;
		AnimationData data = projectiles.get(id);
		if (data == null) return ProjectileMovement.STRAIGHT;
		String movement = null;
		if (data.clones != null)
		{
			if (data.clones.startsWith("projectiles/")) return projectileMovement(Integer.parseInt(data.clones.substring("projectiles/".length())));
			else return ProjectileMovement.STRAIGHT;
		} else movement = data.animationMovement;
		if (movement != null) try
		{
			return ProjectileMovement.valueOf(movement.toUpperCase());
		} catch (Exception e2)
		{}
		return ProjectileMovement.STRAIGHT;
	}

	public static void register(AnimationData animation, AnimationGroup group)
	{
		if (!existsAnimation(animation.id, registry(group))) registry(group).put(animation.id, animation);
	}

	private static HashMap<Integer, AnimationData> registry(AnimationGroup group)
	{
		switch (group)
		{
			case Abilities:
				return abilities;

			case Custom:
				return custom;

			case Items:
				return items;

			case Moves:
				return moves;

			case MoveTargets:
				return moveTargets;

			case Projectiles:
				return projectiles;

			case Statuses:
				return statuses;
		}
		return null;
	}

	private static void save(Element root, HashMap<Integer, AnimationData> registry, String registryName, String itemName)
	{
		ArrayList<AnimationData> anims = new ArrayList<>(registry.values());
		anims.sort(Comparator.naturalOrder());

		Element xml = new Element(registryName);
		for (AnimationData anim : anims)
		{
			Element a = new Element(itemName);
			anim.toXML(a);
			xml.addContent(a);
		}

		root.addContent(xml);
	}

	public static void save(File file)
	{
		Element root = new Element("animations");
		save(root, abilities, "abilities", "a");
		save(root, custom, "custom", "c");
		save(root, items, "items", "item");
		save(root, moves, "moves", "move");
		save(root, moveTargets, "movetargets", "movetarget");
		save(root, projectiles, "projectiles", "projectile");
		save(root, statuses, "statuses", "status");
		XMLUtils.saveFile(file, root);
	}

	public static Pair<Integer, AnimationGroup> splitID(String id)
	{
		while (id.startsWith("/"))
			id = id.substring(0);
		if (!id.contains("/"))
		{
			if (!id.matches("\\d+")) return null;
			return new Pair<>(Integer.parseInt(id), AnimationGroup.Custom);
		}
		String group = id.substring(0, id.indexOf("/"));
		int anim = Integer.parseInt(id.substring(id.indexOf("/") + 1, id.length()));
		switch (group)
		{
			case "custom":
				return new Pair<>(anim, AnimationGroup.Custom);

			case "ability":
			case "abilities":
				return new Pair<>(anim, AnimationGroup.Abilities);

			case "item":
			case "items":
				return new Pair<>(anim, AnimationGroup.Items);

			case "move":
			case "moves":
				return new Pair<>(anim, AnimationGroup.Moves);

			case "projectile":
			case "projectiles":
				return new Pair<>(anim, AnimationGroup.Projectiles);

			case "status":
			case "statuses":
			case "statuscondition":
			case "statusconditions":
				return new Pair<>(anim, AnimationGroup.Statuses);

			case "target":
			case "targets":
			case "movetarget":
			case "movetargets":
				return new Pair<>(anim, AnimationGroup.MoveTargets);

			default:
				break;
		}
		return null;
	}

	public static void unregister(int id, AnimationGroup group)
	{
		if (existsAnimation(id, registry(group))) registry(group).remove(id);
	}

	private Animations()
	{}

}
