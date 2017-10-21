package com.darkxell.client.mechanics.animation;

import java.awt.Graphics2D;
import java.io.File;
import java.util.HashMap;

import org.jdom2.Element;

import com.darkxell.client.resources.images.AnimationSpriteset;
import com.darkxell.common.item.Item;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.XMLUtils;

public class SpritesetAnimation extends PokemonAnimation
{

	private static final HashMap<Integer, Element> items = new HashMap<Integer, Element>();
	private static final HashMap<Integer, Element> moves = new HashMap<Integer, Element>();

	private static AbstractAnimation getAnimation(int id, HashMap<Integer, Element> registry, DungeonPokemon target, AnimationEndListener listener)
	{
		if (!registry.containsKey(id)) return new AbstractAnimation(60, listener);
		Element xml = registry.get(id);

		if (xml.getAttribute("width") == null || xml.getAttribute("height") == null)
		{
			Logger.e("Missing dimension for animation " + id + "!");
			return null;
		}
		int width = Integer.parseInt(xml.getAttributeValue("width"));
		int height = Integer.parseInt(xml.getAttributeValue("height"));
		AnimationSpriteset spriteset = AnimationSpriteset.getSpriteset((registry == items ? "/items" : "/moves") + "/" + id + ".png", width,
				height);
		int x = XMLUtils.getAttribute(xml, "x", width / 2);
		int y = XMLUtils.getAttribute(xml, "y", height / 2);
		int spriteDuration = XMLUtils.getAttribute(xml, "spriteduration", 3);
		int[] sprites = XMLUtils.readIntArray(xml);

		String back = xml.getAttributeValue("backsprites");
		boolean[] backSprites = new boolean[spriteset.spriteCount()];
		if (back != null) for (String b : back.split(","))
			backSprites[Integer.parseInt(b)] = true;

		return new SpritesetAnimation(target, spriteset, sprites, backSprites, spriteDuration, x, y, listener);
	}

	public static AbstractAnimation getItemAnimation(DungeonPokemon target, Item i, AnimationEndListener listener)
	{
		return getAnimation(i.id, items, target, listener);
	}

	public static AbstractAnimation getMoveAnimation(DungeonPokemon target, Move m, AnimationEndListener listener)
	{
		return getAnimation(m.id, moves, target, listener);
	}

	public static void loadData()
	{
		Element xml = XMLUtils.readFile(new File("resources/data/animations.xml"));
		for (Element item : xml.getChild("items").getChildren("item"))
			items.put(Integer.parseInt(item.getAttributeValue("id")), item);
		for (Element move : xml.getChild("moves").getChildren("move"))
			moves.put(Integer.parseInt(move.getAttributeValue("id")), move);
	}

	/** For each sprite, true if it should be drawn behind the Pokémon. */
	private final boolean[] backSprites;
	/** Gravity values for this Animation. Defaults to half this Spriteset's size. */
	public final int gravityX, gravityY;
	/** The duration of each sprite. */
	public final int spriteDuration;
	/** The order of sprites. -1 for no sprite. */
	private final int[] sprites;
	/** The spriteset to use. */
	public final AnimationSpriteset spriteset;

	public SpritesetAnimation(DungeonPokemon target, AnimationSpriteset spriteset, int[] sprites, boolean[] backSprites, int spriteDuration, int gravityX,
			int gravityY, AnimationEndListener listener)
	{
		super(target, sprites.length * spriteDuration, listener);
		this.spriteset = spriteset;
		this.sprites = sprites;
		this.backSprites = backSprites;
		this.spriteDuration = spriteDuration;
		this.gravityX = gravityX;
		this.gravityY = gravityY;
	}

	private void draw(Graphics2D g, boolean back)
	{
		int index = this.index();
		if (index != -1 && this.backSprites[index] == back) g.drawImage(this.spriteset.getSprite(index), (int) this.x - this.gravityX,
				(int) (this.y - this.gravityY), null);
	}

	public int index()
	{
		return this.sprites[this.tick() / this.spriteDuration];
	}

	@Override
	public void postrender(Graphics2D g, int width, int height)
	{
		super.postrender(g, width, height);
		this.draw(g, false);
	}

	@Override
	public void prerender(Graphics2D g, int width, int height)
	{
		super.prerender(g, width, height);
		this.draw(g, true);
	}

}
