package com.darkxell.client.resources.images;

import com.darkxell.client.resources.images.others.FontSpriteSet;
import com.darkxell.client.resources.images.others.ItemsSpriteset;
import com.darkxell.client.resources.images.others.MenuHudSpriteset;
import com.darkxell.client.resources.images.pokemon.ShadowSpriteSet;
import com.darkxell.client.resources.images.tilesets.CommonDungeonTileset;
import com.darkxell.client.resources.images.tilesets.DungeonMapTileset;

/** Class that holds SpriteSets used in the whole project. */
public final class SpriteSets
{
	// HUD
	public static final FontSpriteSet font = new FontSpriteSet();
	public static final MenuHudSpriteset menuHud = new MenuHudSpriteset();

	// Dungeon
	public static final CommonDungeonTileset dungeonCommon = new CommonDungeonTileset();
	public static final DungeonMapTileset dungeonMap = new DungeonMapTileset();
	public static final ItemsSpriteset items = new ItemsSpriteset();
	public static final ShadowSpriteSet shadows = new ShadowSpriteSet();

	/** Loads SpriteSets used in the whole project. */
	public static void loadCommon()
	{

	}

	private SpriteSets()
	{}

}
