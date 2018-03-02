package com.darkxell.client.mechanics.animation;

import java.io.File;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.mechanics.animation.SpritesetAnimation.BackSpriteUsage;
import com.darkxell.client.mechanics.animation.movement.TackleAnimationMovement;
import com.darkxell.client.resources.images.AnimationSpriteset;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.item.Item;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.PokemonStats;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;

public final class Animations
{
	public static final int HURT = 1;
	public static final int ORB = 2;
	public static final int HEAL = 3;

	public static final int ATTACK_UP = 10;
	public static final int DEFENSE_UP = 11;
	public static final int SP_ATTACK_UP = 12;
	public static final int SP_DEFENSE_UP = 13;
	public static final int SPEED_UP = 14;
	public static final int EVASION_UP = 15;
	public static final int ACCURACY_UP = 16;

	public static final int STAT_UP_TO_DOWN = 10;
	public static final int ATTACK_DOWN = ATTACK_UP + STAT_UP_TO_DOWN;
	public static final int DEFENSE_DOWN = DEFENSE_UP + STAT_UP_TO_DOWN;
	public static final int SP_ATTACK_DOWN = SP_ATTACK_UP + STAT_UP_TO_DOWN;
	public static final int SP_DEFENSE_DOWN = SP_DEFENSE_UP + STAT_UP_TO_DOWN;
	public static final int SPEED_DOWN = SPEED_UP + STAT_UP_TO_DOWN;
	public static final int EVASION_DOWN = EVASION_UP + STAT_UP_TO_DOWN;
	public static final int ACCURACY_DOWN = ACCURACY_UP + STAT_UP_TO_DOWN;

	private static final HashMap<Integer, Element> custom = new HashMap<Integer, Element>();
	private static final HashMap<Integer, Element> items = new HashMap<Integer, Element>();
	private static final HashMap<Integer, Element> moves = new HashMap<Integer, Element>();
	private static final HashMap<Integer, Element> moveTargets = new HashMap<Integer, Element>();
	private static final HashMap<Integer, Element> statuses = new HashMap<Integer, Element>();

	private static PokemonAnimation getAnimation(int id, HashMap<Integer, Element> registry, DungeonPokemon target, AnimationEndListener listener)
	{
		return getAnimation(id, registry, target, null, listener);
	}

