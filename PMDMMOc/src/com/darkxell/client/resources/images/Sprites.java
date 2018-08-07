package com.darkxell.client.resources.images;

import com.darkxell.client.resources.images.others.FontSpriteSet;
import com.darkxell.client.resources.images.others.ItemsSpriteset;
import com.darkxell.client.resources.images.others.MenuHudSpriteset;
import com.darkxell.client.resources.images.pokemon.ShadowSpriteSet;
import com.darkxell.client.resources.images.tilesets.CommonDungeonTileset;
import com.darkxell.client.resources.images.tilesets.DungeonMapTileset;

/** Class that holds Sprites & SpriteSets used in the whole project. */
public final class Sprites
{
	// HUD
	public static class Hud
	{
		public static final FontSpriteSet font = new FontSpriteSet();
		public static final MenuHudSpriteset menuHud = new MenuHudSpriteset();

		public static final Sprite button = new Sprite("/hud/button.png");
		public static final Sprite portrait = new Sprite("/hud/portrait.png");
		public static final Sprite textwindow = new Sprite("/hud/textwindow.png");
		public static final Sprite textwindow_transparent = new Sprite("/hud/textwindow_transparent.png");
		public static final Sprite gametitle = new Sprite("/hud/title.png");
		public static final Sprite loginframe = new Sprite("/hud/login.png");
		public static final Sprite createaccountframe = new Sprite("/hud/create.png");
		public static final Sprite proceedaccountframe = new Sprite("/hud/create2.png");
	}

	// Dungeon
	public static final CommonDungeonTileset dungeonCommon = new CommonDungeonTileset();
	public static final DungeonMapTileset dungeonMap = new DungeonMapTileset();
	public static final ItemsSpriteset items = new ItemsSpriteset();
	public static final ShadowSpriteSet shadows = new ShadowSpriteSet();

	/** Loads SpriteSets used in the whole project. */
	public static void loadCommon()
	{}

	private Sprites()
	{}

}
