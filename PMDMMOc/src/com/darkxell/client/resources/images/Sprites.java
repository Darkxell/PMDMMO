package com.darkxell.client.resources.images;

import com.darkxell.client.resources.images.others.FontSpriteSet;
import com.darkxell.client.resources.images.others.ItemsSpriteset;
import com.darkxell.client.resources.images.others.MenuHudSpriteset;
import com.darkxell.client.resources.images.pokemon.ShadowSpriteSet;
import com.darkxell.client.resources.images.tilesets.CommonDungeonTileset;
import com.darkxell.client.resources.images.tilesets.DungeonMapTileset;
import com.darkxell.client.ui.Frame.FrameIconSprite;

/** Class that holds Sprites & SpriteSets used in the whole project. */
public final class Sprites
{

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

	public static class FrameResources
	{
		public static final Sprite ICON = new FrameIconSprite("/hud/framebackgrounds/icon.png");

		private static final Sprite source = new Sprite("/hud/boxcorners.png");
		public static final Sprite box_NW = SpriteFactory.instance().subSprite(source, 0, 0, 7, 3);
		public static final Sprite box_NE = SpriteFactory.instance().subSprite(source, 8, 0, 7, 3);
		public static final Sprite box_SW = SpriteFactory.instance().subSprite(source, 0, 4, 7, 3);
		public static final Sprite box_SE = SpriteFactory.instance().subSprite(source, 8, 4, 7, 3);
		public static final Sprite box_N = SpriteFactory.instance().subSprite(source, 7, 0, 1, 3);
		public static final Sprite box_E = SpriteFactory.instance().subSprite(source, 8, 3, 7, 1);
		public static final Sprite box_S = SpriteFactory.instance().subSprite(source, 7, 4, 1, 3);
		public static final Sprite box_W = SpriteFactory.instance().subSprite(source, 0, 3, 7, 1);

		public static final Sprite BG1 = new Sprite("/hud/framebackgrounds/1.jpg");
		public static final Sprite BG2 = new Sprite("/hud/framebackgrounds/2.jpg");
		public static final Sprite BG3 = new Sprite("/hud/framebackgrounds/3.png");
		public static final Sprite BG4 = new Sprite("/hud/framebackgrounds/4.png");
		public static final Sprite BG5 = new Sprite("/hud/framebackgrounds/5.jpg");
		public static final Sprite BG6 = new Sprite("/hud/framebackgrounds/6.png");
		public static final Sprite BG7 = new Sprite("/hud/framebackgrounds/7.jpg");
	}

	public static class MapResources
	{
		public static final Sprite LOCALMAP = new Sprite("/hud/map/localmap.png");
		public static final Sprite GLOBALMAP = new Sprite("/hud/map/globalmap.png");
		public static final Sprite PIN_RED, PIN_YELLOW, PIN_BLUE, PIN_GREEN;

		static
		{
			SpriteSet pinsBase = new SpriteSet("/hud/map/pins.png", 48, 12);
			PIN_RED = pinsBase.createSprite("red", 0, 0, 12, 12);
			PIN_YELLOW = pinsBase.createSprite("yellow", 12, 0, 12, 12);
			PIN_BLUE = pinsBase.createSprite("blue", 24, 0, 12, 12);
			PIN_GREEN = pinsBase.createSprite("green", 36, 0, 12, 12);
		}
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