	private static PokemonAnimation getAnimation(int id, HashMap<Integer, Element> registry, DungeonPokemon target, PokemonSpriteState defaultState,
			AnimationEndListener listener)
	{
		if (!registry.containsKey(id)) return null;
		Element xml = registry.get(id);
		Element defaultXml = xml.getChild("default", xml.getNamespace());

		if (XMLUtils.getAttribute(xml, "oriented", false) && xml.getChild(target.facing().name().toLowerCase(), xml.getNamespace()) != null)
			xml = xml.getChild(target.facing().name().toLowerCase(), xml.getNamespace());
		else xml = defaultXml;

		PokemonAnimation a;
		String sprites = XMLUtils.getAttribute(xml, "sprites", XMLUtils.getAttribute(defaultXml, "sprites", "default"));
		if (sprites.equals("default")) sprites = String.valueOf(id) + (xml == defaultXml ? "" : ("-" + target.facing().index()));

		if (sprites.equals("none")) a = new PokemonAnimation(target, 0, listener);
		else
		{
			if ((xml.getAttribute("width") == null && defaultXml.getAttribute("width") == null)
					|| xml.getAttribute("height") == null && defaultXml.getAttribute("height") == null)
			{
				Logger.e("Missing dimension for animation " + id + "!");
				return null;
			}
			int width = XMLUtils.getAttribute(xml, "width", XMLUtils.getAttribute(defaultXml, "width", 0));
			int height = XMLUtils.getAttribute(xml, "height", XMLUtils.getAttribute(defaultXml, "height", 0));

			BackSpriteUsage backsprites = BackSpriteUsage.valueOf(XMLUtils.getAttribute(xml, "backsprites", XMLUtils.getAttribute(xml, "backsprites", "no")));
			AnimationSpriteset spriteset = AnimationSpriteset.getSpriteset(
					(registry == items ? "/items" : registry == moves ? "/moves" : registry == statuses ? "/status" : "/animations") + "/" + sprites + ".png",
					width, height);
			int x = XMLUtils.getAttribute(xml, "x", XMLUtils.getAttribute(defaultXml, "x", width / 2));
			int y = XMLUtils.getAttribute(xml, "y", XMLUtils.getAttribute(defaultXml, "y", height / 2));
			int spriteDuration = XMLUtils.getAttribute(xml, "spriteduration", XMLUtils.getAttribute(defaultXml, "spriteduration", 2));
			int[] spriteOrder = XMLUtils.readIntArray(xml);
			if (spriteOrder.length == 0) XMLUtils.readIntArray(defaultXml);
			if (spriteOrder.length == 0)
			{
				spriteOrder = new int[backsprites == BackSpriteUsage.yes ? spriteset.spriteCount() / 2 : spriteset.spriteCount()];
				for (int i = 0; i < spriteOrder.length; ++i)
					spriteOrder[i] = i;
			}

			a = new SpritesetAnimation(target, spriteset, spriteOrder, backsprites, spriteDuration, x, y, listener);
		}

		/* String movement = XMLUtils.getAttribute(xml, "movement", null); if (movement != null) { try { Class<?> c = Class.forName("com.darkxell.client.mechanics.animation.movement." + movement + "AnimationMovement"); a.movement = (PokemonAnimationMovement)
		 * c.getConstructor(PokemonAnimation.class).newInstance(a); a.duration = Math.max(a.duration, a.movement.duration); } catch (Exception e1) { Logger.e("Movement instanciation failed!"); e1.printStackTrace(); } } */

		a.sound = XMLUtils.getAttribute(xml, "sound", XMLUtils.getAttribute(defaultXml, "sound", "null"));
		if (a.sound.equals("null")) a.sound = null;
		String state = XMLUtils.getAttribute(xml, "state", XMLUtils.getAttribute(defaultXml, "state", "null"));
		a.state = state.equals("null") ? defaultState : state.equals("none") ? null : PokemonSpriteState.valueOf(state.toUpperCase());
		if (a.state != null)
		{
			a.duration = Math.max(a.duration, a.renderer.sprite.pointer.getSequence(a.state, a.renderer.pokemon.facing()).duration);
			if (a.state.hasDash)
			{
				a.movement = new TackleAnimationMovement(a);
				a.duration = Math.max(a.duration, a.movement.duration);
			}
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

	public static AbstractAnimation getOrbAnimation(DungeonPokemon user, AnimationEndListener listener)
	{
		return getAnimation(ORB, custom, user, listener);
	}

	public static PokemonAnimation getStatusAnimation(DungeonPokemon target, StatusCondition s, AnimationEndListener listener)
	{
		PokemonAnimation a = getAnimation(s.id, statuses, target, listener);
		if (a != null) a.plays = -1;
		return a;
	}

	public static void loadData()
	{
		Element xml = XMLUtils.readFile(new File("resources/data/animations.xml"));
		for (Element c : xml.getChild("custom", xml.getNamespace()).getChildren("c", xml.getNamespace()))
			custom.put(Integer.parseInt(c.getAttributeValue("id")), c);
		for (Element item : xml.getChild("items", xml.getNamespace()).getChildren("item", xml.getNamespace()))
			items.put(Integer.parseInt(item.getAttributeValue("id")), item);
		for (Element move : xml.getChild("moves", xml.getNamespace()).getChildren("move", xml.getNamespace()))
			moves.put(Integer.parseInt(move.getAttributeValue("id")), move);
		for (Element move : xml.getChild("movetargets", xml.getNamespace()).getChildren("movetarget", xml.getNamespace()))
			moveTargets.put(Integer.parseInt(move.getAttributeValue("id")), move);
		for (Element move : xml.getChild("statuses", xml.getNamespace()).getChildren("status", xml.getNamespace()))
			statuses.put(Integer.parseInt(move.getAttributeValue("id")), move);
	}

	public static boolean playsOrbAnimation(DungeonPokemon user, Move move)
	{
		if (!moves.containsKey(move.id)) return false;
		Element xml = moves.get(move.id);
		Element defaultXml = xml.getChild("default", xml.getNamespace());

		if (XMLUtils.getAttribute(xml, "oriented", false) && xml.getChild(user.facing().name().toLowerCase(), xml.getNamespace()) != null)
			xml = xml.getChild(user.facing().name().toLowerCase(), xml.getNamespace());
		else xml = defaultXml;

		return XMLUtils.getAttribute(xml, "playsorbanim", XMLUtils.getAttribute(defaultXml, "playsorbanim", false));
	}

	private Animations()
	{}

	public static AbstractAnimation getStatChangeAnimation(StatChangedEvent event, AnimationEndListener listener)
	{
		int statID;

		switch (event.stat)
		{
			case PokemonStats.DEFENSE:
				statID = DEFENSE_UP;
				break;
			case PokemonStats.SPECIAL_ATTACK:
				statID = SP_ATTACK_UP;
				break;
			case PokemonStats.SPECIAL_DEFENSE:
				statID = SP_DEFENSE_UP;
				break;
			case PokemonStats.SPEED:
				statID = SPEED_UP;
				break;
			case PokemonStats.EVASIVENESS:
				statID = EVASION_UP;
				break;
			case PokemonStats.ACCURACY:
				statID = ACCURACY_UP;
				break;

			default:
				statID = ATTACK_UP;
				break;
		}

		if (event.stage < 0) statID += STAT_UP_TO_DOWN;
		return getCustomAnimation(event.target, statID, listener);
	}

}
