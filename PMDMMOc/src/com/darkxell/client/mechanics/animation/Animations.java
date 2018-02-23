package com.darkxell.client.mechanics.animation;

import java.io.File;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.mechanics.animation.movement.TackleAnimationMovement;
import com.darkxell.client.resources.images.AnimationSpriteset;
import com.darkxell.client.resources.images.pokemon.PokemonSprite.PokemonSpriteState;
import com.darkxell.common.item.Item;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;

public final class Animations
{

	private static final HashMap<Integer, Element> custom = new HashMap<Integer, Element>();
	private static final HashMap<Integer, Element> items = new HashMap<Integer, Element>();
	private static final HashMap<Integer, Element> moves = new HashMap<Integer, Element>();
	private static final HashMap<Integer, Element> moveTargets = new HashMap<Integer, Element>();
	private static final HashMap<Integer, Element> statuses = new HashMap<Integer, Element>();

	private static AbstractAnimation getAnimation(int id, HashMap<Integer, Element> registry, DungeonPokemon target, AnimationEndListener listener)
	{
		return getAnimation(id, registry, target, null, listener);
	}

	private static AbstractAnimation getAnimation(int id, HashMap<Integer, Element> registry, DungeonPokemon target, PokemonSpriteState defaultState,
			AnimationEndListener listener)
	{
		if (!registry.containsKey(id)) return null;
		Element xml = registry.get(id);

		PokemonAnimation a;
		String sprites = XMLUtils.getAttribute(xml, "sprites", null);
		if (sprites == null) sprites = String.valueOf(id);

		if (sprites.equals("none")) a = new PokemonAnimation(target, 0, listener);
		else
		{
			if (xml.getAttribute("width") == null || xml.getAttribute("height") == null)
			{
				Logger.e("Missing dimension for animation " + id + "!");
				return null;
			}
			int width = Integer.parseInt(xml.getAttributeValue("width"));
			int height = Integer.parseInt(xml.getAttributeValue("height"));

			AnimationSpriteset spriteset = AnimationSpriteset.getSpriteset(
					(registry == items ? "/items" : registry == moves ? "/moves" : registry == statuses ? "/status" : "/animations") + "/" + sprites + ".png",
					width, height);
			int x = XMLUtils.getAttribute(xml, "x", width / 2);
			int y = XMLUtils.getAttribute(xml, "y", height / 2);
			int spriteDuration = XMLUtils.getAttribute(xml, "spriteduration", 2);
			int[] spriteOrder = XMLUtils.readIntArray(xml);

			String back = xml.getAttributeValue("backsprites");
			boolean[] backSprites = new boolean[spriteset.spriteCount()];
			if (back != null) for (String b : back.split(","))
				backSprites[Integer.parseInt(b)] = true;

			a = new SpritesetAnimation(target, spriteset, spriteOrder, backSprites, spriteDuration, x, y, listener);
		}

		/* String movement = XMLUtils.getAttribute(xml, "movement", null); if (movement != null) { try { Class<?> c = Class.forName("com.darkxell.client.mechanics.animation.movement." + movement + "AnimationMovement"); a.movement = (PokemonAnimationMovement)
		 * c.getConstructor(PokemonAnimation.class).newInstance(a); a.duration = Math.max(a.duration, a.movement.duration); } catch (Exception e1) { Logger.e("Movement instanciation failed!"); e1.printStackTrace(); } } */

		a.sound = XMLUtils.getAttribute(xml, "sound", null);
		a.state = xml.getAttribute("state") == null ? defaultState : PokemonSpriteState.valueOf(XMLUtils.getAttribute(xml, "state", null).toUpperCase());
		if (a.state != null)
		{
			a.duration = Math.max(a.duration, a.renderer.sprite.pointer.getSequence(a.state, a.renderer.pokemon.facing()).duration);
			if (a.state.hasDash) a.movement = new TackleAnimationMovement(a);
		}

		return a;
	}

	public static AbstractAnimation getCustomAnimation(DungeonPokemon target, int id, AnimationEndListener listener)
	{
		return getAnimation(id, custom, target, listener);
	}

	public static AbstractAnimation getItemAnimation(DungeonPokemon target, Item i, AnimationEndListener listener)
	{
		return getAnimation(i.id, items, target, listener);
	}

	public static AbstractAnimation getMoveAnimation(DungeonPokemon user, Move m, AnimationEndListener listener)
	{
		return getAnimation(m.id, moves, user, m.category == MoveCategory.Physical ? PokemonSpriteState.ATTACK : PokemonSpriteState.SPECIAL, listener);
	}

	public static AbstractAnimation getMoveTargetAnimation(DungeonPokemon target, Move m, AnimationEndListener listener)
	{
		return getAnimation(m.id, moveTargets, target, listener);
	}

	public static AbstractAnimation getStatusAnimation(DungeonPokemon target, StatusCondition s, AnimationEndListener listener)
	{
		AbstractAnimation a = getAnimation(s.id, statuses, target, listener);
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

	private Animations()
	{}

}
